FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
    --with-default-hybris-ld-library-path=/usr/libexec/hal-droid/system/lib \
"

SRC_URI += " \
            file://0001-hooks-include-mm-hooks-only-when-WANT_LINKER_MM-is-d.patch;striplevel=2 \
            file://0002-hooks-fix-crash-with-__get_tls_hooks.patch;striplevel=2 \
            file://0003-Add_support_for_hwcomposer_newer_versions.patch;striplevel=2 \
            "

SRCREV = "240ae5681a46c5c722b26eadd0ad8b53955cb869"
