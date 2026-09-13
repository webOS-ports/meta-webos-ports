# Copyright (c) 2023-2024 LG Electronics, Inc.
#
# LS2 security configuration validation
#

inherit webos_filesystem_paths

WEBOS_LS2_CONF_VALIDATE_ERROR_ON_WARNING ?= "0"
WEBOS_LS2_CONF_VALIDATE_SKIP_GROUP ?= ""

# For some reason, using expr directly doesn't work
accumulate() {
    echo $(expr $1 + $2)
}

scan_and_validate() {
    # $1: directory to scan
    # $2: schema to use

    if [ ! -d "$1" ]; then
        bbnote "Skipping non-existent directory $1"
        echo 0; return
    fi

    local errors=0
    local schema="${IMAGE_ROOTFS}${webos_sysconfdir}/schemas/luna-service2/$2"
    local schema_old_group="${IMAGE_ROOTFS}${webos_sysconfdir}/schemas/luna-service2/old_groups.schema"

    bbnote "Scanning directory $1"
    if [ -f "$schema" ]; then
        bbnote "    with schema: $schema"
    else
        bbwarn "    without schema: $schema (missing)"
    fi

    for ff in $(find "$1" -type f -name "*.json"); do
        local errcode=0
        if [ ! -f "$schema" ]; then
            ${STAGING_BINDIR_NATIVE}/pbnjson_validate -f "$ff" > /dev/null 2>&1 || errcode=1
        else
            ${STAGING_BINDIR_NATIVE}/pbnjson_validate -f "$ff" -s "$schema" > /dev/null 2>&1 || errcode=2
            if [ $errcode -ne 0 -a "$2" = "groups.schema" -a -f $schema_old_group ]; then
                # additional handling with old_groups.schema
                errcode=0
                # Prepare allowed_names.schema if necessary
                if [ ! -f "${T}/allowed_names.schema" ]; then
                    echo '{"type":"object","properties":{"allowedNames":{"type":"array","items":{"type":"string"},"minItems":1,"uniqueItems":true}},"required":["allowedNames"]}' > "${T}/allowed_names.schema"
                fi
                if ( ${STAGING_BINDIR_NATIVE}/pbnjson_validate -f "$ff" -s "${T}/allowed_names.schema" > /dev/null 2>&1 ); then
                    ${STAGING_BINDIR_NATIVE}/pbnjson_validate -f "$ff" -s "$schema_old_group" > /dev/null 2>&1 || errcode=3
                else
                    ${STAGING_BINDIR_NATIVE}/pbnjson_validate -f "$ff" > /dev/null 2>&1 || errcode=1
                fi
            fi
        fi

        case $errcode in
            1)
                bberror "\t$(basename $ff) isn't a valid json"
                errors=$(accumulate $errors 1)
                ;;
            2)
                bberror "\t$(basename $ff) isn't valid as per $2"
                errors=$(accumulate $errors 1)
                ;;
            3)
                bberror "\t$(basename $ff) isn't valid as per old_groups.schema"
                errors=$(accumulate $errors 1)
                ;;
            *)
                bbnote "\t$(basename $ff) looks ok"
                ;;
        esac
    done

    bbnote "    Found $errors error(s)"
    bbnote "    Done for directory $1"

    # returns the number of validation errors
    echo $errors; return
}

