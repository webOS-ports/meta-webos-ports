SUMMARY = "SmugMug connector for Synergy PHOTO.UPLOAD"
DESCRIPTION = "SmugMug account type (OAuth 1.0a), its com.palm.service.smugmug service and the com.palm.app.smugmug-auth sign-in app. Photos only."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "smugmug"
CLOUD_SERVICE = "com.palm.service.smugmug"
CLOUD_TEMPLATE = "com.palm.smugmug"
CLOUD_ACCOUNT_DIR = "account/com.palm.smugmug"
CLOUD_AUTH_APP = "com.palm.app.smugmug-auth"

# Merged into luneos-synergy-connectors main (pull request #12) after the pin in
# luneos-synergy-connectors-srcrev.inc, so this recipe alone builds from main at the merged
# commit. Drop these two lines when LSC_SRCREV is bumped past it.
LSC_BRANCH = "main"
LSC_SRCREV = "3de58ada0c73aba0459fdcbfcc13e759d4e6f692"
