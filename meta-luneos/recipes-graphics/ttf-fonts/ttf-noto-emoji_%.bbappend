# Unicode 18.0, not the Unicode 13 meta-oe pins. An emoji the font does not
# cover is a tofu box, not a fallback: on the pinned revision U+1F600 drew but
# U+1FAE0 (Unicode 14) did not. One current font makes emoji simply text for
# Chromium apps, QML apps, the shell and the keyboard alike.
#
# The modern repository keeps some fonts in git-LFS, but only 3D/fonts/*.ttf
# (see its .gitattributes); 2D/fonts/NotoColorEmoji.ttf, the one file
# installed below, is ordinary git content. Without lfs=0 the fetcher refuses
# the whole repository on a build host without git-lfs ("Repository ... has
# LFS content, install git-lfs on host"), which is how the aarch64 build VM
# failed. lfs=0 also skips downloading the unused 3D fonts.
SRC_URI = "git://github.com/googlefonts/noto-emoji;branch=main;protocol=https;lfs=0"
SRCREV = "e20cbc2bbec1926686be9f9bee7d1d2cfa1fea0e"
PV = "2026-09-24-unicode18.0"

# The tree was reorganised: the licence is at the root rather than under fonts/.
LIC_FILES_CHKSUM = "file://LICENSE;md5=cdc5040ed1e8cf5d3516f5285fd7b636"

# ttf.inc installs every .ttf in the tree, flattened, which is now a dozen
# builds of the same font. Install the one meant by "the emoji font".
do_install() {
    install -d ${D}${datadir}/fonts/truetype/
    install -m 0644 ${S}/2D/fonts/NotoColorEmoji.ttf ${D}${datadir}/fonts/truetype/
}

# The monochrome face moved to notofonts/notoemoji, so that package would be
# empty.
PACKAGES = "${PN}-color"
FONT_PACKAGES = "${PN}-color"
