#!/usr/bin/env python3
# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>
#
# SPDX-License-Identifier: Apache-2.0
"""Generate third-party-grantable.json from the ACG data of a LuneOS rootfs.

appinstalld only lets an application it installs keep the access control
groups (ACGs) listed in /usr/share/luna-service2/third-party-grantable.json.
This script writes that file.  It is not run during the build: the policy is
the GRANT table below, reviewed by a human, and the rootfs is only used to
check it against what the image really ships.

    gen-third-party-grantable.py <rootfs> [-o third-party-grantable.json]
                                 [--report report.md]

For each GRANT entry the group must
  - exist in <rootfs>/usr/share/luna-service2/api-permissions.d,
  - need no more than trust level "dev" in groups.d (apps appinstalld installs
    get a "dev" role, so a group that needs "oem" would be refused by the
    callee anyway), and
  - not match ALWAYS_DENIED, the patterns appinstalld strips whatever this
    file says.
Anything that fails a check is left out and reported.  Every group in the
rootfs that is in neither GRANT nor DENY is reported as unclassified, so a new
service's groups are noticed; unclassified groups are not granted.

Exit status: 0 when the output was written and every GRANT entry passed,
1 when some entry was dropped, 2 on usage or input errors.
"""

import argparse
import fnmatch
import glob
import json
import os
import sys

# Kept in sync with PermissionFilter::isAlwaysDenied() in appinstalld2
# (src/installer/PermissionFilter.cpp) and with luneos-app-permissions.
ALWAYS_DENIED = [
    "all",
    "private",
    "*.management",
    "*.devmode",
    "*.devutility",
    "*.debug",
    "*.internal",
    "*.profiling",
    "auth.*",
    "devmode*",
    "applicationinstall.*",
    "arescli.*",
]

# Groups a third-party application may hold.  DRAFT: decision D5, to be
# reviewed.  The reason is written to the report.
GRANT = {
    "public": "Default for an appinfo.json without requiredPermissions; maps to no method.",
    "application.operation": "registerApp/registerNativeApp, own app info and life cycle; appinstalld adds it to every QML app.",
    "application.launcher": "launch/open other apps (links, share). REVIEW: also close/closeByAppId/lockApp/pause of any app.",
    "notification.operation": "createToast/createAlert/createNotification.",
    "activity.operation": "Activity manager: schedule and adopt the app's own activities.",
    "activity.query": "Read-only activity status.",
    "activity.wakeup": "Scheduled wakeup callback, for alarm-style apps.",
    "audio.operation": "Playback volume and audio control. REVIEW: 155 methods, includes system-wide volume/routing.",
    "audio.query": "Read-only audio state.",
    "audiofocus.operation": "Request/abandon audio focus.",
    "audiofocus.query": "Read-only audio focus state.",
    "media.operation": "uMediaServer playback (load/play/seek). REVIEW: also startCameraRecord/takeCameraSnapshot.",
    "media.query": "Read-only media state.",
    "camera.operation": "Camera capture. REVIEW: no runtime consent yet (M10).",
    "camera.query": "Camera list/properties.",
    "mediarecorder.operation": "Audio/video recording. REVIEW: microphone without runtime consent (M10).",
    "tts.operation": "Text to speech.",
    "vibrate.operation": "Vibration.",
    "torch.operation": "Flashlight.",
    "database.operation": "db8 for the app's own kinds; db8 enforces per-kind permissions (put/find/del on others' kinds is refused).",
    "tempdatabase.operation": "Temporary db8, same per-kind model.",
    "filecache.operation": "File cache for the app's own cache types.",
    "download.operation": "Download manager. REVIEW: getAllHistory/listPending show other apps' downloads.",
    "preferences.operation": "set/removeAppProperty for the app's own properties.",
    "preferences.systempropertyquery": "System properties (locale, device name). REVIEW: includes the nduid device id.",
    "settings.query": "Read-only system settings (locale, units).",
    "systemsettings.query": "Read-only system preferences, deviceInfo/osInfo. REVIEW: deviceInfo may carry device ids.",
    "systemconfig.query": "Read-only configd values.",
    "software.query": "softwareInfo/query (OS version).",
    "time.query": "Time, time zone.",
    "networkconnection.query": "Online/offline status.",
    "memorymanager.query": "Memory pressure status.",
    "display.query": "Display on/off and lock status (read-only).",
    "service.communication": "luna-service2 call cancel/ping ('*/com/palm/luna/private/cancel'): needed to cancel any call or subscription.",
    "servicebus.communication": "Hub server-status and signal registration (LSRegisterServerStatus, addmatch).",
    "print.operation": "Print jobs. REVIEW: also printers/add/delete/setCurrent.",
    "print.query": "Read-only printer list.",
    "mediapermissions-service.operation": "request: asks the user for camera/microphone consent (the prompt itself is answered by the shell).",
}

