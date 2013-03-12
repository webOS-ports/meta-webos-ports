#!/bin/bash

PERSISTENT_DIR="/OE/webos/owub/build-webos/cache"
if [ $# -eq 1 ]; then
  PERSISTENT_DIR=$1
fi

CACHE=${PERSISTENT_DIR}/bb_persist_data.sqlite3
for FILE in `git grep "\(^WEBOS_GIT_TAG\)\|\(^SRC_URI.*git://.*tag=\)\|\(^# corresponds to tag \)" | sed 's/:.*//g' | sort -u | xargs`; do
  PN=`basename $FILE | sed 's/\.bb//; s/\.inc//;'`;
  PV=`grep "^PV *=" ${FILE} | sed 's/^[^"]*"\([^"]*\)".*/\1/g'`
  SUBMISSION=`echo ${PV} | sed 's/.*-//g'`
  
  if [ "${PN}" == "yajl_1.0.12" ] ; then
    PN="yajl"
    PV="1.0.12"
    SUBMISSION="1.0.12"
  fi
  if [ -z "${PV}" ] ; then
    echo "Undefined PV in ${FILE}";
    continue
  fi
  if [ -z "${SUBMISSION}" ] ; then
    echo "Undefined SUBMISSION in ${FILE}";
    continue
  fi
  TAG="submissions/${SUBMISSION}"
  if [ "${PN}" == "isis-fonts" ] ; then
    TAG="v${SUBMISSION}"
  fi
  KEY="${TAG}-${PN}"
  SRCREV=`sqlite3 ${CACHE} "select value from BB_URI_HEADREVS where key like '%${KEY}'"`;
  if [ -z "${SRCREV}" -a "${PN}" == "nyx-utils" -a "${PV}" == "1.0.0~rc1-1" ] ; then
    echo "nyx-utils using hardcoded SRCREV"
    SRCREV=9331e4447db70d306d0c9e29c843c6520d4fe731
  fi
  if [ -z "${SRCREV}" ] ; then
    TAG="${SUBMISSION}"
    KEY="${TAG}-${PN}"
    SRCREV=`sqlite3 ${CACHE} "select value from BB_URI_HEADREVS where key like '%${KEY}'"`;
    if [ -z "${SRCREV}" ] ; then
      echo "Undefined SRCREV for ${FILE}, key ${KEY} possible values:";
      sqlite3 ${CACHE} "select * from BB_URI_HEADREVS where key like '%${PN}%'"
      continue
    fi
  fi
  if ! grep -q "^SRCREV *" $FILE; then
    echo "moving from tag ${TAG} to SRCREV ${SRCREV} to file ${FILE}"
    # /^WEBOS_GIT_TAG/d; keep this to find recipes with tags
    sed -i 's@;tag=${WEBOS_GIT_TAG}@@g; s@;tag=${PV}@@g;' $FILE;
    sed -i "s@^PV *=@# corresponds to tag ${TAG}\nSRCREV = \"${SRCREV}\"\nPV =@g" $FILE;
    if ! grep -q "^PV *=" $FILE; then
      echo "No PV in $FILE to anchor SRCREV, using SRC_URI"
      sed -i "s@^SRC_URI *=@# corresponds to tag ${TAG}\nSRCREV = \"${SRCREV}\"\nSRC_URI =@g" $FILE;
    fi
  else
    # update if changed
    sed -i "s@^# corresponds to tag .*@# corresponds to tag ${TAG}@g;
            s@^SRCREV *=.*@SRCREV = \"${SRCREV}\"@g" $FILE;
  fi
done

# No tags in *_COMPLETE variables
sed -i 's/;tag=${WEBOS_GIT_TAG}//g' conf/distro/include/*-repositories.inc

exit 0

# older version:
for i in `git grep inherit.*webos_.*submissions | grep bb: | sed 's/:.*//g' | xargs`; do 
  PN=`basename $i | sed 's/\.bb//'`; echo $PN; 
  VER=`grep "PREFERRED_VERSION_${PN} " conf/distro/include/webos-preferred-versions.inc | sed 's/^[^"]*"/PV = "/'`; 
  REV=`sqlite3 ${CACHE} "select value from BB_URI_HEADREVS where key like '%${PN}submissions/%'"`; 
  echo $VER >> $i; 
  echo "SRCREV = \"${REV}\"" >> $i; 
done

for i in `git grep inherit.*webos_.*submissions | grep bb: | sed 's/:.*//g' | xargs`; do 
  sed -i '/^WEBOS_GIT_TAG/d; s/;tag=${WEBOS_GIT_TAG}//g; s/;protocol=git//g; /^inherit *webos_enhanced_submissions$/d' $i; 
done

vi `git grep inherit.*webos_.*submissions | grep bb: | sed 's/:.*//g' | xargs`

for i in `git grep inherit.*webos_.*submissions | grep bb: | sed 's/:.*//g' | xargs`; do
  PN=`basename $i | sed 's/\.bb//'`; echo $PN;
  VER=`grep "PREFERRED_VERSION_${PN} " conf/distro/include/webos-preferred-versions.inc | sed 's/^[^"]*"//g; s/"//g'`;
  REV=`sqlite3 ${CACHE} "select value from BB_URI_HEADREVS where key like '%-${PN}'"`;
  sed -i '/^WEBOS_GIT_TAG/d; 
         s/;tag=${WEBOS_GIT_TAG}//g; 
         s/;protocol=git//g; 
         /^inherit *webos_enhanced_submissions *$/d;
         /^inherit *webos_submissions *$/d' $i;
  sed -i "s/^SRC_URI *=/PV = \"${VER}\"\nSRCREV = \"${REV}\"\nSRC_URI =/g" $i;
done
