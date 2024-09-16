SUMMARY = "MPV media player"
DESCRIPTION = "MPV is a free, open-source, and cross-platform media player that supports a wide variety of formats."
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://COPYING;md5=7b20a0b06f1f66dc7d1c317b70fce4d3"

SRC_URI = "https://github.com/mpv-player/mpv/archive/v0.35.1.tar.gz"
#SRC_URI[md5sum] = "d41d8cd98f00b204e9800998ecf8427e"
#SRC_URI[sha256sum] = "dd8e6c8f66a9e5b9d765431bbd8cb8a857021fbd649a046b65ec7db4c44193b"

S = "${WORKDIR}/mpv-0.35.1"

inherit autotools pkgconfig

DEPENDS = "ffmpeg libass libx11"

EXTRA_OEMAKE = 'PREFIX=${D}${prefix}'

do_configure() {
    ./bootstrap.py
    ./waf configure --prefix=${prefix}
}

do_compile() {
    ./waf build
}

do_install() {
    ./waf install --destdir=${D}
}