# Groups not granted, with the reason; only used for the report.  Patterns in
# ALWAYS_DENIED need no entry here.
DENY = {
    "appmanager.management": "Contains com.palm.appinstaller/install and installNoVerify. FOLLOW-UP: move com.palm.applicationManager/open and the getHandlerFor*/listAllHandlers* lookups to application.launcher, or legacy apps cannot open links.",
    "accountsservice.operation": "readCredentials/writeCredentials of accounts.",
    "keymanager-service.operation": "Key store (export/fetchKey/import).",
    "keymanager-service.backup": "Key store backup.",
    "keymgrdatabase.operation": "Key manager database.",
    "ipkg-service.operation": "Installs packages as root (org.webosports.service.ipkg).",
    "update-service.operation": "System update.",
    "devmode-service.operation": "Developer mode switch.",
    "lumberjack-service.operation": "Bus/log capture.",
    "tweaks-service.operation": "System tweaks.",
    "certmgr-service.operation": "Certificate store.",
    "backup-service.operation": "Backup of all app data.",
    "backupagent.operation": "Backup agent.",
    "filemanager-service.operation": "File manager service, whole-filesystem access.",
    "license-service.operation": "Not needed by apps.",
    "storaged.operation": "erase/EraseAll, Wipe, enterMSM.",
    "display.operation": "setLockStatus/setState/setProperty.",
    "power.operation": "Wake locks and power state transitions.",
    "location.query": "Position without a consent prompt (M10); also setGPSParameters/sendExtraCommand. REVIEW: grant once a prompt exists.",
    "location.operation": "Needs trust oem.",
    "wifi.query": "SSID/BSSID lists are a location proxy.",
    "networking.query": "Contains com.webos.service.wan/connect, disconnect, setHostRoutes.",
    "networkconnection.management": "Network configuration.",
    "nettools.operation": "Raw network tools (ping, arp).",
    "telephony.query": "IMSI/IMEI/subscriber data.",
    "bluetooth.operation": "Pairing and connections.",
    "bluetooth.query": "Paired-device list.",
    "nfc.operation": "NFC.",
    "nfc.query": "NFC.",
    "ir.operation": "IR blaster.",
    "rtc.operation": "RTC engine.",
    "vpn.query": "VPN profiles.",
    "faceunlock.identify": "Biometrics.",
    "faceunlock.operation": "Biometrics.",
    "faceunlock.query": "Biometrics.",
    "fingerprint.operation": "Biometrics.",
    "fingerprint.query": "Biometrics.",
    "luna-sysmgr.operation": "System manager.",
    "contactsservice.operation": "PIM data (contacts).",
    "contactslinkerservice.operation": "PIM data (contacts).",
    "calendarremindersservice.operation": "PIM data (calendar).",
    "photosservice.operation": "PIM data (photos service).",
    "messaging-service.operation": "Messages.",
    "mojomail-imap.operation": "Mail transport.",
    "mojomail-pop.operation": "Mail transport.",
    "mojomail-smtp.operation": "Mail transport (sends mail).",
    "imlibpurple-service.operation": "IM transport.",
    "imlibpurple-call.operation": "IM calls.",
    "imaccountvalidator.operation": "Account validation.",
    "cdav-service.operation": "CalDAV/CardDAV sync.",
    "universalsearch.operation": "Changes the system search list and preferences.",
    "mediacontroller.operation": "Other apps' media sessions.",
    "mediacontroller.query": "Needs trust oem.",
    "physicaldevice.query": "USB device list.",
    "peripheralmanager.query": "Peripheral manager.",
    "camerahal.operation": "Camera HAL; apps use camera.operation.",
    "camerahal.query": "Camera HAL.",
    "camerasolution.operation": "Camera solutions.",
    "booster.control": "CPU booster.",
    "booster.status": "CPU booster.",
    "android-property-service.operation": "Android properties.",
    "eventmonitor.plugin": "Event monitor.",
    "synergy-box.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-dropbox.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-flickr.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-gdrive.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-hidrive.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-kdrive.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-koofr.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-mega.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-onedrive.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-pcloud.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-s3.operation": "Cloud storage connector holding the user's account tokens.",
    "synergy-yandex.operation": "Cloud storage connector holding the user's account tokens.",
}


