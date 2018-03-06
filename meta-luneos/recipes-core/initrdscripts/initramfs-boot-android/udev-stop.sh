#!/bin/sh -e

PREREQS=""

prereqs() { echo "$PREREQS"; }

case "$1" in
    prereqs)
    prereqs
    exit 0
    ;;
esac

# Stop udevd, we'll miss a few events while we run init, but we catch up
udevadm control --exit

# move the /dev tmpfs to the rootfs
mount -n -o move /dev ${rootmnt}/dev

# create a temporary symlink to the final /dev for other initramfs scripts
if command -v nuke >/dev/null; then
    nuke /dev
else
    rm -rf /dev
fi
ln -s ${rootmnt}/dev /dev

