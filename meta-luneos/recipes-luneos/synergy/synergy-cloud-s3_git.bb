SUMMARY = "S3-compatible storage connector for Synergy DOCUMENTS and PHOTO.UPLOAD"
DESCRIPTION = "Account type for S3-compatible object storage (AWS Signature V4 with the user's own keys), its com.palm.service.s3 service and the com.palm.app.s3-auth key entry app."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "s3"
CLOUD_SERVICE = "com.palm.service.s3"
CLOUD_TEMPLATE = "com.palm.s3"
CLOUD_ACCOUNT_DIR = "account"
CLOUD_AUTH_APP = "com.palm.app.s3-auth"
