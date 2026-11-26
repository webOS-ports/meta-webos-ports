#!/bin/bash
#
# Compile the C files our connman patches touch, and connman's unit tests,
# with GCC and Clang for armv7, aarch64 and x86-64: six builds, warnings
# printed with the build they came from.
#
# Usage: cross-check.sh [-s SRCDIR] [-w WORKDIR] [-a]
#
#   -a   every .c file in src/ gdhcp/ gsupplicant/ plugins/ unit/
#
# GCC is the image's cross compiler from tmp/sysroots-components; Clang is
# the host's, pointed at the same sysroots. A sysroot is put together in
# WORKDIR from tmp/sysroots-components, with the dbus and iptables headers
# taken from the -dev packages in tmp/deploy/ipk where a component has
# none (the armv7 ones, typically). Files are compiled to assembly only, so
# no assembler or linker is involved: this is about warnings, not about
# building a binary.
#
# Uses the patched, configured tree host-check.sh leaves (it runs
# "host-check.sh gcc" when there is none).
#
# Exit status: 0 when no build printed a warning or an error.

set -u
export LC_ALL=C

HERE=$(cd "$(dirname "$0")" && pwd)
. "$HERE/common.sh"
BUILDDIR=${BUILDDIR:-$(cd "$HERE/../../../../.." && pwd)}
WORK=${HOME}/.cache/luneos-connman-tests
SRC=
ALL=

while getopts "s:w:ah" opt; do
	case $opt in
	s) SRC=$OPTARG ;;
	w) WORK=$OPTARG ;;
	a) ALL=1 ;;
	*) sed -n '2,/^$/s/^# \{0,1\}//p' "$0"; exit 2 ;;
	esac
done

die() { echo "cross-check: $*" >&2; exit 2; }

"$HERE/host-check.sh" ${SRC:+-s "$SRC"} -w "$WORK" gcc >/dev/null \
	|| die "host-check.sh gcc failed; run it on its own to see why"

S=$WORK/src
B=$WORK/build-gcc
COMP=$BUILDDIR/tmp/sysroots-components
IPK=$BUILDDIR/tmp/deploy/ipk
[ -d "$COMP/x86_64" ] || die "no $COMP; build something with bitbake first"

# sysroot NAME COMPONENT-ARCH IPK-ARCH...
sysroot() {
	local name=$1 comp=$2 dir=$WORK/sysroot-$1 c pkg a
	shift 2

	if [ ! -f "$dir/.done" ]; then
		rm -rf "$dir" && mkdir -p "$dir"
		for c in glibc linux-libc-headers glib-2.0 dbus iptables libgcc gcc-runtime; do
			[ -d "$COMP/$comp/$c" ] && cp -a "$COMP/$comp/$c/." "$dir/"
		done
		for pkg in dbus-dev iptables-dev; do
			case $pkg in
			dbus-dev) [ -f "$dir/usr/include/dbus-1.0/dbus/dbus.h" ] && continue ;;
			iptables-dev) [ -f "$dir/usr/include/xtables.h" ] && continue ;;
			esac
			for a in "$@"; do
				f=$(ls -t "$IPK/$a/${pkg}_"*.ipk 2>/dev/null | head -n 1)
				[ -n "$f" ] || continue
				(cd "$dir" && ar p "$f" data.tar.zst 2>/dev/null | tar --zstd -x 2>/dev/null \
					|| ar p "$f" data.tar.xz | tar -xJ 2>/dev/null \
					|| ar p "$f" data.tar.gz | tar -xz)
				break
			done
		done
		# xtables.h is the same on every architecture
		[ -f "$dir/usr/include/xtables.h" ] || cp "$WORK/xtables/include/"xtables*.h "$dir/usr/include/"
		touch "$dir/.done"
	fi
	for f in usr/include/dbus-1.0/dbus/dbus.h usr/lib/dbus-1.0/include/dbus/dbus-arch-deps.h \
		 usr/lib/glib-2.0/include/glibconfig.h usr/include/xtables.h; do
		[ -f "$dir/$f" ] || die "sysroot $name has no $f"
	done
	echo "$dir"
}

