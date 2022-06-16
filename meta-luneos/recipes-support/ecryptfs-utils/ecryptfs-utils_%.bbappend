# webOS OSE mask this from webos-initscripts:
# https://github.com/webosose/webos-initscripts/blob/0988b39fc73c47fda47adb79442402879aaef7ca/files/systemd/services/mask/ecryptfs.service
# lets just disable it as it fails to start anyway due to missing /dev/ecryptfs /dev/misc/ecryptfs
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
