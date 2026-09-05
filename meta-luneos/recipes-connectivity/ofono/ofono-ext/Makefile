# ofono-ext: the Sailfish oFono extension APIs, built out of tree.
OFONO_DIR ?= ../../work/ofono
GUTIL_DIR ?= ../libglibutil
PREFIX    ?= /usr
LIBDIR    ?= $(PREFIX)/lib
PLUGINDIR ?= $(LIBDIR)/ofono/plugins
VERSION   ?= 2.19
STORAGEDIR?= /var/lib/ofono

# libglibutil installs its headers under include/gutil, and oFono installs
# gdbus.h as ofono/gdbus.h, so ask pkg-config rather than guessing layouts.
PKGS = glib-2.0 gobject-2.0 dbus-1 libglibutil
CFLAGS += -fPIC -Wall -O2 -D_GNU_SOURCE \
	-DOFONO_API_SUBJECT_TO_CHANGE \
	-DVERSION='"$(VERSION)"' -DSTORAGEDIR='"$(STORAGEDIR)"' \
	-Isrc -Iinclude \
	$(shell pkg-config --cflags $(PKGS))
LIBS = $(shell pkg-config --libs $(PKGS))

SRCS = $(wildcard src/*.c)
OBJS = $(SRCS:.c=.o)

all: libofonoext.so ofonoextplugin.so ofono-ext.pc

# $(LDFLAGS) must reach the link line: the distro passes hardening and
# GNU_HASH settings through it, and package QA fails without them. No
# rpath: libofonoext lands in the default library path.
libofonoext.so: $(OBJS)
	$(CC) -shared -Wl,-soname,libofonoext.so.1 -o $@ $^ $(LDFLAGS) $(LIBS)

ofonoextplugin.so: plugin/ofonoext-plugin.o libofonoext.so
	$(CC) -shared -o $@ plugin/ofonoext-plugin.o $(LDFLAGS) -L. -lofonoext

# Plugins that use these APIs must link the library themselves: oFono
# dlopen()s plugins with RTLD_NOW, so the loader plugin's copy is not in
# scope for anything loaded after it.
ofono-ext.pc: ofono-ext.pc.in
	sed -e 's|@PREFIX@|$(PREFIX)|' -e 's|@LIBDIR@|$(LIBDIR)|' \
		-e 's|@VERSION@|$(VERSION)|' $< > $@

clean:
	rm -f $(OBJS) plugin/*.o libofonoext.so ofonoextplugin.so ofono-ext.pc

install: all
	install -d $(DESTDIR)$(LIBDIR) $(DESTDIR)$(PLUGINDIR)
	install -m 644 libofonoext.so $(DESTDIR)$(LIBDIR)/libofonoext.so.1
	ln -sf libofonoext.so.1 $(DESTDIR)$(LIBDIR)/libofonoext.so
	install -m 644 ofonoextplugin.so $(DESTDIR)$(PLUGINDIR)/
	install -d $(DESTDIR)$(PREFIX)/include/ofono
	install -m 644 include/ofono/*.h $(DESTDIR)$(PREFIX)/include/ofono/
	install -d $(DESTDIR)$(LIBDIR)/pkgconfig
	install -m 644 ofono-ext.pc $(DESTDIR)$(LIBDIR)/pkgconfig/

.PHONY: all clean install
