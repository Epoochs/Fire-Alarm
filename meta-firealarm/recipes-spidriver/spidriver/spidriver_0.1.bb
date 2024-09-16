DESCRIPTION = "My custom device driver"
LICENSE = "CLOSED"

SRC_URI = "file://spidriver.c"

S = "${WORKDIR}"

inherit kernel

# Set the module name (without extension) and other necessary variables
KERNEL_MODULE_NAME = "spidriver"

do_compile() {
    # Use the kernel build system to compile the module
    oe_runmake
}

do_install() {
    # Install the kernel module to the appropriate directory
    install -d ${D}/${kernel_modules_dir}
    install -m 0644 ${B}/spidriver.ko ${D}/${kernel_modules_dir}
}

#FILES_${PN} += "${kernel_modules_dir}/spidriver.ko"
