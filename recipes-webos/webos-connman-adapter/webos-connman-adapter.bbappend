FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

# NOTE: The following patch can be dropped once the change is included in upstream
SRC_URI += "file://0001-Start-daemon-on-different-upstart-event-and-not-in-d.patch"
