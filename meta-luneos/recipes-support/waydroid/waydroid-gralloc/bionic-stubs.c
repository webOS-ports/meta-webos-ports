/*
 * Link-time stand-ins for the bionic libraries gralloc.waydroid.so uses.
 *
 * Only their names and symbols matter: the linker records DT_NEEDED and the
 * undefined symbols from them, and Android's own libc.so, libdl.so and
 * liblog.so provide the real ones at run time. None of this code is shipped
 * or ever runs. Built once per library, with STUB_LIB_<name> picking which.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#if defined(STUB_LIB_dl)
void *dlopen(const char *filename, int flags) { (void)filename; (void)flags; return 0; }
void *dlsym(void *handle, const char *symbol) { (void)handle; (void)symbol; return 0; }
const char *dlerror(void) { return 0; }
#elif defined(STUB_LIB_log)
int __android_log_print(int prio, const char *tag, const char *fmt, ...)
{
    (void)prio; (void)tag; (void)fmt;
    return 0;
}
#elif defined(STUB_LIB_c)
/* Anything the compiler emits a call to on its own. */
void *memcpy(void *d, const void *s, __SIZE_TYPE__ n) { (void)s; (void)n; return d; }
void *memset(void *d, int c, __SIZE_TYPE__ n) { (void)c; (void)n; return d; }
#else
#error "define STUB_LIB_c, STUB_LIB_dl or STUB_LIB_log"
#endif
