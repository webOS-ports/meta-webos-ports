# Only what com.palm.service.photos (app-services) needs: heif-dec turning HEIC
# photos, as iPhones take them, into JPEG. That is HEVC decoding with libde265
# and heif-dec's JPEG writer. meta-oe builds every codec besides (x265, aom,
# dav1d, openh264, ffmpeg, OpenJPEG) and the gdk-pixbuf loader, none of which
# anything on LuneOS uses.

PACKAGECONFIG = ""
DEPENDS:remove = "gdk-pixbuf x265 tiff libwebp"
DEPENDS:append = " jpeg"

EXTRA_OECMAKE:append = " \
    -DWITH_X265=OFF \
    -DWITH_X264=OFF \
    -DWITH_OpenH264_DECODER=OFF \
    -DWITH_GDK_PIXBUF=OFF \
    -DWITH_EXAMPLE_HEIF_VIEW=OFF \
    -DWITH_EXAMPLE_HEIF_THUMB=OFF \
    -DWITH_LIBSHARPYUV=OFF \
    -DENABLE_PLUGIN_LOADING=OFF \
    -DBUILD_TESTING=OFF \
    -DBUILD_DOCUMENTATION=OFF \
    -DCMAKE_DISABLE_FIND_PACKAGE_TIFF=ON \
"
