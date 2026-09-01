#!/bin/sh
# Prepare the host for the Waydroid container.
#
# Everything here is state that does not survive a reboot but is not Waydroid's
# own job to create. It runs before waydroid-container.service.
#
# Device specifics come from /etc/default/waydroid-luneos, which is optional; a
# machine that needs none of them ships no file and every step below no-ops.
set -u

CONF=/etc/default/waydroid-luneos
[ -r "$CONF" ] && . "$CONF"

WAYDROID_IMAGES_DIR="${WAYDROID_IMAGES_DIR:-}"
WAYDROID_HOST_HAL_LIBS="${WAYDROID_HOST_HAL_LIBS:-}"

log() { echo "waydroid-prepare: $*"; }

# Wait for a path, bounded. Ordering alone is not enough: the Halium mounts this
# script depends on - binderfs and /vendor - are brought up by units that are
# not part of this service's transaction, so After= on them does nothing. Rather
# than name units that differ from port to port, wait for the thing itself.
WAIT_SECS="${WAYDROID_PREPARE_WAIT:-60}"
wait_for() {
    _p=$1
    [ -e "$_p" ] && return 0
    _n=0
    while [ "$_n" -lt "$WAIT_SECS" ]; do
        sleep 1
        _n=$((_n + 1))
        [ -e "$_p" ] && { log "waited ${_n}s for $_p"; return 0; }
    done
    return 1
}

# ---------------------------------------------------------------- binder nodes
#
# On a Halium host Waydroid will not share the host HAL's /dev/binder: it looks
# for anbox-binder, puddlejumper or bonder and raises when none exist. It never
# probes for them on that path, so something else has to create them.
#
# Where the kernel names them in CONFIG_ANDROID_BINDER_DEVICES they are already
# present and this is a no-op. Where the kernel has binderfs instead they can be
# allocated at runtime, which is what this does - no kernel change, but also no
# persistence, hence doing it on every boot.
alloc_binder_nodes() {
    have_all=1
    for n in anbox-binder anbox-hwbinder anbox-vndbinder; do
        [ -e "/dev/$n" ] || have_all=0
    done
    [ "$have_all" = 1 ] && { log "binder nodes already present"; return 0; }

    wait_for /dev/binderfs/binder-control || {
        log "no binder nodes and no binderfs after ${WAIT_SECS}s: the kernel must name them in CONFIG_ANDROID_BINDER_DEVICES"
        return 1
    }

    python3 - <<'PY' || return 1
import ctypes, fcntl, os, struct, sys

# sizeof(struct binderfs_device). The original binderfs ABI declared major and
# minor as __u8, giving 258; they became __u32 later, giving 264. Waydroid
# hardcodes 264, which is why its own allocator returns EINVAL on a kernel
# carrying the older definition. Try both rather than guessing.
NAME = 256
def ioc(size):
    return (3 << 30) | (size << 16) | (98 << 8) | 1

nodes = ("anbox-binder", "anbox-hwbinder", "anbox-vndbinder")
with open("/dev/binderfs/binder-control", "rb") as f:
    for size, fmt in ((NAME + 2, "256sBB"), (NAME + 8, "256sII")):
        try:
            for n in nodes:
                if os.path.exists("/dev/binderfs/" + n):
                    continue
                fcntl.ioctl(f.fileno(), ioc(size), struct.pack(fmt, n.encode(), 0, 0))
            break
        except OSError as e:
            if size == NAME + 8:
                print("binderfs allocation failed:", e, file=sys.stderr)
                raise SystemExit(1)
PY

    for n in anbox-binder anbox-hwbinder anbox-vndbinder; do
        [ -e "/dev/binderfs/$n" ] || { log "binderfs did not create $n"; return 1; }
        chmod 666 "/dev/binderfs/$n"
        ln -sfn "/dev/binderfs/$n" "/dev/$n"
    done
    log "allocated binder nodes via binderfs"
}

