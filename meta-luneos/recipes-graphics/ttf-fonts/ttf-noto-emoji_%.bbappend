# Unicode 18.0, not Unicode 13.
#
# meta-oe pins a September 2020 revision, which stops around Unicode 13 - and an
# emoji the font does not cover is a tofu box, not a fallback. Measured on a Q25
# with the pinned revision installed: U+1F600 drew a colour glyph while U+1FAE0
# (melting face, Unicode 14) came out as the same empty box as an unassigned
# private-use codepoint.
#
# This is also why LuneOS has had no emoji to speak of, and why the Messaging app
# carries bundled EmojiOne images for the ones it receives: nothing on the device
# could draw them. One current font fixes that for every consumer at once -
# Chromium apps, QML apps, the shell and the keyboard - because an emoji is then
# simply text, which is what legacy's seven ASCII emoticons never were.
#
# The modern repository keeps its fonts in git-LFS, so the build host needs
# git-lfs installed; without it the fetch fails rather than quietly leaving a
# pointer file behind.
SRCREV = "e20cbc2bbec1926686be9f9bee7d1d2cfa1fea0e"
PV = "2026-09-24-unicode18.0"

# The tree was reorganised: the licence is at the root rather than under fonts/.
LIC_FILES_CHKSUM = "file://LICENSE;md5=cdc5040ed1e8cf5d3516f5285fd7b636"

# And ttf.inc's install is "every .ttf in the tree, flattened", which now means
# a dozen builds of the same font - flags-only, no-flags, Windows-compatible,
# emojicompat, the COLRv1 cut and the 3D set. Install the one that is meant by
# "the emoji font" and leave the rest where they are.
do_install() {
    install -d ${D}${datadir}/fonts/truetype/
    install -m 0644 ${S}/2D/fonts/NotoColorEmoji.ttf ${D}${datadir}/fonts/truetype/
}

# NotoEmoji-Regular.ttf, the monochrome outline face, is no longer in this
# repository - it moved to notofonts/notoemoji - so the package that held it
# would ship nothing.
PACKAGES = "${PN}-color"
FONT_PACKAGES = "${PN}-color"
