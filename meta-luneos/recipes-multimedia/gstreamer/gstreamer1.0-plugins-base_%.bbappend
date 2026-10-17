# opusenc and opusdec. libopus is in the image already (opusfile); without
# this GStreamer has no Opus encoder it can use - libav's gives no output
# caps - and Qt Multimedia offers no Opus recording.
PACKAGECONFIG:append = " opus"