# ----------------------------------------------------------------- image store
#
# Waydroid treats /etc/waydroid-extra/images as pre-installed and skips the OTA
# channel when it finds images there. On a machine whose rootfs has no room for
# them - mindphone's is a 2.4G loop image - the images live on userdata and the
# directory is bind mounted over the path Waydroid looks at.
bind_images() {
    [ -n "$WAYDROID_IMAGES_DIR" ] || return 0
    [ -d "$WAYDROID_IMAGES_DIR" ] || { log "WAYDROID_IMAGES_DIR $WAYDROID_IMAGES_DIR does not exist"; return 1; }
    mkdir -p /etc/waydroid-extra/images
    mountpoint -q /etc/waydroid-extra/images && { log "image store already mounted"; return 0; }
    mount --bind "$WAYDROID_IMAGES_DIR" /etc/waydroid-extra/images || return 1
    log "bind mounted $WAYDROID_IMAGES_DIR over /etc/waydroid-extra/images"
}

# -------------------------------------------------------------- host HAL libs
#
# Waydroid bind mounts the host's /vendor/lib/egl into the container but not
# /vendor/lib/hw, and some GPU stacks need both: MTK's PowerVR libEGL_mtk.so
# dlopens /vendor/lib/hw/gralloc.rogue.so, and without it SurfaceFlinger fails
# eglInitialize and crash-loops, so Android never reaches sys.boot_completed.
#
# /var/lib/waydroid/overlay/vendor is Waydroid's own lower layer over the vendor
# image, so dropping the host's copy there is the supported way to add it.
# Listed per machine rather than copied wholesale: shadowing a module the vendor
# image supplies correctly would break a working device.
copy_host_hal_libs() {
    [ -n "$WAYDROID_HOST_HAL_LIBS" ] || return 0
    wait_for /vendor/lib/hw || { log "/vendor/lib/hw absent after ${WAIT_SECS}s"; return 1; }
    dest=/var/lib/waydroid/overlay/vendor/lib/hw
    mkdir -p "$dest"
    for lib in $WAYDROID_HOST_HAL_LIBS; do
        src="/vendor/lib/hw/$lib"
        [ -f "$src" ] || { log "host HAL $lib not present, skipping"; continue; }
        [ -f "$dest/$lib" ] && continue
        cp "$src" "$dest/$lib" && log "bridged host HAL $lib into the vendor overlay"
    done
}

# ----------------------------------------------------------------- no RIL
#
# On a Halium host the modem belongs to the host - ofono owns it - so the
# container has no radio to claim. com.android.phone does not discover that
# gracefully: PhoneFactory.makeDefaultPhone throws
#
#   java.lang.RuntimeException: PhoneFactory probably already running
#
# and system_server restarts it, forever. Measured on mindphone: a crash every
# six seconds and the container's process count climbing from 77 to 182 with
# stale launcher instances piling up behind it. Android still reports
# sys.boot_completed=1 throughout, so nothing in "waydroid status" shows it.
#
# ro.radio.noril stops telephony coming up at all. Do not reach for
# "pm disable com.android.phone" instead: it strips the package's permission
# grants and the loop returns as a SecurityException about
# BIND_TELECOM_CONNECTION_SERVICE, several times faster.
#
# waydroid_base.prop is read by make_prop() at session start, and only exists
# once waydroid init has run, so this is best effort.
set_no_ril() {
    [ "${WAYDROID_NO_RIL:-1}" = 1 ] || return 0
    base=/var/lib/waydroid/waydroid_base.prop
    [ -f "$base" ] || return 0
    grep -q "^ro.radio.noril" "$base" && return 0
    echo "ro.radio.noril=1" >> "$base" && log "disabled container telephony (ro.radio.noril=1)"
}

rc=0
alloc_binder_nodes  || rc=1
bind_images         || rc=1
copy_host_hal_libs  || rc=1
set_no_ril          || rc=1
exit $rc
