#! /bin/sh

# machine.conf should provide $sdcard_partition and $system_partition
. /machine.conf

# distro.conf should provide $distro_name
. /distro.conf

setup_devtmpfs() {
    mount -t devtmpfs -o mode=0755,nr_inodes=0 devtmpfs $1/dev
    # Create additional nodes which devtmpfs does not provide
    test -c $1/dev/fd || ln -sf /proc/self/fd $1/dev/fd
    test -c $1/dev/stdin || ln -sf fd/0 $1/dev/stdin
    test -c $1/dev/stdout || ln -sf fd/1 $1/dev/stdout
    test -c $1/dev/stderr || ln -sf fd/2 $1/dev/stderr
    test -c $1/dev/socket || mkdir -m 0755 $1/dev/socket
}

echo "Mounting pseudo-filesystems ..."
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
    echo "$distro_name initramfs failed:" > /dev/kmsg
    echo "$1" > /dev/kmsg
    echo "Rebooting now ..." > /dev/kmsg
    /sbin/reboot
}

# Check wether we need to start adbd for interactive debugging
cat /proc/cmdline | grep enable_adb
if [ $? -ne 1 ] ; then

    #system partition is needed for accessing build.prop by android-gadget-setup
    /sbin/fsck.ext4 -p /dev/$system_partition
    mount -t auto -o rw,noatime,nodiratime,nodelalloc /dev/$system_partition /system

    #below are now needed in order to use FunctionFS for ADB, tested to work with 3.4+ kernels
    mkdir -p /dev/usb-ffs/adb 
    mount -t functionfs adb /dev/usb-ffs/adb > /dev/kmsg
    #android-gadget-setup doesn't provide below 2 and without them it won't work, so we provide them here.
    echo adb > /sys/class/android_usb/android0/f_ffs/aliases
    echo ffs > /sys/class/android_usb/android0/functions 

    /usr/bin/android-gadget-setup adb
    /usr/bin/adbd
fi


mkdir -m 0755 /rfs

sdcard_device=$( echo "$sdcard_partition" | sed -e 's/p[[:digit:]]\+$//' )
while [ ! -e /sys/block/$sdcard_device ] ; do
    info "Waiting for SD Card/NAND ..."
    sleep 1
done

# Try unpartitioned card
if [ ! -e /sys/block/$sdcard_device/$sdcard_partition ] ; then
    sdcard_partition=$sdcard_device
fi

ANDROID_SDCARD_DIR="/sdcard"
ANDROID_MEDIA_DIR="$ANDROID_SDCARD_DIR/media/"

info "Mounting SD card/NAND as /dev/$sdcard_partition ..."
mkdir -m 0777 $ANDROID_SDCARD_DIR
mount -t auto -o rw,noatime,nodiratime /dev/$sdcard_partition $ANDROID_SDCARD_DIR >/dev/kmsg 2>&1
[ $? -eq 0 ] || fail "Failed to mount the SD card/NAND, cannot continue."

# Workaround for multi-user functionality in Android 4.2
if [ -d $ANDROID_SDCARD_DIR/media/0 ] ; then
    ANDROID_MEDIA_DIR="$ANDROID_SDCARD_DIR/media/0"
fi

# Run any fixups needed to bring the system into the state we expect it to be in
. /fixups.sh

info "Checking for rootfs image on SD card/NAND for $ANDROID_SDCARD_DIR/$distro_name ..."
if [ -d $ANDROID_SDCARD_DIR/$distro_name ] ; then
    info "Rootfs folder found at $ANDROID_SDCARD_DIR/$distro_name; chrooting into ..."
    mount -o bind $ANDROID_SDCARD_DIR/$distro_name /rfs
    [ $? -eq 0 ] || fail "Failed to mount /rootfs"
else
    fail "No root filesystem found on SD card/NAND"

    # We don't have anything to boot from SDcard/NAND. Cleanup and boot from system partition
    umount $SDCARD_DIR

    mount -t auto -o rw,noatime,nodiratime /dev/$system_partition /rfs
    [ $? -eq 0 ] || fail "Failed to mount system partition /dev/$system_partition"
fi

setup_devtmpfs "/rfs"

info "Unmounting unneeded filesystems ..."
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
