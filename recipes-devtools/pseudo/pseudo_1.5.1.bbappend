# Copyright (c) 2014 LG Electronics, Inc.

# https://bugzilla.yoctoproject.org/show_bug.cgi?id=5135
# There is no need for 32bit binaries while building for SDK.
NO32LIBS_class-nativesdk = '1'
