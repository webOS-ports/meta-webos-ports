/*
 * gralloc.waydroid.so - Android's default gralloc, with render targets padded
 * to the size Mesa's llvmpipe assumes they have.
 *
 * Without a GPU render node, Waydroid renders through ANGLE on lavapipe, or
 * through Mesa itself, and either way it is llvmpipe that draws. llvmpipe
 * rasterises in 4x4-pixel blocks and lays out its own render targets with the
 * height rounded up to a multiple of 4 (llvmpipe_texture_layout,
 * LP_RASTER_BLOCK_SIZE), so a block on the bottom edge reads and writes whole
 * rows past the visible height. gralloc.default allocates exactly
 * stride * height, rounded to a page. For a window whose height is 2 more than
 * a multiple of 4, which on the BraX3 is the full-screen and most app windows
 * (720x1390, 720x1462, 720x1470), the last block runs off the end of the page.
 * SurfaceFlinger then dies with SEGV_ACCERR in llvmpipe's blend code, and with
 * it every app on screen.
 *
 * This module is the stock one with one change: it allocates RGB buffers tall
 * enough for the whole last block. Everything else - the buffer handle, its
 * stride, mapping, locking - is gralloc.default's own, loaded from the vendor
 * partition and called with its own module pointer, so the handle layout
 * Waydroid's hwcomposer reads (it treats the module as "default") is the
 * stock one.
 *
 * Android loads it by name: with ro.hardware.gralloc unset, hw_get_module()
 * tries gralloc.<ro.board.platform>.so, and that is "waydroid" in every
 * Waydroid vendor image. Processes that find no module of their own ABI here
 * fall back to gralloc.default, which maps and locks the same handles.
 *
 * Freestanding on purpose: it is built with the Yocto toolchain against
 * stub bionic libraries, so it uses nothing but dlopen, dlsym and the log.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include <stddef.h>
#include <stdint.h>

/* --- The AOSP HAL ABI (hardware/libhardware/include/hardware) ------------ */

#define HARDWARE_MODULE_TAG 0x48574d54 /* "HWMT" */

struct hw_module_t;
struct hw_device_t;
typedef const struct native_handle *buffer_handle_t;

typedef struct hw_module_methods_t {
    int (*open)(const struct hw_module_t *module, const char *id,
                struct hw_device_t **device);
} hw_module_methods_t;

typedef struct hw_module_t {
    uint32_t tag;
    uint16_t module_api_version;
    uint16_t hal_api_version;
    const char *id;
    const char *name;
    const char *author;
    struct hw_module_methods_t *methods;
    void *dso;
#ifdef __LP64__
    uint64_t reserved[32 - 7];
#else
    uint32_t reserved[32 - 7];
#endif
} hw_module_t;

typedef struct hw_device_t {
    uint32_t tag;
    uint32_t version;
    struct hw_module_t *module;
#ifdef __LP64__
    uint64_t reserved[12];
#else
    uint32_t reserved[12];
#endif
    int (*close)(struct hw_device_t *device);
} hw_device_t;

struct android_ycbcr;

typedef struct gralloc_module_t {
    struct hw_module_t common;
    int (*registerBuffer)(const struct gralloc_module_t *module,
                          buffer_handle_t handle);
    int (*unregisterBuffer)(const struct gralloc_module_t *module,
                            buffer_handle_t handle);
    int (*lock)(const struct gralloc_module_t *module, buffer_handle_t handle,
                int usage, int l, int t, int w, int h, void **vaddr);
    int (*unlock)(const struct gralloc_module_t *module,
                  buffer_handle_t handle);
    int (*perform)(const struct gralloc_module_t *module, int operation, ...);
    int (*lock_ycbcr)(const struct gralloc_module_t *module,
                      buffer_handle_t handle, int usage, int l, int t, int w,
                      int h, struct android_ycbcr *ycbcr);
    int (*lockAsync)(const struct gralloc_module_t *module,
                     buffer_handle_t handle, int usage, int l, int t, int w,
                     int h, void **vaddr, int fenceFd);
    int (*unlockAsync)(const struct gralloc_module_t *module,
                       buffer_handle_t handle, int *fenceFd);
    int (*lockAsync_ycbcr)(const struct gralloc_module_t *module,
                           buffer_handle_t handle, int usage, int l, int t,
                           int w, int h, struct android_ycbcr *ycbcr,
                           int fenceFd);
    int32_t (*getTransportSize)(const struct gralloc_module_t *module,
                                buffer_handle_t handle, uint32_t *outNumFds,
                                uint32_t *outNumInts);
    int32_t (*validateBufferSize)(const struct gralloc_module_t *module,
                                  buffer_handle_t handle, uint32_t w,
                                  uint32_t h, int32_t format, int usage,
                                  uint32_t stride);
    void *reserved_proc[1];
} gralloc_module_t;

