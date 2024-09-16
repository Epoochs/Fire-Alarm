SUMMARY = "meta -iti layers recipe"
DESCRIPTION = "a simple sh script to turn led on if failed to ping"

LICENSE = "CLOSED"
FILES_PATH:append = " /home/mohamed/Desktop/Yocto/meta-iti/recipes-pingled/pingled/"

SRC_URI += "file://pingscript.sh \
           file://myservice.service"

inherit systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE = "myservice.service"

S = "${WORKDIR}/src"
D = "${WORKDIR}/Dest"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/pingscript.sh ${D}${bindir}
    install -d ${D}/${sysconfdir}/systemd/system/
    install -m 0755 ${THISDIR}/myservice.service ${D}/${sysconfdir}/systemd/system/
}



