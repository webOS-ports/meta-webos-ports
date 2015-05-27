#! /bin/sh

create_swap_file=1

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

check_and_start_adb() {
    mkdir -p /dev/pts
    mount -t devpts devpts /dev/pts

    # Check wether we need to start adbd for interactive debugging
    /usr/bin/android-gadget-setup adb
    /usr/bin/adbd
}

echo "Mounting relevant filesystems ..."
mkdir -p -m 0755 /proc
mount -t proc proc /proc
mkdir -p -m 0755 /sys
mount -t sysfs sys /sys
mkdir -p /dev

setup_devtmpfs ""

log() {
    echo "$1" > /dev/kmsg
}

info() {
    log "info: $1"
}

error() {
    log "error: $1"
}

fail() {
    log "error: $1"
    log "Waiting 15 seconds before rebooting ..."
    sleep 15s
    reboot
}

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
datadir=$ANDROID_SDCARD_DIR/$distro_name-data

# Workaround for multi-user functionality in Android 4.2
if [ -d /sdcard/media/0 ] ; then
    ANDROID_MEDIA_DIR="/sdcard/media/0"
fi

# Run any fixups needed to bring the system into the state we expect it to be in
. /fixups.sh

info "Checking for rootfs image on sdcard/nand ..."
if [ -d $ANDROID_SDCARD_DIR/$distro_name ] ; then
    info "Rootfs folder found at $ANDROID_SDCARD_DIR/$distro_name; mounting it ..."
    mount -o bind $ANDROID_SDCARD_DIR/$distro_name /rfs
    [ $? -eq 0 ] || fail "Failed to mount /rootfs"
else
    fail "Failed to find valid rootfs"
fi

if [ -e $datadir/system-update.zip ] ; then
    info "Found system update package. Starting update process ..."

    . system-updater.sh

    update_rootfs /rfs $datadir/system-update.zip $datadir/update-work

    if [ ! $? -eq 0 ] ; then
        rm $datadir/system-update.zip
        fail "Failed to apply system update!"
        exit 1
    fi

    rm $datadir/system-update.zip

    reboot
fi

setup_devtmpfs "/rfs"

info "Umount not needed filesystems ..."
umount -l /proc
umount -l /sys

# We need to mount some directories read-write in order to have a working
# system so bind mount them from the outside into the rootfs. If we're
# doing this the first time we have to remove the old data and copy the
# initial data
if [ ! -e /rfs/.firstboot_done ] ; then
    for dir in var home ; do
        rm -rf $datadir/$dir
        mkdir -p $datadir/$dir

        # Copy initial content to new location outside rootfs
        cp -rav /rfs/$dir/* $datadir/$dir
    done

    # setup cryptofs which is not a real cryptofs yet
    if [ -d $datadir/userdata/.cryptofs ] ; then
        rm -rf $datadir/userdata/.cryptofs
    fi
    mkdir -p $datadir/userdata/.cryptofs
    cp -rav /rfs/media/cryptofs/* $datadir/userdata/.cryptofs

    # We're done with our first boot actions
    touch /rfs/.firstboot_done
fi

info "Creating userdata mount point ..."
mkdir -p /rfs/userdata
mount /dev/$sdcard_partition /rfs/userdata

if [ "$create_swap_file" -eq "1" ] ; then
    if [ ! -e /rfs/SWAP.img ] ; then
        info "Creating SWAP device ..."
        dd if=/dev/zero of=/rfs/SWAP.img bs=4096 count=131072
        chmod 600 /rfs/SWAP.img
        chown 0:0 /rfs/SWAP.img
        mkswap /rfs/SWAP.img
    fi
fi

# Setup fstab
info "Setting up new fstab ..."

fstab=/rfs/etc/fstab
new_fstab=/rfs/userdata/luneos-data/fstab
temp_fstab=${new_fstab}.tmp

[ -e $new_fstab ] && rm -f $new_fstab
cp $fstab $temp_fstab

sed -i '/^rootfs/,+0d' $temp_fstab
cp ${temp_fstab} $new_fstab

MOUNT="ro"
if [ -e /rfs/.writable_image ] ; then
    MOUNT="rw"
fi
echo "rootfs / auto defaults,bind,remount,$MOUNT 1 1" > $new_fstab
cat $temp_fstab >> $new_fstab

# Additional mounts we need for user writable data
echo "/userdata/luneos-data/var /var none bind 0 0" >> $new_fstab
echo "/userdata/luneos-data/home /home none bind 0 0" >> $new_fstab
echo "/userdata/luneos-data/userdata /media/internal none bind 0 0" >> $new_fstab
echo "/userdata/luneos-data/userdata/.cryptofs /media/cryptofs none bind 0 0" >> $new_fstab

if [ "$create_swap_file" -eq "1" ] ; then
    echo "/SWAP.img none swap sw 0 0" >> $new_fstab
fi

mount -o bind $new_fstab $fstab

info "Switching to rootfs..."
exec switch_root /rfs /sbin/init
