#!/usr/bin/env python3
"""Add a whole-file signature to a zip, the one an Android recovery checks.

usage: sign-recovery-zip.py <private key> <certificate> <zip>

Does what AOSP's "signapk -w" does, with openssl instead of a JVM. The signature
is a detached PKCS#7 block over everything before the zip's comment length
field, stored in the comment and followed by a six-byte footer:

    <signature start, from end of file> ff ff <comment size>     (16-bit LE)
"""
import struct
import subprocess
import sys
import threading

EOCD_MAGIC = b'PK\x05\x06'
EOCD_SIZE = 22
FOOTER_SIZE = 6


def sign(key, cert, path, signed_len):
    proc = subprocess.Popen(
        ['openssl', 'cms', '-sign', '-binary', '-noattr', '-nosmimecap',
         '-md', 'sha256', '-outform', 'DER', '-signer', cert, '-inkey', key],
        stdin=subprocess.PIPE, stdout=subprocess.PIPE)
    output = []
    reader = threading.Thread(target=lambda: output.append(proc.stdout.read()))
    reader.start()
    with open(path, 'rb') as f:
        left = signed_len
        while left:
            chunk = f.read(min(left, 1 << 24))
            if not chunk:
                sys.exit('short read from ' + path)
            proc.stdin.write(chunk)
            left -= len(chunk)
    proc.stdin.close()
    reader.join()
    if proc.wait() != 0 or not output[0]:
        sys.exit('openssl could not sign ' + path)
    return output[0]


def main():
    if len(sys.argv) != 4:
        sys.exit(__doc__)
    key, cert, path = sys.argv[1:]
    with open(path, 'rb') as f:
        f.seek(0, 2)
        size = f.tell()
        f.seek(size - EOCD_SIZE)
        eocd = f.read(EOCD_SIZE)
    if eocd[:4] != EOCD_MAGIC or eocd[-2:] != b'\0\0':
        sys.exit(path + ' does not end in an end-of-central-directory record with no comment')

    signature = sign(key, cert, path, size - 2)
    comment_size = len(signature) + FOOTER_SIZE
    if comment_size > 0xffff:
        sys.exit('signature too large')
    # Recovery refuses a second marker after the real one.
    if EOCD_MAGIC in eocd[4:] + signature:
        sys.exit('the end-of-central-directory marker occurs in the signature')
    footer = struct.pack('<HBBH', comment_size, 0xff, 0xff, comment_size)
    with open(path, 'r+b') as f:
        f.seek(size - 2)
        f.write(struct.pack('<H', comment_size) + signature + footer)


main()
