# The Teams and Signal call media engines protect their audio with AEAD_AES_256_GCM SRTP, and
# libsrtp only has the GCM ciphers with a crypto library behind it; its built-in one does AES-CM
# and HMAC-SHA1 alone.
PACKAGECONFIG:append = " openssl"
