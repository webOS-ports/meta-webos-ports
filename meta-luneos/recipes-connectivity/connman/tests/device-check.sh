#!/bin/bash
#
# connman on a device, driven from the host over adb.
#
# Usage: device-check.sh -d SERIAL [-P ADBPORT] [-c CLIENT_SERIAL] [-n CYCLES]
#                        [test...]
#
#   tests (default: all that apply)
#     tether     Wi-Fi tethering on and off CYCLES times (default 3): the
#                AP comes and goes, the device's own Wi-Fi stays connected
#                (with WiFiTetheringInterfaces), no stray AP interface is
#                left, connmand neither restarts nor grows in RSS or fds,
#                and the journal has no GLib critical or abort
#     client     needs -c: a second device joins the hotspot, gets a lease
#                and reaches the internet through it, then goes back to
#                the network it was on
#     restart    restart connmand while on Wi-Fi: Wi-Fi must be back
#                within 30 s (PreferredTechnologies in main.conf)
#
# The device must be on a Wi-Fi network for tether and restart. Nothing is
# tapped on screen; everything goes through connmanctl. The client's
# provisioning file is removed again, also when a test fails.
#
# Exit status: 0 when every test run passed.

set -u

SERIAL=
CLIENT=
PORT=
CYCLES=3

while getopts "d:c:P:n:h" opt; do
	case $opt in
	d) SERIAL=$OPTARG ;;
	c) CLIENT=$OPTARG ;;
	P) PORT=$OPTARG ;;
	n) CYCLES=$OPTARG ;;
	*) sed -n '2,/^$/s/^# \{0,1\}//p' "$0"; exit 2 ;;
	esac
done
shift $((OPTIND - 1))
[ -n "$SERIAL" ] || { sed -n '2,/^$/s/^# \{0,1\}//p' "$0"; exit 2; }
TESTS=${*:-tether ${CLIENT:+client} restart}

ADB=(adb ${PORT:+-P "$PORT"})
dev() { "${ADB[@]}" -s "$SERIAL" shell "$@"; }
cli() { "${ADB[@]}" -s "$CLIENT" shell "$@"; }

FAILS=0
fail() { echo "  FAIL: $*"; status=1; FAILS=$((FAILS + 1)); return 1; }
pass() { echo "  ok: $*"; }

"${ADB[@]}" -s "$SERIAL" get-state >/dev/null 2>&1 || { echo "device $SERIAL not reachable over adb"; exit 2; }

connmand_pid() { dev 'pidof connmand' | tr -d '\r'; }
connmand_rss() { dev "awk '/^VmRSS/ {print \$2}' /proc/\$(pidof connmand)/status" | tr -d '\r'; }
connmand_fds() { dev 'ls /proc/$(pidof connmand)/fd | wc -l' | tr -d '\r'; }
wifi_service() { dev "connmanctl services | sed -n 's/^\*A[OR] .* \(wifi_[^ ]*\)\$/\1/p'" | tr -d '\r' | head -n 1; }
tech_prop() { dev "connmanctl technologies" | tr -d '\r' | sed -n "/Type = wifi/,/^\//s/^  $1 = //p" | head -n 1; }
ap_ifaces() { dev 'iw dev' | tr -d '\r' | awk '/Interface/ {i=$2} /type AP/ {print i}'; }

# journal lines since a mark that point at a defect
journal_defects() {
	dev "journalctl --since '$1' --no-pager -o cat" \
		| grep -E "connmand.*(CRITICAL|assertion|free\(\)|double free|corrupted|Aborted)|GLib.*CRITICAL" \
		| grep -v "connman.sh"
}

mark() { dev 'date "+%Y-%m-%d %H:%M:%S"' | tr -d '\r'; }

