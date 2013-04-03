# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

PRINC := "${@int(PRINC) + 1}"

EXTRA_OECONF += "--disable-selinux \
                 --with-cluster=none \
                "
