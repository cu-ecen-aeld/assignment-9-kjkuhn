
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-kjkuhn.git;protocol=ssh;branch=main \
    file://scull.sh \
    "

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "24407f68cc9a89638af9778766062f7b02093d91"

inherit update-rc.d 
inherit module

INITSCRIPT_PACKAGES += "${PN}"
INITSCRIPT_NAME:${PN} = "scull.sh"

S = "${WORKDIR}/git/scull"


do_install() {
    install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra
    install -m 0755 ${S}/scull.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 "${WORKDIR}/scull.sh" ${D}${sysconfdir}/init.d/
}

EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

FILES:${PN} += "${sysconfdir}/init.d/scull.sh"