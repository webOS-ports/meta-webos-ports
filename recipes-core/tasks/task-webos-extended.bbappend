PRINC := "${@int(PRINC) + 1}"

# Until upstream provides internal URLs for some components which are not accessible
# from outside we have limit the content of the image here.
RDEPENDS_${PN} = " \
	nyx-lib \
	cjson \
	pmloglib \
	luna-service2 \
	powerd \
	sleepd \
	storaged \
"
