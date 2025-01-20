FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://0001-Use-Google-as-default-and-add-API-key.patch"

# Avoid putting a dependency on modemmanager
PACKAGECONFIG = "nmea lib"
