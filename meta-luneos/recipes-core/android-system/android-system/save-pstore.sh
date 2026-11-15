#!/bin/sh

# Copy the kernel's records of the last boot out of pstore, so a crash or a
# watchdog reset can be read afterwards.
#
# Nothing on LuneOS did this. systemd-pstore is not in the image, and on a
# Pixel the vendor's init-ramoops.rc unmounts /sys/fs/pstore at boot and
# mounts it again only in the container once init.ramoops.sh has decrypted
# the records (75-ramoops-keys). The host never mounted it, and the next crash
# overwrites the buffer.
#
# Where the vendor decrypts, wait for it: before that, pstore shows nothing or
# ciphertext. Then copy every record to /var/lib/pstore/<seq>-<boot id>/,
# with the boot reason the bootloader passed, and keep the newest $KEEP.
# The records are left in pstore; removing them is the kernel's business.

STORE=/var/lib/pstore
KEEP=10
WAIT=120

if [ -e /android/vendor/bin/init.ramoops.sh ]; then
    i=0
    while [ "$(getprop vendor.ramoops.decrypted 2>/dev/null)" != true ]; do
        i=$((i + 1))
        if [ $i -gt $WAIT ]; then
            echo "vendor ramoops not decrypted after ${WAIT}s, saving what is there"
            break
        fi
        sleep 1
    done
    err=$(getprop vendor.ramoops.decryption.error 2>/dev/null)
    [ -n "$err" ] && echo "vendor ramoops decryption error: $err"
fi

if ! mountpoint -q /sys/fs/pstore; then
    mount -t pstore pstore /sys/fs/pstore || {
        echo "cannot mount pstore"
        exit 0
    }
fi

set -- /sys/fs/pstore/*
if [ ! -e "$1" ]; then
    echo "pstore is empty"
    exit 0
fi

boot_id=$(cat /proc/sys/kernel/random/boot_id)
# Once per boot, however often this is started.
for d in "$STORE"/*-"$boot_id"; do
    [ -d "$d" ] && { echo "already saved in $d"; exit 0; }
done

# Numbered, not dated: the clock is often wrong this early (sargo boots into
# 1970 or 2025 until it is set), and the order is what the pruning needs.
# Leading zeros stripped, or $(( )) reads 000008 as a bad octal number.
last=$(ls -1 "$STORE" 2>/dev/null | sed -n 's/^\([0-9]\{6\}\)-.*/\1/p' | sort | tail -n 1 | sed 's/^0*//')
seq=$(printf '%06d' $(( ${last:-0} + 1 )))
dest="$STORE/$seq-$boot_id"
mkdir -p "$dest" || exit 0
chmod 0700 "$STORE" "$dest"

n=0
for f in "$@"; do
    [ -f "$f" ] && cp "$f" "$dest/" && n=$((n + 1))
done
reason=$(tr ' ' '\n' < /proc/cmdline | sed -n 's/^androidboot.bootreason=//p')
echo "${reason:-unknown}" > "$dest/bootreason"
echo "saved $n records to $dest (boot reason: ${reason:-unknown})"

# Newest first by sequence number; drop the rest.
ls -1d "$STORE"/[0-9]*-*/ 2>/dev/null | sort -r | sed "1,${KEEP}d" | while read -r old; do
    rm -rf -- "$old"
done

exit 0
