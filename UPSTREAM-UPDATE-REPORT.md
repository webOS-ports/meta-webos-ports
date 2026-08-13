# meta-webos-ports — Third-Party Upstream Update Analysis

**Date:** 2026-08-11 · **Branch:** `scarthgap` · **Base:** OE-Core 5.0 LTS (scarthgap)

## Execution status

Everything below was written before any of it was attempted. What actually
happened, on branch `luneos-upstream-updates`:

**Done and building:** the whole binder/telephony stack, ofono 2.19 + ell 0.83,
the Qt/QML bumps, libqofono/libqofonoext (qmake→CMake), and all the Tier 6
small items. Tier 4 (mesa 26.2.0, meson 1.12.0, wayland-protocols 1.49),
extra-cmake-modules 6.28.0 and lxc 4.0.12 are committed but not yet built.

**Four ratings in this report were wrong**, and are corrected in place below:

1. **ofono was rated High, it is Low.** The patch-failure count came from
   testing patches individually instead of in sequence, and included two orphan
   patches plus one belonging to a different recipe.
2. **Tier 7 was badly overstated.** Only anbox and the local
   python3-jsonschema turned out to be removable. `snowboy`, `libbson`, `ptmalloc3`,
   `leveldb-tl` and `googleapis` are all actively depended on, and every one of
   their "dead" sources still fetches. Worse, `ashmem.bb`/`binder.bb` are not
   part of anbox at all — they build the kernel modules Waydroid needs.
3. **mesa was rated Low; it has a cross-repo dependency.** meta-smartphone
   carries a version-pinned `mesa_<ver>.bbappend` with a 15-patch freedreno
   series for the HP TouchPad that silently stops applying unless renamed in
   lockstep.
4. **lxc 7.0.0 is not reachable as a bump.** 5.0.0 moved to meson, so the tier
   landed on 4.0.12, the last autotools release.

**cordova looked removable and is not.** The dependency is indirect, via
`webos_cordova_application.bbclass` (`RDEPENDS:${PN} += "cordova"`), which the
calculator and testr apps inherit. The "zero references" check that missed it
could not have found it: `\b` does not match across `_`, and `.bbclass` files
were not in the search at all. It stays.

**Not attempted, with reasons recorded in the relevant sections:** waydroid
(needs a rebase in the LuneOS fork first), voicecall and kf5bluezqt (design
decisions, not mechanical work), and node-gyp / node-sqlite3 / node-red /
telegraf (all blocked on regenerating vendored npm or Go metadata).

## Scope

Everything in `meta-luneos`, `meta-luneui`, `meta-luneos-backports-5.1` and
`meta-luneos-backports-6.0` that tracks a **third-party upstream** — i.e. not
`recipes-webos-ose/`, not `recipes-webos-owo/`, and not the LuneOS-authored
components in `recipes-luneos/`. Pure `.bbappend`s over OE-Core / meta-oe recipes
are excluded (they follow the base layers), but the **backport layers are
included**, because those are locally pinned copies whose version is this repo's
decision.

All version data was obtained by querying the actual upstream remotes
(`git ls-remote`, blobless clones, GitHub compare API) on 2026-08-11, and all
patch-rebase verdicts come from running `git apply --check` of the layer's
patches against current upstream `HEAD`.

---

## Summary table

