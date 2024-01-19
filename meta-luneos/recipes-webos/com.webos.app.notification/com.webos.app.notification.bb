inherit webos_enactjs_app
inherit webos_public_repo
inherit webos_enhanced_submissions

WEBOS_ENACTJS_APP_ID = "com.webos.app.notification"

# Workaround for network access issue during do_compile task
do_compile[network] = "1"
