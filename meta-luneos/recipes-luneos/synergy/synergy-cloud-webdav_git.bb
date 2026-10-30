SUMMARY = "WebDAV connector (Nextcloud, ownCloud, any WebDAV server) for Synergy DOCUMENTS and PHOTO.UPLOAD"
DESCRIPTION = "Account type for Nextcloud, ownCloud and any WebDAV server (HTTP Basic auth with the user's own password or app password), its com.palm.service.webdav service and the com.palm.app.webdav-auth sign-in app."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "webdav"
CLOUD_SERVICE = "com.palm.service.webdav"
CLOUD_TEMPLATE = "com.palm.webdav"
CLOUD_ACCOUNT_DIR = "account"
CLOUD_AUTH_APP = "com.palm.app.webdav-auth"

# Merged into luneos-synergy-connectors main (pull request #15) after the pin in
# luneos-synergy-connectors-srcrev.inc, so this recipe alone builds from main at the merged
# commit. Drop these two lines when LSC_SRCREV is bumped past it.
LSC_BRANCH = "main"
LSC_SRCREV = "867ec909a0c53320aa91625f38032d13e2758648"
