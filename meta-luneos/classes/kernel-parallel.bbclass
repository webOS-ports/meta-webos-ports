# Give kernel recipes the whole machine.
#
# PARALLEL_MAKE is deliberately conservative globally, because the memory budget
# is written around C++ translation units - the cc1plus processes the OOM killer
# took on 2026-09-07 were 1.3-1.7GB resident each. Kernel objects are C and
# nowhere near that, so a kernel can run far wider than the global default
# without going near the ceiling. A kernel is also a serial long pole: nothing
# else in the image can proceed until virtual/kernel is staged.
#
# There is no wildcard form of PARALLEL_MAKE:pn-*, so rather than listing every
# kernel recipe by name (and missing the next one added), key off the class the
# recipe inherits. This runs for every recipe, so keep it cheap.
#
# Set PARALLEL_MAKE_KERNEL in local.conf to tune or, set to empty, to disable.
#
# NB: this only helps recipes that are actually CPU-bound. linux-google-sargo,
# for example, spends its time in single-threaded shell loops over dependency
# lists (kernel "size_append" accounting), with all three PSI pressure metrics at
# zero - a wider -j does nothing there.

PARALLEL_MAKE_KERNEL ??= "-j ${@oe.utils.cpu_count()}"

python kernel_parallel_handler () {
    if not (bb.data.inherits_class('kernel', d) or
            bb.data.inherits_class('kernel-fitimage', d)):
        return
    pm = d.getVar('PARALLEL_MAKE_KERNEL')
    if pm:
        d.setVar('PARALLEL_MAKE', pm)
}

addhandler kernel_parallel_handler
kernel_parallel_handler[eventmask] = "bb.event.RecipeParsed"
