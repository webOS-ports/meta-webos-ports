# CVE_VERSION for the LuneOS kernels.
#
# kernel.bbclass already sets CVE_PRODUCT to linux_kernel, but CVE_VERSION
# defaults to PV, which the device kernels spell "<version>+git" (or "-git" for
# linux-megi.inc and linux-mecha.inc). Strip that suffix so the SBOM and the
# CVE scan see the kernel's own version. Kernels whose PV carries only the
# major and minor (7.2) stay that coarse; their recipes do not record more.
#
# Added to KERNEL_CLASSES in luneos-sbom.inc.

CVE_VERSION ?= "${@luneos_kernel_cve_version(d)}"

def luneos_kernel_cve_version(d):
    import re
    return re.sub(r'[+-]git.*$', '', d.getVar('PV') or '')