ARM_SYS=$(sysroot armv7 cortexa8t2hf-neon-halium cortexa8t2hf-neon-halium cortexa8t2hf-neon) || exit 2
A64_SYS=$(sysroot aarch64 aarch64-halium aarch64-halium aarch64) || exit 2
X86_SYS=$(sysroot x86_64 corei7-64 corei7-64) || exit 2

XC=$COMP/x86_64
GCC_ARM=$XC/gcc-cross-arm/usr/bin/arm-webos-linux-gnueabi/arm-webos-linux-gnueabi-gcc
GCC_A64=$XC/gcc-cross-aarch64/usr/bin/aarch64-webos-linux/aarch64-webos-linux-gcc
GCC_X86=$XC/gcc-cross-x86_64/usr/bin/x86_64-webos-linux/x86_64-webos-linux-gcc
for g in "$GCC_ARM" "$GCC_A64" "$GCC_X86"; do
	[ -x "$g" ] || die "no cross compiler $g"
done
command -v clang >/dev/null || die "no clang on the host"

# What the recipe builds with (OE's default TARGET_CFLAGS), plus warnings.
CFLAGS=(-O2 -D_FORTIFY_SOURCE=2 -fstack-protector-strong -Wformat
	-Wformat-security -Werror=format-security -Wall -Wextra
	-Wno-unused-parameter -Wno-sign-compare -Wno-missing-field-initializers)
# 32-bit arm: the image is built with a 64-bit time_t
ARM_FLAGS="-march=armv7-a -mthumb -mfpu=neon -mfloat-abi=hard -D_TIME_BITS=64 -D_FILE_OFFSET_BITS=64"

if [ -n "$ALL" ]; then
	FILES=$(cd "$S" && ls src/*.c gdhcp/*.c gsupplicant/*.c plugins/*.c unit/*.c)
else
	FILES=$( (changed_c_files "$HERE/../connman"; cd "$S" && ls unit/*.c) | sort -u)
fi

warnings=0
skipped=
for f in $FILES; do
	[ -f "$S/$f" ] || continue
	for v in "gcc-armv7|$GCC_ARM|$ARM_SYS|$ARM_FLAGS" \
		 "gcc-aarch64|$GCC_A64|$A64_SYS|" \
		 "gcc-x86_64|$GCC_X86|$X86_SYS|" \
		 "clang-armv7|clang --target=armv7a-linux-gnueabihf|$ARM_SYS|$ARM_FLAGS" \
		 "clang-aarch64|clang --target=aarch64-linux-gnu|$A64_SYS|" \
		 "clang-x86_64|clang --target=x86_64-linux-gnu|$X86_SYS|"; do
		IFS='|' read -r label cc sys aflags <<<"$v"
		read -ra CC <<<"$cc"
		read -ra AF <<<"$aflags"
		read -ra INC <<<"$(connman_includes "$S" "$B")"
		out=$("${CC[@]}" --sysroot="$sys" "${AF[@]}" "${CFLAGS[@]}" \
			"${CONNMAN_DEFS[@]}" "${INC[@]}" \
			-I"$sys/usr/include/glib-2.0" -I"$sys/usr/lib/glib-2.0/include" \
			-I"$sys/usr/include/dbus-1.0" -I"$sys/usr/lib/dbus-1.0/include" \
			-S "$S/$f" -o /dev/null 2>&1)
		if echo "$out" | grep -q "fatal error:.*No such file"; then
			skipped="$skipped $f($label)"
			continue
		fi
		echo "$out" | grep -E "warning:|error:" | sed "s|$S/||; s|^|$label: |"
		n=$(echo "$out" | grep -cE "warning:|error:")
		warnings=$((warnings + n))
	done
done

[ -n "$skipped" ] && echo "skipped (a header the sysroot lacks):$skipped"
echo "$(echo $FILES | wc -w) files, 6 builds each, $warnings warnings/errors"
[ "$warnings" -eq 0 ]
