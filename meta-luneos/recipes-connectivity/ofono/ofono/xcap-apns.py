#!/usr/bin/env python3
#
# Builds xcap-apns.json, the XCAP access point of each network, from
# Android's APN lists.
#
# Supplementary services (call forwarding, call waiting, caller ID,
# barring) go over XCAP when the phone is on LTE with IMS. The modem
# needs to be told which APN to reach the XCAP server through. oFono's
# provision.json has no XCAP type, so it comes from Android, which names
# it in its APN lists as type "xcap".
#
# Sources, in order of preference:
#   1. AOSP device/sample/etc/apns-full-conf.xml (Apache-2.0)
#   2. The apns-conf.xml MediaTek ships on its Android devices (carries
#      the same Apache-2.0 notice); it has many networks AOSP's lacks,
#      Odido (204/16) among them
#
# Only access points for a whole network are taken: an entry with an
# mvno_type is for one MVNO, which provision.json cannot express.
# Where a network's XCAP traffic goes over its internet APN, as
# Vodafone NL's does, that APN is listed as the XCAP one: oFono keeps
# one access point per type, so XCAP gets its own entry either way.
#
#   xcap-apns.py apns-full-conf.xml mtk-apns-conf.xml > xcap-apns.json

import json
import sys
import xml.etree.ElementTree as ET

PROTOCOLS = {'IP': 'ipv4', 'IPV6': 'ipv6', 'IPV4V6': 'ipv4v6'}
AUTH = {'1': 'pap', '2': 'chap', '3': 'chap'}


def xcap_entries(path):
    """Yields ((mcc, mnc), entry, dedicated) for each XCAP access point."""
    for apn in ET.parse(path).getroot().iter('apn'):
        types = [t.strip().lower() for t in apn.get('type', '').split(',')]
        if 'xcap' not in types or apn.get('mvno_type'):
            continue

        mcc, mnc, name = apn.get('mcc'), apn.get('mnc'), apn.get('apn')
        if not (mcc and mnc and name):
            continue

        entry = {'name': 'XCAP', 'apn': name, 'type': ['xcap']}

        protocol = PROTOCOLS.get(apn.get('protocol', '').upper())
        if protocol:
            entry['protocol'] = protocol

        user, password = apn.get('user'), apn.get('password')
        if user or password:
            entry['username'] = user or ''
            entry['password'] = password or ''
            auth = AUTH.get(apn.get('authtype', ''))
            if auth:
                entry['authentication'] = auth
        else:
            entry['authentication'] = 'none'

        yield (mcc, mnc), entry, types == ['xcap'], apn.get('carrier', '')


def main(paths):
    picked = {}

    for path in paths:
        found = {}
        for key, entry, dedicated, carrier in xcap_entries(path):
            # Within one list an XCAP-only access point wins over one that
            # also carries internet, then the first one listed
            if key not in found or (dedicated and not found[key][1]):
                found[key] = (entry, dedicated, carrier)

        # An earlier list wins over a later one
        for key, value in found.items():
            picked.setdefault(key, value)

    providers = []
    for (mcc, mnc) in sorted(picked):
        entry, _, carrier = picked[(mcc, mnc)]
        providers.append({
            'name': carrier or 'XCAP',
            'ids': [mcc + mnc],
            'apns': [entry],
        })

    json.dump(providers, sys.stdout, indent=2, ensure_ascii=False)
    sys.stdout.write('\n')


if __name__ == '__main__':
    if len(sys.argv) < 2:
        sys.exit('usage: xcap-apns.py <apns-conf.xml>...')
    main(sys.argv[1:])
