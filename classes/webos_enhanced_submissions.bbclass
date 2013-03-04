# Copyright (c) 2012-2013 LG Electronics, Inc.
# 
# webos_enhanced_submissions
#
# Parse a PV in the following format:
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

# The component version is PV with the last hyphen-separated field removed; 
# i.e., it does not include the submission.
def webos_enhsub_get_component_version(pv):
    split_version = pv.split('-')
    if len(split_version) == 1:
        # If there's no submission, then the component version can't
        # contain a hyphen
        return pv
    return "-".join(split_version[:-1])

# The submission is the first underscore-separated field in the enhanced
# submission field, which is the last hyphen-separated field in PV
# If there is no hypen in PV, '0' will be returned.
def webos_enhsub_get_submission(pv):
    split_version = pv.split('-')
    if len(split_version) == 1:
        # If there no hyphen, that means there's no submission
        return '0'
    return split_version[-1]

# These two are intended for use in the recipes that inherit this file:
WEBOS_COMPONENT_VERSION = "${@webos_enhsub_get_component_version('${PV}')}"
WEBOS_SUBMISSION = "${@webos_enhsub_get_submission('${PV}')}"
