SUMMARY = "OneDrive connector for Synergy DOCUMENTS and PHOTO.UPLOAD"
DESCRIPTION = "Microsoft OneDrive account type (OAuth2 + PKCE through Microsoft Graph, public client) and its com.palm.service.onedrive service."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "onedrive"
CLOUD_SERVICE = "com.palm.service.onedrive"
CLOUD_TEMPLATE = "com.palm.onedrive"
CLOUD_ACCOUNT_DIR = "account"
# Microsoft takes a public client with PKCE; OneDrive needs no client secret.
CLOUD_SECRET_OPTIONAL = "1"
