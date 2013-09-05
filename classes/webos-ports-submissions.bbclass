# NOTE: -wop prefix is here to indicate the source is being modified by the webos-ports project
# and is different to what openwebos provides.
GIT-PREFIX = "wop"

# The default repo name is the "base" component name (no -native, etc.)
WEBOS_PORTS_BRANCH ??= "webOS-ports/master"
WEBOS_GIT_PARAM_BRANCH = "${WEBOS_PORTS_BRANCH}"

# we don't use tags for webOS-ports repos
WEBOS_GIT_TAG = ""

SRC_URI = "git://github.com/webOS-ports/${WEBOS_REPO_NAME}${WEBOS_GIT_TAG}${WEBOS_GIT_BRANCH}"

python() {
    if not bb.data.inherits_class('webos_enhanced_submissions', d):
        file = d.getVar('FILE', True)
        bb.fatal("%s: inherit webos_enhanced_submissions when the recipe is using webos-ports-submissions" % file)
}
