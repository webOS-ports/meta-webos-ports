SUMMARY = "PhotoPrism connector for Synergy PHOTO.UPLOAD"
DESCRIPTION = "PhotoPrism (self-hosted photo server) account type: the server's address, a user name and a password (kept as an app password), its com.palm.service.photoprism service and the com.palm.app.photoprism-auth sign-in app. Photos only."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "photoprism"
CLOUD_SERVICE = "com.palm.service.photoprism"
CLOUD_TEMPLATE = "com.palm.photoprism"
CLOUD_ACCOUNT_DIR = "account/com.palm.photoprism"
CLOUD_AUTH_APP = "com.palm.app.photoprism-auth"

# Not merged into luneos-synergy-connectors main yet (pull request #17), so the pin in
# luneos-synergy-connectors-srcrev.inc has no cloud/photoprism. Until it does, this recipe alone
# builds from that pull request's branch (rebased on main: its cloud/_core/service is the pin's; only cloud/<x> is packaged here). Drop
# these two lines when the pull request is merged and LSC_SRCREV is bumped past it.
LSC_BRANCH = "herrie/photoprism"
LSC_SRCREV = "fdd41d05d340d3de799923fe26ecdc8fbcf0438f"
