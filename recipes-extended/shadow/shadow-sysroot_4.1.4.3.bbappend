# update login.defs to use UID_MIN > 10 000, lower numbers are reserved for android-system
# http://www.netmite.com/android/mydroid/system/core/include/private/android_filesystem_config.h
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

# As we're overriding login.defs_shadow-sysroot we have to readd the checksum for it here
# as it's containing the license for this component
LIC_FILES_CHKSUM = "file://login.defs_shadow-sysroot;md5=0b6fdfc8cdf9dbf137ba229ac93e4142"
