#!/bin/sh
# This script tries to automatically configure UIM application for Qualcomm QMI
# modems with more than one sim slot.

name="UIM Slot selection"
description="Select UIM slot and application on the embedded QMI nodem."

sim_wait_time=1

eend() {
	echo $*
}
ewarn() {
	echo $*
}
veinfo() {
	echo $*
}

depend() {
	need rmtfs
	before ofono
	before modemmanager
}

# All of the logic is placed in the service start method as we want to block
# other services while the modem isn't ready yet.
start() {
	case "$(cat /sys/devices/soc0/machine)" in
	APQ*)
		eend 0 'Skipping SIM configuration on APQ SoC.'
		return 0
	esac

	# libqmi must be present to use this script.
	if ! [ -x "$(command -v qmicli)" ]
	then
		eend 1 'qmicli is not installed.'
		return 1
	fi

	# Prepare a qmicli command with desired modem path.
	# The modem may appear after some delay, wait for it.
	count=0
	while [ -z "$QMICLI_MODEM" ] && [ "$count" -lt "45" ]
	do
		# Check if legacy rpmsg exported device exists.
		if [ -e "/dev/modem" ]
		then
			QMICLI_MODEM="qmicli --silent -d /dev/modem"
			veinfo "Using /dev/modem"
		# Check if the qmi device from wwan driver exists.
		elif [ -e "/dev/wwan0qmi0" ]
		then
			# Using --device-open-qmi flag as we may have libqmi
			# version that can't automatically detect the type yet.
			QMICLI_MODEM="qmicli --silent -d /dev/wwan0qmi0 --device-open-qmi"
			veinfo "Using /dev/wwan0qmi0"
		# Check if QRTR is available for new devices.
		elif qmicli --silent -pd qrtr://0 --uim-noop > /dev/null
		then
			QMICLI_MODEM="qmicli --silent -pd qrtr://0"
			veinfo "Using qrtr://0"
		fi
		sleep 1
		count=$((count+1))
	done
	veinfo "Waited $count seconds for modem device to appear"

	if [ -z "$QMICLI_MODEM" ]
	then
		eend 2 'No modem available.'
		return 2
	fi

	QMI_CARDS=$($QMICLI_MODEM --uim-get-card-status)

	# Fail if all slots are empty but wait a bit for the sim to appear.
	count=0
	while ! printf "%s" "$QMI_CARDS" | grep -Fq "Card state: 'present'"
	do
		if [ "$count" -ge "$sim_wait_time" ]
		then
			eend 4 "No sim detected after $sim_wait_time seconds."
			return 4
		fi

		sleep 1
		count=$((count+1))
		QMI_CARDS=$($QMICLI_MODEM --uim-get-card-status)
	done
	veinfo "Waited $count seconds for modem to come up"

	# Clear the selected application in case the modem is in a bugged state
	if ! printf "%s" "$QMI_CARDS" | grep -Fq "Primary GW:   session doesn't exist"
	then
		ewarn 'Application was already selected.'
		$QMICLI_MODEM --uim-change-provisioning-session='activate=no,session-type=primary-gw-provisioning' > /dev/null
        fi

	# Extract first available slot number and AID for usim application
	# on it. This should select proper slot out of two if only one UIM is
	# present or select the first one if both slots have UIM's in them.
	FIRST_PRESENT_SLOT=$(printf "%s" "$QMI_CARDS" | grep "Card state: 'present'" -m1 -B1 | head -n1 | cut -c7-7)
	FIRST_PRESENT_AID=$(printf "%s" "$QMI_CARDS" | grep "usim (2)" -m1 -A3 | tail -n1 | awk '{print $1}')

	veinfo "Selecting $FIRST_PRESENT_AID on slot $FIRST_PRESENT_SLOT"

	# Finally send the new configuration to the modem.
	$QMICLI_MODEM --uim-change-provisioning-session="slot=$FIRST_PRESENT_SLOT,activate=yes,session-type=primary-gw-provisioning,aid=$FIRST_PRESENT_AID" > /dev/null
	eend $?
	
	# if needed, create the rmnet_data[012] QMUX links
	if [ $(ip link show type rmnet | grep rmnet_data | wc -l) -lt 3 ]; then
		ip link add link rmnet_ipa0 name rmnet_data0 type rmnet mux_id 1
		ip link add link rmnet_ipa0 name rmnet_data1 type rmnet mux_id 2
		ip link add link rmnet_ipa0 name rmnet_data2 type rmnet mux_id 3
	fi
}

start
