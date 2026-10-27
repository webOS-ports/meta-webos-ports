SUMMARY = "Google Drive connector for Synergy DOCUMENTS"
DESCRIPTION = "Google Drive account type (OAuth2 + PKCE with a client secret) and its com.palm.service.gdrive service. Documents only: Google Photos cannot be reached without the interactive Picker."

require cloud-synergy-connector.inc

# The connector directory is google/; the service and template keep their gdrive names.
CLOUD_CONNECTOR = "google"
CLOUD_SERVICE = "com.palm.service.gdrive"
CLOUD_TEMPLATE = "com.palm.gdrive"
CLOUD_ACCOUNT_DIR = "account"
# config.js names its placeholders after the service, not the directory.
CLOUD_SECRET_NAME = "GDRIVE"
