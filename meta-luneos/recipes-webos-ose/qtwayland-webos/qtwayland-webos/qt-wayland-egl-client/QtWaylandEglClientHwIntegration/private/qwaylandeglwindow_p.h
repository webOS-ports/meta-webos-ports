// Forwarding header supplied by LuneOS.
//
// Qt 6.10 moved the QtWayland client into qtbase and stopped publishing the
// client-side EGL hardware integration as the private module
// QtWaylandEglClientHwIntegration. webos-wayland-egl still includes its
// headers by that module path, so recreate just enough of it to keep those
// includes working, pointing at the copies compiled into this plugin.
#include "../../qwaylandeglwindow_p.h"
