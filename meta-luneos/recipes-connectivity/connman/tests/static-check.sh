#!/bin/bash
#
# Static analysis of the C files our connman patches touch, reporting only
# what the patches add.
#
# Usage: static-check.sh [-s SRCDIR] [-w WORKDIR] [-a] [tool...]
#
#   tools: fanalyzer clang-analyze clang-tidy cppcheck flawfinder sparse
#          coccinelle (default: all that are installed)
#   -a     every .c file in src/ gdhcp/ gsupplicant/ plugins/, not only
#          the ones the patches touch
#
# Every tool runs twice, on the patched sources and on pristine connman
# from DL_DIR. Findings are compared without line numbers; the ones only
# the patched tree has are printed as NEW (with their location), the rest
# are counted as pre-existing. Full output stays in WORKDIR/static/.
#
# Needs a configured build from host-check.sh (it runs "host-check.sh gcc"
# itself when there is none) for config.h and the generated include/.
# Files the host cannot compile (wispr.c wants gnutls) are listed as
# skipped, never silently dropped.
#
# Exit status: 0 when nothing new was found.

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
shift $((OPTIND - 1))
TOOLS=${*:-fanalyzer clang-analyze clang-tidy cppcheck flawfinder sparse coccinelle}

die() { echo "static-check: $*" >&2; exit 2; }

"$HERE/host-check.sh" ${SRC:+-s "$SRC"} -w "$WORK" gcc >/dev/null \
	|| die "host-check.sh gcc failed; run it on its own to see why"

S=$WORK/src
B=$WORK/build-gcc
OUT=$WORK/static
export PKG_CONFIG_PATH=$WORK/pkgconfig${PKG_CONFIG_PATH:+:$PKG_CONFIG_PATH}
mkdir -p "$OUT"

# Pristine connman from DL_DIR
DL_DIR=${DL_DIR:-$BUILDDIR/downloads}
P=$WORK/pristine
if [ ! -f "$P/configure.ac" ]; then
	TARBALL=$(ls -t "$DL_DIR"/connman-*.tar.xz 2>/dev/null | head -n 1)
	[ -n "$TARBALL" ] || die "no connman tarball in $DL_DIR"
	mkdir -p "$P" && tar -xJf "$TARBALL" -C "$P" --strip-components=1 \
		|| die "cannot unpack $TARBALL"
fi

