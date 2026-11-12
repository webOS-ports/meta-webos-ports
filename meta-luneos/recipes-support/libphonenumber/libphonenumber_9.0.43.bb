SUMMARY = "Google's library for parsing, formatting and validating phone numbers"
DESCRIPTION = "C++ port of libphonenumber. mmsd-tng uses it to put the numbers \
of MMS senders and recipients into E.164 form."
HOMEPAGE = "https://github.com/google/libphonenumber"
SECTION = "libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e23fadd6ceef8c618fc1c65191d846fa"

DEPENDS = "protobuf protobuf-native abseil-cpp icu"

# v9.0.43 (the tag is not on a branch)
SRC_URI = "git://github.com/google/libphonenumber.git;protocol=https;nobranch=1"
SRCREV = "86ea33cee5c22ab7e23b3de368e7cc7fa439a2b7"

OECMAKE_SOURCEPATH = "${S}/cpp"

inherit cmake

# REGENERATE_METADATA needs Java; the generated metadata is in the tree.
# The geocoder (offline location lookup) and tests are not needed.
# std::mutex instead of Boost for thread safety, so Boost is not pulled in.
EXTRA_OECMAKE = " \
    -DREGENERATE_METADATA=OFF \
    -DBUILD_GEOCODER=OFF \
    -DBUILD_TESTING=OFF \
    -DBUILD_STATIC_LIB=OFF \
    -DUSE_BOOST=OFF \
    -DUSE_STDMUTEX=ON \
"

# The exported CMake targets name ICU and protobuf by absolute sysroot
# path, which buildpaths QA rejects (and which would point a consumer at
# the build host). Turn them into bare library names, which CMake emits
# as -l<name> for the linker to find in the consumer's sysroot.
do_install:append() {
    for f in ${D}${libdir}/cmake/libphonenumber/*.cmake; do
        [ -e "$f" ] || continue
        sed -i -e 's|${RECIPE_SYSROOT}${libdir}/lib\([A-Za-z0-9_.+-]*\)\.so|\1|g' \
               -e 's|${RECIPE_SYSROOT}||g' "$f"
    done
}
