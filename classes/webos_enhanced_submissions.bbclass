# Copyright (c) 2012-2013 LG Electronics, Inc.
# 
# webos_enhanced_submissions
#
# Parse a PREFERRED_VERSION_<packagename> in the following format:
#
#    <component-version>-<enhanced-submission>
#
# setting WEBOS_COMPONENT_VERSION and WEBOS_SUBMISSION
#
# Explicitly inheriting from this bbclass also implies that the component uses
# the Open webOS convention for submission tags, i.e., they are of the form:
#    submissions/<integer>
# or, when the component has been branched:
#    submissions/<integer>.<integer>
# where <integer> does not contain leading zeros. If they are different, inherit
# from webos_submissions instead.
#

# PV is the same as PREFERRED_VERSION_<packagename>,
# i.e., it includes the submission. If there is no PREFERRED_VERSION_<packagename>
# setting, '0' will be returned.
def webos_enhsub_get_pv(pn, d):
    preferred_v = d.getVar('PREFERRED_VERSION_' + pn, True) or '0'
    return preferred_v

# The component version is PREFERRED_VERSION_<packagename> with the last hyphen-
# separated field removed; i.e., it does not include the submission. If there is
# no PREFERRED_VERSION_<packagename> setting, '0' will be returned.
def webos_enhsub_get_component_version(pn, d):
    preferred_v = d.getVar('PREFERRED_VERSION_' + pn, True) or '0'
    split_preferred_v = preferred_v.split('-')
    if len(split_preferred_v) == 1:
        # If there's no submission, then the component version can't
        # contain a hyphen
        return preferred_v
    return "-".join(split_preferred_v[:-1])

# The submission is the first underscore-separated field in the enhanced
# submission field, which is the last hyphen-separated field in
# PREFERRED_VERSION_<packagename>. If there is no PREFERRED_VERSION_<packagename>
# setting, '0' will be returned.
def webos_enhsub_get_submission(pn, d):
    preferred_v = d.getVar('PREFERRED_VERSION_' + pn, True) or '0'
    split_preferred_v = preferred_v.split('-')
    if len(split_preferred_v) == 1:
        # If there no hyphen, that means there's no submission
        return '0'
    return split_preferred_v[-1]


PV = "${@webos_enhsub_get_pv('${PN}', d)}"
PV[vardeps] += "PREFERRED_VERSION_${PN}"

# These two are intended for use in the recipes that inherit this file:
WEBOS_COMPONENT_VERSION = "${@webos_enhsub_get_component_version('${PN}', d)}"
WEBOS_SUBMISSION = "${@webos_enhsub_get_submission('${PN}', d)}"
