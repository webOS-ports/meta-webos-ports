PACKAGES_remove = "kernel-module-vboxvideo"
do_install_append() {
    # we prefer vboxvideo from kernel staging drivers
    rm -f $MODULE_DIR/vboxvideo.ko
}
