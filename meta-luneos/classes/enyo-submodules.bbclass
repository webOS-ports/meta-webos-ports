SRC_URI += "\
  ${ENYOJS_GIT_REPO}/enyo;protocol=git;name=enyo;destsuffix=git/enyo \
  ${ENYOJS_GIT_REPO}/layout;protocol=git;name=layout;destsuffix=git/lib/layout \
  ${ENYOJS_GIT_REPO}/moonraker;protocol=ssh;name=moonraker;destsuffix=git/lib/moonraker \
  ${ENYOJS_GIT_REPO}/onyx;protocol=git;name=onyx;destsuffix=git/lib/onyx \
  ${ENYOJS_GIT_REPO}/spotlight;protocol=ssh;name=spotlight;destsuffix=git/lib/spotlight \
"
SRCREV_FORMAT="main"
SRCREV_enyo = "7d08cbf035ea25985788f5456e4b3241f8e70740"
SRCREV_layout = "14a7d8e3e622126b635ae1a79031a50378390c11"
SRCREV_moonraker = "700338ab3ed06f76bd5047ee663861f3aa7acbdd"
SRCREV_onyx = "bc69f798b6dbc40a9f7baaa797ac8f36d63df0bc"
SRCREV_spotlight = "1d424af2f59ead9aab9af1d508b3895ebcaaa200"

do_configure_prepend() {
    SUBMODULES="enyo lib/onyx lib/layout lib/spotlight lib/moonraker"
    # check that all our "submodules" exist in upstream repo
    for SUBMODULE in $SUBMODULES; do
        git submodule | grep -qw "$SUBMODULE" || \
          SUBMODULES_ERRORS="${SUBMODULES_ERRORS}ERROR: '$SUBMODULE' does not exist as submodule in upstream repository\n"
    done
    # check that all submodules in upstream repo exist in your checkout
    # command grouping is needed for SUBMODULES_ERRORS
    git submodule | sed 's/^[- ]//g' | {
        while read SUBMODULE_REV SUBMODULE_PATH; do
            case $SUBMODULE_PATH in
                enyo)          CHECKOUT_REVISION="${SRCREV_enyo}" ;;
                lib/onyx)      CHECKOUT_REVISION="${SRCREV_onyx}" ;;
                lib/layout)    CHECKOUT_REVISION="${SRCREV_layout}" ;;
                lib/spotlight) CHECKOUT_REVISION="${SRCREV_spotlight}" ;;
                lib/moonraker) CHECKOUT_REVISION="${SRCREV_moonraker}" ;;
                *)             CHECKOUT_REVISION="" ;;
            esac

            if [ -z "$CHECKOUT_REVISION" ] ; then
                SUBMODULES_ERRORS="${SUBMODULES_ERRORS}ERROR: '$SUBMODULE_PATH' exists as submodule in upstream repository, but not in our checkout\n"
            elif [ "$CHECKOUT_REVISION" != "$SUBMODULE_REV" ] ; then
                SUBMODULES_ERRORS="${SUBMODULES_ERRORS}ERROR: '$SUBMODULE_PATH' SRCREV '$CHECKOUT_REVISION' does not match submodule '$SUBMODULE_REV'\n"
            fi
        done
        # stop if we have some errors already
        if [ -n "$SUBMODULES_ERRORS" ] ; then
            printf "$SUBMODULES_ERRORS";
            exit 1;
        fi
    }
}
