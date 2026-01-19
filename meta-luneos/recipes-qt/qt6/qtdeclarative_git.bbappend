# Copyright (c) 2014-2023 LG Electronics, Inc.

inherit webos_qt_global

EXTENDPRAUTO:append = "webos85"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PATCHTOOL = "git"

# Upstream-Status: Submitted
SRC_URI:append = " \
    file://0001-Check-if-a-device-in-knownPointingDevices-is-destroy.patch \
"

# On ARMv7, avoid mat4 in shaders uniform structures, as it is not supported
# by the hardware.
# Idea is to replace mat4 by vec4[4] and to cast back to mat4 during compute
do_sed_uniform_mat4() {
  :
# patching is not needed on other MACHINEs
}
do_sed_uniform_mat4:armv7a() {
  cd ${S}
  for glsl_file in $(git grep -l "mat4 " -- "*.vert" "*.frag") ; do
    sed -i -E 's/mat4 ([a-zA-Z_]+);/vec4 \1[4];/g' ${glsl_file}
    sed -i -E 's/ (matrix|qt_Matrix|qt_ubuf.qt_Matrix|projectionMatrix|modelViewMatrix|gradientMatrix|ubuf.matrix|ubuf.rotation|ubuf.gradientMatrix|ubuf.fillMatrix|ubuf.qt_Matrix) \* / mat4(\1[0],\1[1],\1[2],\1[3]) * /g' ${glsl_file}
    sed -i -E 's/(ubuf.gradientMatrix|ubuf.fillMatrix) \* vertexCoord/mat4(\1[0],\1[1],\1[2],\1[3]) * vertexCoord/g' ${glsl_file}
    sed -i -E 's/mat4 matrix = (ubuf.matrix);/mat4 matrix = mat4(\1[0],\1[1],\1[2],\1[3]);/' ${glsl_file}
# Do not replace the "multi-view" declarations, as arrays of arrays aren't supported for old GLSL.
# However this is not an issue, because multi view shader aren't supported either on our old devices.
  done
}
addtask do_sed_uniform_mat4 before do_configure after do_patch

# Supplement tool for qmllint
inherit webos_qmake6_paths
DEPENDS:append:class-native = " python3-regex-native"
SRC_URI:append:class-native = " file://qmllint-supplement.py"
do_install:append:class-native() {
    install -m 755 ${WORKDIR}/qmllint-supplement.py ${D}${OE_QMAKE_PATH_QT_BINS}
}

# TODO: To workaround the build issue where a recipe that depends on
# qtdeclarative fails at do_configure with CMake errors like:
# The imported target "Qt6::qmltyperegistrar" references the file
# ".../recipe-sysroot/usr/libexec/qmltyperegistrar"
# The imported target "Qt6::qmldom" references the file
# ".../recipe-sysroot/usr/bin/qmldom"
SYSROOT_DIRS:append = " \
    ${bindir} \
    ${libexecdir} \
"
