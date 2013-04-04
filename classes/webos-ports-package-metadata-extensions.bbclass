# Set one of the two variables to 1 to enable the needed restart option.
PACKAGE_NEEDS_DEVICE_RESTART ?= "0"
PACKAGE_NEEDS_LUNA_RESTART ?= "0"

# Make sure nobody else has modified this variable before
PACKAGE_ADD_METADATA_IPK = ""

python() {
    flags = ""
    if d.getVar('PACKAGE_NEEDS_DEVICE_RESTART', True):
        flags += "RestartDevice"
    if d.getVar('PACKAGE_NEEDS_LUNA_RESTART', True):
        if len(flags) > 0:
            flags += ", "
        flags += "RestartLuna"
    if len(flags) > 0:
        metadata = "Tags: %s" % flags
        d.setVar('PACKAGE_ADD_METADATA_IPK', metadata)
}
