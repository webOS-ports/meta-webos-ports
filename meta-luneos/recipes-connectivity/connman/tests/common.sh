# Sourced by the check scripts. Bash arrays, so the quoted defines reach
# the compiler intact.

# What connman's Makefile passes to every object (AM_CFLAGS), with the
# paths the recipe configures.
CONNMAN_DEFS=(
	-DHAVE_CONFIG_H -DCONNMAN_PLUGIN_BUILTIN -D_GNU_SOURCE
	'-DSTATEDIR="/run/connman"' '-DVPN_STATEDIR="/run/connman-vpn"'
	'-DPLUGINDIR="/usr/lib/connman/plugins"'
	'-DSCRIPTDIR="/usr/lib/connman/scripts"'
	'-DSTORAGEDIR="/var/lib/connman"' '-DVPN_STORAGEDIR="/var/lib/connman-vpn"'
	'-DCONFIGDIR="/etc/connman"' '-DRUNSTATEDIR="/run"'
)

# connman_includes TREE BUILD: the source tree first, then the configured
# build (config.h, the generated include/connman).
connman_includes() {
	echo "-I$1 -I$1/gdbus -I$2 -I$2/include"
}

# changed_c_files PATCHDIR: the .c files our patches touch
changed_c_files() {
	sed -n 's|^+++ b/\(.*\.c\)$|\1|p' "$1"/*.patch | sort -u
}
