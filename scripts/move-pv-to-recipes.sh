#!/bin/bash

INC_FILE=conf/distro/include/*-preferred-versions.inc
grep ^PREFERRED_VERSION ${INC_FILE} | while read LINE; do
  PN=`echo $LINE | sed 's/^PREFERRED_VERSION_\([^ ]*\) *?*= *"\([^"]*\)".*/\1/g'`
  PV=`echo $LINE | sed 's/^PREFERRED_VERSION_\([^ ]*\) *?*= *"\([^"]*\)".*/\2/g'`
  PATTERN="${PN}.bb"
  PATTERN_PR="PR"
  if [ "${PN}" == "qt4-webos" ] ; then
    # SRC_URI is in qt4-webos.inc
    PATTERN="${PN}.inc"
    PATTERN_PR="INC_PR"
  fi
  if [ "${PN}" == "qmake-webos-native" ] ; then
    # done in qt4-webos.inc
    sed -i "/^PREFERRED_VERSION_${PN}  */d" $INC_FILE;
    continue
  fi

  if [ "${PN}" == "pmloglib" ] ; then
    PV_PMLOGLIB=${PV}
  fi
  if [ "${PN}" == "pmloglib-private" -a "${PV}" == '${PREFERRED_VERSION_pmloglib}' ] ; then
    PV=${PV_PMLOGLIB}
  fi

  FILE=`find . -name ${PATTERN}`
  if [ -z ${FILE} ] ; then
    echo "file for ${PN} wasn't found"
  else
    if ! grep -q "^${PATTERN_PR} *=" ${FILE} ; then
      echo "file ${FILE} does not have '${PATTERN_PR}' field to add PV above it"
      continue
    fi
    echo "moving PV ${PV} to file ${FILE}"
    grep -q "^PV  *" $FILE && sed -i "s/^PV  *=.*/PV = \"${PV}\"/g" $FILE;
    grep -q "^PV  *" $FILE || sed -i "s/^${PATTERN_PR} *=/PV = \"${PV}\"\n${PATTERN_PR} =/g" $FILE;
    sed -i "/^PREFERRED_VERSION_${PN}  */d" $INC_FILE;
  fi
done 