typedef struct alloc_device_t {
    struct hw_device_t common;
    int (*alloc)(struct alloc_device_t *dev, int w, int h, int format,
                 int usage, buffer_handle_t *handle, int *stride);
    int (*free)(struct alloc_device_t *dev, buffer_handle_t handle);
    void (*dump)(struct alloc_device_t *dev, char *buff, int buff_len);
    void *reserved_proc[7];
} alloc_device_t;

/* HAL_PIXEL_FORMAT_*, from system/core/libsystem/include/system/graphics-base */
enum {
    FMT_RGBA_8888 = 1,
    FMT_RGBX_8888 = 2,
    FMT_RGB_888 = 3,
    FMT_RGB_565 = 4,
    FMT_BGRA_8888 = 5,
    FMT_RGBA_FP16 = 0x16,
    FMT_RGBA_1010102 = 0x2b,
};

/* --- bionic ---------------------------------------------------------------- */

#define RTLD_NOW 2
#define ANDROID_LOG_INFO 4
#define ANDROID_LOG_ERROR 6

void *dlopen(const char *filename, int flags);
void *dlsym(void *handle, const char *symbol);
const char *dlerror(void);
int __android_log_print(int prio, const char *tag, const char *fmt, ...)
    __attribute__((format(printf, 3, 4)));

#define LOG_TAG "gralloc.waydroid"

/* --- the module ------------------------------------------------------------ */

#ifndef GRALLOC_ORIG_PATH
#ifdef __LP64__
#define GRALLOC_ORIG_PATH "/vendor/lib64/hw/gralloc.default.so"
#else
#define GRALLOC_ORIG_PATH "/vendor/lib/hw/gralloc.default.so"
#endif
#endif

#define GPU0 "gpu0" /* GRALLOC_HARDWARE_GPU0 */

/* llvmpipe's block: LP_RASTER_BLOCK_SIZE, in both directions. */
#define LP_BLOCK 4
/* llvmpipe aligns its row stride to a cache line, util_get_cpu_caps()->cacheline;
 * 64 bytes on every CPU the Waydroid images are built for. */
#define LP_CACHELINE 64

static const gralloc_module_t *orig;
static int (*orig_alloc)(struct alloc_device_t *, int, int, int, int,
                         buffer_handle_t *, int *);

static int streq(const char *a, const char *b)
{
    while (*a && *a == *b) {
        a++;
        b++;
    }
    return *a == *b;
}

static unsigned bytes_per_pixel(int format)
{
    switch (format) {
    case FMT_RGBA_8888:
    case FMT_RGBX_8888:
    case FMT_BGRA_8888:
    case FMT_RGBA_1010102:
        return 4;
    case FMT_RGB_888:
        return 3;
    case FMT_RGB_565:
        return 2;
    case FMT_RGBA_FP16:
        return 8;
    default:
        /* YUV and the rest are not render targets, and a taller YUV buffer
         * would move its chroma planes. */
        return 0;
    }
}

/*
 * n / d, by shift and subtract. A plain 64-bit division is a call into the
 * compiler's runtime on 32-bit ARM (__aeabi_uldivmod), which nothing provides
 * to a library linked the way this one is. d is never 0 here, and is below
 * 2^35, so r never overflows.
 */
static uint64_t udiv64(uint64_t n, uint64_t d)
{
    uint64_t q = 0;
    uint64_t r = 0;

    for (int i = 63; i >= 0; i--) {
        r = (r << 1) | ((n >> i) & 1);
        if (r >= d) {
            r -= d;
            q |= (uint64_t)1 << i;
        }
    }
    return q;
}

