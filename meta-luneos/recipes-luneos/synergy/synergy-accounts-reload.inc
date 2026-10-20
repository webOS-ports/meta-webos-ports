# Make a running device see account templates as they are installed or removed.
#
# com.palm.service.accounts builds its template list once when it starts and does not rescan, so a
# template installed while it runs stays invisible to listAccountTemplates (and a removed one
# stays listed) until the service restarts. Stopping it is enough: it is an on-demand
# run-js-service, so ls-hubd starts it again on the next call and it reads the templates afresh
# ("A template was added or deleted"). The legacy webOS packaging does the same by killing
# accounts.js from its postinst/prerm.
#
# ls-control scan-services first, as luna-displaymanager and maliit-framework-webos do after
# install, so ls-hubd has read this package's luna-service2 files before anything calls it.
# Live devices only: an image build ($D set) reads all of this at first boot anyway.
#
# Packages listed in SYNERGY_ACCOUNTS_RELOAD_PACKAGES get it as pkg_postinst and pkg_postrm
# (postrm, not prerm, so that the restarted service no longer finds the removed template).

SYNERGY_ACCOUNTS_RELOAD_PACKAGES ?= "${PN}"

def synergy_accounts_reload_script():
    # Joined from a list: bitbake's parser does not take a multi-line string literal here.
    # The shebang matters when this is the whole script; appended to another one it is only a
    # comment.
    return "\n".join([
        "#!/bin/sh",
        "if [ -z \"$D\" ]; then",
        "    if command -v ls-control > /dev/null 2>&1; then",
        "        ls-control scan-services || true",
        "    fi",
        "    # run-js-service puts the service directory last on node's command line.",
        "    pkill -f '/usr/palm/services/com\\.palm\\.service\\.accounts( |$)' || true",
        "fi",
        "",
    ])

python __anonymous() {
    script = synergy_accounts_reload_script()
    for pkg in (d.getVar('SYNERGY_ACCOUNTS_RELOAD_PACKAGES') or '').split():
        for hook in ('pkg_postinst', 'pkg_postrm'):
            d.appendVar('%s:%s' % (hook, pkg), script)
}
