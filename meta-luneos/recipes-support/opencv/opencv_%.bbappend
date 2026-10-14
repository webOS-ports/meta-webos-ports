# The only opencv users here are libfart and luneos-faced, which use
# core/imgproc/imgcodecs. Drop what nothing uses:
#
#   gtk      the highgui GTK backend, on by default whenever DISTRO_FEATURES
#            has x11 or wayland; it drags gtk+3 (and through it
#            adwaita-icon-theme) into every build with opencv, and nothing
#            here opens a highgui window.
#   gapi     the G-API graph module; its ade dependency exists only for it.
#   samples  example programs that nothing ships.
#
# gstreamer, gphoto2 and libav stay: those libraries are built and shipped
# for other components anyway, so turning them off here saves nothing.
PACKAGECONFIG:remove = "gtk gapi samples"
