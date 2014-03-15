# The default repo name is the "base" component name (no -native, etc.)
WEBOS_REPO_NAME ??= "${BPN}"

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/master"
WEBOS_GIT_BRANCH ?= ";branch=${WEBOS_GIT_PARAM_BRANCH}"
# Default is empty but webos_enhanced_submissions.bbclass can set the value
WEBOS_GIT_PARAM_TAG ?= ""
# we don't use tags for webOS-ports repos
# WEBOS_GIT_TAG ?= ";tag=${WEBOS_GIT_PARAM_TAG}"
WEBOS_GIT_TAG = ""

SRC_URI = "git://github.com/webOS-ports/${WEBOS_REPO_NAME}${WEBOS_GIT_TAG}${WEBOS_GIT_BRANCH}"
