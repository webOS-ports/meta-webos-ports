SUMMARY = "SmugMug connector for Synergy PHOTO.UPLOAD"
DESCRIPTION = "SmugMug account type (OAuth 1.0a), its com.palm.service.smugmug service and the com.palm.app.smugmug-auth sign-in app. Photos only."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "smugmug"
CLOUD_SERVICE = "com.palm.service.smugmug"
CLOUD_TEMPLATE = "com.palm.smugmug"
CLOUD_ACCOUNT_DIR = "account/com.palm.smugmug"
CLOUD_AUTH_APP = "com.palm.app.smugmug-auth"

# Not merged into luneos-synergy-connectors main yet (pull request #12), so the pin in
# luneos-synergy-connectors-srcrev.inc has no cloud/smugmug. Until it does, this recipe alone
# builds from that pull request's branch; cloud/_core there is the same as at the pin. Drop
# these two lines when the pull request is merged and LSC_SRCREV is bumped past it.
LSC_BRANCH = "herrie/smugmug"
LSC_SRCREV = "3de58ada0c73aba0459fdcbfcc13e759d4e6f692"
