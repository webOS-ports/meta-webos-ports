FILESEXTRAPATHS_prepend := "${THISDIR}/${BP}:"

PKGV .= "-0webos1"
S = "${WORKDIR}"

# First patch is from oe-core after 1.5 (Dora) release
# Second patch is fix for not setting permissions on existing device nodes
SRC_URI += " \
    file://0002-makedevs-Add-trace-option-and-fix-permissions-on-fil.patch \
"

# S is already set to WORKDIR
do_configure() {
    :
}

# Add ${LDFLAGS}
do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -o ${S}/makedevs ${S}/makedevs.c
}
