#!/bin/sh

for i in `git grep PR_append | grep bbappend: | sed 's/:.*//g'`; do sed -i 's/^PR_append *= *"[^0-9]*\([0-9]*\)"/PRINC := "${@int(PRINC) + \1}"/g' $i; done
