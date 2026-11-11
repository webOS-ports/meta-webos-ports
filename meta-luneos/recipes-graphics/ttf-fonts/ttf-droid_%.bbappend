# Arabic and Hebrew. Nothing in the image had Arabic glyphs, so Arabic text -
# the keyboard's own "العربية" in its language menu, a contact, a song - was a
# row of tofu boxes, and Hebrew came out in Liberation Mono, the one font that
# happened to cover it. meta-oe fetches Droid Sans Arabic and Droid Sans Hebrew
# with the rest of the Droid fonts (whose ttf-droid-sans-fallback the image
# already ships for CJK) and then deletes them; put them back, each in a
# package of its own. 36KB and 23KB.
do_install:append() {
    for f in DroidSansArabic.ttf DroidSansHebrew.ttf; do
        install -m 0644 ${S}/$f ${D}${datadir}/fonts/truetype/
    done
}

PACKAGES += "ttf-droid-sans-arabic ttf-droid-sans-hebrew"
FONT_PACKAGES += "ttf-droid-sans-arabic ttf-droid-sans-hebrew"

FILES:ttf-droid-sans-arabic = "${datadir}/fonts/truetype/DroidSansArabic.ttf"
FILES:ttf-droid-sans-hebrew = "${datadir}/fonts/truetype/DroidSansHebrew.ttf"