if [ -n "$ALL" ]; then
	FILES=$(cd "$S" && ls src/*.c gdhcp/*.c gsupplicant/*.c plugins/*.c)
else
	FILES=$(changed_c_files "$HERE/../connman")
fi

read -ra PKG_FLAGS <<<"$(pkg-config --cflags glib-2.0 dbus-1 xtables)"

# set_flags TREE: FL becomes the full flag list for files of TREE
set_flags() {
	read -ra FL <<<"$(connman_includes "$1" "$B")"
	FL+=("${CONNMAN_DEFS[@]}" "${PKG_FLAGS[@]}")
}

# run_tool TOOL TREE FILE -> findings on stdout, one per line
run_tool() {
	local tool=$1 t=$2 f=$3 o c
	set_flags "$t"
	case $tool in
	fanalyzer)
		for o in -O0 -O2; do
			gcc "${FL[@]}" -fanalyzer $o -c "$t/$f" -o /dev/null 2>&1 \
				| grep -E "warning:.*\[-Wanalyzer" | sed "s/^/$o /"
		done ;;
	clang-analyze)
		clang "${FL[@]}" --analyze -Xanalyzer -analyzer-output=text \
			-Xanalyzer -analyzer-checker=alpha.unix,alpha.security,alpha.core \
			-w -o /dev/null "$t/$f" 2>&1 | grep -E "warning:" ;;
	clang-tidy)
		clang-tidy -quiet "-checks=-*,bugprone-*,cert-*,clang-analyzer-*,-bugprone-easily-swappable-parameters,-bugprone-reserved-identifier,-cert-dcl37-c,-cert-dcl51-cpp,-bugprone-assignment-in-if-condition,-cert-err33-c,-clang-analyzer-security.insecureAPI.DeprecatedOrUnsafeBufferHandling" \
			"$t/$f" -- "${FL[@]}" 2>/dev/null | grep -E "^/.*warning:" ;;
	cppcheck)
		cppcheck -q --enable=all --check-level=exhaustive --inconclusive \
			--suppress=missingIncludeSystem --suppress=missingInclude \
			--suppress=unusedFunction --suppress=staticFunction \
			--suppress=checkersReport -D__GNUC__ \
			$(printf '%s\n' "${FL[@]}" | grep -E '^-I') \
			--template='{file}:{line}: {severity}: {message} [{id}]' \
			"$t/$f" 2>&1 | grep -E ": (error|warning|style|performance|portability):" ;;
	flawfinder)
		flawfinder -Q -D -m 1 -S "$t/$f" 2>/dev/null | grep -E "^\S+:[0-9]+:" ;;
	sparse)
		sparse -Wno-decl -Wno-non-pointer-null "${FL[@]}" "$t/$f" 2>&1 \
			| grep -E "(warning|error):" | grep -vE "^/usr/" ;;
	coccinelle)
		for c in "$HERE"/coccinelle/*.cocci; do
			[ -f "$c" ] || continue
			spatch --very-quiet --sp-file "$c" "$t/$f" 2>/dev/null \
				| grep -E "^[-+][^-+]" | sed "s|^|$f: $(basename "$c"): |"
		done ;;
	esac
}

# Strip the tree prefix and line/column numbers, so a finding that only
# moved still matches.
norm() { sed -E "s|$S/||g; s|$P/||g; s/:[0-9]+(:[0-9]+)?:/:/g; s/\[[0-9]+\]//g" | sort -u; }

have() {
	case $1 in
	fanalyzer) command -v gcc ;; clang-analyze) command -v clang ;;
	coccinelle) command -v spatch ;; *) command -v "$1" ;;
	esac >/dev/null
}

skipped=
CHECK=
for f in $FILES; do
	[ -f "$S/$f" ] || continue
	set_flags "$S"
	if ! gcc "${FL[@]}" -fsyntax-only -w "$S/$f" 2>"$OUT/compile.err"; then
		skipped="$skipped $f($(grep -m1 -o 'fatal error: [^ ]*' "$OUT/compile.err" | cut -d' ' -f3))"
		continue
	fi
	CHECK="$CHECK $f"
done
[ -n "$CHECK" ] || die "no file could be compiled; see $OUT/compile.err"

total_new=0
for tool in $TOOLS; do
	if ! have "$tool"; then
		echo "== $tool: not installed, skipped"
		continue
	fi
	: >"$OUT/$tool.patched"; : >"$OUT/$tool.pristine"
	for f in $CHECK; do
		run_tool "$tool" "$S" "$f" >>"$OUT/$tool.patched"
		[ -f "$P/$f" ] && run_tool "$tool" "$P" "$f" >>"$OUT/$tool.pristine"
	done
	norm <"$OUT/$tool.patched" >"$OUT/$tool.patched.n"
	norm <"$OUT/$tool.pristine" >"$OUT/$tool.pristine.n"
	comm -23 "$OUT/$tool.patched.n" "$OUT/$tool.pristine.n" >"$OUT/$tool.new.n"
	# the new findings again, with their line numbers
	: >"$OUT/$tool.new"
	while IFS= read -r line; do
		while IFS= read -r full; do
			[ "$(echo "$full" | norm)" = "$line" ] && echo "$full" >>"$OUT/$tool.new"
		done <"$OUT/$tool.patched"
	done <"$OUT/$tool.new.n"
	n=$(wc -l <"$OUT/$tool.new.n")
	old=$(comm -12 "$OUT/$tool.patched.n" "$OUT/$tool.pristine.n" | wc -l)
	echo "== $tool: $n new, $old pre-existing"
	sort -u "$OUT/$tool.new" | sed "s|$S/||; s/^/   NEW /"
	total_new=$((total_new + n))
done

[ -n "$skipped" ] && echo "skipped (does not compile on this host):$skipped"
echo "files checked:$CHECK"
echo "full output: $OUT"

[ "$total_new" -eq 0 ]
