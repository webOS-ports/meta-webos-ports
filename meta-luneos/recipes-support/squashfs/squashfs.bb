SUMMARY = "Squashfs compressed read-only filesystem"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://squashfs.h;endline=22;md5=bf92dd48469f6b45027dde7c5aeb9f0b"

inherit module

SRC_URI = " \
           file://Makefile \
           file://inode.c \
           file://squashfs.h \
           file://squashfs2_0.c \
           file://squashfs_fs.h \
           file://squashfs_fs_i.h \
           file://squashfs_fs_sb.h \
          "

S = "${WORKDIR}"
