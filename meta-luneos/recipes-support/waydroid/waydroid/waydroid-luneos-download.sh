# First start: the Android download, asked about before it starts and shown
# while it runs. Sourced by waydroid-luneos-app, not executed.
#
# "waydroid init" fetches the Android system and vendor images from the OTA
# channel - on arm64 that is about 950 MB to download and 2 GB once unpacked -
# and it used to do so the moment the icon was tapped, with nothing on screen
# for the ten minutes or more it takes. Now:
#
#   1. a tap asks Waydroid what it would download ("waydroid init --plan"),
#      and puts that to the user as an alert: the download size, the storage
#      it needs while it installs, and whether that is over mobile data
#   2. "Download" relaunches Waydroid with {"download": "accept"}, which starts
#      waydroid-luneos-init.service; luna-downloadmgr does the transfer, and
#      the notification list carries its progress (tools/helpers/luna.py)
#   3. a tap while it runs - on the icon or on that notification - says how far
#      it has got, with a button to stop it ({"download": "cancel"})
#
# The answer comes back as a relaunch because there is no other way for it to
# arrive: an alert's button is a luna call that the shell makes on its own
# behalf, and nothing tells whoever posted the alert that it was pressed.
# Relaunching with parameters is the call that reaches this script.
#
# Defines waydroid_luneos_first_start <SAM params>, which returns
#   0  Waydroid is initialised: carry on and show Android
#   1  it is not, and this launch is done - the user has been asked, or told
#      why it cannot go ahead, or the download was stopped

WAYDROID_WORK=${WAYDROID_WORK:-/var/lib/waydroid}
WAYDROID_RUN=${WAYDROID_RUN:-/run/waydroid-luneos}
WAYDROID_INIT_UNIT=waydroid-luneos-init.service
WAYDROID_APPMGR=luna://com.webos.applicationManager/launch

# What Waydroid itself means by initialised (initializer.is_initialized): a
# config and a rootfs. The config alone is not it - init writes that before it
# downloads anything, so a download that failed or was stopped leaves one
# behind with no images under it.
waydroid_initialised() {
    [ -f "$WAYDROID_WORK/waydroid.cfg" ] && [ -d "$WAYDROID_WORK/rootfs" ]
}

# Every payload is built by json.dumps, never pasted together: an error text
# from the download, or a path, can carry a quote.
#
#   _wd_json key value [key value ...]
#
# A value written as @<json> is inserted as JSON rather than as a string.
_wd_json() {
    python3 -I -c '
import json, sys
a = sys.argv[1:]
print(json.dumps({k: json.loads(v[1:]) if v.startswith("@") else v
                  for k, v in zip(a[::2], a[1::2])}))' "$@"
}

# One field of a JSON file, or nothing.
_wd_field() {
    python3 -I -c '
import json, sys
try:
    with open(sys.argv[1]) as f:
        v = json.load(f).get(sys.argv[2])
except (OSError, ValueError, AttributeError):
    v = None
print("" if v is None else v)' "$1" "$2"
}

_wd_luna() { luna-send -n 1 "luna://$1" "$2" 2>/dev/null; }

# _wd_toast <message> [light]
_wd_toast() {
    _wd_luna com.webos.notification/createToast "$(_wd_json \
        sourceId Waydroid title Waydroid message "$1" type "${2:-standard}")" \
        >/dev/null
}

# A button that relaunches Waydroid with {"download": <answer>}.
_wd_button() {
    _wd_json label "$1" buttonType "$2" onclick "$WAYDROID_APPMGR" \
        params "@$(_wd_json id Waydroid params "@$(_wd_json download "$3")")"
}

# _wd_alert <title> <message> <buttons as a JSON array>
#
# One alert at a time: tapping the icon twice would otherwise stack two of the
# same question, so whatever this posted last is taken away first.
_wd_alert() {
    _id=$(cat "$WAYDROID_RUN/alert" 2>/dev/null)
    [ -n "$_id" ] && _wd_luna com.webos.notification/closeAlert \
        "$(_wd_json alertId "$_id")" >/dev/null
    _r=$(_wd_luna com.webos.notification/createAlert "$(_wd_json \
        sourceId Waydroid title "$1" message "$2" buttons "@$3")")
    printf '%s' "$_r" | sed -n 's/.*"alertId" *: *"\([^"]*\)".*/\1/p' \
        > "$WAYDROID_RUN/alert"
}

