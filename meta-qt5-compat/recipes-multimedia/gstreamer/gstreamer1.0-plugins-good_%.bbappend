# gstreamer in zeus doesn't use meson for build, but bbappend in meta-qt5 was
# already updated to expect meson options
# https://github.com/meta-qt5/meta-qt5/commit/7f324f11bbbfe326cbb90edc8efbbe3f4fcc0874
inherit qmake5_paths

PACKAGECONFIG[qt5] = '--enable-qt \
                      --with-moc="${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/moc" \
                      --with-uic="${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/uic" \
                      --with-rcc="${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/rcc" \
                     ,--disable-qt,gstreamer1.0-plugins-base qtbase qtdeclarative qtbase-native'
