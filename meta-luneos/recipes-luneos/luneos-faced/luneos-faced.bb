SUMMARY = "LuneOS face unlock service"
DESCRIPTION = "Headless face authentication for the lockscreen. Pulls frames from \
com.webos.service.camera2 into shared memory without a preview, runs them through \
libfart (detection, MiniFASNet passive anti-spoofing, MobileFaceNet match) and \
exposes com.webos.service.faceunlock on the luna-service2 bus. \
\
Like webos-fingerprint-adapter this is a sensor adapter, not an authentication \
authority: it reports whether a face matched and owns no retry budget. That policy \
belongs in luna-authmanager once the luna-sysmgr carve-out lands."
SECTION = "webos/services"

# libfart is GPL-2.0-only and is linked in, so this has to be GPL-2 compatible.
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "luna-service2 glib-2.0 opencv openssl nlohmann-json libfart camera-utils"

# libfart pulls libtensorflow-lite and libgbinder; camera-utils provides
# libcamera_shared_memory and libluna_client.
RDEPENDS:${PN} += "libfart"
# The service is useful without the camera service running (it reports
# available=false), but pointless to ship without it.
RRECOMMENDS:${PN} += "com.webos.service.camera"

PV = "0.1.0-1+git"

SRCREV = "10afec6c02e37f62960ee0d5a52231d4c0c2a0f6"

inherit webos_ports_repo
WEBOS_GIT_PARAM_BRANCH = "master"
WEBOS_REPO_NAME = "luneos-faced"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

# opencv/libfart reach machine-specific libraries through libgbinder; keep this
# per-machine rather than tune-level.
PACKAGE_ARCH = "${MACHINE_ARCH}"
