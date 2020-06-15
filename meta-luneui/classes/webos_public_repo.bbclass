# Copyright (c) 2012-2013 LG Electronics, Inc.
#
# webos_public_repo
#
# This class is to be inherited by the recipe for every component that has (or
# will have) a public Open webOS repo.
#

# The default repo name is the "base" component name (no -native, etc.)
WEBOS_REPO_NAME ??= "${BPN}"

WEBOS_GIT_PARAM_BRANCH ?= "master"
WEBOS_GIT_BRANCH ?= ";branch=${WEBOS_GIT_PARAM_BRANCH}"
# Default is empty
WEBOS_GIT_PARAM_TAG ?= ""
# WEBOS_GIT_TAG ?= ";tag=${WEBOS_GIT_PARAM_TAG}"
WEBOS_GIT_TAG ?= ""

OPENWEBOS_GIT_REPO ?= "git://github.com/openwebos"
OPENWEBOS_GIT_REPO_COMPLETE ?= "${OPENWEBOS_GIT_REPO}/${WEBOS_REPO_NAME}${WEBOS_GIT_TAG}${WEBOS_GIT_BRANCH}"

ENYOJS_GIT_REPO ?= "git://github.com/enyojs"
ENYOJS_GIT_REPO_COMPLETE ?= "${ENYOJS_GIT_REPO}/${WEBOS_REPO_NAME}${WEBOS_GIT_TAG}${WEBOS_GIT_BRANCH}"

WEBOSOSE_GIT_REPO ?= "git://github.com/webosose"
WEBOSOSE_GIT_REPO_COMPLETE ?= "${WEBOSOSE_GIT_REPO}/${WEBOS_REPO_NAME}${WEBOS_GIT_TAG}${WEBOS_GIT_BRANCH}"

WEBOSOSE_GIT_PARAM_PROTOCOL ?= "git"
WEBOSOSE_GIT_PROTOCOL = ";protocol=${WEBOSOSE_GIT_PARAM_PROTOCOL}"
