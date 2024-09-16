SUMMARY = "Example of how to build an external Linux kernel module"
DESCRIPTION = "${SUMMARY}"
LICENSE = "CLOSED"


inherit module

SRC_URI = "file://mydriver.c \
           file://led_device1.c \
           file://led_device2.c \
           file://led_device3.c \
           file://led_device4.c \
           file://Makefile"

KERNEL_SRC = "${STAGING_KERNEL_DIR}"

S = "${WORKDIR}"

COMPATIBLE_MACHINE = "raspberrypi4|raspberrypi4-64"
# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

# Ensure that the module is installed correctly
do_install() {
    oe_runmake DESTDIR=${D} install
}

# Ensure the compiled module is packaged correctly
FILES_${PN} += "/lib/modules/${KERNEL_VERSION}/kernel/drivers/ledmod/*.ko"


RPROVIDES:${PN} += "kernel-module-led"
