# gstreamer in zeus doesn't use meson for build, but bbappend in meta-qt5 was
# already updated to expect meson options
# https://github.com/meta-qt5/meta-qt5/commit/7f324f11bbbfe326cbb90edc8efbbe3f4fcc0874
EXTRA_OECONF_remove = "-Dqt5=disabled"
