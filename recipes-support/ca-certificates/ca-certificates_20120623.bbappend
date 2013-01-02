PRINC := "${@int(PRINC) + 1}"

pkg_postrm_${PN}() {
    # Remove possible installed certificates by the update-ca-certificates script
    rm -rf ${sysconfdir}/ssl/certs
}