/*
 * The height to ask gralloc.default for, so that its buffer is at least as
 * large as llvmpipe's own layout of a w x h target: the height rounded up to a
 * whole block, and the width to a whole block and its row to a cache line.
 * gralloc.default's rows are w * bpp rounded up to 4 bytes, and its buffer is
 * that times the height it is given, so the extra width is paid for in rows.
 *
 * Never less than h: the sizes come from apps, through the allocator service,
 * and a height smaller than asked for would hand an app a buffer smaller than
 * it was told. Anything too large to pad is passed through unchanged, for
 * gralloc.default to judge as it always has.
 */
static int gralloc_waydroid_padded_height(int w, int h, int format)
{
    unsigned bpp = bytes_per_pixel(format);
    uint64_t lp_row;
    uint64_t lp_rows;
    uint64_t gr_row;
    uint64_t need;
    uint64_t rows;

    if (bpp == 0 || w <= 0 || h <= 0)
        return h;

    /* Below 2^35 and 2^32: neither sum nor product here can wrap. */
    lp_row = ((uint64_t)w + LP_BLOCK - 1) / LP_BLOCK * LP_BLOCK * bpp;
    lp_row = (lp_row + LP_CACHELINE - 1) / LP_CACHELINE * LP_CACHELINE;
    lp_rows = ((uint64_t)h + LP_BLOCK - 1) / LP_BLOCK * LP_BLOCK;
    gr_row = ((uint64_t)w * bpp + 3) & ~(uint64_t)3;

    if (__builtin_mul_overflow(lp_row, lp_rows, &need) ||
        __builtin_add_overflow(need, gr_row - 1, &need))
        return h;
    rows = udiv64(need, gr_row);

    if (rows > INT32_MAX || rows < (uint64_t)h)
        return h;
    return (int)rows;
}

static int wd_alloc(struct alloc_device_t *dev, int w, int h, int format,
                    int usage, buffer_handle_t *handle, int *stride)
{
    return __atomic_load_n(&orig_alloc, __ATOMIC_ACQUIRE)(
        dev, w, gralloc_waydroid_padded_height(w, h, format), format, usage,
        handle, stride);
}

/* The device is gralloc.default's own, holding its own module pointer; only
 * its alloc is swapped. Every gpu0 device it opens shares one alloc
 * function, so one saved pointer serves them all. The allocator service
 * answers on several binder threads, so the pointer is shared between them:
 * stored and loaded atomically, though every store writes the same value. */
static int wd_open(const struct hw_module_t *module, const char *id,
                   struct hw_device_t **device)
{
    int err;

    (void)module;
    if (!orig)
        return -19; /* -ENODEV: gralloc.default did not load; see the log */
    err = orig->common.methods->open(&orig->common, id, device);
    if (err != 0 || !streq(id, GPU0))
        return err;

    alloc_device_t *dev = (alloc_device_t *)*device;
    if (dev->alloc != wd_alloc) {
        __atomic_store_n(&orig_alloc, dev->alloc, __ATOMIC_RELEASE);
        dev->alloc = wd_alloc;
    }
    return 0;
}

/* Each entry point hands gralloc.default its own module, not this one: its
 * private_module_t is larger than gralloc_module_t, and only its own copy
 * has the rest of it. */
static int wd_register(const gralloc_module_t *m, buffer_handle_t h)
{
    (void)m;
    return orig->registerBuffer(orig, h);
}

static int wd_unregister(const gralloc_module_t *m, buffer_handle_t h)
{
    (void)m;
    return orig->unregisterBuffer(orig, h);
}

static int wd_lock(const gralloc_module_t *m, buffer_handle_t h, int usage,
                   int l, int t, int w, int hh, void **vaddr)
{
    (void)m;
    return orig->lock(orig, h, usage, l, t, w, hh, vaddr);
}

static int wd_unlock(const gralloc_module_t *m, buffer_handle_t h)
{
    (void)m;
    return orig->unlock(orig, h);
}

static int wd_lock_ycbcr(const gralloc_module_t *m, buffer_handle_t h,
                         int usage, int l, int t, int w, int hh,
                         struct android_ycbcr *ycbcr)
{
    (void)m;
    return orig->lock_ycbcr(orig, h, usage, l, t, w, hh, ycbcr);
}

