# not compatible with libhybris
PACKAGECONFIG_remove_class-target = "${@oe.utils.conditional('PREFERRED_PROVIDER_virtual/egl', 'libhybris', 'egl wayland', '', d)}"
