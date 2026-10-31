# Copyright (c) 2026 Herman van Hazendonk
# SPDX-License-Identifier: MIT
#
# Root's SSH access in a LuneOS image, done at rootfs time by luneos_image.bbclass:
#
# - sshd_config: passwords only over the local link (sshd_config_local_password);
# - build-time authorized keys (install_authorized_keys);
# - the QA check that no .ssh or authorized_keys in the image is open to others
#   (ssh_mode_problems).
#
# Plain Python on a rootfs path, so it can be tried without bitbake.

import base64
import os
import re
import stat

# The key types sshd takes, and for the ECDSA ones their curve; as
# org.webosports.service.devmode's lib/access.js.
KEY_TYPES = {
    'ssh-ed25519': None,
    'ssh-rsa': None,
    'ecdsa-sha2-nistp256': b'nistp256',
    'ecdsa-sha2-nistp384': b'nistp384',
    'ecdsa-sha2-nistp521': b'nistp521',
    'sk-ssh-ed25519@openssh.com': None,
    'sk-ecdsa-sha2-nistp256@openssh.com': b'nistp256',
}
MIN_RSA_BITS = 2048

MATCH_MARK = '# LuneOS: root\'s password over the local link only'


class SshError(Exception):
    pass


def _ssh_strings(blob):
    out = []
    i = 0
    while i < len(blob):
        if i + 4 > len(blob):
            raise SshError('the key\'s data is damaged')
        n = int.from_bytes(blob[i:i + 4], 'big')
        i += 4
        if i + n > len(blob):
            raise SshError('the key\'s data is damaged')
        out.append(blob[i:i + n])
        i += n
    return out


def parse_public_key(line):
    """One OpenSSH public key line, without options -> 'type base64 [comment]'.

    Raises SshError for anything else: options, a type sshd should not be
    given, damaged base64, a blob of another type, RSA under 2048 bits,
    control characters.
    """
    if not isinstance(line, str):
        raise SshError('not a line of text')
    line = line.strip()
    if re.search(r'[\x00-\x08\x0a-\x1f\x7f]', line):
        raise SshError('control characters in the line')
    m = re.match(r'^(\S+)\s+([A-Za-z0-9+/]+={0,2})(?:\s+(.*))?$', line)
    if not m or m.group(1) not in KEY_TYPES:
        raise SshError('not an OpenSSH public key line (or it has options)')
    ktype, b64, comment = m.group(1), m.group(2), (m.group(3) or '').strip()
    try:
        blob = base64.b64decode(b64, validate=True)
    except ValueError:
        raise SshError('the key\'s base64 is damaged')
    if base64.b64encode(blob).decode() != b64:
        raise SshError('the key\'s base64 is damaged')
    parts = _ssh_strings(blob)
    if not parts or parts[0] != ktype.encode():
        raise SshError('the key\'s data does not match its type')
    if ktype == 'ssh-rsa':
        if len(parts) != 3:
            raise SshError('the RSA key is damaged')
        n = parts[2].lstrip(b'\0')
        if len(n) * 8 < MIN_RSA_BITS:
            raise SshError('RSA keys must be at least %d bits' % MIN_RSA_BITS)
    elif ktype == 'ssh-ed25519':
        if len(parts) != 2 or len(parts[1]) != 32:
            raise SshError('the Ed25519 key is damaged')
    elif KEY_TYPES[ktype] is not None and (len(parts) < 2 or parts[1] != KEY_TYPES[ktype]):
        raise SshError('the ECDSA key\'s curve does not match its type')
    return ktype + ' ' + b64 + (' ' + comment if comment else '')


def passwd_home(rootfs, user='root'):
    """A user's home as the image's /etc/passwd has it (uid 0 for root)."""
    try:
        with open(os.path.join(rootfs, 'etc/passwd')) as f:
            lines = f.read().splitlines()
    except OSError as e:
        raise SshError('no /etc/passwd in the image: %s' % e)
    for line in lines:
        f = line.split(':')
        if len(f) >= 7 and f[0] == user:
            if user == 'root' and f[2] != '0':
                raise SshError('root is not uid 0 in /etc/passwd')
            home = f[5]
            if not home.startswith('/') or os.path.normpath(home) != home:
                raise SshError('%s has no usable home in /etc/passwd: %r' % (user, home))
            return home
    raise SshError('no %s in /etc/passwd' % user)


def _in_rootfs(rootfs, path):
    return os.path.join(rootfs, path.lstrip('/'))


def read_key_files(paths):
    """Every key in the build host's files, each checked; comments and blank lines skipped."""
    keys = []
    for p in paths:
        try:
            with open(p) as f:
                text = f.read()
        except OSError as e:
            raise SshError('%s: %s' % (p, e.strerror))
        for n, line in enumerate(text.splitlines(), 1):
            if not line.strip() or line.lstrip().startswith('#'):
                continue
            try:
                keys.append(parse_public_key(line))
            except SshError as e:
                raise SshError('%s:%d: %s' % (p, n, e))
    return keys


