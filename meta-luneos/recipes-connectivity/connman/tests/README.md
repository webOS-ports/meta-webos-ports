# connman tests and hardening

connman is upstream code with our patches on top, so the tests live in two
places:

- **Unit tests** are in connman itself, in `unit/`, added by patches
  0021-0023 next to upstream's `test-ippool` and `test-iptables`. Each one
  covers one of our fixes and fails without it. They are `noinst_PROGRAMS`,
  so every bitbake build compiles and links them for the target.
- **The scripts here** run them and the other checks. They are not part of
  the recipe; bitbake ignores this folder.

All scripts take `-s SRCDIR` (patched connman sources) and `-w WORKDIR`
(default `~/.cache/luneos-connman-tests`). Without `-s` they use the newest
`tmp/work/*/connman/*/sources/connman-*`, so run
`bitbake connman -c patch` first. The sources are copied, never written to.

## host-check.sh: unit tests, sanitizers, valgrind

    tests/host-check.sh [-s SRCDIR] [gcc clang asan-gcc asan-clang tsan valgrind]

Builds connman natively with the host's compilers and runs `make check` in
separate out-of-tree builds:

| variant      | what                                               |
|--------------|----------------------------------------------------|
| `gcc`, `clang` | plain builds; warnings are printed               |
| `asan-gcc`, `asan-clang` | AddressSanitizer + UndefinedBehaviorSanitizer |
| `tsan`       | ThreadSanitizer                                    |
| `valgrind`   | every unit test from an -O0 build under memcheck   |

libxtables is built from the iptables tarball in `DL_DIR`, the version the
image ships. The host needs glib-2.0 and dbus-1 development files;
automake comes from the host or from connman's `recipe-sysroot-native`.
`valgrind.supp` holds the suppressions, each with the reason it is not a
connman defect.

## static-check.sh: analyzers, new findings only

    tests/static-check.sh [-s SRCDIR] [-a] [fanalyzer clang-analyze clang-tidy cppcheck flawfinder sparse coccinelle]

Runs each analyzer on the `.c` files our patches touch (`-a`: every file),
once on the patched tree and once on pristine connman from `DL_DIR`, and
prints only the findings the patches add: all of them, 0001 onwards, so
code an older patch brought in (wifi-mac.c, for one) shows up as new too.
Many hits are false positives by design (flawfinder flags every `memcpy`,
cppcheck wants const on stubs whose prototype is fixed); triage them, do
not chase a zero. Line numbers are ignored when
comparing, so code that only moved does not show up. Full output is kept in
`WORKDIR/static/`. Files the host cannot compile (wispr.c wants gnutls) are
listed as skipped. Coccinelle runs the semantic patches in `coccinelle/`
when that folder has any.

## cross-check.sh: six builds

    tests/cross-check.sh [-s SRCDIR] [-a]

Compiles the changed files and the unit tests with GCC (the image's cross
compilers from `tmp/sysroots-components`) and the host's Clang, for armv7,
aarch64 and x86-64, at the recipe's optimisation and hardening flags plus
`-Wall -Wextra`. armv7 builds with a 64-bit `time_t`, as the image does.
Sysroots are put together from `tmp/sysroots-components`, with the dbus
and iptables headers taken from the `-dev` packages in `tmp/deploy/ipk`
where a component lacks them.

## device-check.sh: on a phone

    tests/device-check.sh -d SERIAL [-P ADBPORT] [-c CLIENT_SERIAL] [-n CYCLES] [tether client restart]

Over adb, through connmanctl only (nothing is tapped on screen):

- `tether`: Wi-Fi tethering on and off. The AP must come and go, no
  interface may stay in AP mode, connmand must not restart or grow in RSS
  or file descriptors, and the journal must show no GLib critical or abort.
  With `WiFiTetheringInterfaces` the device's own Wi-Fi stays connected.
- `client`: a second device joins the hotspot, must get a lease (not
  link-local) and reach the internet through it, then goes back to the
  network it was on.
- `restart`: connmand is restarted while on Wi-Fi, which must be back
  within 30 s.

## What the tests cover

| fix | unit test | device test |
|-----|-----------|-------------|
| 0016 iptables: libxtables freeing the static option table | `test-iptables /iptables/test_merge_options_frees` | `tether` |
| 0017 gsupplicant: plain WPA2-PSK on an AP | none (static helper behind D-Bus objects) | `client` with a MediaTek client |
| 0018 gdhcp: unicast OFFER/ACK | `test-dhcp-server` | `client` with a MediaTek client |
| 0019 wifi: dedicated AP interface | none (needs the driver) | `tether` |
| 0020 tethering: client table walk | `test-tethering /tethering/disable-drops-clients` | `tether` (journal check) |
| PreferredTechnologies (connman-conf) | none | `restart` |

Every unit test above was checked against the code before its fix and
fails there.
