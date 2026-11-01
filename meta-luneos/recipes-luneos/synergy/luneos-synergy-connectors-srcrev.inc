# The luneos-synergy-connectors commit LuneOS builds from. One pin for every recipe that takes
# anything from that repository: the cloud connectors and the music backend through
# luneos-synergy-connectors.inc, the libpurple plug-ins (account templates, validator apps and
# call-service bus files) through recipes-support/pidgin/purple-connectors.inc. Bump it here
# and they all move together.
#
# Pinned deliberately. AUTOREV would make every LuneOS build depend on whatever main happened to
# be that day.
#
# LSC_BRANCH is the branch LSC_SRCREV has to be on. It is normally main; it names a pull request's
# branch only while LuneOS needs that pull request before it is merged. Set it back to main, with
# LSC_SRCREV on the merge, once it is in.
#
LSC_BRANCH = "herrie/integration-testing"
LSC_SRCREV = "8aebf9169f70e76cf7326ea88170ffa61a0e99df"
