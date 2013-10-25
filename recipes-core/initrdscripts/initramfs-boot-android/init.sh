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
    echo "$1" > /dev/ttyprintk
}

fail() {
    echo "Failed" > /dev/ttyprintk
    echo "$1" > /dev/ttyprintk
    echo "Waiting for 15 seconds before rebooting ..." > /dev/ttyprintk
    sleep 15s
    reboot
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

info "Mounting sdcard/nand ..."
mkdir -m 0777 /sdcard
mount -t auto -o rw,noatime,nodiratime /dev/$sdcard_partition /sdcard
[ $? -eq 0 ] || fail "Failed to mount the sdcard/nan. Cannot continue."

ANDROID_SDCARD_DIR="/sdcard"
ANDROID_MEDIA_DIR="/sdcard/media/"

# Workaround for multi-user functionality in Android 4.2
if [ -d /sdcard/media/0 ] ; then
    ANDROID_MEDIA_DIR="/sdcard/media/0"
fi

info "Checking for rootfs image on sdcard/nand ..."
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
if [ ! -e /rfs/.firstboot_done ] ; then
    for dir in var home ; do
        rm -rf $datadir/$dir
        mkdir -p $datadir/$dir

        # Copy initial content to new location outside rootfs
        cp -rav /rfs/$dir/* $datadir/$dir
    done
    # We're done with our first boot actions
    touch /rfs/.firstboot_done
fi

# bind-mount everything we need from the outside
mount -o bind,rw $datadir/var /rfs/var
mount -o bind,rw $datadir/home /rfs/home

# finally setup the user data directory
mkdir -p $datadir/userdata
mount -o bind,rw $datadir/userdata /rfs/media/internal

# ..and mount also the android user directory there
mkdir -p /rfs/media/internal/android
mount -o bind,rw $ANDROID_MEDIA_DIR /rfs/media/internal/android

info "Switching to rootfs..."
exec switch_root /rfs /sbin/init