def install_authorized_keys(rootfs, key_paths, chown=os.chown):
    """Root's authorized_keys in the image: .ssh 0700 and the file 0600, root's.

    Keys already there are kept; the ones from key_paths are added once.
    Returns the number added.
    """
    keys = read_key_files(key_paths)
    if not keys:
        return 0
    home = _in_rootfs(rootfs, passwd_home(rootfs))
    if not os.path.isdir(home):
        os.makedirs(home, mode=0o700)
    sshdir = os.path.join(home, '.ssh')
    if os.path.islink(sshdir):
        raise SshError('root\'s .ssh in the image is a link')
    os.makedirs(sshdir, mode=0o700, exist_ok=True)
    os.chmod(sshdir, 0o700)
    ak = os.path.join(sshdir, 'authorized_keys')
    old = []
    if os.path.lexists(ak):
        if os.path.islink(ak):
            raise SshError('root\'s authorized_keys in the image is a link')
        with open(ak) as f:
            old = [l for l in f.read().splitlines() if l.strip()]
    have = set(l.split()[1] for l in old if len(l.split()) > 1)
    added = []
    for k in keys:
        if k.split()[1] not in have:
            have.add(k.split()[1])
            added.append(k)
    fd = os.open(ak + '.new', os.O_WRONLY | os.O_CREAT | os.O_TRUNC, 0o600)
    with os.fdopen(fd, 'w') as f:
        f.write('\n'.join(old + added) + '\n')
    os.chmod(ak + '.new', 0o600)
    os.rename(ak + '.new', ak)
    for p in (sshdir, ak):
        chown(p, 0, 0)
    return len(added)


def ssh_mode_problems(rootfs):
    """Every .ssh and authorized_keys in the image's homes that others may read or change, or not its user's."""
    problems = []
    try:
        with open(os.path.join(rootfs, 'etc/passwd')) as f:
            entries = [l.split(':') for l in f.read().splitlines()]
    except OSError:
        return problems
    seen = set()
    for f in entries:
        if len(f) < 7 or not f[5].startswith('/'):
            continue
        try:
            uid = int(f[2])
        except ValueError:
            continue
        home = _in_rootfs(rootfs, f[5])
        for rel, kind in (('.ssh', 'directory'), ('.ssh/authorized_keys', 'file'), ('.ssh/authorized_keys2', 'file')):
            p = os.path.join(home, rel)
            if p in seen or not os.path.lexists(p):
                continue
            seen.add(p)
            st = os.lstat(p)
            name = os.path.join(f[5], rel)
            if stat.S_ISLNK(st.st_mode):
                problems.append('%s is a link' % name)
                continue
            if st.st_mode & 0o077:
                problems.append('%s is mode %04o; it must be %s to %s alone' %
                                (name, stat.S_IMODE(st.st_mode), '0700' if kind == 'directory' else '0600', f[0]))
            if st.st_uid != uid:
                problems.append('%s belongs to uid %d, not %s (%d)' % (name, st.st_uid, f[0], uid))
    return problems


def sshd_config_local_password(text, addresses, debug_access=False):
    """sshd_config with root's password allowed over the local link only.

    Key-only everywhere (PasswordAuthentication no; KbdInteractive stays
    off, so PAM cannot ask for a password either), except for connections to
    one of addresses - the device's end of its USB network, and on an
    emulator its NAT address - where root may log in with its password.
    With debug_access (LUNEOS_DEBUG_ACCESS) PasswordAuthentication is left
    alone: the empty root password logs in through it.
    """
    lines = text.splitlines()
    if not any(re.match(r'^KbdInteractiveAuthentication\s+no\s*$', l) for l in lines):
        raise SshError('no "KbdInteractiveAuthentication no" line: a password could come in through PAM')
    if any(l.strip() == MATCH_MARK for l in lines):
        raise SshError('sshd_config has the local-link block already')
    if not debug_access:
        n = 0
        for i, l in enumerate(lines):
            if re.match(r'^[#\s]*PasswordAuthentication\s', l):
                lines[i] = 'PasswordAuthentication no'
                n += 1
        if not n:
            raise SshError('no PasswordAuthentication line to set')
    addrs = [a for a in addresses if a]
    for a in addrs:
        if not re.match(r'^[0-9a-fA-F:.]+$', a):
            raise SshError('not an address: %r' % a)
    if addrs:
        lines += [
            '',
            MATCH_MARK,
            '# (luneos_image.bbclass, LUNEOS_SSH_PASSWORD_ADDRESSES): the device\'s end',
            '# of its USB network, and an emulator\'s NAT address. Last in the file: a',
            '# Match block runs to the next Match or the end.',
            'Match LocalAddress ' + ','.join(addrs),
            '\tPasswordAuthentication yes',
            '\tPermitRootLogin yes',
        ]
    return '\n'.join(lines) + '\n'
