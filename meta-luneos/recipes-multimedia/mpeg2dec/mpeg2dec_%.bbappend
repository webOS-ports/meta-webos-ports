# Copyright (c) 2022-2024 LG Electronics, Inc.

# ERROR: mpeg2dec-0.5.1-r0 do_package_qa: QA Issue: ELF binary '/home/jenkins/workspace/luneos-unstable/webos-ports/tmp-glibc/work/cortexa8t2hf-neon-webos-linux-gnueabi/mpeg2dec/0.5.1-r0/packages-split/libmpeg2/usr/lib/libmpeg2.so.0.1.0' has relocations in .text [textrel]
INSANE_SKIP:libmpeg2 += "textrel"

# Remove an executable flag in GNU_STACK
CFLAGS += "-Wa,--noexecstack"
