#!/bin/bash
#
# Build connman natively on the build host and run its unit tests
# ("make check") in several variants:
#
#   gcc, clang          plain builds, warnings shown
#   asan-gcc, asan-clang AddressSanitizer + UndefinedBehaviorSanitizer
#   tsan                ThreadSanitizer (gcc)
#   valgrind            every unit test (-O0 gcc build) under memcheck
#
# Usage: host-check.sh [-s SRCDIR] [-w WORKDIR] [variant...]
#
#   SRCDIR   patched connman sources; default: the newest
#            $BUILDDIR/tmp/work/*/connman/*/sources/connman-* (run
#            "bitbake connman -c patch" first). Copied, never written to.
#   WORKDIR  where the builds go; default $HOME/.cache/luneos-connman-tests
#
# The unit tests themselves live in connman (unit/, our patches 0021-0023
# add to upstream's). This only builds and runs them, so they cannot rot
# here: bitbake compiles the same tests for the target on every build.
#
# libxtables is built from the iptables tarball in DL_DIR, the version the
# image ships, so the iptables tests run against the same library. The
# host needs glib-2.0 and dbus-1 development files; automake/autoconf are
# taken from the host or, failing that, from connman's recipe-sysroot-native.
#
# Exit status: 0 when every variant run passed.

set -u
export LC_ALL=C

HERE=$(cd "$(dirname "$0")" && pwd)
BUILDDIR=${BUILDDIR:-$(cd "$HERE/../../../../.." && pwd)}
WORK=${HOME}/.cache/luneos-connman-tests
SRC=

while getopts "s:w:h" opt; do
	case $opt in
	s) SRC=$OPTARG ;;
	w) WORK=$OPTARG ;;
	*) sed -n '2,/^$/s/^# \{0,1\}//p' "$0"; exit 2 ;;
	esac
done
shift $((OPTIND - 1))
VARIANTS=${*:-gcc clang asan-gcc asan-clang tsan valgrind}

die() { echo "host-check: $*" >&2; exit 2; }

if [ -z "$SRC" ]; then
	SRC=$(ls -dt "$BUILDDIR"/tmp/work/*/connman/*/sources/connman-* \
							2>/dev/null | head -n 1)
	[ -n "$SRC" ] || die "no patched connman sources under $BUILDDIR/tmp/work; run 'bitbake connman -c patch' or pass -s"
fi
[ -f "$SRC/configure.ac" ] || die "$SRC is not a connman source tree"

for p in glib-2.0 dbus-1; do
	pkg-config --exists $p || die "host is missing $p development files"
done

if ! command -v automake >/dev/null; then
	NATIVE=$(ls -d "$BUILDDIR"/tmp/work/*/connman/*/recipe-sysroot-native/usr/bin \
							2>/dev/null | head -n 1)
	[ -x "$NATIVE/automake" ] || die "no automake on the host or in connman's recipe-sysroot-native"
	export PATH=$PATH:$NATIVE
fi

mkdir -p "$WORK" || die "cannot create $WORK"
echo "sources: $SRC"
echo "work:    $WORK"

# --- libxtables from DL_DIR -------------------------------------------------

DL_DIR=${DL_DIR:-$BUILDDIR/downloads}
XT=$WORK/xtables
if [ ! -f "$XT/lib/libxtables.so" ]; then
	TARBALL=$(ls -t "$DL_DIR"/iptables-*.tar.xz 2>/dev/null | head -n 1)
	[ -n "$TARBALL" ] || die "no iptables tarball in $DL_DIR"
	echo "building libxtables from $(basename "$TARBALL")"
	rm -rf "$WORK/iptables" && mkdir -p "$WORK/iptables"
	tar -xJf "$TARBALL" -C "$WORK/iptables" --strip-components=1 || die "cannot unpack $TARBALL"
	(cd "$WORK/iptables" && ./configure -q --prefix="$XT" --disable-nftables \
		&& make -s -j"$(nproc)" -C libxtables \
		&& make -s -C libxtables install) >"$WORK/iptables.log" 2>&1 \
		|| die "libxtables build failed, see $WORK/iptables.log"
	mkdir -p "$XT/include"
	cp "$WORK/iptables/include/xtables.h" "$WORK/iptables/include/xtables-version.h" "$XT/include/"
fi
mkdir -p "$WORK/pkgconfig"
cat >"$WORK/pkgconfig/xtables.pc" <<EOF
prefix=$XT
Name: xtables
Description: libxtables built by host-check.sh
Version: 1.8.13
Cflags: -I\${prefix}/include
Libs: -L\${prefix}/lib -lxtables -Wl,-rpath,\${prefix}/lib
EOF
export PKG_CONFIG_PATH=$WORK/pkgconfig${PKG_CONFIG_PATH:+:$PKG_CONFIG_PATH}

