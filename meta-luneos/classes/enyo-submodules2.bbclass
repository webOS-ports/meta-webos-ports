SRC_URI += "\
  ${ENYOJS_GIT_REPO}/enyo;protocol=git;name=enyo;destsuffix=git/enyo \
  ${ENYOJS_GIT_REPO}/canvas;protocol=git;name=canvas;destsuffix=git/lib/canvas \
  ${ENYOJS_GIT_REPO}/extra;protocol=git;name=extra;destsuffix=git/lib/extra \
  ${ENYOJS_GIT_REPO}/g11n;protocol=git;name=g11n;destsuffix=git/lib/g11n \
  ${ENYOJS_GIT_REPO}/layout;protocol=git;name=layout;destsuffix=git/lib/layout \
  ${ENYOJS_GIT_REPO}/onyx;protocol=git;name=onyx;destsuffix=git/lib/onyx \
"
SRCREV_FORMAT="main"
SRCREV_enyo = "fb50b39b9b614b98ece05b19edc96abca605c3f6"
SRCREV_canvas = "de5cb5106ee9857d372c66ab0fd4b47173e6e1b0"
SRCREV_extra = "1755d9c48606bec4f70a48dc160b0ebb2cc51bbb"
SRCREV_g11n = "efd279a517662404aeb97b05f677ffed04b8e88f"
SRCREV_layout = "246692a3505898ff361523ed8b545592c261eb83"
SRCREV_onyx = "e5873bca7ba5111f58cf99fb5d66fb17d5a5bbce"

do_configure_prepend() {
    SUBMODULES="enyo lib/canvas lib/extra lib/g11n lib/layout lib/onyx"
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
                enyo)       CHECKOUT_REVISION="${SRCREV_enyo}" ;;
                lib/canvas) CHECKOUT_REVISION="${SRCREV_canvas}" ;;
                lib/extra)  CHECKOUT_REVISION="${SRCREV_extra}" ;;
                lib/g11n)   CHECKOUT_REVISION="${SRCREV_g11n}" ;;
                lib/layout) CHECKOUT_REVISION="${SRCREV_layout}" ;;
                lib/onyx)   CHECKOUT_REVISION="${SRCREV_onyx}" ;;
                *)          CHECKOUT_REVISION="" ;;
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
