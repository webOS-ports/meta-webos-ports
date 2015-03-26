#!/bin/sh

eval `/usr/bin/dbus-launch --sh-syntax`

systemctl set-environment DBUS_SESSION_BUS_ADDRESS=${DBUS_SESSION_BUS_ADDRESS}
systemctl set-environment DBUS_SESSION_US_PID=${DBUS_SESSION_BUS_PID}
