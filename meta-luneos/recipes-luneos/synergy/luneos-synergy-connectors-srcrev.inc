# The luneos-synergy-connectors commit LuneOS builds from. One pin for every recipe that takes
# anything from that repository: the cloud connectors and the music backend through
# luneos-synergy-connectors.inc, the libpurple plug-ins (account templates, validator apps and
# call-service bus files) through recipes-support/pidgin/purple-connectors.inc. Bump it here
# and they all move together.
#
# Pinned deliberately. AUTOREV would make every LuneOS build depend on whatever main happened to
# be that day.
LSC_SRCREV = "a7c1efb5220dbe3c8ce8314bb56330f1b665e050"
