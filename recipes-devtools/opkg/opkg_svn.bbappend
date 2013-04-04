# Copyright (c) 2013 Hewlett-Packard Development Company, L.P.

# We're running opkg-postinst on first boot a little bit earlier to be able to run
# different needed hardware initilization after this which needs things only available
# after postinst-phase is done (e.g. android binaries).
POSTINSTALL_INITPOSITION = "80"
