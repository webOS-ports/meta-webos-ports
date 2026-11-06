SUMMARY = "WebDAV connector (Nextcloud, ownCloud, any WebDAV server) for Synergy DOCUMENTS and PHOTO.UPLOAD"
DESCRIPTION = "Account type for Nextcloud, ownCloud and any WebDAV server (HTTP Basic auth with the user's own password or app password), its com.palm.service.webdav service and the com.palm.app.webdav-auth sign-in app."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "webdav"
CLOUD_SERVICE = "com.palm.service.webdav"
CLOUD_TEMPLATE = "com.palm.webdav"
CLOUD_ACCOUNT_DIR = "account"
CLOUD_AUTH_APP = "com.palm.app.webdav-auth"
