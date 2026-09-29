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

# NYXMOD_OW_LED is on here for the same reason NYXMOD_OW_SYSTEM below is: the LED
# controller was unified into nyx-modules, which now probes the Android lights
# HAL at runtime instead of linking it, so it covers a halium device as well as a
# mainline one and there is nothing left for nyx-modules-hybris to own. Dropping
# it there was what let a device with an RGB LED in /sys/class/leds but no legacy
# lights.<board>.so - the Zinwa Q25, and every Android 12 or later base - reach
# its notification LED at all.
#
# NYXMOD_OW_SYSTEM is on here even though halium once took its system module from
# nyx-modules-hybris: that module moved into nyx-modules along with the wakeup
# alarm layer, which stopped being Android-specific when timerfd replaced
# /dev/alarm. The gate in nyx-modules-hybris is "if(NOT NYXMOD_OW_SYSTEM)", so
# this switch is what makes it stand down; both provide the same twelve nyx
# methods, so nothing above them can tell the difference.
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
NYX_MODULES_REQUIRED:athene            = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:hammerhead-halium = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:mako              = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
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

# The x86 emulators have no ALS, haptics or LED. Their touchpanel is the same
# module as everywhere else now: it used to be the plain evdev one, asked for as
# TOUCHPANEL where other machines asked for TOUCHPANEL_MTDEV, and the single
# module decides that per panel instead.
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

# The A3 2015 carries a cm36652 ambient light/proximity combo that has no
# mainline driver, so there is no IIO light channel for the ALS module to
# open and asking for it only produces an NYX_OPEN_ERR every boot.
NYX_MODULES_REQUIRED:a3-2015 = "${NYX_MODULES_REQUIRED_NO_ALS}"
