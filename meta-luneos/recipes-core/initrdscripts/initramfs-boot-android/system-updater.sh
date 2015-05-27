#!/bin/sh

flash_kernel_on_partition() {
	kernel_image=$1

	if [ -z "$boot_partition" ] ; then
		error "Can't flash new kernel as boot partition is not specified!"
		return
	fi

	path="/dev/$boot_partition"
	if [ ! -e $path ] ; then
		error "Boot partition $path does not exist!"
		return
	fi

	info "Flashing new kernel image $kernel_image to $path ..."
	dd if=$kernel_image of=$path
}

update_rootfs() {
	target=$1
	update_package=$2
	workdir=$3

	/usr/bin/fbprogress -s 0 &

	if [ -e $workdir ] ; then
		rm -rf $workdir/*
	else
		mkdir -p $workdir
	fi

	echo 5 > /tmp/fbprogress/progress

	info "Extracting update package ..."
	unzip $update_package -d $workdir

	echo 15 > /tmp/fbprogress/progress

	if [ ! -e $workdir/webos-rootfs.tar.gz ] ; then
		fail "Update package is invalid as it doesn't contain the rootfs tarball!"
		return
	fi

	info "Applying update ..."
	(cd $target ; find . ! -name . ! -name .firstboot_done ! -name SWAP.img -exec rm -rf {} \;)
	tar xzf $workdir/webos-rootfs.tar.gz -C $target

	echo 30 > /tmp/fbprogress/progress

	# Recreate marker so we don't reinitialize our data directories
	touch $target/.firstboot_done

	# Only update kernel if we have an image for it
	if [ -e $workdir/boot.img ] ; then
		flash_kernel_on_partition $workdir/boot.img
	elif [ -e $workdir/uImage ] ; then
		error "uImage not supported yet!"
	else
		error "Not updating kernel as no supported image found"
	fi

	echo 35 > /tmp/fbprogress/progress

	# Cleanup so we don't consume any unnecessary space
	rm -rf $workdir

	echo 40 > /tmp/fbprogress/progress

	info "Done!"

	# Give fbprogress a bit to update before we kill it
	sleep 1

	kill -KILL `pidof fbprogress`
}
