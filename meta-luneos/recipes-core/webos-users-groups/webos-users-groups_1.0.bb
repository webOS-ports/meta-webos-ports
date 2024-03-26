DESCRIPTION = "User and group configuration for webOS"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit allarch
inherit useradd

USERADD_PACKAGES = "${PN}"

USERADD_PARAM:${PN} = " \
    -u 1001 -d /var -s /usr/sbin/nologin -U radio ;\
    -u 1003 -d /var -s /usr/sbin/nologin -G video -U graphics ;\
    -u 1007 -d /var -s /usr/sbin/nologin -U log ;\
    -u 1022 -d /var -s /usr/sbin/nologin -U security ;\
    -u 1009 -d /var -s /usr/sbin/nologin -G disk -U sdcard ;\
    -u 1018 -d /var -s /usr/sbin/nologin -G disk -U usb ;\
    -u 1019 -d /var -s /usr/sbin/nologin -U drm ;\
"

GROUPMEMS_PARAM:${PN} = " \
    -a system -g input; \
    -a system -g disk; \
    -a system -g audio; \
    -a graphics -g video; \
    -a system -g video; \
    -a system -g netdev; \
    -a nobody -g nogroup; \
"

ALLOW_EMPTY:${PN} = "1"
