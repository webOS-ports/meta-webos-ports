# Move xuser's id to 10000 as 1000-9999 is reserverd for Android system users
USERADD_PARAM_${PN}_prepend = "--uid 10000 "
