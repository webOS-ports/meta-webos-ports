DESCRIPTION = "User and group configuration for webOS"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit allarch
inherit useradd

USERADD_PACKAGES = "${PN}"

USERADD_PARAM:${PN} = " \
                       -u 504 -r -g 504 -c developer -d /home/developer -s /bin/sh developer -G developer;\
                       -u 505 -r -g 505 -c wam -d /var/lib/wam -s /bin/false -G audio,video,compositor,pulse-access,se,media wam; \
                       -u 507 -r -g 507 -c pulse -d /var/run/pulse -s /bin/false -G audio,pulse pulse ;\
                       -u 1000 -g 1000 -d /var -s /bin/false -G system system ;\
                       -u 1001 -g 1001 -d /var -s /bin/false radio ;\
                       -u 1002 -g 1002 -d /var -s /bin/false -G netdev bluetooth ;\
                       -u 1003 -g 1003 -d /var -s /bin/false -G video,compositor graphics ;\
                       -u 1006 -g 1006 -d /var -s /bin/false -G video camera ;\
                       -u 1007 -g 1007 -d /var -s /bin/false log ;\
                       -u 1022 -g 1022 -d /var -s /bin/false security ;\
                       -u 1009 -g 1009 -d /var -s /bin/false -G disk sdcard ;\
                       -u 1010 -g 1010 -d /var -s /bin/false -G netdev wifi ;\
                       -u 1013 -g 1013 -d /var -s /bin/false -G video,compositor,media media ;\
                       -u 1018 -g 1018 -d /var -s /bin/false -G disk usb ;\
                       -u 1019 -g 1019 -d /var -s /bin/false drm ;\
                       -u 1025 -g 1025 -d /var -s /bin/false -G netdev network ;\
                       -u 2003 -g 2003 -d /var -s /bin/false debug ;\
                       -u 2004 -g 2004 -d /var -s /bin/false -G developer dev-func ;\
                       "

GROUPADD_PARAM:${PN} = " \
                        -g 81 dbus; \
                        -g 82 netdev; \
                        -g 504 developer; \
                        -g 505 compositor; \
                        -g 506 pulse-access; \
                        -g 507 pulse; \
                        -g 509 se; \
                        -g 1000 system; \
                        -g 1001 radio; \
                        -g 1002 bluetooth ;\
                        -g 1003 graphics ;\
                        -g 1004 input ;\
                        -g 1006 camera ;\
                        -g 1007 log ;\
                        -g 1022 security ; \
                        -g 1009 sdcard ;\
                        -g 1010 wifi ;\
                        -g 1013 media ;\
                        -g 1018 usb ;\
                        -g 1019 drm ;\
                        -g 1025 network ;\
                        -g 2003 debug ;\
                        -g 2004 dev-fund ;\
                        -g 2024 blemesh; \
                        "

inherit extrausers 

EXTRA_USERS_PARAMS = "\
    usermod -aG audio system; \
    usermod -aG input,wam input; \
    usermod -aG graphics,system,camera,media video; \
    usermod -aG system,network,bluetooth,wifi netdev; \
    usermod -aG graphics,media compositor; \
    usermod -aG root,system pulse-access; \
    usermod -aG root,system se; \
    usermod -aG nobody nogroup; \
    "