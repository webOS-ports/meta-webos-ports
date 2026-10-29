# Two build-time checks around signing keys, for every recipe (luneos.inc
# INHERITs this class).
#
# srcuri-private-key
#   Fails do_unpack when a local file:// entry of SRC_URI holds a PEM private
#   key ("-----BEGIN ... PRIVATE KEY-----"). Signing keys belong outside the
#   layers - MODULE_SIG_KEY in local.conf for kernel modules, see
#   meta-android/recipes-kernel/linux/kernel-module-signing.inc in
#   meta-smartphone. A module-signing key committed to meta-smartphone in 2026
#   is why this exists. Only files the layer itself ships are checked; fetched
#   upstream sources are not (their test keys are not ours to police).
#   A recipe that really must carry a private test key can opt out with
#   INSANE_SKIP:${PN} += "srcuri-private-key" and a comment saying why.
#
# unsigned-module
#   For recipes that inherit module (out-of-tree kernel modules): after
#   do_install, every .ko must carry an appended signature when the kernel it
#   was built against has CONFIG_MODULE_SIG_ALL=y. kbuild ignores a failed
#   sign-file for external modules ("|| true" in scripts/Makefile.modinst), so a
#   missing or misnamed key silently ships an unsigned module. On a kernel with
#   CONFIG_MODULE_SIG_FORCE=y such a module can never load, so that case is an
#   error; without MODULE_SIG_FORCE it is a warning.
ERROR_QA:append = " srcuri-private-key unsigned-module"

LUNEOS_PRIVATE_KEY_RE = "-----BEGIN (?:[A-Z0-9]+ )*PRIVATE KEY-----"

def luneos_qa_skipped(check, d):
    return check in (d.getVar('INSANE_SKIP:' + (d.getVar('PN') or '')) or '').split() or \
           check in (d.getVar('INSANE_SKIP') or '').split()

python luneos_check_srcuri_private_keys() {
    import re
    if luneos_qa_skipped('srcuri-private-key', d):
        return
    marker = re.compile(d.getVar('LUNEOS_PRIVATE_KEY_RE').encode())
    src_uri = (d.getVar('SRC_URI') or '').split()
    if not any(u.startswith('file://') for u in src_uri):
        return
    fetcher = bb.fetch2.Fetch(src_uri, d)
    found = []
    for url in fetcher.urls:
        if fetcher.ud[url].type != 'file':
            continue
        local = fetcher.localpath(url)
        if not local or not os.path.exists(local):
            continue
        paths = [local]
        if os.path.isdir(local):
            paths = [os.path.join(r, f) for r, _, fs in os.walk(local) for f in fs]
        for p in paths:
            try:
                with open(p, 'rb') as f:
                    if marker.search(f.read()):
                        found.append(p)
            except OSError:
                pass
    for p in found:
        oe.qa.handle_error('srcuri-private-key',
                           '%s: SRC_URI ships a private key in %s. Keep signing keys out of '
                           'the layers (for kernel modules: MODULE_SIG_KEY in local.conf).'
                           % (d.getVar('PN'), p), d)
    oe.qa.exit_if_errors(d)
}
do_unpack[prefuncs] += "luneos_check_srcuri_private_keys"
# A check, not part of what do_unpack produces: keep it out of the task hash,
# or adding (or changing) this class changes the signature of every recipe's
# do_unpack and so rebuilds the whole world, sstate included.
do_unpack[vardepsexclude] += "luneos_check_srcuri_private_keys"

python luneos_check_modules_signed() {
    if luneos_qa_skipped('unsigned-module', d):
        return
    config = os.path.join(d.getVar('STAGING_KERNEL_BUILDDIR'), '.config')
    try:
        with open(config) as f:
            options = set(l.strip() for l in f)
    except OSError:
        bb.warn('%s: no kernel .config at %s, so module signatures were not checked'
                % (d.getVar('PN'), config))
        return
    if 'CONFIG_MODULE_SIG_ALL=y' not in options:
        return
    forced = 'CONFIG_MODULE_SIG_FORCE=y' in options
    magic = b'~Module signature appended~\n'
    dest = d.getVar('D')
    unsigned = []
    for root, _, files in os.walk(dest):
        for name in files:
            if not name.endswith('.ko'):
                continue
            path = os.path.join(root, name)
            with open(path, 'rb') as f:
                f.seek(0, os.SEEK_END)
                size = f.tell()
                f.seek(max(0, size - len(magic)))
                if f.read() != magic:
                    unsigned.append(os.path.relpath(path, dest))
    if not unsigned:
        return
    msg = ('%s: kernel modules installed without a signature, although the kernel has '
           'CONFIG_MODULE_SIG_ALL=y%s: %s. Is the signing key staged in '
           'STAGING_KERNEL_BUILDDIR/certs (see kernel-module-signing.inc)?'
           % (d.getVar('PN'), ' and CONFIG_MODULE_SIG_FORCE=y, so they cannot load' if forced else '',
              ' '.join(sorted(unsigned))))
    if forced:
        oe.qa.handle_error('unsigned-module', msg, d)
        oe.qa.exit_if_errors(d)
    else:
        bb.warn(msg)
}

python () {
    if bb.data.inherits_class('module', d):
        d.appendVarFlag('do_install', 'postfuncs', ' luneos_check_modules_signed')
}
