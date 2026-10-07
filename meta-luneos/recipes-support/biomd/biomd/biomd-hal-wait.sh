#!/bin/sh
# Wait for the vendor fingerprint HAL to register before biomd starts.
#
# biomd looks the HAL up once, at startup, and never again. On the Unihertz
# Titan Pocket the vendor service (android.hardware.biometrics.fingerprint@2.1,
# Sunwave sensor) first opens its TrustKernel TEE session, which takes a few
# seconds, so biomd asked two seconds after the service started, found nothing,
# and gave up for the rest of the boot (5 Oct 2026):
#
#   biomd: Failed to get fingerprint remote service
#   biomd: No usable fingerprint HAL: neither ...IFingerprint nor the HIDL
#          IBiometricsFingerprint service answered
#
# The lazy start biomd's lookup triggers cannot help there: the vendor rc
# declares no "interface" line for the service ("Could not find ... for
# ctl.interface_start"), init just starts it normally.
#
# Same shape as ofono-binder-wait.sh: poll with binder-ping (libgbinder-tools)
# for either the HIDL or the AIDL service, and always exit 0, so a device whose
# HAL never comes up still gets biomd, exactly as before.
HIDL=android.hardware.biometrics.fingerprint@2.1::IBiometricsFingerprint/default
AIDL=android.hardware.biometrics.fingerprint.IFingerprint/default
TIMEOUT=${BIOMD_HAL_WAIT_TIMEOUT:-30}
VENDOR=/android/vendor

command -v binder-ping >/dev/null 2>&1 || exit 0

# No fingerprint HAL shipped at all: nothing to wait for.
if ! ls $VENDOR/etc/init/*fingerprint*.rc $VENDOR/etc/init/*biometrics*.rc >/dev/null 2>&1; then
    echo "biomd-hal-wait: the vendor ships no fingerprint HAL, not waiting"
    exit 0
fi

i=0
while [ "$i" -lt "$TIMEOUT" ]; do
    if binder-ping -d /dev/hwbinder "$HIDL" >/dev/null 2>&1; then
        echo "biomd-hal-wait: $HIDL registered after ${i}s"
        exit 0
    fi
    if binder-ping -d /dev/binder "$AIDL" >/dev/null 2>&1; then
        echo "biomd-hal-wait: $AIDL registered after ${i}s"
        exit 0
    fi
    sleep 1
    i=$((i + 1))
done
echo "biomd-hal-wait: no fingerprint HAL after ${TIMEOUT}s, starting biomd anyway"
exit 0
