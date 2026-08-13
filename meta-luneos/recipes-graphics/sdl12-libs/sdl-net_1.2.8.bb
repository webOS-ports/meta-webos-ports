SUMMARY = "Networking library for SDL 1.2"
DESCRIPTION = "SDL_net 1.2, providing libSDL_net-1.2.so.0. 96 titles link it, \
mostly for high-score submission and multiplayer lobbies that no longer exist - \
but a missing soname stops the application loading at all."

SDLLIB = "SDL_net"
SDLBRANCH = "SDL-1.2"
SRCREV = "7aa8ffae32fda6455792c5538a8b64a655dd0721"

LIC_FILES_CHKSUM = "file://COPYING;md5=9cf3de2d872bf510f88eb20d06d700b5"

require sdl12-libs.inc
