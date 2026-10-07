NYX_MODULES_REQUIRED = "\
    NYXMOD_OW_ALS;\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_HAPTICS;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_LED;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL;\
"

# LED and SYSTEM are on for halium now: both modules moved into nyx-modules and
# probe at runtime (the lights HAL, and timerfd instead of /dev/alarm) rather than
# being Android-specific. nyx-modules-hybris gates on "if(NOT NYXMOD_OW_<X>)", so
# these switches are what make it stand down. Dropping LED there is what lets a
# device with an RGB LED but no lights.<board>.so reach it at all.
NYX_MODULES_REQUIRED:halium = "\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_HAPTICS;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_LED;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL;\
"

NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS = "\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_LED;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL;\
"
# hammerhead-halium is not listed above: its vibrator is a kernel timed_output device, which the
# generic haptics module drives. The hybris one calls libhardware_legacy's vibrator_exists(), which
# the 16.0 GSI no longer exports.
NYX_MODULES_REQUIRED:athene            = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:mako              = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:mako-halium       = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:onyx              = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:oxygen            = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:sagit             = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"

NYX_MODULES_REQUIRED:rpi = "\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_TOUCHPANEL;\
"

NYX_MODULES_REQUIRED_PINEPHONE = "\
    NYXMOD_OW_ALS;\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_GPS;\
    NYXMOD_OW_HAPTICS;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_LED;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL;\
"
NYX_MODULES_REQUIRED:pinephone    = "${NYX_MODULES_REQUIRED_PINEPHONE}"
NYX_MODULES_REQUIRED:pinephonepro = "${NYX_MODULES_REQUIRED_PINEPHONE}"

# The x86 emulators have no ALS, haptics or LED. TOUCHPANEL is now the same
# module everywhere - it decides evdev vs mtdev per panel.
NYX_MODULES_REQUIRED_QEMU_X86 = "\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL;\
"
NYX_MODULES_REQUIRED:qemux86    = "${NYX_MODULES_REQUIRED_QEMU_X86}"
NYX_MODULES_REQUIRED:qemux86-64 = "${NYX_MODULES_REQUIRED_QEMU_X86}"

# qemuarm had almost nothing in its cmake file - no battery, charger, keys or
# touchpanel of any kind - so it gets only what the class default gave it.
NYX_MODULES_REQUIRED:qemuarm = "\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
"

NYX_MODULES_REQUIRED_NO_ALS = "\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_HAPTICS;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_LED;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL;\
"
NYX_MODULES_REQUIRED:pinetab2 = "${NYX_MODULES_REQUIRED_NO_ALS}"

# The A3 2015's cm36652 light/proximity combo has no mainline driver, so the ALS
# module would only log NYX_OPEN_ERR every boot.
NYX_MODULES_REQUIRED:a3-2015 = "${NYX_MODULES_REQUIRED_NO_ALS}"
