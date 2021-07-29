FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://0001-Use-google-as-default-and-add-api-key.patch"

# Avoid putting a dependency on modemmanager
PACKAGECONFIG = "nmea lib"