fakeroot do_validate_ls2_security_conf() {
    local errors=0

    errors=$(accumulate $errors \
        $(scan_and_validate \
            ${IMAGE_ROOTFS}${webos_sysbus_manifestsdir} \
            "manifest.schema" ))

    errors=$(accumulate $errors \
        $(scan_and_validate \
            ${IMAGE_ROOTFS}${webos_sysbus_apipermissionsdir} \
            "api_permissions.schema" ))

    errors=$(accumulate $errors \
        $(scan_and_validate \
            ${IMAGE_ROOTFS}${webos_sysbus_permissionsdir} \
            "client_permissions.schema" ))

    errors=$(accumulate $errors \
        $(scan_and_validate \
            ${IMAGE_ROOTFS}${webos_sysbus_containersdir} \
            "container.schema" ))

    errors=$(accumulate $errors \
        $(scan_and_validate \
            ${IMAGE_ROOTFS}${webos_sysbus_rolesdir} \
            "role.schema" ))

    errors=$(accumulate $errors \
        $(scan_and_validate \
            ${IMAGE_ROOTFS}${webos_sysbus_pubrolesdir} \
            "old_role.schema" ))

    errors=$(accumulate $errors \
        $(scan_and_validate \
            ${IMAGE_ROOTFS}${webos_sysbus_prvrolesdir} \
            "old_role.schema" ))

    errors=$(accumulate $errors \
        $(scan_and_validate \
            ${IMAGE_ROOTFS}${webos_sysbus_groupsdir} \
            "groups.schema" ))

    if [ $errors -gt 0 ]; then
        bbfatal_log "Found $errors validation error(s). See details in log messages above."
    fi
}

fakeroot python do_validate_ls2_acg() {
    import os
    import json

    rootfs_groups_d = d.getVar("IMAGE_ROOTFS") + d.getVar("webos_sysbus_groupsdir")
    rootfs_api_perms_d = d.getVar("IMAGE_ROOTFS") + d.getVar("webos_sysbus_apipermissionsdir")
    rootfs_clientperms_dir = d.getVar("IMAGE_ROOTFS") + d.getVar("webos_sysbus_permissionsdir")

    # There can be no sysbus directories in a tiny image
    if not os.path.isdir(rootfs_groups_d):
        bb.note("Directory '%s' is missing, skipping validation." % rootfs_groups_d)
        return
    if not os.path.isdir(rootfs_api_perms_d):
        bb.note("Directory '%s' is missing, skipping validation." % rootfs_api_perms_d)
        return
    if not os.path.isdir(rootfs_clientperms_dir):
        bb.note("Directory '%s' is missing, skipping validation." % rootfs_clientperms_dir)
        return

    # List of group names to skip checking
    skip_group = d.getVar("WEBOS_LS2_CONF_VALIDATE_SKIP_GROUP").split()
    if len(skip_group) > 0:
        # These are configured deliberately via WEBOS_LS2_CONF_VALIDATE_SKIP_GROUP,
        # so listing them is informational rather than something to act on.
        msg = "=== LIST BEGIN: Groups considered as exception ===\n"
        for group in sorted(skip_group):
            msg += "  %s\n" % group
        msg += "=== LIST END ===\n"
        bb.note(msg)
    # Always skip 'allowedNames' which is being used a key in old-style groups.json
    skip_group.append("allowedNames")

    # Returns a set of group names defined in 'dir'.
    # Json files in 'dir' are expected to have groups as keys.
    def read_groups(dir):
        msg = "Reading groups from %s\n" % dir
        groups = set()
        with os.scandir(dir) as it:
            for entry in it:
                if entry.is_file():
                    msg += "  %s\n" % entry.name
                    with open(entry.path) as fp:
                        groups_json = json.load(fp)
                        groups.update(filter(lambda x: x not in skip_group, groups_json.keys()))
        msg += "Done reading groups from %s\n" % dir
        bb.debug(1, msg)
        return groups

    # Returns a set of group names used in 'perm_entry' but not in 'groups'.
    # 'groups' is a set of group names to match and 'perm_entry' is an
    # iterator entry of a file that refers groups in an array form.
    def get_missing_groups_in_perm(groups, perm_entry):
        msg = "Checking groups in %s\n" % perm_entry.name
        missing_groups = set()
        with open(perm_entry.path) as fp:
            perm_json = json.load(fp)
            for groups_used in perm_json.values():
                for group in groups_used:
                    if not group in groups:
                        missing_groups.add(group)
            for group in sorted(missing_groups):
                msg += "  %s%s" % (group, " => missing" if not group in groups else "\n")
        msg += "Done checking groups in %s\n" % perm_entry.name
        bb.debug(1, msg)
        return missing_groups

    # First, we build a set of groups defined in "groups.d".
    groups_defined = read_groups(rootfs_groups_d)
    msg = "=== LIST BEGIN: Groups defined in groups.d(%s) ===\n" % rootfs_groups_d
    for group in sorted(groups_defined):
        msg += "  %s\n" % group
    msg += "=== LIST END ===\n"

    # Second, get groups from "api-permissions.d".
    # Those groups are also considered as valid.
    groups_defined2 = read_groups(rootfs_api_perms_d)
    msg += "=== LIST BEGIN: Groups used in api-permissions.d(%s) ===\n" % rootfs_api_perms_d
    for group in sorted(groups_defined2):
        msg += "  %s\n" % group
    msg += "=== LIST END ===\n"
    bb.debug(2, msg)

    # Merge groups from "groups.d" and "api-permissions.d" with showing differences.
    # Those differences are recommended to define in "groups.d".
    groups_defined2.difference_update(groups_defined)
    cnt = len(groups_defined2)
    if cnt > 0:
        msg = "Found %d group(s) that appear only in api-permissions.d, consider define them in groups.d\n" % cnt
        msg += "=== LIST BEGIN: Groups used in api-permissions.d but not defined in groups.d ===\n"
        for group in sorted(groups_defined2):
            msg += "  %s\n" % group
        msg += "=== LIST END ===\n"
        bb.warn(msg)
    groups_valid = groups_defined.union(groups_defined2)
    msg = "=== LIST BEGIN: Groups considered as valid ===\n"
    for group in sorted(groups_valid):
        msg += "  %s\n" % group
    msg += "=== LIST END ===\n"
    bb.note(msg)

    # Iterate files in "client-permissions.d" and list up groups
    # which don't appear in the set built above.
    groups_missing = {}
    with os.scandir(rootfs_clientperms_dir) as it:
        for entry in it:
            if entry.is_file():
                for group in get_missing_groups_in_perm(groups_valid, entry):
                    if group not in skip_group:
                        if group in groups_missing:
                            groups_missing[group] += [entry.name]
                        else:
                            groups_missing[group] = [entry.name]

    # Raise a warning or error(if enabled) if any missing group is found.
    cnt = len(groups_missing)
    if cnt > 0:
        msg = "Found %d group(s) used in client-permissions.d but not defined\n" % cnt
        msg += "=== LIST BEGIN ===\n"
        for group in sorted(groups_missing):
            msg += "'%s' being used in:\n" % group
            for entry in sorted(groups_missing[group]):
                msg += "  %s\n" % entry
        msg += "=== LIST END =====\n"
        bb.warn(msg)
        if d.getVar("WEBOS_LS2_CONF_VALIDATE_ERROR_ON_WARNING") != "0":
            bb.fatal("Fatal error while checking groups, aborting!")
}

