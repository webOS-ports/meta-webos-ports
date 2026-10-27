SUMMARY = "Shared runtime and OAuth sign-in app for the Synergy cloud connectors"
DESCRIPTION = "_cloudcore/node, the node runtime every cloud connector service requires by relative \
path (the bus, an http client on fetch, OAuth2 + PKCE, credential storage through the Accounts \
service, the caller ACL and the generic file/photo commands), and com.palm.app.cloud-auth, the validator app \
that runs the OAuth sign-in for every connector that uses one. Install a synergy-cloud-<name> \
package on top to add an account type."

require cloud-synergy.inc

do_install() {
    # The connectors require ../_cloudcore/node, so this has to land next to them in webos_servicesdir. It is not a service itself: no services.json, no bus
    # name, no ACG files of its own.
    install -d ${D}${webos_servicesdir}
    cp -R --no-preserve=ownership ${S}/cloud/_core/service/_cloudcore ${D}${webos_servicesdir}/

    # The OAuth page every OAuth connector's template names as validator.customUI.appId. On
    # LuneOS it opens the provider's login in a window.open() WAM window (oauthWindow.js); the
    # inline Atlas view it uses on legacy webOS needs NPAPI, which LuneOS does not have.
    install -d ${D}${webos_applicationsdir}
    cp -R --no-preserve=ownership ${S}/cloud/_core/apps/com.palm.app.cloud-auth ${D}${webos_applicationsdir}/
}

RDEPENDS:${PN} = "${CLOUD_RUNTIME_RDEPENDS}"