# Puts the download to the user.
#
#   0  there is nothing to download (pre-installed images): go ahead
#   1  the user has been asked, or told why it cannot happen
_wd_ask() {
    _plan=$WAYDROID_RUN/plan.json
    rm -f "$_plan"
    # Working this out takes a while on a phone - a Python start, the two
    # channels and a look inside each zip - so say something is happening.
    _wd_toast "Checking what Android needs to download" light
    if ! /usr/bin/waydroid init --plan "$_plan" >/dev/null 2>&1 ||
            [ ! -s "$_plan" ]; then
        log "could not work out the download"
        _wd_toast "Waydroid downloads Android the first time it starts. Connect to the internet and try again."
        return 1
    fi

    # The plan and the network status, turned into what the alert says.
    # Printed as shell assignments, each value quoted by shlex.
    _net=$(_wd_luna com.palm.connectionmanager/getStatus '{}')
    eval "$(python3 -I - "$_plan" "$_net" <<'EOF'
import json, shlex, sys

def size(n):
    if n >= 10**9:
        return "{:.1f} GB".format(n / 10**9)
    return "{} MB".format(max(1, round(n / 10**6)))

with open(sys.argv[1]) as f:
    plan = json.load(f)
try:
    net = json.loads(sys.argv[2])
except ValueError:
    net = {}
known = all(f.get("unpacked") is not None for f in plan["files"])
space = plan["space"]
worst = max(space, key=lambda s: s["needed"]) if space else None
short = [s for s in space if s["needed"] > s["free"]]
mobile = (net.get("wan", {}).get("connected") is True and
          net.get("wifi", {}).get("state") != "connected")
out = {
    "WD_BYTES": str(plan["download"]),
    "WD_SIZE": size(plan["download"]),
    "WD_NEED": size(worst["needed"]) if worst and known else "",
    "WD_SHORT": ("{} free in {}, {} needed".format(
        size(short[0]["free"]), short[0]["path"], size(short[0]["needed"]))
        if short else ""),
    "WD_MOBILE": "1" if mobile else "",
}
for k, v in out.items():
    print("{}={}".format(k, shlex.quote(v)))
EOF
)" || return 1

    [ "$WD_BYTES" -gt 0 ] 2>/dev/null || return 0

    if [ -n "$WD_SHORT" ]; then
        log "not enough room for Android: $WD_SHORT"
        _wd_toast "There is not enough room to install Android: $WD_SHORT."
        return 1
    fi

    _msg="Waydroid needs to download Android before it can start: $WD_SIZE."
    [ -n "$WD_NEED" ] &&
        _msg="$_msg Installing it takes about $WD_NEED of storage."
    [ -n "$WD_MOBILE" ] &&
        _msg="$_msg You are on mobile data, so this may cost you."
    log "asking before downloading $WD_SIZE"
    _wd_alert "Download Android?" "$_msg" "[$(_wd_button Download ok accept), \
$(_wd_json label 'Not now' buttonType cancel)]"
    return 1
}

# Where a download that is already running has got.
_wd_status() {
    _s=$WAYDROID_RUN/download.json
    case "$(_wd_field "$_s" state)" in
        extracting) _msg="Android has been downloaded and is being installed." ;;
        downloading)
            _total=$(_wd_field "$_s" total)
            _msg="Downloading Android: $(_wd_field "$_s" percent)% of $(
                python3 -I -c 'import sys; n = int(sys.argv[1] or 0)
print("{:.1f} GB".format(n / 10**9) if n >= 10**9 else
      "{} MB".format(max(1, round(n / 10**6))))' "$_total")." ;;
        *) _msg="Getting Android ready." ;;
    esac
    # Once it is unpacking there is no download left to stop.
    if [ "$(_wd_field "$_s" state)" = extracting ]; then
        _wd_alert "Waydroid" "$_msg" "[$(_wd_json label OK buttonType ok)]"
        return
    fi
    _wd_alert "Waydroid" "$_msg" "[$(_wd_button 'Stop download' cancel cancel), \
$(_wd_json label 'Keep going' buttonType ok)]"
}

waydroid_luneos_first_start() {
    waydroid_initialised && return 0
    mkdir -p "$WAYDROID_RUN"

    _answer=$(printf '%s' "${1:-}" |
        sed -n 's/.*"download" *: *"\([a-z]*\)".*/\1/p')
    _running=""
    [ "$(systemctl show -p ActiveState --value "$WAYDROID_INIT_UNIT" \
        2>/dev/null)" = activating ] && _running=1

    if [ "$_answer" = cancel ]; then
        if [ -n "$_running" ]; then
            log "stopping the Android download"
            # The SIGTERM makes "waydroid init" cancel its downloadmgr ticket.
            systemctl stop "$WAYDROID_INIT_UNIT"
            _wd_toast "Stopped downloading Android."
        fi
        return 1
    fi

    if [ -n "$_running" ]; then
        # Started by an earlier launch, which SAM may since have ended. Say
        # how far it is, then wait for it like the launch that started it:
        # starting a oneshot that is already starting joins that job.
        _wd_status
    elif [ "$_answer" != accept ]; then
        # Only one plan at a time: a second tap while the first is still
        # working it out would ask twice.
        exec 9> "$WAYDROID_RUN/plan.lock"
        flock -n 9 || return 1
        _wd_ask || return 1
        exec 9>&-
        # Nothing to download - pre-installed images. Straight to init.
    fi

    # A state file left by an earlier run would otherwise be read as this
    # run's outcome when this one fails before the download starts.
    [ -n "$_running" ] || rm -f "$WAYDROID_RUN/download.json"
    log "installing Android through $WAYDROID_INIT_UNIT"
    if systemctl start "$WAYDROID_INIT_UNIT" && waydroid_initialised; then
        log "initialised"
        return 0
    fi

    _err=$(_wd_field "$WAYDROID_RUN/download.json" error)
    log "initialisation failed${_err:+: $_err}"
    case "$_err" in
        # Stopped on purpose; that launch has said so already.
        "Download stopped") ;;
        "") _wd_toast "Could not set up Waydroid. Check the internet connection and tap Waydroid to try again." ;;
        *) _wd_toast "Could not install Android: $_err. Tap Waydroid to try again." ;;
    esac
    return 1
}
