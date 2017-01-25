#! /bin/sh

. /machine.conf
. /distro.conf

setup_devtmpfs() {
    mount -t devtmpfs -o mode=0755,nr_inodes=0 devtmpfs $1/dev
    # Create additional nodes which devtmpfs does not provide
    test -c $1/dev/fd || ln -sf /proc/self/fd $1/dev/fd
    test -c $1/dev/stdin || ln -sf fd/0 $1/dev/stdin
    test -c $1/dev/stdout || ln -sf fd/1 $1/dev/stdout
    test -c $1/dev/stderr || ln -sf fd/2 $1/dev/stderr
}

echo "Mounting relevant filesystems ..."
mkdir -m 0755 /proc
mount -t proc proc /proc
mkdir -m 0755 /sys
mount -t sysfs sys /sys
mkdir -p /dev

setup_devtmpfs ""

info() {
    echo "$1" > /dev/kmsg
}

fail() {
    echo "Failed" > /dev/kmsg
    echo "$1" > /dev/kmsg
    echo "Rebooting now ..." > /dev/kmsg
    /sbin/reboot
}

# Check wether we need to start adbd for interactive debugging
cat /proc/cmdline | grep enable_adb
if [ $? -ne 1 ] ; then
    /usr/bin/android-gadget-setup adb
    /usr/bin/adbd
fi


mkdir -m 0755 /rfs

while [ ! -e /sys/block/mmcblk0 ] ; do
    info "Waiting for sdcard/nand ..."
    sleep 1
done

# Try unpartitioned card
if [ ! -e /sys/block/mmcblk0/$sdcard_partition ] ; then
    sdcard_partition=mmcblk0
fi

info "Mounting sdcard/nand as /dev/$sdcard_partition ..."
mkdir -m 0777 /sdcard
mount -t auto -o rw,noatime,nodiratime /dev/$sdcard_partition /sdcard >/dev/kmsg 2>&1
[ $? -eq 0 ] || fail "Failed to mount the sdcard/nan. Cannot continue."

ANDROID_SDCARD_DIR="/sdcard"
ANDROID_MEDIA_DIR="/sdcard/media/"

# Workaround for multi-user functionality in Android 4.2
if [ -d /sdcard/media/0 ] ; then
    ANDROID_MEDIA_DIR="/sdcard/media/0"
fi

# Run any fixups needed to bring the system into the state we expect it to be in
. /fixups.sh

info "Checking for rootfs image on sdcard/nand for $ANDROID_SDCARD_DIR/$distro_name ..."
if [ -d $ANDROID_SDCARD_DIR/$distro_name ] ; then
    info "Rootfs folder found at $ANDROID_SDCARD_DIR/$distro_name; chrooting into ..."
    mount -o bind $ANDROID_SDCARD_DIR/$distro_name /rfs
    [ $? -eq 0 ] || fail "Failed to mount /rootfs"
else
    fail "Failed to find valid rootfs"
fi

setup_devtmpfs "/rfs"

info "Umount not needed filesystems ..."
umount -l /proc
umount -l /sys

# We need to mount some directories read-write in order to have a working
# system so bind mount them from the outside into the rootfs. If we're
# doing this the first time we have to remove the old data and copy the
# initial data
datadir=$ANDROID_SDCARD_DIR/$distro_name-data
info "Preparing $datadir"
if [ ! -e /rfs/.firstboot_done ] ; then
    for dir in var home ; do
        rm -rf $datadir/$dir
        mkdir -p $datadir/$dir

        # Copy initial content to new location outside rootfs
        cp -rav /rfs/$dir/* $datadir/$dir
    done

    mkdir -p $datadir/userdata
    # Copy initial media to userdata
    cp -rav /rfs/media/internal/* $datadir/userdata/

    # setup cryptofs which is not a real cryptofs yet
    if [ -d $datadir/userdata/.cryptofs ] ; then
        rm -rf $datadir/userdata/.cryptofs
    fi
    mkdir -p $datadir/userdata/.cryptofs

    # We're done with our first boot actions
    touch /rfs/.firstboot_done
fi

info "Bind-mount the directories"
# bind-mount the directories to their correct place
for dir in var home ; do
    mount -o bind,rw $datadir/$dir /rfs/$dir
done

info "Setup the user data directory"
# finally setup the user data directory
mount -o bind,rw $datadir/userdata /rfs/media/internal
mount -o bind,rw $datadir/userdata/.cryptofs /rfs/media/cryptofs

# ..and mount also the android user directory there
mkdir -p /rfs/media/internal/android
mount -o bind,rw $ANDROID_MEDIA_DIR /rfs/media/internal/android

if [ ! -e /rfs/SWAP.img ] ; then
    info "Creating SWAP device ..."
    dd if=/dev/zero of=/rfs/SWAP.img bs=4096 count=131072
    chmod 600 /rfs/SWAP.img
    chown 0:0 /rfs/SWAP.img
    mkswap /rfs/SWAP.img
fi

info "Switching to rootfs..."
exec switch_root /rfs /sbin/init
