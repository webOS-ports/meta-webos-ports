#
# Makefile for the linux squashfs routines.
#

obj-m += squashfs.o
squashfs-y += inode.o
squashfs-y += squashfs2_0.o

all:
	$(MAKE) -C $(KERNEL_SRC) V=0 M=$$PWD

modules_install:
	$(MAKE) -C $(KERNEL_SRC) M=$$PWD modules_install
