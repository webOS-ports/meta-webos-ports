# Local filesystem mounting			-*- shell-script -*-

_log_msg() {
	if [ "$quiet" = "y" ]; then return; fi
	printf "$@" > /dev/kmsg || true
}

pre_mountroot() {
	[ "$quiet" != "y" ] && log_begin_msg "Running /scripts/local-top"
	run_scripts /scripts/local-top
	[ "$quiet" != "y" ] && log_end_msg
}

tell_kmsg() {
	# Echos a string into /dev/kmsg, ignoring errors.
	echo "initrd: $1" >/dev/kmsg || true
}

halium_panic() {
	# Puts panic reason into kmsg and then starts the panic handlers
	REASON="$1"
	tell_kmsg "PANIC for reason: $REASON"
	panic $REASON
}

identify_boot_mode() {
	# Our current list of supported boot modes:
	## BOOT_MODE = halium and android
	BOOT_MODE='halium'

	# The boot reason is exported via /proc/cmdline
	# The standard method is using androidboot.mode parameter.

	for x in $(cat /proc/cmdline); do
		case ${x} in
		androidboot.mode=*)
			android_bootmode=${x#*=}
			;;
		esac
	done

	if echo "$android_bootmode" | grep charger; then
		BOOT_MODE="android"
	fi

	## Some devices may be using 'bootreason', others 'boot_reason'
	## XXX: Find a better way to handle device specifics here

	# Krillin
	if [ -f /sys/class/BOOT/BOOT/boot/boot_mode ]; then
		boot_reason=$(cat /sys/class/BOOT/BOOT/boot/boot_mode)
		case "${boot_reason}" in
		1) BOOT_MODE="android" ;; # Meta
		4) BOOT_MODE="android" ;; # Factory
		8) BOOT_MODE="android" ;; # Power off charging
		9) BOOT_MODE="android" ;; # Low power charging
		esac
	fi

	tell_kmsg "boot mode: $BOOT_MODE"
}

set_halium_version_properties() {
	halium_system=$1
	android_data=$2

	channel_ini=$1/etc/system-image/channel.ini
	def_language=$1/custom/default_language

	halium="unknown"
	device="unknown"
	custom="unknown"
	version="unknown"
	channel="unknown"
	def_lang="unknown"

	if [ -f "$channel_ini" ]; then
		IFS=','
		for i in $(grep version_detail $channel_ini | awk -F ' ' '{print $2}'); do
			id=${i%=*}
			case $id in
			halium) halium=${i#halium=} ;;
			device) device=${i#device=} ;;
			custom) custom=${i#custom=} ;;
			version) version=${i#version=} ;;
			esac
		done
		unset IFS
		channel=$(grep channel $channel_ini | awk -F ' ' '{print $2}')
	fi

	if [ -f "$def_language" ]; then
		lang=$(cat $def_language)
		if [ -n "$lang" ]; then
			def_lang=$lang
		fi
	fi

	# Write down so the android property system can load them automatically
	mkdir -p $android_data/property
	chmod 700 $android_data/property
	echo -n "$halium" >$android_data/property/persist.halium.version.rootfs
	echo -n "$device" >$android_data/property/persist.halium.version.device
	echo -n "$custom" >$android_data/property/persist.halium.version.custom
	echo -n "$channel" >$android_data/property/persist.halium.version.channel
	echo -n "$version" >$android_data/property/persist.halium.version
	echo -n "$def_lang" >$android_data/property/persist.halium.default_language
	chmod 600 $android_data/property/persist.halium*
}

mount_android_partitions() {
	fstab=$1
	mount_root=$2

	tell_kmsg "checking fstab $fstab for additional mount points"

	cat ${fstab} | while read line; do
		set -- $line

		# stop processing if we hit the "#endhalium" comment in the file
		echo $1 | egrep -q "^#endhalium" && break

		# Skip any unwanted entry
		echo $1 | egrep -q "^#" && continue
		([ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ] || [ -z "$4" ]) && continue
		([ "$2" = "/system" ] || [ "$2" = "/data" ]) && continue

		label=$(echo $1 | awk -F/ '{print $NF}')
		[ -z "$label" ] && continue

		tell_kmsg "checking mount label $label"

		# In case fstab provides /dev/mmcblk0p* lines
		path="/dev/$label"
		for dir in by-partlabel by-name by-label by-path by-uuid by-partuuid by-id; do
			if [ -e "/dev/disk/$dir/$label" ]; then
				path="/dev/disk/$dir/$label"
				break
			fi
		done

		[ ! -e "$path" ] && continue

		mkdir -p ${mount_root}/$2
		tell_kmsg "mounting $path as ${mount_root}/$2"
		mount $path ${mount_root}/$2 -t $3 -o $4
	done
}

