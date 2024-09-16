SUMMARY = "Linux Kernel Headers"
DESCRIPTION = "Install Linux kernel headers."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=4fbd65380cdd255951079008b364516c"

inherit kernel

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;branch=linux-6.6.y"

# Replace with your kernel version
SRCREV = "HEAD"

S = "${WORKDIR}/git"

do_compile () {
    oe_runmake headers_install \
        INSTALL_HDR_PATH=${D}/usr/include
}

do_install () {
    install -d ${D}/usr/include
    cp -r ${S}/usr/include/* ${D}/usr/include/
}

FILES_${PN} = "/usr/include"