static int wd_lock_async(const gralloc_module_t *m, buffer_handle_t h,
                         int usage, int l, int t, int w, int hh, void **vaddr,
                         int fence)
{
    (void)m;
    return orig->lockAsync(orig, h, usage, l, t, w, hh, vaddr, fence);
}

static int wd_unlock_async(const gralloc_module_t *m, buffer_handle_t h,
                           int *fence)
{
    (void)m;
    return orig->unlockAsync(orig, h, fence);
}

static int wd_lock_async_ycbcr(const gralloc_module_t *m, buffer_handle_t h,
                               int usage, int l, int t, int w, int hh,
                               struct android_ycbcr *ycbcr, int fence)
{
    (void)m;
    return orig->lockAsync_ycbcr(orig, h, usage, l, t, w, hh, ycbcr, fence);
}

static int32_t wd_transport_size(const gralloc_module_t *m, buffer_handle_t h,
                                 uint32_t *fds, uint32_t *ints)
{
    (void)m;
    return orig->getTransportSize(orig, h, fds, ints);
}

static int32_t wd_validate_size(const gralloc_module_t *m, buffer_handle_t h,
                                uint32_t w, uint32_t hh, int32_t format,
                                int usage, uint32_t stride)
{
    (void)m;
    return orig->validateBufferSize(orig, h, w, hh, format, usage, stride);
}

static hw_module_methods_t wd_methods = {
    .open = wd_open,
};

/*
 * Filled in from gralloc.default when this library is loaded, before
 * hw_get_module() looks it up. Its id is set here because that lookup checks
 * it; the versions and the name are gralloc.default's, since callers decide
 * what to use by them (Mesa picks its gralloc support by the name).
 *
 * perform is left NULL: it is variadic and cannot be forwarded, and
 * gralloc.default has none.
 */
__attribute__((visibility("default")))
gralloc_module_t HMI = {
    .common = {
        .tag = HARDWARE_MODULE_TAG,
        .id = "gralloc",
        .name = "Waydroid gralloc (unloaded)",
        .author = "LuneOS",
        .methods = &wd_methods,
    },
};

__attribute__((constructor))
static void wd_init(void)
{
    void *lib = dlopen(GRALLOC_ORIG_PATH, RTLD_NOW);

    if (!lib) {
        const char *e = dlerror();
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "cannot load %s: %s",
                            GRALLOC_ORIG_PATH, e ? e : "?");
        return;
    }
    orig = (const gralloc_module_t *)dlsym(lib, "HMI");
    if (!orig || !orig->common.methods || !orig->common.methods->open) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "%s has no usable HMI",
                            GRALLOC_ORIG_PATH);
        orig = NULL;
        return;
    }

    HMI.common.module_api_version = orig->common.module_api_version;
    HMI.common.hal_api_version = orig->common.hal_api_version;
    HMI.common.name = orig->common.name;
    HMI.common.author = orig->common.author;
    HMI.registerBuffer = orig->registerBuffer ? wd_register : NULL;
    HMI.unregisterBuffer = orig->unregisterBuffer ? wd_unregister : NULL;
    HMI.lock = orig->lock ? wd_lock : NULL;
    HMI.unlock = orig->unlock ? wd_unlock : NULL;
    HMI.lock_ycbcr = orig->lock_ycbcr ? wd_lock_ycbcr : NULL;
    HMI.lockAsync = orig->lockAsync ? wd_lock_async : NULL;
    HMI.unlockAsync = orig->unlockAsync ? wd_unlock_async : NULL;
    HMI.lockAsync_ycbcr = orig->lockAsync_ycbcr ? wd_lock_async_ycbcr : NULL;
    HMI.getTransportSize = orig->getTransportSize ? wd_transport_size : NULL;
    HMI.validateBufferSize = orig->validateBufferSize ? wd_validate_size : NULL;

    __android_log_print(ANDROID_LOG_INFO, LOG_TAG,
                        "wrapping %s, render targets padded to %d-row blocks",
                        GRALLOC_ORIG_PATH, LP_BLOCK);
}
