# Sourced by webapp-mgr.sh: chooses how WebAppMgr rasterises from what the device has.
#
# The stock switches are LG's TV settings: CPU raster with 2 threads, low-res tiling off and
# 60 MB of GPU memory. On a phone that leaves tiles blank while a list is flicked and drops
# scrolling to ~30 fps. With GPU raster, 4 threads and 256 MB a Pixel 3a (4 GB, 8 cores)
# scrolls at 16-17 ms a frame. A 1 GB dual core like the TouchPad cannot spare that memory,
# so the choice is made per device:
#
#   gpu  at least ~3 GB RAM and 4 cores: GPU raster, low-res tiling, 256 MB GPU memory,
#        half the cores as raster threads (2 to 4)
#   cpu  anything smaller: the stock settings
#
# A device or a user can override the choice, read in this order:
#   /etc/wam/devices/<nyx device_name>.conf   shipped for a device that needs an exception
#   /etc/wam/raster.conf                      local override
# Either may set WAM_RASTER=gpu|cpu, WAM_GPU_MEM_MB and WAM_RASTER_THREADS.
#
# Sets MAX_GPU_MEM_LIMIT, BLINK_NUM_RASTER_THREADS and WAM_RASTER_SWITCHES.
# WAM_PROFILE_* replace the inputs in tests.

wam_profile_meminfo="${WAM_PROFILE_MEMINFO:-/proc/meminfo}"
wam_profile_cpus="${WAM_PROFILE_CPU_POSSIBLE:-/sys/devices/system/cpu/possible}"
wam_profile_conf="${WAM_PROFILE_CONF_DIR:-/etc/wam}"

# MemTotal is what the kernel kept after carve-outs: a 3 GB phone shows ~2.9 GB, a 4 GB ~3.5 GB.
wam_mem_kb=$(awk '$1 == "MemTotal:" { print $2; exit }' "$wam_profile_meminfo" 2>/dev/null)
# "possible", not "online": cores that are hotplugged off still count (the TouchPad does that).
wam_cpus=$(awk -F, '{ n = 0; for (i = 1; i <= NF; i++) { if (split($i, r, "-") == 2) n += r[2] - r[1] + 1; else n++ } print n; exit }' \
    "$wam_profile_cpus" 2>/dev/null)
case "$wam_mem_kb" in ''|*[!0-9]*) wam_mem_kb=0 ;; esac
case "$wam_cpus" in ''|*[!0-9]*) wam_cpus=1 ;; esac

if [ "$wam_mem_kb" -ge 2621440 ] && [ "$wam_cpus" -ge 4 ]; then
    WAM_RASTER=gpu
else
    WAM_RASTER=cpu
fi
WAM_GPU_MEM_MB=
WAM_RASTER_THREADS=

if [ -n "${WAM_PROFILE_DEVICE+set}" ]; then
    wam_device="$WAM_PROFILE_DEVICE"
else
    wam_device=$(nyx-cmd DeviceInfo query device_name 2>/dev/null)
fi
case "$wam_device" in
    ''|*[!A-Za-z0-9_.-]*) ;;
    *) if [ -r "$wam_profile_conf/devices/$wam_device.conf" ]; then
           # shellcheck source=/dev/null
           . "$wam_profile_conf/devices/$wam_device.conf" || :
       fi ;;
esac
if [ -r "$wam_profile_conf/raster.conf" ]; then
    # shellcheck source=/dev/null
    . "$wam_profile_conf/raster.conf" || :
fi

if [ "$WAM_RASTER" = gpu ]; then
    wam_threads=$((wam_cpus / 2))
    [ "$wam_threads" -lt 2 ] && wam_threads=2
    [ "$wam_threads" -gt 4 ] && wam_threads=4
    MAX_GPU_MEM_LIMIT=256
    BLINK_NUM_RASTER_THREADS=$wam_threads
    WAM_RASTER_SWITCHES="--enable-gpu-rasterization"
else
    WAM_RASTER=cpu
    MAX_GPU_MEM_LIMIT=60
    BLINK_NUM_RASTER_THREADS=2
    WAM_RASTER_SWITCHES="--disable-gpu-rasterization --disable-low-res-tiling"
fi
case "$WAM_GPU_MEM_MB" in ''|*[!0-9]*) ;; *) MAX_GPU_MEM_LIMIT=$WAM_GPU_MEM_MB ;; esac
case "$WAM_RASTER_THREADS" in ''|*[!0-9]*) ;; *) BLINK_NUM_RASTER_THREADS=$WAM_RASTER_THREADS ;; esac

echo "webapp-mgr: raster $WAM_RASTER (device ${wam_device:-unknown}, ${wam_mem_kb} kB, $wam_cpus cpus):" \
    "gpu-mem ${MAX_GPU_MEM_LIMIT} MB, $BLINK_NUM_RASTER_THREADS raster threads"
export MAX_GPU_MEM_LIMIT BLINK_NUM_RASTER_THREADS WAM_RASTER_SWITCHES
unset wam_profile_meminfo wam_profile_cpus wam_profile_conf wam_mem_kb wam_cpus wam_device wam_threads
