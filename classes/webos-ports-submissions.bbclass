# NOTE: -wop prefix is here to indicate the source is being modified by the webos-ports project
# and is different to what openwebos provides.
GIT-PREFIX = "wop"

# The default repo name is the "base" component name (no -native, etc.)
WEBOS_PORTS_REPO_NAME ??= "${BPN}"
WEBOS_PORTS_BRANCH ??= "webOS-ports/master"

SRC_URI = "git://github.com/webOS-ports/${WEBOS_PORTS_REPO_NAME};branch=${WEBOS_PORTS_BRANCH}"
