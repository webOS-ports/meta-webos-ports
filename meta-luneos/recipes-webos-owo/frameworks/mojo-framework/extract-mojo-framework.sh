#!/bin/sh
# Build the source tarball mojo-framework.bb expects, from a legacy webOS
# root filesystem.
#
# Mojo was never open-sourced - Open webOS released Enyo and kept Mojo
# proprietary to Palm/HP - so there is no repository for the recipe to fetch.
# The framework has to come off a webOS 3.0.5 image you already hold a licence
# to use.  Point this script at an unpacked rootfs (or a mounted doctor image)
# and drop the result into your downloads directory.
#
#   ./extract-mojo-framework.sh /path/to/nova-cust-image-*.rootfs ~/downloads
#
# Only the pieces the framework needs at runtime are taken:
#
#   mojo/         mojo.js, the submission's assets and the builtin blobs
#                 (both Mojo 1's and Mojo 2's)
#   mojo2/        Mojo 2 bootstrap mojo.js and its submission's assets
#   mojocommon/   shared images and templates; mojo/ and mojo2/ symlink into it
#   mojo.core/    loadable framework behind mojo-core.js
#   prototype/    stock Prototype 1.6.0.3, used instead of the builtin rewrite
#
set -e

ROOTFS="$1"
OUTDIR="${2:-.}"
SUBMISSION="${MOJO_SUBMISSION:-506}"

if [ -z "$ROOTFS" ]; then
    echo "usage: $0 <webos-rootfs-dir> [output-dir]" >&2
    exit 1
fi

FW="$ROOTFS/usr/palm/frameworks"
if [ ! -d "$FW/mojo/submissions/$SUBMISSION" ]; then
    echo "error: $FW/mojo/submissions/$SUBMISSION not found." >&2
    echo "       Is '$ROOTFS' an unpacked webOS root filesystem?" >&2
    exit 1
fi

VERSION="1.0-$SUBMISSION"
STAGE=$(mktemp -d)
trap 'rm -rf "$STAGE"' EXIT
DEST="$STAGE/mojo-framework-$VERSION"
mkdir -p "$DEST"

# -a keeps the relative symlinks under mojo/submissions/NNN that point into
# mojocommon; they resolve once both are installed side by side.
for part in mojo mojo2 mojocommon mojo.core prototype; do
    if [ ! -e "$FW/$part" ]; then
        echo "error: missing $FW/$part" >&2
        exit 1
    fi
    cp -a "$FW/$part" "$DEST/"
done
cp -a "$FW/mojo-core.js" "$DEST/"

# Other Mojo 1 submissions are dead weight; 3.0.5 images carry only one
# Mojo 2 submission, which is kept as-is.
find "$DEST/mojo/submissions" -mindepth 1 -maxdepth 1 -type d \
     ! -name "$SUBMISSION" -exec rm -rf {} +

# Only the two framework blobs survive de-nativization; the per-library
# builtins publish an object built inside their trailer and have loadable
# equivalents under /usr/palm/frameworks anyway.
find "$DEST/mojo/builtins" -type f -name '*.js' \
     ! -name 'palmInitFramework*.js' -delete

TARBALL="$OUTDIR/mojo-framework-$VERSION.tar.gz"
mkdir -p "$OUTDIR"
tar -czf "$TARBALL" -C "$STAGE" "mojo-framework-$VERSION"

echo "wrote $TARBALL"
echo
echo "Add to your build, e.g. in conf/local.conf:"
echo "    MOJO_FRAMEWORK_TARBALL = \"mojo-framework-$VERSION.tar.gz\""
echo "and make sure $OUTDIR is on DL_DIR or FILESEXTRAPATHS."
echo
echo "sha256sum:"
sha256sum "$TARBALL"
