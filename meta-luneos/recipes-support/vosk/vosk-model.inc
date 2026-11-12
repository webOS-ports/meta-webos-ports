# A Vosk speech model for one language, from Alpha Cephei's list
# (https://alphacephei.com/vosk/models), installed as
# ${datadir}/vosk/<language>. A recipe sets VOSK_MODEL (the archive's name
# without .zip), VOSK_LANGUAGE (the folder: a BCP 47 tag) and the archive's
# sha256.
HOMEPAGE = "https://alphacephei.com/vosk/models"
SECTION = "multimedia"
LICENSE = "Apache-2.0"
# The models' licences are in Alpha Cephei's list, pinned here; the
# recipe's VOSK_LICENSE_LINE is the model's line in it.
VOSK_SPACE_REV = "f10f63bc6d751cf5c4198a7dc88515f40f2d7020"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/models.md;beginline=${VOSK_LICENSE_LINE};endline=${VOSK_LICENSE_LINE};md5=${VOSK_LICENSE_MD5}"

SRC_URI = " \
    https://alphacephei.com/vosk/models/${VOSK_MODEL}.zip;name=model \
    https://raw.githubusercontent.com/alphacep/vosk-space/${VOSK_SPACE_REV}/models.md;name=license \
"
SRC_URI[license.sha256sum] = "be3599963ee12a6424f194c50f5358e190a19f125a27611f77b590549d8e4915"

S = "${UNPACKDIR}/${VOSK_MODEL}"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${datadir}/vosk/${VOSK_LANGUAGE}
    cp -R --no-preserve=ownership ${S}/. ${D}${datadir}/vosk/${VOSK_LANGUAGE}/
}

FILES:${PN} = "${datadir}/vosk/${VOSK_LANGUAGE}"
RPROVIDES:${PN} = "vosk-model"
