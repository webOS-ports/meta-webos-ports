# only for compatibility with meta-qt5/master

# the bbclass was renamed in oe-core/dunfell in:
# http://git.openembedded.org/openembedded-core/commit/?id=5f4875b950ce199e91f99c8e945a0c709166dc14
# the old name is still available (only triggers warning)
# but meta-qt5/master is using the new bbclass which doesn't
# exist in oe-core/zeus, so we need to introduce it here
# in this case without any new features to make it simple
inherit distro_features_check
