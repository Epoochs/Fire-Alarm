SUMMARY = "meta -iti layers recipe"
DESCRIPTION = "clone demo app from fady"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/FadyKhalil/DemoApp.git;protocol=https;branch=main"
SRCREV = "720c663c5fd7246b4b42c5205d74db7d9784b5b2"

S= "${WORKDIR}/git"
inherit cmake

#EXTRA_OEMAKE = "STATIC_BUILD=1"

do_configure() {
    cmake -S ${S} -B ${B} \
        #-DCMAKE_BUILD_TYPE=Release \
        #-DCMAKE_EXE_LINKER_FLAGS="-static" \
       # -DBUILD_SHARED_LIBS=OFF
}

do_compile() {
    cd ${B}
    make
}

do_install (){
    install -d ${D}${bindir}
    install -m 0755 ${B}/calculator ${D}${bindir}
}