fakeroot python do_validate_ls2_wiring() {
    """Check the parts of the LS2 security configuration that do_validate_ls2_acg
    does not look at.

    do_validate_ls2_acg answers one question: is every ACG named in
    client-permissions.d defined somewhere?  That leaves most of the ways a call
    can be refused unchecked.  ls-hubd only ever opens files a manifest lists, it
    resolves roles by the executable's real path, and it refuses to register a
    name that has no permissions[] entry -- none of which is visible today until
    something fails on a device.

    Everything below mirrors luna-service2 3.21.2 (src/ls-hubd).  Findings are
    warnings; set WEBOS_LS2_CONF_VALIDATE_ERROR_ON_WARNING to make them fatal.
    """
    import os
    import json
    import re
    import fnmatch

    rootfs = d.getVar("IMAGE_ROOTFS")
    dirs = {
        "manifests": d.getVar("webos_sysbus_manifestsdir"),
        "roles":     d.getVar("webos_sysbus_rolesdir"),
        "api":       d.getVar("webos_sysbus_apipermissionsdir"),
        "client":    d.getVar("webos_sysbus_permissionsdir"),
        "groups":    d.getVar("webos_sysbus_groupsdir"),
        "services":  d.getVar("webos_sysbus_servicedir"),
    }
    if not os.path.isdir(rootfs + dirs["manifests"]):
        bb.note("No %s, skipping." % dirs["manifests"])
        return

    problems = []
    def report(code, subject, msg):
        problems.append("%-26s %s\n        %s" % (code, subject, msg))

    def load(path):
        try:
            with open(path) as fp:
                return json.load(fp)
        except Exception as e:
            return e

    # ---------------------------------------------------------------- manifests
    LIST_KEYS = ("roleFiles", "roleFilesPub", "roleFilesPrv", "serviceFiles",
                 "apiPermissionFiles", "clientPermissionFiles", "groupsFiles")

    manifests, referenced = [], set()
    mdir = rootfs + dirs["manifests"]
    for name in sorted(os.listdir(mdir)):
        path = os.path.join(mdir, name)
        if not os.path.isfile(path):
            continue
        js = load(path)
        if isinstance(js, Exception):
            report("MANIFEST_PARSE", name, "not valid JSON: %s" % js)
            continue
        manifests.append((name, js))
        for key in LIST_KEYS:
            for entry in js.get(key, []):
                referenced.add(os.path.normpath(rootfs + entry))
                if not os.path.exists(rootfs + entry):
                    report("MANIFEST_MISSING_FILE", name,
                           "lists %s, which is not installed. ProcessManifest() "
                           "returns false, so ls-hubd drops the WHOLE manifest -- "
                           "every role, service and permission it lists." % entry)

    # SecurityData::AddManifest keeps only the highest version per id
    by_id = {}
    for name, js in manifests:
        by_id.setdefault(js.get("id"), []).append((name, js.get("version")))
    for mid, entries in sorted(by_id.items()):
        if mid is None:
            continue
        if len(entries) > 1:
            report("MANIFEST_ID_COLLISION", mid,
                   "claimed by %s. Only the highest version is loaded; the rest are "
                   "dropped together with everything they list."
                   % ", ".join("%s(%s)" % e for e in entries))

    # ls-hubd.conf points at ManifestsDirectories and nothing else, so anything
    # no manifest names is never read
    for key in ("roles", "api", "client", "groups", "services"):
        dpath = rootfs + dirs[key]
        if not os.path.isdir(dpath):
            continue
        for name in sorted(os.listdir(dpath)):
            path = os.path.normpath(os.path.join(dpath, name))
            if os.path.isfile(path) and path not in referenced:
                report("ORPHAN_FILE", dirs[key] + "/" + name,
                       "no manifest lists this file, so ls-hubd never opens it and "
                       "everything it declares is missing at runtime.")

    # ---------------------------------------------------------------- roles
    def name_match(pattern, name):
        if pattern.endswith("*"):
            return name.startswith(pattern[:-1])
        return pattern == name

    def resolve_exec(path, depth=0):
        """Follow symlinks and #! chains: the hub matches /proc/<pid>/exe."""
        if depth > 5 or not path.startswith("/"):
            return path
        target = rootfs + path
        if os.path.islink(target):
            real = os.path.realpath(target)
            root = os.path.realpath(rootfs)
            path = real[len(root):] if real.startswith(root) else real
            target = rootfs + path
        try:
            with open(target, "rb") as fh:
                head = fh.read(200)
        except OSError:
            return path
        m = re.match(br"^#!\s*(\S+)(?:\s+(\S+))?", head)
        if not m:
            return path
        interp = m.group(1).decode()
        arg = (m.group(2) or b"").decode()
        if interp.endswith("/env") and arg:
            for cand in ("/usr/bin", "/bin", "/usr/sbin", "/sbin"):
                if os.path.exists(rootfs + cand + "/" + arg):
                    interp = cand + "/" + arg
                    break
            else:
                return path
        return resolve_exec(interp, depth + 1)

    roles = {}            # role id -> dict
    perm_index = {}       # service pattern -> {role id: entry}
    containers = set()

    cdir = rootfs + d.getVar("webos_sysbus_containersdir")
    if os.path.isdir(cdir):
        for name in os.listdir(cdir):
            js = load(os.path.join(cdir, name))
            if isinstance(js, dict):
                containers.update(js.get("exeNames", []))

    for name, js in manifests:
        for key in ("roleFiles", "roleFilesPub", "roleFilesPrv"):
            for entry in js.get(key, []):
                path = rootfs + entry
                if not os.path.exists(path):
                    continue
                rjs = load(path)
                if isinstance(rjs, Exception):
                    report("ROLE_PARSE", entry, "not valid JSON: %s" % rjs)
                    continue
                node = rjs.get("role", rjs) if key != "roleFiles" else rjs
                rid = node.get("exeName") or node.get("appId")
                if not rid:
                    report("ROLE_NO_ID", entry, "neither exeName nor appId")
                    continue
                if rid in roles and roles[rid]["file"] != entry:
                    report("ROLE_ID_COLLISION", rid,
                           "declared by both %s and %s. RoleMap::Add() keeps whichever "
                           "the hub reads first (readdir order) and logs 'Role already "
                           "exists' for the other."
                           % (roles[rid]["file"], entry))
                    continue
                roles[rid] = {
                    "file": entry,
                    "kind": "exe" if node.get("exeName") else "app",
                    "type": node.get("type"),
                    "allowed": list(node.get("allowedNames", [])),
                }
                for pe in rjs.get("permissions", node.get("permissions", [])):
                    svc = pe.get("service")
                    if isinstance(svc, str):
                        perm_index.setdefault(svc, {})[rid] = pe

    def lookup_perms(svc):
        """PermissionsMap::LookupServicePermissions: exact, then longest wildcard."""
        if svc in perm_index:
            return svc, perm_index[svc]
        best, best_len = None, -1
        for pattern in perm_index:
            if pattern.endswith("*") and svc.startswith(pattern[:-1]) and len(pattern) > best_len:
                best, best_len = pattern, len(pattern)
        return (best, perm_index[best]) if best else (None, None)

    for rid, role in sorted(roles.items()):
        if role["kind"] == "exe":
            if not os.path.lexists(rootfs + rid):
                report("ROLE_EXE_MISSING", rid,
                       "%s is keyed on an executable that is not installed, so the "
                       "role can never match a running process." % role["file"])
            elif os.path.islink(rootfs + rid):
                report("ROLE_EXE_SYMLINK", rid,
                       "%s is keyed on a symlink; the hub matches the resolved "
                       "/proc/<pid>/exe, so this role is never found." % role["file"])

        if role["type"] not in (None, "regular", "privileged", "proxy", "devmode"):
            report("ROLE_BAD_TYPE", rid,
                   "%s has type %r, outside the schema enum -- the file is rejected "
                   "and its whole manifest with it." % (role["file"], role["type"]))

        for allowed in role["allowed"]:
            if not allowed:
                continue
            probe = allowed[:-1] + "x" if allowed.endswith("*") else allowed
            pattern, entries = lookup_perms(probe)
            if not entries:
                report("NO_PERMISSION_FOR_NAME", allowed,
                       "%s allows this bus name, but no role file declares a "
                       "permissions[] entry with \"service\": \"%s\". "
                       "LSHubActivePermissionMapAdd() then fails and LSRegister() "
                       "returns an error -- the service never starts."
                       % (role["file"], allowed))
                continue
            outbound = []
            for pe in entries.values():
                outbound += pe.get("outbound", [])
            if not outbound:
                report("NO_OUTBOUND", allowed,
                       "the permissions entry in %s grants no outbound at all, so "
                       "LSHubIsClientAllowedOutbound() refuses every call it makes."
                       % role["file"])

    # ---------------------------------------------------------------- services.d
    by_exe = {}
    for rid, role in roles.items():
        if role["kind"] == "exe":
            by_exe.setdefault(rid, []).append(role)

    for name, js in manifests:
        for entry in js.get("serviceFiles", []):
            path = rootfs + entry
            if not os.path.exists(path):
                continue
            if "/luna-service2/services.d/" not in entry:
                report("FOREIGN_SERVICE_FILE", entry,
                       "manifest %s registers this as a luna-service2 service file, "
                       "but it lives outside services.d -- a D-Bus activation file "
                       "pulled into the LS2 namespace." % name)
            names, execs = [], None
            with open(path) as fp:
                for line in fp:
                    line = line.strip()
                    if line.startswith("Name="):
                        names += [x for x in re.split(r"[;, ]+", line[5:]) if x]
                    elif line.startswith("Exec="):
                        execs = line[5:].strip()
            for svc in names:
                if not any(any(name_match(a, svc) for a in r["allowed"])
                           for r in roles.values()):
                    report("SERVICE_NO_ROLE", svc,
                           "%s can launch this name, but no role file allows any "
                           "executable to register it -- the process starts and then "
                           "fails at LSRegister()." % entry)
            if not execs or not execs.split()[0].startswith("/"):
                continue
            binary = execs.split()[0]
            if not os.path.lexists(rootfs + binary):
                report("SERVICE_EXEC_MISSING", names[0] if names else entry,
                       "%s has Exec=%s but that file is not installed." % (entry, binary))
                continue
            effective = resolve_exec(binary)
            candidates = by_exe.get(effective, []) or by_exe.get(binary, [])
            if not candidates and effective in containers:
                continue
            if not candidates:
                # a wrapper script execs something else; accept any binary it names
                # that does have a role
                if effective != binary:
                    continue
                report("EXEC_NO_ROLE", names[0] if names else entry,
                       "%s launches %s, but no role file is keyed on that executable. "
                       "The hub resolves the role from /proc/<pid>/exe and finds "
                       "nothing." % (entry, binary))
            else:
                for svc in names:
                    if not any(any(name_match(a, svc) for a in c["allowed"])
                               for c in candidates):
                        report("EXEC_ROLE_MISMATCH", svc,
                               "%s launches %s to provide this name, but the role "
                               "keyed on that executable does not allow it."
                               % (entry, binary))

    # ---------------------------------------------------------------- api patterns
    provides = {}     # service -> {category pattern: set(groups)}
    api_dir = rootfs + dirs["api"]
    if os.path.isdir(api_dir):
        for name in sorted(os.listdir(api_dir)):
            js = load(os.path.join(api_dir, name))
            if isinstance(js, Exception):
                continue
            for group, patterns in js.items():
                if not isinstance(patterns, list):
                    continue
                for pattern in patterns:
                    if "/" not in pattern:
                        report("API_NO_CATEGORY", name,
                               "maps ACG %r onto the bare service name %r. "
                               "GroupsMap::AddProvided() turns a pattern with no '/' "
                               "into the category pattern '/', which matches the root "
                               "category and never a method -- write \"%s/*\"."
                               % (group, pattern, pattern))
                        continue
                    svc, category = pattern.split("/", 1)
                    if "*" in svc[:-1]:
                        report("API_BAD_SERVICE_WILDCARD", name,
                               "pattern %r: the text before the first '/' is a trie "
                               "key, where only a trailing '*' is a wildcard, so this "
                               "matches no service." % pattern)
                    provides.setdefault(svc, {}).setdefault("/" + category, set()).add(group)

    # A method that only the universal 'all' group covers is reachable by the
    # handful of clients holding 'all' and by nobody else.
    for svc, categories in sorted(provides.items()):
        if svc.endswith("*"):
            continue
        groups = set()
        for gs in categories.values():
            groups |= gs
        if groups and groups <= {"all"}:
            report("ONLY_MASTER_GROUP", svc,
                   "every pattern for this service resolves to the universal 'all' "
                   "group only, so nothing without 'all' can call it.")

    # ---------------------------------------------------------------- report
    if problems:
        msg = ("Found %d luna-service2 wiring problem(s)\n=== LIST BEGIN ===\n  %s\n"
               "=== LIST END ===\n" % (len(problems), "\n  ".join(problems)))
        bb.warn(msg)
        if d.getVar("WEBOS_LS2_CONF_VALIDATE_ERROR_ON_WARNING") != "0":
            bb.fatal("Fatal error while checking LS2 wiring, aborting!")
    else:
        bb.note("LS2 wiring validation found no problems.")
}

addtask do_validate_ls2_security_conf after do_rootfs before do_image
addtask do_validate_ls2_acg after do_validate_ls2_security_conf before do_image
addtask do_validate_ls2_wiring after do_validate_ls2_acg before do_image
do_validate_ls2_security_conf[depends] += "libpbnjson-native:do_populate_sysroot"
