#!/bin/sh

[ -e /dev/input/touchscreen0 ] && exit 0

if grep -q "input/ts" /sys/class/$MDEV/device/phys ; then
	ln -sf /dev/$MDEV /dev/input/touchscreen0
fi
