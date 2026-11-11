# Pixel Tablet: Trusty boot-crash workaround

**Experimental. This is a source change and an offline rootfs overlay, not a
complete operating-system image. It has not yet passed a LuneOS boot test on our
tablet. Do not treat the bypass as a repair of Trusty's underlying storage.**

## Why this exists

[Issue #822](https://github.com/webOS-ports/meta-webos-ports/issues/822) reports a
Trusty secure-storage assertion on the Pixel Tablet (`tangorpro`) after its first
boot. The author verified that preventing `storageproxyd` from running avoids
the crash across reboots. The report used Android 17 vendor build
`CP3A.260905.009`; our tablet has that same vendor build.

This packages the service-disabling bypass using the existing Android pre-start
hook, `50-stub-services`. A per-codename list selects only `tangorpro` and
bind-mounts the existing `stub-sleep` over `/vendor/bin/storageproxyd` before
Android init starts. Neither its `early_hal` class nor an explicit `restart`
can then start the real storage proxy. There is no stop-after-start race and no
rewrite of the read-only vendor partition. The vendor binary and secure-storage
files are preserved.

**Tradeoff:** Android-backed secure storage is unavailable while this rule is
enabled. KeyMint, Gatekeeper, fingerprint and other clients may depend on it.
This does not make the port generally stable or eliminate flashing risks.

## The maintainer's persist-mount suggestion

The maintainer asked whether `/mnt/vendor/persist` was actually mounted in the
LuneOS container. An empty directory there could leave the storage tiers out of
sync. That explanation still needs a LuneOS boot test; a successful bypass does
not prove the cause.

The newer Android startup code already has
[`9f0cdd70e36d` (28 September 2026)](https://github.com/Herrie82/meta-smartphone/commit/9f0cdd70e36d14ec3123387cd46da23957766e2b),
which reads every vendor fstab, including `fstab.persist`. The public tablet
archive is dated 16 September. A fresh build should include the newer mount
code before deciding whether the bypass remains necessary.

There is a separate firmware compatibility problem: the public archive's README
says its boot images were built against `BP4A.251205.006` and warns not to flash
them on CP-series Android 17 firmware. The date in the archive name does not
establish compatibility. Adding this rootfs rule does not fix that mismatch;
matching boot images and a stock recovery image are still required before a
hardware test. Do not run the old archive's installer on a CP3A tablet.

`tools/check-tangorpro-persist.py` is a separate, read-only diagnostic for a
running LuneOS host. It checks that the exact mount point has a writable ext4 or
f2fs filesystem, that its backing block device matches the persist partition,
and that LXC recursively shares `/mnt`. It never mounts, repairs, formats or
clears storage, and is not an automatic boot prerequisite.

The stock Android baseline on our tablet is an ext4 persist mount at
`/mnt/vendor/persist`. Its vendor fstab has ext4 and f2fs alternatives. Do not
copy the fstab's `formattable` fs_mgr flag into a recovery procedure.

## Build integration

Use this branch as the `meta-webos-ports` layer in a compatible wrynose LuneOS
build, together with the newer `meta-smartphone` startup code. Rebuild
`android-system` and the shared Halium rootfs. The bbappend adds the rule to
`SRC_URI`, so changing it changes the install task signature.

The deployed file is:

```
/var/lib/lxc/android/stubbed-services.d/tangorpro
```

Other device codenames keep their existing behaviour. No service restart is
requested by this change; the rule is read when the container next starts.

## Stage in an existing offline rootfs

On a Linux build/recovery host, first mount the LuneOS rootfs offline. Then:

```
python3 tools/stage-tangorpro-workaround.py check /path/to/mounted/luneos-rootfs
python3 tools/stage-tangorpro-workaround.py apply /path/to/mounted/luneos-rootfs
```

The tool refuses the running `/`, symlinks that escape the supplied rootfs,
different existing tablet rules, and startup hooks that differ from the pinned
versions covered by the tests. Inspection of the public archive confirmed that
its stub hook and sleep stub match those fixtures exactly. A shared rootfs does
not need a tangorpro display adaptation to use this separate per-device rule.
It does not flash a device or produce a complete boot image.

To undo this overlay before booting, use `remove` instead of `apply`. On an
installed LuneOS system, remove only this rule and reboot after verifying that
the underlying storage problem is resolved. Do not restart the Android container
under a live graphical session.

## Validation and hardware test still required

```
python3 tests/test_tangorpro_trusty.py
# Linux only, with mounts isolated from the host:
sudo unshare --mount --propagation private python3 tests/test_tangorpro_trusty.py --real-mounts
```

The Linux test checks a real bind mount over a read-only vendor filesystem and
verifies that unmounting exposes the original binary unchanged. It cannot test
Trusty or the tablet's boot sequence.

On the tablet, collect the persist diagnostic and container mount state, then
test first boot and several subsequent reboots beyond the reported 50–60 second
failure window. Separately test the newer mount code without the bypass only
when the real persist mount is confirmed. Record which configuration was used;
do not close #822 based only on host tests.

## Returning the tablet to Google

Keep an official Google factory image and its published checksum available
before installing the experimental OS. Preserve the factory calibration and
persist partitions throughout the experiment. When finished, restore Google's
firmware, verify that Android boots successfully, and only then relock the
bootloader. A rootfs overlay is not a substitute for a stock restore.
