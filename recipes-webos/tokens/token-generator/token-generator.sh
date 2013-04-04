#!/bin/sh

if [ ! -d /dev/tokens ] ; then
    mkdir -p /dev/tokens
fi

for token in WIFIoADDR BToADDR; do
    touch /dev/tokens/${token}
done

