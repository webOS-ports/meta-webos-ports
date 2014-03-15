inherit webos-ports-repo

# NOTE: -wop prefix is here to indicate the source is being modified by the webos-ports project
# and is different to what openwebos provides.
GIT-PREFIX = "wop"

python() {
    if not bb.data.inherits_class('webos_enhanced_submissions', d):
        file = d.getVar('FILE', True)
        bb.fatal("%s: inherit webos_enhanced_submissions when the recipe is using webos-ports-submissions" % file)
}