| # | Component | Recipe version | Upstream latest | Behind | Complexity | Priority |
|---|-----------|----------------|-----------------|--------|-----------|----------|
| 1 | **ofono** (backport) | 2.9 (2024-07-08) | **2.19** (2025-11-21) | 160 commits | **Low** | 🔴 Security |
| 2 | **ofono-halium** (sailfishos fork) | rev `3afa0876` (2023-04-14) | `bea32ca9` (2026-08-10) | 24 commits | **Low** | 🔴 Security |
| 3 | ell (backport) | 0.67 | **0.83** | 16 releases | Low | 🔴 (ofono dep) |
| 4 | libgbinder | 1.1.35 (2023-12-01) | **1.1.52** | 124 commits | Low | 🟠 High |
| 5 | libgbinder-radio | 1.5.6 (2023-04-21) | **1.6.6** | 40 commits | Low–Med | 🟠 High |
| 6 | ofono-binder-plugin (+ext) | 1.1.12 (2023-05-23) | **1.1.28** | 112 commits | Medium | 🟠 High |
| 7 | libglibutil | PV 1.0.61 / rev = 1.0.75 | **1.0.82** | 35 commits | Trivial | 🟠 High |
| 8 | bluebinder | 1.0.12 (2020-08-03) | **1.0.20** | 27 commits | Low–Med | 🟠 High |
| 9 | sensorfw | 0.15.1 (2025-04-29) | **0.15.2** | 7 commits | **Trivial** | 🟠 High |
| 10 | waydroid (herrie82 fork) | 1.4.2 (fork 2024-01-25) | **1.6.3** | 125 commits | Medium | ⏸ Blocked on fork rebase |
| 11 | python3-gbinder | 1.1.2 (2023-09-16) | **1.3.1** | 18 commits | **Trivial** | 🟠 High |
| 12 | libconnman-qt | 1.3.3 (2023-05-31) | **1.4.25** | 110 commits | Low–Med | 🟡 Medium |
| 13 | voicecall | 0.7.14 (2022-01-11) | **0.9.0** | 45 commits | **High** | ⏸ Needs mlite5/commhistory decision |
| 14 | libqofono | 0.123 (2024-06-06) | **0.130** | 20 commits | **Medium–High** | 🟡 Medium |
| 15 | libqofonoext | 1.0.32 (2024-03-19) | **1.0.35** | 11 commits | **Medium–High** | 🟡 Medium |
| 16 | nemo-qml-plugin-dbus | 2.1.28 (2022-09-15) | **2.1.30+** | 26 commits | Low | 🟡 Medium |
| 17 | qt6-qpa-hwcomposer-plugin | 5.6.2.24 (2023-02-18) | **5.6.2.27** | 13 commits | Low | 🟡 Medium |
| 18 | audiosystem-passthrough | 1.2.1 (2021-11-17) | **1.3.1** | 10 commits | Low | 🟡 Medium |
| 19 | libmce-glib | 1.0.7 (2019-08-30) | **1.1.0** | 25 commits | Trivial | 🟡 Medium |
| 20 | ofono-ril-binder-plugin | 1.2.7 (2022-01-10) | **1.2.8** | 4 commits | Trivial | 🟢 Low |
| 21 | qtscenegraph-adaptation | 0.8.0 (2022-08-11) | **0.8.1** | 4 commits | Trivial | 🟢 Low |
| 22 | kf5bluezqt-mer | 5.24.0 (2017-04-20) | **5.112.0** | 38 commits | **Highest** | ⏸ Upstream ships no source; consider KF6 |
| 23 | mesa (backport) | 26.1.0+git | **26.2.0** | — | Low **+ cross-repo** | ✅ Done (needs meta-smartphone rename) |
| 24 | meson (backport) | 1.5.1 | **1.12.0** | — | Low (2 patches reworked) | ✅ Done |
| 25 | wayland-protocols (backport) | 1.47 | **1.49** | — | Trivial | ✅ Done |
| 26 | node-gyp-native | 10.0.1 (2023-11-02) | **13.0.1** | 155 commits | **High** | ⏸ Vendored npm registry must be regenerated |
| 27 | node-sqlite3 | 3.1.13+git (mapbox) | **6.0.1** (TryGhost) | repo moved | **High** | ⏸ node-pre-gyp → prebuild-install rewrite |
| 28 | node-red | 3.0.2 | **5.0.4** | 2 majors | **High** | ⏸ 5223-line npm-shrinkwrap must be regenerated |
| 29 | telegraf | 1.21.4 | **1.39.3** | branch EOL | **High** | ⏸ 4 patches written for v1.21.4 specifically |
| 30 | lxc | 4.0.6 | 7.0.0 (**→ 4.0.12 taken**) | 3 majors | Medium | ✅ Done to 4.0.12; 5.0+ = meson rewrite |
| 31 | tidy-html5 | rev @ **2012-08-21** | **5.8.0** / 5.9.19 | 1613 commits | Medium | 🟡 Medium |
| 32 | extra-cmake-modules | 5.95.0 | **6.28.0** | KF5→KF6 | Low (1 patch rebased) | ✅ Done |
| 33 | smemstat | 0.01.10 (2014-06-03) | **0.02.13** | 157 commits | Trivial | 🟢 Low |
| 34 | powerstat | 0.03.03 (2023-07-11) | **0.04.06** | 41 commits | Trivial | 🟢 Low |
| 35 | sdl2-opengles-test | 1.0.9 (2015-01-15) | **1.0.11** | 7 commits | Trivial | 🟢 Low |
| 36 | sdbus-cpp (anbox dep) | rev @ 2020-07-22 | **2.3.1** | 244 commits | — | ✅ Gone with anbox |
| 37 | anbox + anbox-data | rev @ 2021 | **archived 2024-02** | dead | — | ✅ Removed (`ashmem`/`binder` **kept**) |
| 38 | snowboy | 1.3.0 | defunct upstream, **source still fetches** | — | ❌ Keep — libgoogleassistant needs it |
| 39 | libbson | 1.9.0+git | archived → mongo-c-driver 2.x (API differs) | — | ❌ Keep — settingsservice needs it |
| 40 | ptmalloc3 · cordova | frozen 2012 · 2013 | ptmalloc3 tarball still serves | — | ❌ **Keep both.** ptmalloc3 (wam); cordova is RDEPENDed via `webos_cordova_application.bbclass` |
| 41 | python3-jsonschema | 4.17.3 | oe-core ships **4.21.1** | *downgrade* | Trivial | ✅ Removed |

**Already current — no action:** `go` 1.26.5, `presage` 0.9.1, `gphotofs` 0.5,
`ecryptfs-utils` 111, `pulseaudio-pulsecore-private-headers` 17.0,
`pulseaudio-modules-droid`, `pulseaudio-modules-droid-hidl`, `nmeaparser`,
`dbus-cpp`, `process-cpp`, `nodejs-enyo-dev`, `libgrilio`, `tdlib` 1.8.66,
`purple-matrix`.

---

# Tier 1 — Security (do these first)

## 1. ofono 2.9 → 2.19 · `meta-luneos-backports-5.1`

**This is the single most important item in this report.**

The backported oFono is pinned at 2.9 (released 2024-07-08). The
CVE fixes landed in **2.10** (2024-08-22) — six weeks after our pin. The
backport, and therefore every non-Halium LuneOS device, is missing:

| Commit | Issue |
|---|---|
| `90e60ada` | **CVE-2024-7543** — `stkutil` |
| `a240705a` | **CVE-2024-7544** — `stkutil` |
| `79ea6677` | **CVE-2024-7546** |
| `305df050` | **CVE-2024-7547** |
| `5209fd65` | smsutil: user data length exceeds internal buffer |
| `9e9c1cb5` | smsutil: possible buffer overflow |
| `e6d8d526` | qmi/sms: out-of-bounds read |
| `389e2344` | ussd: content overruns buffers |
| `29ff6334` | atmodem/sms: uninitialised buffer use |
| `556e1454`, `2ff2da7a`, `02aa0f9b`, `1e2a7684` | further bounds checks in stkutil/smsutil/util |

All of these are reachable from **attacker-supplied radio traffic** (SMS,
USSD, SIM Toolkit) — the worst possible input surface on a phone.

**Non-security improvements in the same 160-commit window:**
- **QMI**: service request rate limiting with per-device quirk flags
  (`3a096f6f`, `869db334`, `c4b04bbc`, `fd68bb50`, `2d3d018a`) — directly
  relevant to the Qualcomm-modem PinePhone/PinePhone Pro path.
- **QMI**: SMS storage moved to UIM, message ack implemented
  (`d5066b09`, `ad2de57c`).
- **gobi**: raw-IP-only devices, LTE capability detection instead of assumption,
  `OfflineOperatingMode`, PDS service (`813f654f`, `92172e3b`, `4eeededa`).
- **MBIM**: supported-services query at init, MHI MBIM-mode detection in udevng
  (`bd3997b4`, `92f6fe8c`) — relevant to newer modems.
- **HFP** updated to spec 1.8; GCC-14 build fixes.
- SIM: new D-Bus method to clear the forbidden-operator list.

### Complexity: **Low**

