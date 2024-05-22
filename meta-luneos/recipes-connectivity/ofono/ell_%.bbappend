# needed for ofono-2.7
# | checking for ell >= 0.65... no
# | configure: error: Embedded Linux library >= 0.65 is required
# will be moved to backports-5.1 once ofono and ell upgrade is merged in oe-core:
# ell: https://lists.openembedded.org/g/openembedded-core/message/199692
# ofono: https://lists.openembedded.org/g/openembedded-core/message/199693
PV = "0.65"
SRC_URI[sha256sum] = "9ee7ac57b188d391cead705d3596a6d3240341786475149db297782a52269aa5"
