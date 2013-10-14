#!/bin/sh

TOKENS_DIR=/var/tokens
TOKENS_TARGET_DIR=/dev/tokens

if [ ! -d $TOKENS_DIR ] ; then
    mkdir -p $TOKENS_DIR
fi

for token in WIFIoADDR BToADDR; do
    if [ ! -e $TOKENS_DIR/$token ] ; then
        touch $TOKENS_DIR/$token
    fi
done

if [ ! -e $TOKENS_DIR/nduid ] ; then
    nduid=`dd if=/dev/urandom bs=1024 count=1 | md5sum | cut -d' ' -f 1`
    echo $nduid > $TOKENS_DIR/nduid
fi

mkdir -p $TOKENS_TARGET_DIR
mount -o bind,ro $TOKENS_DIR $TOKENS_TARGET_DIR
