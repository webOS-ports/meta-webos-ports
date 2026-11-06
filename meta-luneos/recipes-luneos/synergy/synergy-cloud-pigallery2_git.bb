SUMMARY = "PiGallery2 connector for Synergy PHOTO.UPLOAD"
DESCRIPTION = "PiGallery2 (self-hosted, folder-based photo gallery) account type: the server's address, a user name and a password (none for a gallery with sign-in turned off), its com.palm.service.pigallery2 service and the com.palm.app.pigallery2-auth sign-in app. The gallery's folders become albums in Photos, their folders sub-albums. Photos only, read-only."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "pigallery2"
CLOUD_SERVICE = "com.palm.service.pigallery2"
CLOUD_TEMPLATE = "com.palm.pigallery2"
CLOUD_ACCOUNT_DIR = "account/com.palm.pigallery2"
CLOUD_AUTH_APP = "com.palm.app.pigallery2-auth"