mount_halium_overlay() {
	source=$1
	target=$2

	if [ -d ${source} ]; then
		OLD_PWD=$PWD
		cd ${source}

		for overlay in $(find . -type f); do
			[ -f ${target}/${overlay} ] && mount --bind ${source}/${overlay} ${target}/${overlay}
		done

		cd $OLD_PWD
	fi
}

sync_dirs() {
	base=$1
	source=$2
	target=$3

	OLD_PWD=$PWD
	cd $base

	for file in $source/*; do
		# Skip empty directories
		[ ! -e "$base/$file" ] && continue

		# If the target already exists as a file or link, there's nothing we can do
		[ -e "$target/$file" -o -L "$target/$file" ] && [ ! -d "$target/$file" ] && continue

		# If the target doesn't exist, just copy it over
		if [ ! -e "$target/$file" -a ! -L "$target/$file" ]; then
			cp -Ra "$base/$file" "$target/$file"
			continue
		fi

		# That leaves us with directories and a recursive call
		[ -d $file ] && sync_dirs $base $file $target
	done

	cd $OLD_PWD
}

resize_userdata_if_needed() {

	# See if the filesystem on the userdata partition needs resizing (usually on first boot).
	# If the difference between the partition size and the filesystem size is above a small
	# threshold, assume it needs resizing to fill the partition.

	path=$1

	# Partition size in 1k blocks
	case $path in
	/dev/mmcblk*)
		pblocks=$(grep ${path#/dev/*} /proc/partitions | awk {'print $3'})
		;;
	/dev/disk*)
		pblocks=$(grep $(basename $(readlink $path)) /proc/partitions | awk {'print $3'})
		;;
	esac
	# Filesystem size in 4k blocks
	fsblocks=$(dumpe2fs -h $path | grep "Block count" | awk {'print $3'})
	# Difference between the reported sizes in 1k blocks
	dblocks=$((pblocks - 4 * fsblocks))
	if [ $dblocks -gt 10000 ]; then
		resize2fs -f $path
		tell_kmsg "resized userdata filesystem to fill $path"
	fi
}

identify_file_layout() {
	# Determine if we have a Halium rootfs.img & system.img

	# $file_layout = "halium" means there is a separate rootfs.img and system.img on userdata
	#
	# = "partition" means the rootfs is located on the device's system partition
	# and will contain /var/lib/lxc/android/system.img
	#
	# = "folder" means the rootfs is located in a folder on the device's userdata partition
	# and will contain /var/lib/lxc/android/system.img

	if [ -e /tmpmnt/rootfs.img ]; then
		imagefile=/tmpmnt/rootfs.img
		file_layout="halium"
	elif [ -d /tmpmnt/halium-rootfs ]; then
		imagefile=/tmpmnt/halium-rootfs
		file_layout="subdir"
	else
		file_layout="partition"
	fi

}

process_bind_mounts() {
	# Goes over /etc/system-image/writable-paths to create the correct fstab for
	# the bind-mounts. Writes them into ${rootmnt}/run/image.fstab which is
	# bind-mounted to /etc/fstab

	if [ ! -e ${rootmnt}/etc/system-image/writable-paths ]; then
		tell_kmsg "This rootfs does not have any writable-paths defined"
		return 0
	fi

	# Mount a tmpfs in /run of rootfs to put the future image.fstab
	mount -o rw,nosuid,noexec,relatime,mode=755 -t tmpfs tmpfs ${rootmnt}/run
	# Prepare the fstab
	FSTAB=${rootmnt}/etc/fstab
	touch ${rootmnt}/run/image.fstab
	mount -o bind ${rootmnt}/run/image.fstab $FSTAB ||halium_panic "Could not bind-mount fstab"
	echo "/dev/root / rootfs defaults,ro 0 0" >>$FSTAB

	tell_kmsg "Adding bind-mounts to $FSTAB"
	# Process the list of bind-mounts
	# (but don't mount them, mountall will do it)
	cat ${rootmnt}/etc/system-image/writable-paths | while read line; do
		set -- $line
		# Skip invalid/commented entries
		([ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ] || [ -z "$4" ] || [ -z "$5" ]) && continue
		[ "$1" = "#" ] && continue

		# Skip invalid mount points
		dstpath="${rootmnt}/$1"
		[ ! -e "$dstpath" ] && continue

		if [ "$3" = "temporary" ]; then
			# Temporary entries are simple, just mount a tmpfs
			echo "tmpfs $1 tmpfs $5 0 0" >>$FSTAB
		elif [ "$3" = "persistent" ] || [ "$3" = "synced" ]; then
			# Figure out the source path
			if [ "$2" = "auto" ]; then
				srcpath="${rootmnt}/userdata/system-data/$1"
				path="/userdata/system-data/$1"
			else
				srcpath="${rootmnt}/userdata/$2"
				path="/userdata/$2"
			fi

			if [ ! -e "$srcpath" ]; then
				# Process new persistent or synced paths
				dstown=$(stat -c "%u:%g" $dstpath)
				dstmode=$(stat -c "%a" $dstpath)
				mkdir -p ${srcpath%/*}
				if [ ! -d "$dstpath" ]; then
					# Deal with redirected files
					if [ "$4" = "transition" ]; then
						cp -a $dstpath $srcpath
					else
						touch $srcpath
						chown $dstown $srcpath
						chmod $dstmode $srcpath
					fi
				else
					# Deal with redirected directories
					if [ "$4" = "transition" ] || [ "$3" = "synced" ]; then
						cp -aR $dstpath $srcpath
					else
						mkdir $srcpath
						chown $dstown $srcpath
						chmod $dstmode $srcpath
					fi
				fi
			elif [ "$3" = "synced" ]; then
				# Process existing synced paths
				sync_dirs $dstpath . $srcpath
			fi

			# Write the fstab entry
			if [ "$5" = "none" ]; then
				echo "$path $1 none bind 0 0" >>$FSTAB
			else
				echo "$path $1 none bind,$5 0 0" >>$FSTAB
			fi
		else
			continue
		fi
	done
}

extract_android_ramdisk() {
	# Extracts the ramdisk from /android-system/boot/android-ramdisk.img to
	# /android-rootfs

	# NOTE: we should find a faster way of doing that or cache it
	tell_kmsg "extracting android ramdisk"
	OLD_CWD=$(pwd)
	mount -n -t tmpfs tmpfs /android-rootfs
	cd /android-rootfs
	cat /android-system/boot/android-ramdisk.img | gzip -d | cpio -i
	cd $OLD_CWD
}

mount_kernel_modules() {
	# Bind-mount /lib/modules from Android
	[ -e ${rootmnt}/android/system/lib/modules ] && mount --bind ${rootmnt}/android/system/lib/modules ${rootmnt}/lib/modules
}

mountroot() {
	# list of possible userdata partition names
	partlist="userdata UDA DATAFS USERDATA"

	pre_mountroot

	[ "$quiet" != "y" ] && log_begin_msg "Running /scripts/local-premount"
	run_scripts /scripts/local-premount
	[ "$quiet" != "y" ] && log_end_msg

	# Put all of this script's output into /dev/kmsg
	exec &>/dev/kmsg

	# Mount root
	#
	# Create a temporary mountpoint for the bindmount
	mkdir -p /tmpmnt

	# Make sure the device has been created by udev before we try to mount
	udevadm settle

	# find the right partition
	for partname in $partlist; do
		part=$(find /dev -name $partname | tail -1)
		[ -z "$part" ] && continue
		path=$(readlink -f $part)
		[ -n "$path" ] && break
	done

	# override with a possible cmdline parameter
	if grep -q datapart= /proc/cmdline; then
		for x in $(cat /proc/cmdline); do
			case ${x} in
			datapart=*)
				path=${x#*=}
				;;
			esac
		done
	fi

	if [ -z "$path" ]; then
		halium_panic "Couldn't find data partition."
	fi

	tell_kmsg "checking filesystem integrity for the userdata partition"
	# Mounting and umounting first, let the kernel handle the journal and
	# orphaned inodes (faster than e2fsck). Then, just run e2fsck forcing -y.
	# Also check the amount of time used by to check the filesystem.
	fsck_start=$(date +%s)
	mount -o errors=remount-ro $path /tmpmnt
	umount /tmpmnt
	e2fsck -y $path >/run/e2fsck.out 2>&1
	fsck_end=$(date +%s)
	tell_kmsg "checking filesystem for userdata took (including e2fsck) $((fsck_end - fsck_start)) seconds"

	resize_userdata_if_needed ${path}

	tell_kmsg "mounting $path"

	# Mount the data partition to a temporary mount point
	# FIXME: data=journal used as a workaround for bug 1387214
	mount -o discard,data=journal $path /tmpmnt

	# Set $syspart if it is specified as systempart= on the command line
	if grep -q systempart= /proc/cmdline; then
		for x in $(cat /proc/cmdline); do
			case ${x} in
			systempart=*)
				syspart=${x#*=}
				;;
			esac
		done
	fi

	identify_boot_mode
	identify_file_layout

	# If both $imagefile and $syspart are set, something is wrong. The strange
	# output from this could be a clue in that situation.
	tell_kmsg "Halium rootfs is $imagefile $syspart"

	# Prepare the root filesystem
	# NOTE: We mount it read-write in all cases, then remount read-only.
	#       This is to workaround a behaviour change in busybox which now
	#       uses read-only loops if the fs is initially mounted read-only.
	#       An alternative implementation would be to add losetup support
	#       to busybox and do the mount in two steps (rw loop, ro fs).

	mkdir -p /halium-system
	mkdir -p /android-rootfs
	mkdir -p /android-system

	tell_kmsg "mounting system rootfs at /halium-system"
	if [ -n "$syspart" ]; then
		mount -o rw $syspart /halium-system
	elif [ -f "$imagefile" ]; then
		mount -o loop,rw $imagefile /halium-system
		# If either (android) /data/.writable_image or (on rootfs)
		# /.writable_image exist, mount the rootfs as rw
		if [ -e /tmpmnt/.writable_image ] || [ -e $/halium-system/.writable_image ]; then
			tell_kmsg "mounting $imagefile (image developer mode)"
			mountroot_status="$?"
		else
			tell_kmsg "mounting $imagefile (user mode)"
			mount -o remount,ro /halium-system
			mountroot_status="$?"
		fi
	elif [ -d "$imagefile" ]; then
		mount -o bind /tmpmnt/halium-rootfs /halium-system
	fi

	# Mount the android system partition to a temporary location
	mkdir -p /android-system
	MOUNT="ro"
	[ -e /tmpmnt/.writable_device_image -a -e /halium-system/.writable_device_image ] && MOUNT="rw"
	tell_kmsg "mounting android system image $MOUNT"
	if [ $file_layout = "halium" ]; then
		# rootfs.img and Android system.img are separate
		tell_kmsg "mounting android system image from userdata partition"
		mount -o loop,$MOUNT /tmpmnt/system.img /android-system
	else
		# Android system.img is inside rootfs
		tell_kmsg "mounting android system image from system rootfs"
		mount -o loop,$MOUNT /halium-system/var/lib/lxc/android/system.img /android-system
	fi

	[ $? -eq 0 ] || tell_kmsg "WARNING: Failed to mount Android system.img."

	extract_android_ramdisk

	# Determine whether we should boot to rootfs or Android
	if ([ -e $imagefile ] || [ -n "$syspart" ]) && [ "$BOOT_MODE" = "android" ]; then
		# Bootloader says this is factory or charger mode, boot into Android.
		tell_kmsg "Android boot mode for factory or charger mode"

		mount /halium-system -o remount,ro
		mount --move /android-rootfs ${rootmnt}
		mount --move /android-system ${rootmnt}/system

		# Mount all the Android partitions
		mount_android_partitions "${rootmnt}/fstab*" ${rootmnt}

		mkdir -p ${rootmnt}/halium-system
		mount --move /halium-system ${rootmnt}/halium-system

		# Mounting userdata
		mkdir -p ${rootmnt}/data
		mkdir -p /tmpmnt/android-data
		mount -o bind /tmpmnt/android-data ${rootmnt}/data

		# Set halium version properties
		set_halium_version_properties ${rootmnt}/halium-system ${rootmnt}/data

		# Make sure we're booting into android's init
		ln -s ../init ${rootmnt}/sbin/init
		ln -s ../init ${rootmnt}/sbin/recovery
		tell_kmsg "booting android..."
	elif [ -e $imagefile ] || [ -n "$syspart" ]; then
		# Regular image boot
		tell_kmsg "Normal boot"

		mount --move /halium-system ${rootmnt}

		# Mount some tmpfs
		mkdir -p ${rootmnt}/android
		mount -o rw,size=4096 -t tmpfs none ${rootmnt}/android

		# Create some needed paths on tmpfs
		mkdir -p ${rootmnt}/android/data ${rootmnt}/android/system

		# Mounting userdata
		mkdir -p ${rootmnt}/android/userdata
		mount --move /tmpmnt ${rootmnt}/android/userdata
		mkdir -p ${rootmnt}/android/userdata/android-data

		# All *three* places that the fake userdata needs to be mounted
		mount -o bind ${rootmnt}/android/userdata/android-data ${rootmnt}/android/data
		mount -o bind ${rootmnt}/android/userdata/android-data /android-rootfs/data
		[ ! -h ${rootmnt}/data ] && ln -sf /android/data ${rootmnt}/data

		set_halium_version_properties ${rootmnt} ${rootmnt}/android/userdata/android-data

		# Get device information
		device=$(grep ^ro.product.device= /android-system/build.prop | sed -e 's/.*=//')
		[ -z "$device" ] && device="unknown" && tell_kmsg "WARNING: Didn't find a device name. Is the Android system image mounted correctly?"
		tell_kmsg "device is $device"

		process_bind_mounts

		mount --move /android-rootfs ${rootmnt}/var/lib/lxc/android/rootfs

		# Mount all the Android partitions
		mount_android_partitions "${rootmnt}/var/lib/lxc/android/rootfs/fstab*" ${rootmnt}/android

		# system is a special case
		tell_kmsg "moving Android system to /android/system"
		mount --move /android-system ${rootmnt}/android/system

		# halium overlay available in the Android system image (hardware specific configs)
		if [ -e ${rootmnt}/android/system/halium ]; then
			mount_halium_overlay ${rootmnt}/android/system/halium ${rootmnt}
		fi

		# Apply device-specific udev rules
		if [ -e ${rootmnt}/usr/lib/lxc-android-config/70-$device.rules ] && 
			[ ! -f ${rootmnt}/android/system/halium/lib/udev/rules.d/70-android.rules ] && 
			[ "$device" != "unknown" ]; then
			mount --bind ${rootmnt}/usr/lib/lxc-android-config/70-$device.rules ${rootmnt}/lib/udev/rules.d/70-android.rules
		fi

		# Bind-mount /lib/modules from Android
		mount_kernel_modules

		# Bind-mount /var/lib/ureadahead if available on persistent storage
		# this is required because ureadahead runs before mountall
		if [ -e ${rootmnt}/android/userdata/system-data/var/lib/ureadahead ] &&
			[ -e ${rootmnt}/var/lib/ureadahead ]; then
			mount --bind ${rootmnt}/android/userdata/system-data/var/lib/ureadahead ${rootmnt}/var/lib/ureadahead
		fi

		# Setup the swap device
		[ -e ${rootmnt}/android/userdata/SWAP.img ] && swapon ${rootmnt}/android/userdata/SWAP.img

		# Apply customized content
		for user in ${rootmnt}/android/userdata/user-data/*; do
			if [ -d ${rootmnt}/custom/home ] && [ ! -e "$user/.customized" ]; then
				tell_kmsg "copying custom content tp "
				cp -Rap ${rootmnt}/custom/home/* "$user/"
				cp -Rap ${rootmnt}/custom/home/.[a-zA-Z0-9]* "$user/"
				touch "$user/.customized"
				dstown=$(stat -c "%u:%g" "$user")
				chown -R $dstown "$user/"
			fi
		done

	else
		# Possibly a re-partitioned device
		halium_panic "Couldn't find a system partition."
	fi

	[ "$quiet" != "y" ] && log_begin_msg "Running /scripts/local-bottom"
	run_scripts /scripts/local-bottom
	[ "$quiet" != "y" ] && log_end_msg
}