def load_json(path):
    with open(path, encoding="utf-8") as f:
        return json.load(f)


def always_denied(group):
    return any(fnmatch.fnmatchcase(group, p) for p in ALWAYS_DENIED)


def read_rootfs(rootfs):
    base = os.path.join(rootfs, "usr/share/luna-service2")
    groups = set()
    for f in sorted(glob.glob(os.path.join(base, "api-permissions.d", "*.json"))):
        groups.update(load_json(f).keys())
    trust = {}
    for f in sorted(glob.glob(os.path.join(base, "groups.d", "*.json"))):
        for k, v in load_json(f).items():
            if k == "allowedNames":
                continue
            trust.setdefault(k, set()).update(v if isinstance(v, list) else [v])
    return groups, trust


def main(argv):
    ap = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    ap.add_argument("rootfs")
    ap.add_argument("-o", "--output", default="third-party-grantable.json")
    ap.add_argument("--report", help="write the per-group reasoning as Markdown")
    args = ap.parse_args(argv)

    if not os.path.isdir(os.path.join(args.rootfs, "usr/share/luna-service2/api-permissions.d")):
        print("no usr/share/luna-service2/api-permissions.d under %s" % args.rootfs, file=sys.stderr)
        return 2

    groups, trust = read_rootfs(args.rootfs)
    granted, dropped = [], []
    for g in sorted(GRANT):
        levels = trust.get(g, {"dev"})  # no groups.d entry: the hub defaults to "dev"
        if always_denied(g):
            dropped.append((g, "matches an always-denied pattern"))
        elif g != "public" and g not in groups:
            dropped.append((g, "not in api-permissions.d"))
        elif levels != {"dev"} and "dev" not in levels:
            dropped.append((g, "needs trust level %s" % "/".join(sorted(levels))))
        else:
            granted.append(g)

    clash = sorted(set(GRANT) & set(DENY))
    for g in clash:
        dropped.append((g, "in both GRANT and DENY"))
        if g in granted:
            granted.remove(g)

    unclassified = sorted(g for g in groups
                          if g not in GRANT and g not in DENY and not always_denied(g)
                          and trust.get(g, {"dev"}) & {"dev"})

    out = {
        "comment": "DRAFT (decision D5). Access control groups appinstalld lets an application it "
                   "installs keep from requiredPermissions; everything else is dropped. Generated by "
                   "gen-third-party-grantable.py; edit the policy there, not here.",
        "version": 1,
        "grantable": granted,
    }
    with open(args.output, "w", encoding="utf-8") as f:
        json.dump(out, f, indent=4)
        f.write("\n")

    if args.report:
        with open(args.report, "w", encoding="utf-8") as f:
            f.write("| Group | Decision | Trust | Reason |\n|---|---|---|---|\n")
            for g in sorted(groups | set(GRANT)):
                t = "/".join(sorted(trust.get(g, {"dev (default)"})))
                if g in granted:
                    d, r = "grant", GRANT[g]
                elif always_denied(g):
                    d, r = "deny (always)", "matches %s" % next(p for p in ALWAYS_DENIED if fnmatch.fnmatchcase(g, p))
                elif g in DENY:
                    d, r = "deny", DENY[g]
                elif "dev" not in trust.get(g, {"dev"}):
                    d, r = "deny (oem)", "needs trust level oem; a dev role cannot use it"
                else:
                    d, r = "deny (unclassified)", "not reviewed yet"
                f.write("| `%s` | %s | %s | %s |\n" % (g, d, t, r.replace("|", "\\|")))

    for g, why in dropped:
        print("dropped %s: %s" % (g, why), file=sys.stderr)
    for g in unclassified:
        print("unclassified (not granted): %s" % g, file=sys.stderr)
    print("%d grantable, %d dropped, %d unclassified, %d groups in rootfs"
          % (len(granted), len(dropped), len(unclassified), len(groups)), file=sys.stderr)
    return 1 if dropped else 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