> **Correction.** An earlier draft of this report rated this **High**, claiming
> five of eight patches failed against 2.19. That was a measurement error: the
> patches were tested individually against a pristine tree, when in fact bitbake
> applies them **in sequence**, each on the tree the previous one produced. The
> count also included two orphan patches and one belonging to `ofono-halium`.
> Retested correctly, the series applies cleanly all the way to 2.19.

One real prerequisite, one non-issue:

**(a) ell must move first.** `configure.ac` requirements:

| ofono | requires ell |
|---|---|
| 2.9 | ≥ 0.67 ← *our current pin* |
| 2.12 | ≥ 0.70 |
| 2.15 | ≥ 0.72 |
| 2.19 | ≥ 0.79 |

So `meta-luneos-backports-5.1/recipes-core/ell/ell_0.67.bb` must become
`ell_0.83.bb` (or at least 0.79) in the same series. ell is a small, self-
contained tarball recipe with no local patches — this part is genuinely easy.

**(b) The local patches need no work at all.** `ofono_%.bbappend` applies exactly
five patches, and applied **in sequence** they go on cleanly at 2.10, 2.12, 2.15
and 2.19 alike:

| Patch | 2.9 (today) | 2.19 |
|---|---|---|
| `0001-common-create-GList-helper-ofono_call_compare.patch` | ✅ | ✅ |
| `0002-common-atmodem-move-at_util_call_compare_by_status.patch` | ✅ | ✅ |
| `0003-common-atmodem-move-at_util_call_compare_by_id.patch` | ✅ | ✅ |
| `0004-add-call-list-helper-to-manage-voice-call-lists.patch` | ✅ | ✅ |
| `0006-Allow-qmi-qrtr-without-data.patch` | ✅ | ✅ |

Two further patches sit in the directory but are **orphans — referenced by no
recipe**: `0005-qmimodem-implement-voice-calls.patch` and
`0001-Fix-build-with-ell-0.39-...-unlikely-macro.patch`. They are dead weight and
can be deleted independently of any upgrade. (Upstream has carried its own
`drivers/qmimodem/voicecall.c` since before 2.9, which is presumably why 0005 was
dropped from the bbappend.)

`0002-Add-support-for-the-Ericsson-F5521gw-modem.patch` belongs to
`ofono-halium`, not this recipe; it uses `striplevel=2` and applies cleanly
against the new sailfish revision.

**Effort:** hours, not days. Bump ell, bump the tarball and sha256, rebuild,
re-test telephony on PinePhone.

---

## 2. ofono-halium (sailfishos/ofono) — 24 commits behind

Halium devices use a different recipe (`ofono-halium_1.29.bb`, pinned
`3afa0876`, 2023-04-14) — and the Sailfish fork has since merged **the same CVE
set**:

```
f65bb725 Fix CVE-2024-7546      02dded4a Fix CVE-2024-7547
4f51a41c stkutil: Fix CVE-2024-7543   463b2633 stkutil: Fix CVE-2024-7544
ff9c7463 smsutil: fix possible buffer overflow
434612cc sim: fix use-after-free in open_channel_cb watcher loop
66588118 gdbus: fix double free
b0720e92 ussd: ensure ussd content fits in buffers
bb7d65f4 smsutil: Validate the length of the address field
```

Plus a **use-after-free** and a **double-free** that the mainline tree did not
have.

### Complexity: **Low**

Only 24 commits on a stable fork, no build-system change, and the recipe carries
just one local patch (the Ericsson F5521gw one). This is a pure `SRCREV` bump
plus a patch-refresh check.

**Do this one first** — best security-per-hour ratio in the entire report.

---

# Tier 2 — High value, low risk

## 4. libgbinder 1.1.35 → 1.1.52 (124 commits)

Foundation of the whole Halium stack (ofono binder plugin, bluebinder, sensorfw,
waydroid, pulseaudio droid modules all sit on it).

