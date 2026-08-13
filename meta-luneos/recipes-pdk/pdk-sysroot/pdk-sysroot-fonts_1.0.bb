SUMMARY = "Font names legacy webOS PDK applications open by absolute path"
DESCRIPTION = "Titles in the catalogue open fonts under /usr/share/fonts by \
absolute path and never check TTF_OpenFont for NULL, so a missing file is a \
segfault rather than ugly text. LuneOS already ships the whole Prelude family \
via luna-init; this covers the Microsoft core fonts that legacy webOS licensed \
and LuneOS does not carry, using metric-compatible substitutes."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit allarch

# Prelude comes from luna-init, which ships all 34 faces the legacy image had -
# PreludeCondensed-Medium.ttf included. Nothing to substitute and nothing to
# redistribute; just make sure it is present.
RDEPENDS:${PN} = "luna-init-fonts"

# The gap between the legacy image's 55 fonts and LuneOS's 34 is entirely the
# Microsoft core fonts (Arial, Courier New, Georgia, Times New Roman, Verdana,
# Lucida Console) plus four CJK faces. Those were licensed by Palm and cannot be
# shipped, but Liberation is metric-compatible with the three that matter -
# same advance widths, so text laid out for Arial still fits.
RDEPENDS:${PN} += "liberation-fonts"

# name.ttf -> the Liberation face with the same metrics
PDK_FONT_MAP = " \
    arial.ttf:LiberationSans-Regular.ttf \
    arialbd.ttf:LiberationSans-Bold.ttf \
    ariali.ttf:LiberationSans-Italic.ttf \
    arialbi.ttf:LiberationSans-BoldItalic.ttf \
    times.ttf:LiberationSerif-Regular.ttf \
    timesbd.ttf:LiberationSerif-Bold.ttf \
    timesi.ttf:LiberationSerif-Italic.ttf \
    timesbi.ttf:LiberationSerif-BoldItalic.ttf \
    cour.ttf:LiberationMono-Regular.ttf \
    courbd.ttf:LiberationMono-Bold.ttf \
    couri.ttf:LiberationMono-Italic.ttf \
    courbi.ttf:LiberationMono-BoldItalic.ttf \
    lucon.ttf:LiberationMono-Regular.ttf \
"

# Georgia and Verdana have no metric-compatible free equivalent. These are a
# readability fallback, not a substitute - text will reflow. Kept separate so the
# distinction stays visible to anyone reading the recipe.
PDK_FONT_MAP_APPROX = " \
    georgia.ttf:LiberationSerif-Regular.ttf \
    georgiab.ttf:LiberationSerif-Bold.ttf \
    georgiai.ttf:LiberationSerif-Italic.ttf \
    georgiaz.ttf:LiberationSerif-BoldItalic.ttf \
    verdana.ttf:LiberationSans-Regular.ttf \
    verdanab.ttf:LiberationSans-Bold.ttf \
    verdanai.ttf:LiberationSans-Italic.ttf \
    verdanabi.ttf:LiberationSans-BoldItalic.ttf \
"

# If you have a legacy device image and want the originals, point this at its
# font directory; anything found there is used in preference to a substitute.
PDK_LEGACY_FONTS ?= ""

do_install() {
    install -d ${D}${datadir}/fonts

    for pair in ${PDK_FONT_MAP} ${PDK_FONT_MAP_APPROX}; do
        want=${pair%%:*}
        sub=${pair##*:}

        if [ -n "${PDK_LEGACY_FONTS}" ] && [ -f "${PDK_LEGACY_FONTS}/$want" ]; then
            install -m 0644 "${PDK_LEGACY_FONTS}/$want" ${D}${datadir}/fonts/
        else
            # A symlink, not a copy: a reader of the image can see at a glance
            # that this is a stand-in rather than the real face.
            ln -sf ${datadir}/fonts/ttf/$sub ${D}${datadir}/fonts/$want
        fi
    done
}

FILES:${PN} = "${datadir}/fonts"

# The symlink targets belong to liberation-fonts, which is an RDEPENDS, so they
# are always installed alongside - but they are not in this package.
INSANE_SKIP:${PN} += "dev-so"