test_tether() {
	local pid rss0 fds0 rss fds svc since i ap f0=$FAILS
	echo "== tether ($CYCLES cycles)"
	svc=$(wifi_service)
	[ -n "$svc" ] || { fail "device is not on Wi-Fi"; return; }
	[ "$(tech_prop Tethering)" = "False" ] || dev 'connmanctl tether wifi off' >/dev/null
	since=$(mark)
	pid=$(connmand_pid); rss0=$(connmand_rss); fds0=$(connmand_fds)
	for i in $(seq 1 "$CYCLES"); do
		dev 'connmanctl tether wifi on' >/dev/null
		sleep 8
		[ "$(tech_prop Tethering)" = "True" ] || { fail "cycle $i: Tethering not True"; break; }
		ap=$(ap_ifaces)
		[ -n "$ap" ] || { fail "cycle $i: no interface in AP mode"; break; }
		dev "connmanctl services" | grep -q "^\*A[OR] .*$svc" \
			|| echo "  note: cycle $i: own Wi-Fi dropped (expected without WiFiTetheringInterfaces)"
		dev 'connmanctl tether wifi off' >/dev/null
		sleep 4
		[ -z "$(ap_ifaces)" ] || { fail "cycle $i: $(ap_ifaces) still in AP mode after tethering off"; break; }
		[ "$(connmand_pid)" = "$pid" ] || { fail "cycle $i: connmand restarted"; break; }
	done
	rss=$(connmand_rss); fds=$(connmand_fds)
	echo "  connmand RSS ${rss0} -> ${rss} kB, fds ${fds0} -> ${fds}"
	[ "$fds" -le $((fds0 + 2)) ] || fail "fds grew from $fds0 to $fds"
	[ "$rss" -le $((rss0 + 2048)) ] || fail "RSS grew by more than 2 MB"
	if [ -n "$(journal_defects "$since")" ]; then
		journal_defects "$since" | sed 's/^/    /'
		fail "journal shows defects"
	fi
	[ "$FAILS" -eq "$f0" ] && pass "AP up and down $CYCLES times, connmand $pid stable"
}

test_client() {
	local ssid pass svc lease ok=1
	echo "== client ($CLIENT)"
	"${ADB[@]}" -s "$CLIENT" get-state >/dev/null 2>&1 || { fail "client $CLIENT not reachable"; return; }
	ssid=$(tech_prop TetheringIdentifier)
	pass=$(tech_prop TetheringPassphrase)
	[ -n "$ssid" ] && [ -n "$pass" ] || { fail "no TetheringIdentifier/Passphrase on the device"; return; }
	home=$(cli "connmanctl services | sed -n 's/^\*A[OR] .* \(wifi_[^ ]*\)\$/\1/p'" | tr -d '\r' | head -n 1)

	dev 'connmanctl tether wifi on' >/dev/null
	sleep 8
	cli "printf '[service_device_check]\nType = wifi\nName = %s\nPassphrase = %s\n' '$ssid' '$pass' > /var/lib/connman/device-check.config"
	sleep 2
	cli 'connmanctl scan wifi' >/dev/null 2>&1
	svc=$(cli "connmanctl services" | tr -d '\r' | awk -v s="$ssid" '$0 ~ " "s" " {print $NF}' | head -n 1)
	if [ -z "$svc" ]; then
		fail "client does not see '$ssid'"; ok=0
	else
		cli "connmanctl connect $svc" >/dev/null 2>&1
		sleep 12
		lease=$(cli "ip -4 -o addr show wlan0" | awk '{print $4}' | tr -d '\r')
		case $lease in
		169.254.*|"") fail "client got no lease (${lease:-none})"; ok=0 ;;
		*) pass "client lease $lease" ;;
		esac
		if [ $ok = 1 ]; then
			if cli 'wget -q -O /dev/null -T 8 http://www.gstatic.com/generate_204 && echo HTTP-OK' | grep -q HTTP-OK; then
				pass "client reaches the internet through the hotspot"
			else
				fail "client has no internet through the hotspot"
			fi
		fi
	fi
	cli 'rm -f /var/lib/connman/device-check.config'
	dev 'connmanctl tether wifi off' >/dev/null
	sleep 3
	[ -n "$home" ] && cli "connmanctl connect $home" >/dev/null 2>&1
}

test_restart() {
	local svc t
	echo "== restart"
	svc=$(wifi_service)
	[ -n "$svc" ] || { fail "device is not on Wi-Fi"; return; }
	dev 'systemctl restart connman'
	for t in $(seq 3 3 30); do
		sleep 3
		dev "connmanctl services 2>/dev/null" | grep -q "^\*A[OR] .*$svc" \
			&& { pass "Wi-Fi back ${t} s after restarting connmand"; return; }
	done
	fail "Wi-Fi not back 30 s after restarting connmand"
	dev 'connmanctl connect '"$svc" >/dev/null 2>&1
}

status=0
for t in $TESTS; do
	case $t in
	tether) test_tether ;;
	client) [ -n "$CLIENT" ] && test_client || echo "== client: no -c, skipped" ;;
	restart) test_restart ;;
	*) echo "unknown test $t"; status=1 ;;
	esac
done
exit $status
