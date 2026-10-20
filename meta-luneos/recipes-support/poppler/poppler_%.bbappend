# The Qt 6 frontend, for org.webosports.app.pdf. The recipe has the
# PACKAGECONFIG but only inherits Qt's CMake support for qt5; moc and the
# Qt 6 CMake files need qt6-cmake. Its library goes in a package of its own,
# so the print stack's poppler does not pull Qt in.
PACKAGECONFIG:append = " qt6"
inherit ${@bb.utils.contains('PACKAGECONFIG', 'qt6', 'qt6-cmake', '', d)}

EXTRA_OECMAKE:append = " -DBUILD_QT6_TESTS=OFF"

PACKAGES =+ "libpoppler-qt6"
FILES:libpoppler-qt6 = "${libdir}/libpoppler-qt6.so.*"
