
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-kjkuhn;protocol=ssh;branch=main \
    file://aesd-char-dev.sh \
    "

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "f321da2a0e0e2c147d63955ca6a0c5816766aab7"

inherit update-rc.d 
inherit module

INITSCRIPT_PACKAGES += "${PN}"
INITSCRIPT_NAME:${PN} = "aesd-char-dev.sh"

S = "${WORKDIR}/git/aesd-char-driver"

#DEPENDS += "virtual/kernel"

#do_compile() {
#    oe_runmake -C ${S}/aesd-char-driver KERNELDIR=${STAGING_KERNEL_DIR} M=${S} modules
#}


do_install() {
    install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra
    install -m 0755 ${S}/aesdchar.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 "${WORKDIR}/aesd-char-dev.sh" ${D}${sysconfdir}/init.d/
}

EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/aesd-char-driver"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

FILES:${PN} += "${sysconfdir}/init.d/aesd-char-dev.sh"