# --- the sources, regenerated once ------------------------------------------

S=$WORK/src
if [ ! -f "$S/.stamp" ] || [ -n "$(find "$SRC" -newer "$S/.stamp" -type f -print -quit)" ]; then
	echo "copying and regenerating sources"
	rm -rf "$S" && mkdir -p "$S"
	cp -a "$SRC/." "$S/" || die "cannot copy $SRC"
	(cd "$S" && autoreconf -fi) >"$WORK/autoreconf.log" 2>&1 \
		|| die "autoreconf failed, see $WORK/autoreconf.log"
	touch "$S/.stamp"
fi

# Only what the unit tests and connmand need on a build host.
CONF="--disable-wispr --disable-client --disable-ofono --disable-bluetooth
	--disable-pacrunner --disable-neard --disable-wireguard --disable-wifi
	--disable-polkit"

SAN_CFLAGS="-O1 -g -fno-omit-frame-pointer"
export ASAN_OPTIONS=detect_leaks=1:abort_on_error=1
export UBSAN_OPTIONS=print_stacktrace=1:halt_on_error=1
export TSAN_OPTIONS=halt_on_error=1

# build VARIANT CC CFLAGS
build() {
	local b=$WORK/build-$1

	if [ ! -f "$b/Makefile" ] || [ "$S/.stamp" -nt "$b/Makefile" ]; then
		rm -rf "$b" && mkdir -p "$b"
		(cd "$b" && "$S/configure" -q CC="$2" CFLAGS="$3" LDFLAGS="$4" $CONF) \
			>"$b.configure.log" 2>&1 || { echo "configure failed, see $b.configure.log"; return 1; }
	fi
	make -s -C "$b" -j"$(nproc)" >"$b.build.log" 2>&1 \
		|| { echo "build failed, see $b.build.log"; return 1; }
	grep -E "warning:" "$b.build.log" | sort -u | sed "s|^|  $1: |"
	return 0
}

# check VARIANT
check() {
	local b=$WORK/build-$1

	if make -s -C "$b" -j"$(nproc)" check >"$b.check.log" 2>&1; then
		echo "  $1: $(grep -c '^PASS:' "$b"/unit/*.log | awk -F: '{n+=$2} END {print n}') tests passed"
		return 0
	fi
	echo "  $1: FAILED, see $b.check.log and $b/test-suite.log"
	grep -hE "^(FAIL|ERROR):|ERROR:|runtime error|Sanitizer|CRITICAL" \
		"$b.check.log" "$b"/unit/*.log 2>/dev/null | sort -u | head -n 20 | sed 's/^/    /'
	return 1
}

# valgrind: every unit test, from an -O0 gcc build
run_valgrind() {
	local b=$WORK/build-valgrind t ok=0

	command -v valgrind >/dev/null || { echo "  valgrind: not installed"; return 1; }
	build valgrind gcc "-O0 -g" "" >/dev/null || { echo "  valgrind: build failed, see $WORK/build-valgrind.build.log"; return 1; }
	make -s -C "$b" -j"$(nproc)" check >/dev/null 2>&1
	for t in "$b"/unit/test-*; do
		[ -x "$t" ] && [ -f "$t" ] || continue
		case $t in *.log|*.trs|*.o) continue ;; esac
		# from the build dir, so a vgcore of a crashing test lands there
		if (cd "$b" && valgrind -q --error-exitcode=99 --leak-check=full \
			--errors-for-leak-kinds=definite --child-silent-after-fork=yes \
			--suppressions="$HERE/valgrind.supp" \
			"$t") >"$t.valgrind.log" 2>&1; then
			echo "  valgrind: $(basename "$t") clean"
		else
			echo "  valgrind: $(basename "$t") FAILED, see $t.valgrind.log"
			ok=1
		fi
	done
	return $ok
}

status=0
for v in $VARIANTS; do
	echo "== $v"
	case $v in
	gcc)        build gcc gcc "-O2 -g -Wall -Wextra -Wno-unused-parameter -Wno-sign-compare -Wno-missing-field-initializers" "" && check gcc ;;
	clang)      build clang clang "-O2 -g -Wall -Wextra -Wno-unused-parameter -Wno-sign-compare -Wno-missing-field-initializers" "" && check clang ;;
	asan-gcc)   build asan-gcc gcc "$SAN_CFLAGS -fsanitize=address,undefined" "-fsanitize=address,undefined" && check asan-gcc ;;
	asan-clang) build asan-clang clang "$SAN_CFLAGS -fsanitize=address,undefined" "-fsanitize=address,undefined" && check asan-clang ;;
	tsan)       build tsan gcc "$SAN_CFLAGS -fsanitize=thread" "-fsanitize=thread" && check tsan ;;
	valgrind)   run_valgrind ;;
	*)          echo "unknown variant $v"; false ;;
	esac || status=1
done

exit $status
