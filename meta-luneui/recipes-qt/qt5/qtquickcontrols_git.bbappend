FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-TextInput-forward-passwordCharacter-property-from-Te.patch \
    file://0002-Set-a-transparent-background-for-contextual-menus.patch;patch=1 \
"
