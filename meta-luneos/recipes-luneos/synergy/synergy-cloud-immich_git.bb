SUMMARY = "Immich connector for Synergy PHOTO.UPLOAD"
DESCRIPTION = "Immich (self-hosted photo server) account type: the server's address and an API key, its com.palm.service.immich service and the com.palm.app.immich-auth sign-in app. Photos only."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "immich"
CLOUD_SERVICE = "com.palm.service.immich"
CLOUD_TEMPLATE = "com.palm.immich"
CLOUD_ACCOUNT_DIR = "account/com.palm.immich"
CLOUD_AUTH_APP = "com.palm.app.immich-auth"
