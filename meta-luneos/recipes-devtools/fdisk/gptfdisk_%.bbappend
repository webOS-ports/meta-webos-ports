# Copyright (c) 2013-2019 LG Electronics, Inc.

PACKAGES =+ "${PN}-gdisk ${PN}-cgdisk ${PN}-fixparts ${PN}-sgdisk"
FILES:${PN}-gdisk += "${sbindir}/gdisk"
FILES:${PN}-cgdisk += "${sbindir}/cgdisk"
FILES:${PN}-sgdisk += "${sbindir}/sgdisk"
FILES:${PN}-fixparts += "${sbindir}/fixparts"