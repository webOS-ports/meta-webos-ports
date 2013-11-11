require presage.inc
require presage-${PV}.inc

DEPENDS += "presage-native"

do_configure_prepend_class-target() {
   sed -i "s#\$(top_builddir)/src/tools/text2ngram#${STAGING_BINDIR_NATIVE}/text2ngram#g" ${S}/resources/Makefile.am
}

