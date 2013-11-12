#!/bin/sh
timeout=10
# loop 10 times and then exit, if rild isnt up after 80 sec
# it is likely not starting at all (or not there)
while [ ! -e /dev/socket/rild ]; do
	sleep 8
	if [ "$timeout" -le 0 ]; then
		stop
		exit 0
	fi
	timeout=$(($timeout - 1))
done
sleep 5
