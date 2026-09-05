/*
 * Compatibility shim.
 *
 * The extension sources are taken from Sailfish's oFono tree, where they
 * are built inside the daemon and can include its private headers. Out of
 * tree they cannot, so this header re-declares exactly the internals they
 * use and maps the private helper names onto the public equivalents oFono
 * now exports.
 *
 * Keeping this in one file is deliberate: the diff against Sailfish's
 * sources stays reviewable, and everything this package assumes about
 * oFono's internals is visible in a single place.
 */

#ifndef OFONO_EXT_COMPAT_H
#define OFONO_EXT_COMPAT_H

#ifndef OFONO_API_SUBJECT_TO_CHANGE
#define OFONO_API_SUBJECT_TO_CHANGE
#endif

#include <ofono/types.h>
#include <ofono/log.h>
#include <ofono/modem.h>
#include <ofono/sim.h>
#include <ofono/netreg.h>
#include <ofono/gprs.h>
#include <ofono/voicecall.h>
#include <ofono/dbus.h>
#include <ofono/storage.h>
#include <ofono/misc.h>
#include <ofono/cell-info.h>

#include <glib.h>

/* --- storage: private helpers, reached through the public wrappers --- */
#define storage_open(imsi, store)		\
	ofono_storage_open(imsi, store)
#define storage_sync(imsi, store, kf)		\
	ofono_storage_sync(imsi, store, kf)
#define storage_close(imsi, store, kf, save)	\
	ofono_storage_close(imsi, store, kf, save)

/* --- src/common.h --- */
#define registration_status_to_string(s)	\
	ofono_netreg_status_to_string(s)

/* --- src/ofono.h: watchlist --- */
struct ofono_watchlist_item {
	unsigned int id;
	void *notify;
	void *notify_data;
	ofono_destroy_func destroy;
};

struct ofono_watchlist {
	unsigned int next_id;
	GSList *items;
	ofono_destroy_func destroy;
};

struct ofono_watchlist *__ofono_watchlist_new(ofono_destroy_func destroy);
unsigned int __ofono_watchlist_add_item(struct ofono_watchlist *watchlist,
					struct ofono_watchlist_item *item);
gboolean __ofono_watchlist_remove_item(struct ofono_watchlist *watchlist,
					unsigned int id);
void __ofono_watchlist_free(struct ofono_watchlist *watchlist);

/*
 * --- src/ofono.h: atom identifiers ---
 *
 * This mirrors a private enum. The ordinals are passed to
 * __ofono_modem_add_atom_watch(), so a reordering upstream would break
 * this package silently. It is checked at build time; see check-abi.
 */
enum ofono_atom_type {
	OFONO_ATOM_TYPE_DEVINFO,
	OFONO_ATOM_TYPE_CALL_BARRING,
	OFONO_ATOM_TYPE_CALL_FORWARDING,
	OFONO_ATOM_TYPE_CALL_METER,
	OFONO_ATOM_TYPE_CALL_SETTINGS,
	OFONO_ATOM_TYPE_NETREG,
	OFONO_ATOM_TYPE_PHONEBOOK,
	OFONO_ATOM_TYPE_SMS,
	OFONO_ATOM_TYPE_SIM,
	OFONO_ATOM_TYPE_USSD,
	OFONO_ATOM_TYPE_VOICECALL,
	OFONO_ATOM_TYPE_HISTORY,
	OFONO_ATOM_TYPE_SSN,
	OFONO_ATOM_TYPE_MESSAGE_WAITING,
	OFONO_ATOM_TYPE_CBS,
	OFONO_ATOM_TYPE_CALL_VOLUME,
	OFONO_ATOM_TYPE_GPRS,
	OFONO_ATOM_TYPE_GPRS_CONTEXT,
	OFONO_ATOM_TYPE_RADIO_SETTINGS,
	OFONO_ATOM_TYPE_AUDIO_SETTINGS,
	OFONO_ATOM_TYPE_STK,
	OFONO_ATOM_TYPE_NETTIME,
	OFONO_ATOM_TYPE_CTM,
	OFONO_ATOM_TYPE_CDMA_VOICECALL_MANAGER,
	OFONO_ATOM_TYPE_CDMA_CONNMAN,
	OFONO_ATOM_TYPE_SIM_AUTH,
	OFONO_ATOM_TYPE_EMULATOR_DUN,
	OFONO_ATOM_TYPE_EMULATOR_HFP,
	OFONO_ATOM_TYPE_LOCATION_REPORTING,
	OFONO_ATOM_TYPE_GNSS,
	OFONO_ATOM_TYPE_CDMA_SMS,
	OFONO_ATOM_TYPE_CDMA_NETREG,
	OFONO_ATOM_TYPE_HANDSFREE,
	OFONO_ATOM_TYPE_SIRI,
	OFONO_ATOM_TYPE_NETMON,
	OFONO_ATOM_TYPE_LTE,
	OFONO_ATOM_TYPE_IMS,
};

