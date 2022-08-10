# Remove rsvg because that's the only thing pulling librsvg -> cargo-native -> rust-native into our images
# and Tofee's builder takes 2h+ to build it
PACKAGECONFIG:remove = "rsvg"