**Most important changes:**
- `235cf63` **Support both `checkService` variants for Android 15** (JB#64782) —
  required for any Halium 15 port.
- `6bb2afa` / `546d5ad` **`aidl5` and `aidl6` servicemanager variants**
  (JB#63804) — Android 14/15/16 service manager protocols. Waydroid's newer
  releases *require* these (see item 10).
- `64922b6` AIDL registration notifications; `aea668f` drops the dead `aidl4` SM.
- `008667d` **fixed potential string length overflow in the reader** (JB#54354) —
  a hardening fix on parsed binder data.
- `50a4a97` / `81cc3e0` / `ed40b85` new parcelable reader/writer API
  (`gbinder_reader_start/finish_parcelable`, `read_parcelable2`,
  `skip_parcelable`) — **this is what libgbinder-radio 1.6.x and
  ofono-binder-plugin 1.1.2x consume**, so it must land before or with them.
- `05ebe2d`/`6dcb32e`/`7f3b3cc` AIDL proxy binder-stability propagation fixes.
- `04800ca` reject bridges between incompatible protocols.

### Complexity: **Low**
No local patches. Header-only API additions — nothing removed except the dead
`aidl4` servicemanager. `PV` and `SRCREV` bump; verify `1.1.52` ABI against
consumers (all are in-tree and bumped together).

**Caveat:** the recipe's `PV = "1.1.35"` does not correspond to the pinned
`SRCREV` (which is not on a tag). Take the opportunity to pin an actual release
tag so `PV` means something.

## 5. libgbinder-radio 1.5.6 → 1.6.6 (40 commits)

**The headline is `1.6.0`: a complete AIDL IRadio implementation** —
`IRadioConfig`, `IRadioModem`, `IRadioSim`, `IRadioNetwork`, `IRadioData`,
`IRadioMessaging`, `IRadioVoice`, `IRadioIms` (JB#61702). Android 12+ devices
ship AIDL-only radio HALs; without this, newer Halium ports have no telephony at
all.

Also: `757d9ab` fix response acknowledgements in AIDL interfaces (JB#63753),
`d6b950b` `radio_request_try_submit()` API, `35efcc5` remove retried request from
the pending map (JB#64740), `157f1fc` push the queue after dropping a request.

**Bonus:** `920dc52` *"Makefile: honor an externally provided CC"* — upstream now
does what our bluebinder CC patch does locally, a sign the whole mer-hybris set
is becoming cross-compile friendly.

### Complexity: **Low–Medium**
No local patches. Requires libgbinder ≥ 1.1.4x for the parcelable API. AIDL
structures were deprecated in `c18ddb0` in favour of the new client API —
`ofono-binder-plugin` is the only in-tree consumer and it is bumped in the same
series, so the deprecation is contained.

## 6. ofono-binder-plugin 1.1.12 → 1.1.28 (112 commits)

The Halium telephony plugin. Two recipes share the source
(`ofono-binder-plugin.bb`, `libofonobinderpluginext.bb`) — bump both.

**Most important fixes** (nearly all are user-visible telephony bugs):
- `0c0f7a2` **signal strength wrong on non-2G networks** (JB#63747)
- `80f1a9d` **5G support broken in the AIDL interface**; `ff0bef4` NSA 5G
  indication reliability (JB#64178); `33b9054` AIDL `RegStateResult` parsing
- `419b82b` / `7df9f46` **call forwarding broken in AIDL**, and clearing all
  forwardings (JB#63742)
- `b00ecc5` **VoLTE toggle enable/disable broken** (JB#63009)
- `33f5a70` **double free when SMS sending fails** (JB#63132)
- `20f286f` **null-pointer dereference** + false call-failure reporting (JB#63132)
- `fd57dda` wrong memory allocation size for a string (JB#63068)
- `2effb9c` set IA APN after SIM insertion — fixes **stuck on 2G** (JB#64467)
- `4b66ae6` limit `setRadioPower` retries (JB#64354) — stops a retry storm
- `3ce2c0b` connect to the IMS AIDL interface (JB#63804)
- `6e05af7` network scan timeout raised to 70 s
- `7804af7` external ringback tone support (JB#58545)

### Complexity: **Medium**
No local patches, but it is 112 commits with a hard dependency on libgbinder
≥1.1.4x **and** libgbinder-radio ≥1.6.x. Treat items 4/5/6 as **one atomic
series** — they are developed in lockstep upstream and pinned against each
other's APIs. The risk is not build breakage, it is telephony regression, so this
needs real on-device testing on a Halium phone (mido/tissot/hammerhead).

## 7. libglibutil → 1.0.82 (35 commits) — *fix the version string too*

`PV = "1.0.61-1+git"` but the pinned `SRCREV` is actually tag **1.0.75**. The
recipe has been lying about its version for a while; worth correcting regardless
of whether you bump.

Changes are small and safe: `gutil_source_remove()`/`gutil_source_clear()` added
and exported, `gutil_idle_pool_get_default()`, `gutil_int_array_sized_new()` no
longer over-allocates, a Coverity false positive silenced, `gutil_strstrip`
refactor.

### Complexity: **Trivial** — no patches, additive API only. Do it as part of the
binder-stack series since everything there depends on it.

## 8. bluebinder 1.0.12 → 1.0.20 (27 commits)

- `fecaa79` **AIDL Bluetooth HAL support** — required for Android 13+ Halium bases
- `3794a84` handle rfkill power recovery (JB#63833)
- `17da3c7` missing ISO data handling (LE Audio path)
- `348d471` mask local extended features from page 2
- `2e35747` fix functionality with the `bluetooth.audio` service
- `1e8f5de` / `c8e125c` / `87def4e` rewrite of turn_on/turn_off and rfkill
  handling — fixes the long-standing BT-toggle deadlock (JB#51155)
- `eacf61f` allow `/dev/binder` in systemd service sandboxing
- `8398480` **customizable `CC` variable**

### Complexity: **Low–Medium**
Verified against upstream HEAD:
- `0001-Use-CC-as-compiler.patch` → **drop it**, upstream now has
  `CC ?= $(CROSS_COMPILE)gcc` in the Makefile.
- `0002-service-load-after-wifi-module-load.patch` → **needs rebase**; the
  upstream unit file gained `Type=notify`, `EnvironmentFile`, `ExecStartPre`,
  `ExecStartPost` and `Restart=always`. Re-derive the ordering change on top of
  the new file rather than rebasing the diff.

Net: one patch deleted, one small unit-file patch rewritten.

## 9. sensorfw 0.15.1 → 0.15.2 (7 commits) — **easiest win in this report**

- `b815e0d` **AIDL binder interface support** (JB#61406)
- `a0db37b` **restart sensorfw if the binder service dies**
- `0dd182b` better binder error handling
- `9aa664b` hybris backends split into separate classes

### Complexity: **Trivial — verified**
All three LuneOS patches (`TimeoutStopSec`, `preload_sensors`,
`iioadaptor IIO_CHAN_INFO`) **apply cleanly against upstream HEAD**. This is a
one-line `SRCREV`/`PV` change with no patch work at all, and it directly improves
sensor robustness on Halium devices. Do it today.

## 10. waydroid 1.4.2 → 1.6.3 (125 commits behind the fork point)

`recipes-support/waydroid/waydroid.bb` builds `herrie82/waydroid`, branch
`herrie/luneos`, forked from upstream at 2024-01-25. Upstream `main` is 125
commits ahead.

**Most important changes:**
- `9478d59` **`aidl6` service manager on API 36+**, `c0dd5fc` **`aidl5` on API 35+**
  — Android 15/16 images do not work without these (and they need libgbinder
  ≥1.1.51, item 4)
- `e7d73e7` alternate version detection for **Halium 15+**
- `9bd8db0` **AIDL gralloc5 detection**
- `926ec2f` **`xe` kernel driver support** (new Intel GPUs)
- `73fed11` remove radeon from the Vulkan driver map
- `2f723c6` forward `ro.vendor.arm.egl.*` for newer Mali devices — relevant to
  PinePhone Pro / PineTab2
- `21da80c` new **notification manager** service; `5e3725e` handle missing
  `ActivationToken` signal
- `e777a8d` handle Android reboot/shutdown properly
- `f0b470b` fix **TOCTOU bugs in image checksum verification**
- `5f19fe9` fix post-stop hook erroring on every container stop
- Extensive Python static-analysis cleanup (bare excepts, unclosed files)

### Complexity: **Medium**
The work is a **fork rebase**, not a version bump. `herrie/luneos` carries the
LuneOS integration (`make install_luneos` target, `WAYDROID_VERSION`,
`id.waydro.container` app, systemd units). Rebasing that onto upstream 1.6.3
means touching the fork repo, not just this layer. Upstream has also reorganised
`tools/` and `initializer` significantly, so expect conflicts in any file the
fork touched there.

Also note `SPV = "1.4.2"` is used as the version passed to the build
(`WAYDROID_VERSION`), so it needs updating in lockstep or Waydroid will report the
wrong version to Android.

**Sequencing:** libgbinder (item 4) → python3-gbinder (item 11) → waydroid.

## 11. python3-gbinder 1.1.2 → 1.3.1 (18 commits) — *drops a local patch*

- `875604a` `set_stability` for `LocalObject`
- `b39fb9a` **fix methods that accept or return memory buffers**
- `b0c6337` **correct types to match libgbinder headers — fixes 32-bit builds**
  (directly relevant to the armv7 devices)
- `4a95a7d` migrate off deprecated `distutils.core` to setuptools
- `38ed307` always build with cython

### Complexity: **Trivial — verified**
`0001-setup.py-Migrate-away-from-deprecated-distutils.core.patch` **no longer
applies, because upstream did exactly that migration**. Bump the `SRCREV` and
**delete the patch**. Net change: fewer files in the layer.

---

# Tier 3 — Qt/QML stack (medium effort, real payoff)

## 12. libconnman-qt 1.3.3 → 1.4.25 (110 commits)

- `cb7c6c2` **WPA2/WPA3-mixed and WPA3 SAE support** (JB#63890)
- `dd51bb4` WPA3 SAE options: PWE and Check MFP (JB#63948)
- `db1b2fb` WiFi **WPA3 support-level property** (JB#64061)
- `2c0f2b4` **mDNS support** (JB#63356)
- `02c5a41` tethering client list signals and method (JB#60565)
- `5488301`/`0e003e9` `Supported` bool property, services sorted by it (JB#64131)
- `806fc5e`/`2b05fea` **enum-based `NetworkService::serviceState` and
  `NetworkManager::globalState()`** replacing the string-based API (JB#61909) —
  the old string API still exists but is deprecated; check the LuneOS settings
  app / Wi-Fi UI before assuming it is a drop-in
- `d2176c7` retry VPN `getProperties()` on `UnknownObject` (JB#62518)
- `7b5b9fb` fix `SavedServiceModel` update (JB#63459)
- `00ad674` memory-leak fix in useragent
- Pimplification of `UserAgent` / `ConnmanNetworkProxyFactory`, private VPN
  headers no longer installed, D-Bus adaptors moved out of public headers — **an
  ABI break for anything including those headers**

**Note:** 1.4.2x bumps its ConnMan requirement to `1.40+git14`. Check the ConnMan
version in the base layer before bumping.

### Complexity: **Low–Medium** — and it *removes* a patch
`0001-connman_vpn_manager.xml-Fix-build-with-Qt-6.5.patch` no longer applies —
because **upstream fixed it themselves**. Current
`libconnman-qt/connman_vpn_manager.xml` carries *both* the `In1` and the
`Out1` annotation on `ConnectionAdded`, so `qdbusxml2cpp` under Qt 6.5+ is happy.
**Delete the patch.**

The residual risk is the deprecated string-state API and the header
reorganisation, not the build.

## 13. voicecall 0.7.14 → 0.9.0 (45 commits)

- `ecf6ae0` **Cell Broadcast public warning handling** (JB#50240) — emergency
  alerts, a real feature gap
- `889e83d`/`8d819e5`/`93fe514`/`49e8f17` **incoming-call filtering plugin** with
  pattern matching (call blocking)
- `3756e37`/`1f06616`/`6af84b0` CommHistory call-logging plugin
- `19feef7`/`66ccd0e` **`QDBusInterface` removed from the QML API**, replaced with
  async D-Bus calls (JB#22724) — behavioural change for QML consumers
- `827cd44`/`3df1db7` app-requested ringtone file; ringtone playback now must be
  explicitly requested (JB#33820, JB#59106) — **this will silently change
  ringtone behaviour if LuneOS relied on the implicit default**
- `2b1afb6`/`b1c5479` advertise a call only when ready / handle ServicePoint
- `f5dd6dd` D-Bus adapter classes moved out of the library
- `adacb02` migrated to MDConfItem API — **check this dependency exists in LuneOS**
- `dd9305a` then `e45a27f` devel subpackage removed and re-added

### Complexity: **High — and it is not the patch rebase that makes it so**

Re-measured against a 0.9.0 checkout. The Qt6 patch itself rebases easily: a
3-way apply lands 6 of its 8 files cleanly, and the two that fail are both
*good* failures —

- `lib/src/src.pro`: our hunk removes `CONFIG += c++11`; upstream already
  removed it. Drop the hunk.
- `src/basicringtonenotificationprovider.cpp`: upstream **deleted the file**
  (`1372208`, unused). Drop the hunk.

The audio-recorder port (`QAudioInput`→`QAudioSource`,
`QAudioDeviceInfo`→`QAudioDevice`/`QMediaDevices`, `setSampleSize`→
`setSampleFormat`) is still needed and still applies — 0.9.0 has not touched
that code.

**The actual blocker is new upstream dependencies LuneOS does not have.** 0.9.0's
new call-filtering and CommHistory-logging plugins pull in:

| Dependency | Where | In LuneOS? |
|---|---|---|
| `mlite5` | `src/src.pro` (via `adacb02`, MDConfItem migration) | ❌ |
| `commhistory-qt5` | `plugins/filter/lib/lib.pro`, `plugins/filter/tests/tests.pro` | ❌ |
| `qofono-qt5` | `src/src.pro` **and** `plugins/providers/ofono/src/src.pro` | our patch only fixes the first |
| `nemodevicelock`, `libresourceqt5`, `qt5-boostable` | `src/src.pro`, conditional | ❌ (guarded) |

`$$[QT_INSTALL_LIBS]` also now appears in four `.pro`/`.pri` files where the
existing patch rewrites one.

So the upgrade is not "rebase a patch" — it is **decide whether LuneOS wants the
new filter/CommHistory plugins at all**. Either they get disabled in the recipe,
or `mlite` and `commhistory` have to be packaged. That is a product decision, not
a mechanical one, which is why this was left out of the branch.

## 14/15. libqofono 0.123 → 0.130 and libqofonoext 1.0.32 → 1.0.35

**These two are a build-system migration, not a version bump.**

Upstream changes in both repos:
```
abf67a6 Build with cmake
39dbb86 Change spec to build with cmake
b43e54a Drop qmake            ← the blocker
d6a3596 Qt6 build
92eb36d feat: use ECM to generate the pkgconfig file
c0e11c4 fix(cmake): add the -qt6 suffix to the lib name in the pkgconfig file
873aafa [libqofono] Fix the qmltypes make target (JB#64344)
fa53181 Regenerate qmltypes on aarch64 (JB#62707)
```
Plus `2bc4224` Q_SLOTS macro refactor and test-timing fixes.

Both recipes currently `inherit qt6-qmake`. Upstream has **deleted the `.pro`
files**. Bumping means:
1. Rewrite both recipes to `inherit cmake_qt6`.
2. Rework all the `FILES:${PN}` lists — CMake installs to different paths, and
   the pkgconfig file is now generated by ECM with a `-qt6` suffix (the existing
   `do_install:append` sed hacks that patch `qofono-qt5.pc` /`connman-qt5.pc`
   become obsolete or need retargeting).
3. **Add a build dependency on extra-cmake-modules — which means item 32
   (ECM 5.95 → 6.x) is a hard prerequisite**, since KF5-era ECM will not
   generate correct Qt6 output.
4. `libqofonoext` `DEPENDS` on `libqofono`, so they must move together, and
   `voicecall` links `qofono-qt6` (item 13) so it is affected too.

### Complexity: **Medium–High**
Not conceptually hard, but it is a four-recipe coordinated change
(ECM → libqofono → libqofonoext → voicecall) with lots of packaging fallout.
The payoff is real, though: LuneOS stops maintaining a qmake build that upstream
has abandoned, and gets upstream's native Qt6 support instead.

## 16. nemo-qml-plugin-dbus 2.1.28 → 2.1.30+ (26 commits) — *drops a local patch*

- `2dcadbf` **Allow build with Qt 6**
- `62e4055` support for the `a{sv}` D-Bus type
- `3b3e678` perfect forwarding of arguments (JB#61914)
- `eea300f` deprecation warning on the `org.nemomobile.dbus` import (JB#59766) —
  **check whether LuneOS QML still imports the old name; if so you will get
  console noise**
- Documentation and packaging fixes

### Complexity: **Low** — and it *removes* a patch
`0001-Fix-build-with-Qt-6.5.patch` no longer applies because upstream implemented
the same fix. Current `declarativedbusinterface.cpp` has a version-conditional:
```
946:  args[i] = QGenericArgument(QMetaType(arg.metaType()).name(), arg.data());
948:  args[i] = Q_ARG(QVariant, arg);
```
Upstream's approach differs from ours (they kept the 10-arg `invoke`, we switched
to a `switch`), but both are Qt6-correct. **Delete the patch**, bump, rebuild.

## 17. qt6-qpa-hwcomposer-plugin 5.6.2.24 → 5.6.2.27 (13 commits)

- `e73333d` **slot-based buffer cache** — the important one; HWC2 buffer handling
- `a1654af` **fix the usage of present fences in HWC2** — a class of frame-pacing
  and tearing bug
- `2f3bca7` implement `QPlatformScreen::setPowerState` — proper display power
  control instead of a workaround
- `f45067b` separate legacy HWC quirks so they are no longer applied to HWC2
  devices (JB#62779, JB#62780)

### Complexity: **Low**
No local patches. Recipe already carries `PV = "6.3.0+git"` against the Qt5-named
upstream repo, so it is being built as a Qt6 plugin already — the SRCREV bump is
mechanical. Worth on-device verification on a HWC2 Halium device, since display
plumbing is what changed.

## 22. kf5bluezqt-mer 5.24.0 → 5.112.0 (38 commits)

The layer builds a **nine-year-old** snapshot (2017-04-20).

- `430824e` update to KF 5.50; `0bff152` update to **5.112.0**
- `aa295e2` **Bluez4 support removed**
- `ffa30e4` **build with CMake** (was qmake)
- `968995d` **KeyboardDisplay-type pairing agents** (JB#58346) — modern pairing
  flows fail without this
- `a67a8ad` device-model filtering options; `de7cbc6`
  `Manager::monitorObjectManagerInterfaces`
- `f75c956`/`a148a9b` upstream restructured from a git subtree to a patch series

### Complexity: **Highest of all — the recipe cannot be bumped as written**

Both LuneOS patches fail against HEAD:
- `0001-minimal-migration-to-Qt6.patch` → `bluez-qt/src/adapter.h: No such file`
- `0001-Update-D-Bus-xml-files-to-use-Out-for-signal-type-Qt.patch` →
  `bluez-qt/autotests/...: No such file`

Re-measured: this is worse than "patches need rebasing". At current HEAD the
`sailfishos/kf5bluezqt` repo contains **no source code at all** —

```
$ ls          → doc/  rpm/  upstream/
$ find -name '*.cpp' | wc -l   → 0
$ cat .gitmodules
[submodule "upstream"]
        path = upstream
        url = https://github.com/sailfishos-mirror/bluez-qt.git
```

`f75c956 [kf5bluezqt] Replace the git subtree with patches` converted the repo
from carrying a `bluez-qt/` subtree to being a **spec + 13-patch overlay** on a
`bluez-qt-5.112.0.tar.bz2` tarball, with the source itself in a submodule. Our
2017 pin predates that conversion, which is why the recipe still works today.

Bumping therefore means re-architecting the recipe to fetch KDE's bluez-qt
5.112.0 (tarball or submodule), apply Sailfish's 13 patches, **and** redo the
Qt6 port from scratch — and 5.112.0 is still a KF5 release, so the Qt6 port stays
a LuneOS-local burden forever.

**Recommendation:** do not rebuild the KF5 fork. Move to **KF6 `bluez-qt`**,
which is natively Qt6 and needs no local Qt6 patch at all. That is a larger
decision but it ends the maintenance rather than renewing it.

---

# Tier 4 — Backports layer hygiene

The two backport layers exist to put newer components on top of scarthgap.
Their pins have themselves gone stale:

| Recipe | scarthgap base | Our backport | Upstream latest |
|---|---|---|---|
| `mesa` | 24.0.7 | **26.1.0+git** (`e57fca6d`) | 26.2.0 released |
| `meson` | 1.3.1 | **1.5.1** | 1.12.0 |
| `wayland-protocols` | 1.33 | **1.47** | 1.49 |
| `ell` | 0.64 | **0.67** | 0.83 |
| `ofono` | 2.4 | **2.9** | 2.19 |

**mesa (23)** — pinned to a git `SRCREV` on `main` with
`PREFERRED_VERSION_mesa = "26.1.0%"`. 26.1.6 and 26.2.0 have both shipped since.
Moving to a release tag instead of a `main` snapshot would make the pin
reproducible and reviewable. **Complexity: Low**, but every GPU on every machine
is downstream of it, so it needs a broad test pass (PinePhone/Pro, PineTab2, RPi,
qemu, Rockchip).

**meson (24)** — 1.5.1 → 1.12.0 is seven minor releases. Meson upgrades are
usually painless but occasionally tighten deprecation warnings into errors across
*every* meson-built recipe in the build, including all of oe-core. **Complexity:
Low** to change, **medium** to validate; only worth doing if something concrete
needs it.

**wayland-protocols (25)** — 1.47 → 1.49 is purely additive protocol XML.
**Complexity: Trivial.** Safe.

**ell (3)** — 0.67 → 0.83. **Complexity: Low** (plain tarball recipe, no
patches). Required by item 1; do it in that series.

---

# Tier 5 — Node/JS and tooling

## 26. node-gyp-native 10.0.1 → 13.0.1 (155 commits)
Three majors. node-gyp 11+ requires newer Node and changes Python detection.
Only matters to native-module builds (`node-sqlite3`, enyo tooling).
**Complexity: Medium** — coupled to the Node version in the base layer, so bump
them together or not at all.

## 27. node-sqlite3 3.1.13+git → 6.0.1 — **repo has moved**
`SRC_URI` points at `github.com/mapbox/node-sqlite3`, pinned at a 2017-era commit
(`PV = "3.1.13+git"`). Maintenance moved to **TryGhost/node-sqlite3** years ago;
current release is v6.0.1.
**Complexity: High.** 3.x → 6.x spans a rewrite of the build (node-pre-gyp →
prebuild-install), the NAN → N-API migration, and a bundled-SQLite version jump.
The recipe's existing patch/build assumptions will not survive. Weigh against
whether anything in LuneOS still uses it — it is referenced in only two places.

## 28. node-red 3.0.2 → 5.0.4
Two majors. Node-RED 4 raised the minimum Node version and 5 changed the runtime
API. **Complexity: Medium**, but it is a demo/optional component
(`recipes-imported/`) — **low priority**.

## 29. telegraf 1.21.4 → 1.39.3
Pinned to branch `release-1.21` (EOL long ago) with **four local patches** that
are explicitly written for v1.21.4 (`0001-Modify-the-Makefile-for-v1.21.4.patch`,
`0002-Remove-unused-plugins-for-v1.21.4.patch`, plus sdkagent and dashboard
plugin patches).
**Complexity: High** — Go module layout, the vendored-dependency story and the
plugin registry have all changed across 18 minor releases; the plugin-removal
patch in particular would need a full rewrite. Given telegraf is only referenced
once (sdkagent), **the honest recommendation is to decide whether it is still
needed at all** before investing here.

## 30. lxc 4.0.6 → 7.0.0
Used by Waydroid. LXC 5.0 dropped the Lua bindings and reorganised templates;
6.0 and 7.0 continued tightening. The recipe carries **ten local patches**, most
inherited from an old meta-virtualization copy, several of which target the
template scripts that upstream has since restructured.
**Complexity: Medium–High.** Worth checking whether meta-virtualization's current
lxc recipe can simply be used instead of maintaining a local copy.

## 32. extra-cmake-modules 5.95.0 → 6.28.0
Currently in `meta-luneui/recipes-devtools/cmake/`, pinned to a 2022-era KF5
snapshot, and — as far as static inspection shows — **nothing in the layers
DEPENDS on it today**. It becomes load-bearing the moment items 14/15 land,
because upstream libqofono uses ECM to generate its pkgconfig files.
**Complexity: Low** to bump; the KF5→KF6 jump matters only for Qt6 consumers,
which is exactly the use case. **Do this before libqofono.**

---

# Tier 6 — Small, safe, mechanical

| Component | Change | Notes |
|---|---|---|
| **libmce-glib** 1.0.7 → 1.1.0 | 25 commits | New `MceInactivity` object (JB#60550), arch-specific libdir respected (JB#49681), internal symbols hidden, overlinking fixed. No patches. **Trivial.** |
| **ofono-ril-binder-plugin** 1.2.7 → 1.2.8 | 4 commits | Executable bit on the `.so` (JB#51013) + spec cleanup. **Trivial.** |
| **qtscenegraph-adaptation** 0.8.0 → 0.8.1 | 4 commits | Compiler-warning and spec cleanup only. **Trivial.** |
| **audiosystem-passthrough** 1.2.1 → 1.3.1 | 10 commits | `android.media.IAudioFlingerService` support, **wait for the binder device node if not yet present** (fixes a boot race), configurable binder device path. Relevant given the ongoing audiod work. **Low.** |
| **smemstat** 0.01.10 → 0.02.13 | 157 commits, pinned commit is from **2014-06-03** | 12 years of fixes on a diagnostic tool. **Trivial** — `BASE_PV` + `SRCREV`. |
| **powerstat** 0.03.03 → 0.04.06 | 41 commits | Same author, same story. **Trivial.** |
| **sdl2-opengles-test** 1.0.9 → 1.0.11 | 7 commits, pinned 2015 | Test tool only. **Trivial.** |
| **tidy-html5** → 5.8.0 | **1613 commits**; pinned commit dates to **2012-08-21** | The important upstream work is `91f29ea` *"HTML Tidy now parses HTML non-recursively"* and `132fb35` *"The XML Parser and XML Pretty Printer are now non-recursive"* — these convert deep-nesting stack overflows into heap-bounded loops, plus many static-analyzer fixes. Note the `SRC_URI` points at `w3c/tidy-html5`, a mirror; active development is at **htacg/tidy-html5**. The single local patch (automake file placement) will need review against a build system that has changed completely. **Complexity: Medium** purely because of the size of the jump — but 14 years is a long time for an HTML parser fed untrusted input. |

---

# Tier 7 — Dead upstream: remove or re-point, do not upgrade

| Component | Status | Recommendation |
|---|---|---|
| **anbox** + `anbox-data` | `anbox/anbox` **archived 2024-02-13**; deprecated 2023-02-03 in favour of Waydroid, which this layer already ships | ✅ **Removed.** `ashmem.bb`/`binder.bb` are NOT part of anbox — they build the kernel modules Waydroid RRECOMMENDS, and **stay**. |
| **sdbus-cpp** (anbox submodule, `SRCREV_sdbus-cpp`) | pinned 2020-07-22, 244 commits behind v2.3.1 | Goes away with anbox. |
| **snowboy** 1.3.0 | Kitt-AI defunct, repo archived — **but the repo still fetches** | ❌ **Keep.** `libgoogleassistant` DEPENDS on it and `com.webos.service.ai` depends on that. Replacing it means openWakeWord/Porcupine — a feature change, not a source swap. |
| **libbson** 1.9.0+git | archived, folded into **mongo-c-driver** 2.x; standalone repo still fetches | ❌ **Keep.** `settingsservice` DEPENDS on it. mongo-c-driver's libbson 2.x has a different API so it is not a drop-in, and no base layer ships a standalone libbson. |
| **ptmalloc3** | `malloc.de` frozen since ~2012, **tarball still serves (HTTP 200)** | ❌ **Keep.** `wam` RDEPENDS on it. No upgrade path exists. |
| **cordova** 2.3.0 | PhoneGap 2.3.0 (2013) | ❌ **Keep.** `webos_cordova_application.bbclass` does `RDEPENDS:${PN} += "cordova"`, and the calculator and testr apps inherit it. The "zero references" claim was a bad grep — `\bcordova\b` cannot match inside `webos_cordova_application`, and bbclasses were not searched. |
| **python3-jsonschema** 4.17.3 | oe-core scarthgap already ships **4.21.1**; upstream is 4.26.0; **zero references** | This local recipe is a *downgrade* of what the base layer provides. **Remove it** unless something specifically needs to be pinned below 4.21 — and if so, document why in the recipe. |
| **googleapis** (proto snapshot) | pinned proto snapshot, not a released library | ❌ **Keep.** `libgoogleassistant` and `com.webos.service.tts` DEPEND on it. Bump only when a consumer needs newer protos. |
| **leveldb-tl** | `ony/leveldb-tl` dormant, a few commits behind | ❌ **Keep.** `db8` DEPENDS on it. No action needed. |
| **ecryptfs-utils** 111 | 111 (2016) **is** the last upstream release | Already current. The CVE-2016-6224 patch stays. |
| **presage** 0.9.1 | 0.9.1 **is** the latest upstream release | Already current. |
| **gphotofs** 0.5 | 0.5 is the last release | Already current. |
| **dbus-cpp / process-cpp** (lib-cpp) | pinned at `HEAD`; repos dormant | Already current. |
| **nodejs-enyo-dev** 0.5.2 | pinned at `HEAD` of `enyo-dev-dist` | Already current. |

---

# Recommended execution order

**Sprint 1 — security, ~1 day** ✅ *done on branch `luneos-upstream-updates`*
1. `ofono-halium` SRCREV bump (item 2) — CVEs, ~24 commits, one patch to check.
2. `ell` 0.67 → 0.83 then `ofono` 2.9 → 2.19 (items 3, 1) — all four CVEs plus
   the smsutil/ussd/stkutil hardening; the local patch series needs no rebase.

**Sprint 2 — free wins, ~1 day**
3. `sensorfw` (patches verified clean), `python3-gbinder` (deletes a patch),
   `libmce-glib`, `ofono-ril-binder-plugin`, `qtscenegraph-adaptation`,
   `smemstat`, `powerstat`, `sdl2-opengles-test`, `wayland-protocols`.
4. Delete the dead recipes: anbox/ashmem/binder/anbox-data, cordova,
   python3-jsonschema, and (if agreed) snowboy.

**Sprint 3 — the binder/telephony stack, ~3–5 days incl. device testing**
5. `libglibutil` → `libgbinder` → `libgbinder-radio` → `ofono-binder-plugin`
   as one atomic series (items 7, 4, 5, 6), then `bluebinder` (item 8).
   Test on a Halium device: calls, SMS, data, 5G indication, VoLTE, BT toggle.

**Sprint 4 — Qt/QML, ~3–5 days**
6. `extra-cmake-modules` → KF6 (item 32), then `libqofono` + `libqofonoext`
   qmake→CMake (items 14, 15), then `voicecall` (item 13).
7. `libconnman-qt` (item 12, deletes a patch), `nemo-qml-plugin-dbus`
   (item 16, deletes a patch), `qt6-qpa-hwcomposer-plugin` (item 17).

**Backlog — decide before investing**
- `waydroid` fork rebase to 1.6.3 (item 10) — needed for Android 15/16 images.
- `kf5bluezqt-mer` (item 22) — evaluate KF6 `bluez-qt` instead of rebasing.
- `tidy-html5` (item 31) — re-point to htacg and jump to 5.8.0.
- `telegraf`, `node-sqlite3`, `node-red`, `lxc` — establish whether each is still
  wanted before paying the upgrade cost.
- `ofono` 2.12 → 2.19 with the QMI voicecall patch series rebased.

---

## Cross-cutting observations

**Four local patches can be deleted outright** because upstream implemented the
same fix — `libconnman-qt` Qt6.5 XML, `nemo-qml-plugin-dbus` Qt6.5 `Q_ARG`,
`bluebinder` CC, `python3-gbinder` distutils. That is maintenance burden removed,
not added, and it is the cheapest argument for doing these bumps.

**Three recipes have a `PV` that does not match their `SRCREV`** — `libglibutil`
(says 1.0.61, is 1.0.75), `libgrilio` (says 1.0.39, is actually **1.0.44, i.e.
already current**), `libgbinder` (says 1.1.35, pinned commit is not on any tag).
Worth fixing regardless, since these strings drive package versions and any
future automated upgrade tooling.

**The mer-hybris/sailfishos cluster moves as one unit.** libglibutil, libgbinder,
libgbinder-radio, ofono-binder-plugin, bluebinder and sensorfw are developed
against each other's APIs by the same maintainers. Bumping them piecemeal is
strictly harder than bumping them together — and the whole cluster's recent work
is dominated by one theme: **AIDL**, i.e. support for Android 13/14/15/16-based
Halium ports. If newer Halium bases are on the roadmap, this cluster is the
prerequisite for all of it.
