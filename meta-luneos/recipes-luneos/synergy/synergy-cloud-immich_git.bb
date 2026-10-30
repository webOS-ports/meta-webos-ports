SUMMARY = "Immich connector for Synergy PHOTO.UPLOAD"
DESCRIPTION = "Immich (self-hosted photo server) account type: the server's address and an API key, its com.palm.service.immich service and the com.palm.app.immich-auth sign-in app. Photos only."

require cloud-synergy-connector.inc

CLOUD_CONNECTOR = "immich"
CLOUD_SERVICE = "com.palm.service.immich"
CLOUD_TEMPLATE = "com.palm.immich"
CLOUD_ACCOUNT_DIR = "account/com.palm.immich"
CLOUD_AUTH_APP = "com.palm.app.immich-auth"

# Not merged into luneos-synergy-connectors main yet (pull request #13), so the pin in
# luneos-synergy-connectors-srcrev.inc has no cloud/immich. Until it does, this recipe alone
# builds from that pull request's branch (rebased on main: its cloud/_core/service is the pin's; only cloud/<x> is packaged here). Drop
# these two lines when the pull request is merged and LSC_SRCREV is bumped past it.
LSC_BRANCH = "herrie/immich"
LSC_SRCREV = "b7f9fea232edb8505dabcb3e1b04f99f139cf532"
