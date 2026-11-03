SUMMARY = "PhotoPrism connector for Synergy PHOTO.UPLOAD"
DESCRIPTION = "PhotoPrism (self-hosted photo server) account type: the server's address, a user name and a password (kept as an app password), its com.palm.service.photoprism service and the com.palm.app.photoprism-auth sign-in app. Photos only."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "photoprism"
CLOUD_SERVICE = "com.palm.service.photoprism"
CLOUD_TEMPLATE = "com.palm.photoprism"
CLOUD_ACCOUNT_DIR = "account/com.palm.photoprism"
CLOUD_AUTH_APP = "com.palm.app.photoprism-auth"