/* --- src/ofono.h: atom watch conditions --- */
enum ofono_atom_watch_condition {
	OFONO_ATOM_WATCH_CONDITION_REGISTERED,
	OFONO_ATOM_WATCH_CONDITION_UNREGISTERED
};

/* --- src/ofono.h: modem, atom and netreg observation --- */
struct ofono_atom;

typedef void (*ofono_modemwatch_cb_t)(struct ofono_modem *modem,
					gboolean added, void *data);
typedef void (*ofono_atom_watch_func)(struct ofono_atom *atom,
					enum ofono_atom_watch_condition cond,
					void *data);
typedef void (*ofono_modem_online_notify_func)(struct ofono_modem *modem,
					ofono_bool_t online, void *data);
typedef void (*ofono_netreg_status_notify_cb_t)(int status, int lac, int ci,
					int tech, const char *mcc,
					const char *mnc, void *data);

unsigned int __ofono_modemwatch_add(ofono_modemwatch_cb_t cb, void *user,
					ofono_destroy_func destroy);
gboolean __ofono_modemwatch_remove(unsigned int id);

unsigned int __ofono_modem_add_atom_watch(struct ofono_modem *modem,
					enum ofono_atom_type type,
					ofono_atom_watch_func notify,
					void *data,
					ofono_destroy_func destroy);
gboolean __ofono_modem_remove_atom_watch(struct ofono_modem *modem,
					unsigned int id);
void *__ofono_atom_get_data(struct ofono_atom *atom);

unsigned int __ofono_modem_add_online_watch(struct ofono_modem *modem,
					ofono_modem_online_notify_func notify,
					void *data,
					ofono_destroy_func destroy);
void __ofono_modem_remove_online_watch(struct ofono_modem *modem,
					unsigned int id);

unsigned int __ofono_netreg_add_status_watch(struct ofono_netreg *netreg,
					ofono_netreg_status_notify_cb_t cb,
					void *data,
					ofono_destroy_func destroy);
gboolean __ofono_netreg_remove_status_watch(struct ofono_netreg *netreg,
					unsigned int id);

/* --- src/ofono.h: D-Bus helpers --- */
void __ofono_dbus_pending_reply(DBusMessage **msg, DBusMessage *reply);
DBusMessage *__ofono_error_access_denied(DBusMessage *msg);
DBusMessage *__ofono_error_sim_not_ready(DBusMessage *msg);

/* --- src/dbus.c: error replies, already exported as __ofono_* --- */
#define OFONO_ERROR_INTERFACE "org.ofono.Error"
#define ofono_dbus_error_canceled(m)		__ofono_error_canceled(m)
#define ofono_dbus_error_invalid_args(m)	__ofono_error_invalid_args(m)
#define ofono_dbus_error_not_available(m)	__ofono_error_not_available(m)

DBusMessage *__ofono_error_canceled(DBusMessage *msg);
DBusMessage *__ofono_error_invalid_args(DBusMessage *msg);
DBusMessage *__ofono_error_not_available(DBusMessage *msg);
DBusMessage *__ofono_error_failed(DBusMessage *msg);

/* --- src/common.h: registration status constants --- */
#define NETWORK_REGISTRATION_STATUS_REGISTERED	OFONO_NETREG_STATUS_REGISTERED
#define NETWORK_REGISTRATION_STATUS_ROAMING	OFONO_NETREG_STATUS_ROAMING

/*
 * --- Sailfish's D-Bus access-control plugin ---
 *
 * sailfish_access is not part of this port. It layers per-method policy on
 * top of the D-Bus configuration; stock oFono has no such layer and relies
 * on /etc/dbus-1/system.d/ofono.conf alone, which is what we do too. The
 * checks below therefore always allow, matching stock oFono rather than
 * silently tightening or loosening anything.
 */
#define OFONO_DBUS_ACCESS_INTF_SIMINFO			0
#define OFONO_DBUS_ACCESS_SIMINFO_SET_CARD_LABEL	0
#define ofono_dbus_access_method_allowed(sender, intf, method, arg) (TRUE)

#endif /* OFONO_EXT_COMPAT_H */
