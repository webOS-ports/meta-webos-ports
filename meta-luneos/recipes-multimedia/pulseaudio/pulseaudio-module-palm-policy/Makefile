# Standalone build for module-palm-policy, extracted from LG's PulseAudio fork.
#
# Upstream builds this inside the fork's meson tree, where it is split into a
# shared libpalm-policy-util consumed by several LG-only modules. LuneOS needs
# only module-palm-policy itself, so the util translation unit is linked
# straight into the module and no extra library is installed.

# Derived from the libpulse we build against, never hardcoded: PulseAudio checks
# a module's PA_MODULE_VERSION against its own and refuses a mismatch, so a
# PulseAudio upgrade must not be able to leave a stale version compiled in.
PA_MAJORMINOR ?= $(shell $(PKG_CONFIG) --modversion libpulse | cut -d. -f1,2)
MODULE_DIR    ?= $(libdir)/pulseaudio/modules

SRC := module-palm-policy.c module-palm-policy-util.c
OBJ := $(SRC:.c=.o)

# pulsecore.pc comes from pulseaudio-pulsecore-private-headers; it carries the
# -D__INCLUDED_FROM_PULSE_AUDIO and pulsecore/pulsecommon link flags that any
# out-of-tree PulseAudio module needs.
PKGS      := pulsecore libpulse alsa
PKG_CFLAGS := $(shell $(PKG_CONFIG) --cflags $(PKGS))
PKG_LIBS   := $(shell $(PKG_CONFIG) --libs $(PKGS))

# pulsecore headers hard-#error unless config.h was included first, and key
# struct layout off it, so force-include the very config.h libpulsecore was
# built with (shipped by meta-luneos' pulseaudio bbappend). Deliberately without
# -DHAVE_CONFIG_H: the sources' own guarded "#include <config.h>" must stay
# inactive, since there is no config.h on the plain include path.
ALL_CFLAGS := -std=gnu11 -fPIC -DPIC -D_GNU_SOURCE \
              -include pulsecore/config.h \
              -DPULSEAUDIO_VERSION=\"$(PA_MAJORMINOR)\" \
              $(PKG_CFLAGS) $(CFLAGS)

# PulseAudio dlopen()s modules with RTLD_LOCAL and resolves pa_* against the
# already-loaded daemon, so undefined symbols here are expected and correct.
ALL_LDFLAGS := -shared -fPIC -Wl,-soname,module-palm-policy.so $(LDFLAGS)

all: module-palm-policy.so

%.o: %.c
	$(CC) $(ALL_CFLAGS) -c $< -o $@

module-palm-policy.so: $(OBJ)
	$(CC) $(ALL_LDFLAGS) -o $@ $(OBJ) $(PKG_LIBS)

install: module-palm-policy.so
	install -d $(DESTDIR)$(MODULE_DIR)
	install -m 0755 module-palm-policy.so $(DESTDIR)$(MODULE_DIR)/
	# audiod includes <pulse/module-palm-policy.h> and
	# <pulse/module-palm-policy-tables.h> for the wire protocol. Install the
	# -default variants under those names rather than the in-tree wrappers,
	# which only exist to pick a machine-specific table via config.h. This is
	# the same mapping meta-webos applies in its pulseaudio bbappend.
	install -d $(DESTDIR)$(includedir)/pulse
	install -m 0644 module-palm-policy-default.h $(DESTDIR)$(includedir)/pulse/module-palm-policy.h
	install -m 0644 module-palm-policy-tables-default.h $(DESTDIR)$(includedir)/pulse/module-palm-policy-tables.h

clean:
	rm -f $(OBJ) module-palm-policy.so

.PHONY: all install clean
