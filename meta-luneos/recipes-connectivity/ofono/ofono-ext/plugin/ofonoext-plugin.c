/*
 * Loader for the oFono extension library.
 *
 * libofonoext.so holds the slot manager, watch, cell-info and sim-info
 * implementations. This plugin exists only to start and stop them: oFono
 * dlopen()s plugins with RTLD_NOW rather than RTLD_GLOBAL, so one plugin
 * cannot resolve symbols from another. Both this plugin and the binder
 * driver link the shared library instead, and the dynamic linker resolves
 * it once.
 */
#define OFONO_API_SUBJECT_TO_CHANGE
#include <ofono/plugin.h>
#include <ofono/log.h>

extern int __ofono_slot_manager_init(void);
extern void __ofono_slot_manager_cleanup(void);

static int ofonoext_init(void)
{
	DBG("starting oFono extensions");
	return __ofono_slot_manager_init();
}

static void ofonoext_exit(void)
{
	DBG("stopping oFono extensions");
	__ofono_slot_manager_cleanup();
}

OFONO_PLUGIN_DEFINE(ofonoext, "oFono extension APIs", VERSION,
			OFONO_PLUGIN_PRIORITY_HIGH,
			ofonoext_init, ofonoext_exit)
