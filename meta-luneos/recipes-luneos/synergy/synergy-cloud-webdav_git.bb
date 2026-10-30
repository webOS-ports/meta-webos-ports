SUMMARY = "WebDAV connector (Nextcloud, ownCloud, any WebDAV server) for Synergy DOCUMENTS and PHOTO.UPLOAD"
DESCRIPTION = "Account type for Nextcloud, ownCloud and any WebDAV server (HTTP Basic auth with the user's own password or app password), its com.palm.service.webdav service and the com.palm.app.webdav-auth sign-in app."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "webdav"
CLOUD_SERVICE = "com.palm.service.webdav"
CLOUD_TEMPLATE = "com.palm.webdav"
CLOUD_ACCOUNT_DIR = "account"
CLOUD_AUTH_APP = "com.palm.app.webdav-auth"

# Not merged into luneos-synergy-connectors main yet (pull request #15), so the pin in
# luneos-synergy-connectors-srcrev.inc has no cloud/webdav. Until it does, this recipe alone
# builds from that pull request's branch (rebased on main: its cloud/_core/service is the pin's; only cloud/<x> is packaged here). Drop
# these two lines when the pull request is merged and LSC_SRCREV is bumped past it.
LSC_BRANCH = "herrie/webdav"
LSC_SRCREV = "867ec909a0c53320aa91625f38032d13e2758648"
