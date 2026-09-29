/*
 * kbdscroll - scroll, fling and move the text cursor by sliding a finger over a
 * secondary touch surface.
 *
 * A number of phones carry a touch-sensitive surface that is not the
 * touchscreen: the capacitive keyboard of a BlackBerry KEYone/KEY2/Passport, a
 * trackpad next to the keys, a navigation pad. The kernel exposes it as an
 * ordinary evdev device and nothing in a Wayland stack does anything with it.
 *
 * This daemon reads that surface and replays a one-finger slide as a finger
 * drag on the touchscreen, so every toolkit (Mojo, Enyo, QML, Chromium)
 * scrolls - and flings - exactly as it would under a real finger, with no
 * toolkit or compositor support at all. The injected contact uses a slot and a
 * tracking id of its own, so a finger on the glass is not disturbed. Typing
 * always wins: a key press during the slide, or just before it, cancels the
 * gesture.
 *
 * With the cursor modifier held (Alt by default) the same slide sends arrow
 * keys instead, which moves the text cursor a character or a line at a time.
 *
 * Nothing here is device-specific. The input devices are found by capability
 * and by name, every number is a tunable, and the defaults are derived from the
 * geometry of the hardware that was found rather than baked in. Per-machine
 * tuning belongs in a drop-in under /etc/kbdscroll.conf.d/ - see kbdscroll.conf.
 *
 *   kbdscroll --list      what was found, what was chosen, and why
 *   kbdscroll --help      every option with its default
 *
 * Porting to a new device: run --list. If the surface and the touchscreen were
 * told apart correctly there is usually nothing else to do; if they were not,
 * set "pad" and "screen" by name. Then check the direction with debug=1 and fix
 * the mounting with pad-rotate / pad-invert-x / pad-invert-y, and only then
 * touch scale and slop.
 *
 * Absolute surfaces - multitouch protocol B, and single-touch ABS_X/ABS_Y with
 * BTN_TOUCH - are the natural input here, because a stroke arrives with its own
 * beginning and end.
 *
 * A relative (mouse-like) pad is read too, and the reason is worth stating
 * because it was once excluded. The old argument was that the compositor turns
 * REL_X/REL_Y into a pointer, so such a device does not need this. That holds on
 * a desktop and not on a touch-first shell: webOS content scrolls and flings
 * under a finger, not under a cursor, which is the whole reason this program
 * injects touch instead of moving a pointer. A phone whose only pad is relative
 * - the BlackBerry Classic trackpad of a Zinwa Q25, which reports int8 deltas
 * and nothing else - therefore gets no scrolling at all without it.
 *
 * What a relative pad genuinely lacks is the gesture boundary, so it is
 * synthesised: the first movement after a period of stillness is the contact
 * coming down, and rel-lift-ms of stillness is it lifting. The deltas accumulate
 * into a virtual surface (rel-width/rel-height, by default the screen's size, so
 * one pad unit is one screen pixel at scale 1.0) and everything downstream -
 * slop, axis locking, scale, the fling, the cursor modifier - is the same code
 * the absolute path uses.
 *
 * Note it does NOT grab the device. On the Q25 the pad and the QWERTY are one
 * input node, so a grab would swallow typing; if a cursor also moves and fights
 * the injected finger, the fix belongs in the compositor's input configuration
 * rather than here.
 */
#define _GNU_SOURCE
#include <ctype.h>
#include <errno.h>
#include <fcntl.h>
#include <fnmatch.h>
#include <glob.h>
#include <limits.h>
#include <linux/input.h>
#include <linux/uinput.h>
#include <poll.h>
#include <signal.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ioctl.h>
#include <time.h>
#include <unistd.h>

#define SLOTS		10	/* pad contacts we track */
#define MAX_DEVS	64	/* /dev/input/event* we look at */
#define MAX_KEYDEVS	8	/* keyboards we watch at once */
#define INJ_ID		31337	/* tracking id of the contact we inject */
#define DRIVE_SLOP	25	/* pad units before a contact may steer the cursor */
#define EDGE_DIV	8	/* screen/EDGE_DIV from the edge: re-grab there */
#define SIDE_DIV	10	/* a sideways drag starts screen/SIDE_DIV in */
#define SIDE_STOP_DIV	20	/* ... and holds screen/SIDE_STOP_DIV from the edge */
#define WORD_ECHO_MS	150	/* ignore our own injected chord for this long */
#define UINPUT_SETTLE_MS 300	/* let udev/libinput notice a device we created */

/* ------------------------------------------------------------------ options */

enum otype { O_STR, O_INT, O_DBL };

static char *o_pad = "auto";
static char *o_keys = "auto";
static char *o_screen = "auto";
static char *o_arrows = "auto";
static char *o_screen_size = "";
static char *o_cursor_mod = "leftalt,rightalt";
static char *o_word_delete = "leftctrl+backspace";
static char *o_text_focus = "";
static char *o_backlight = "auto";
static int o_pad_rotate, o_invert_x, o_invert_y;
static double o_scale, o_scale_x, o_gain = 1.0, o_gain_x = 1.5;
static int o_slop, o_typing_ms = 300, o_horizontal = 1;
static int o_step_x, o_step_y;
static int o_rel_lift = 120, o_rel_w, o_rel_h;
static int o_debug;

static const struct {
	const char *key;
	enum otype type;
	void *slot;
	const char *help;
} opts[] = {
{ "pad",	    O_STR, &o_pad,	   "touch surface: device path, name glob, or auto" },
{ "screen",	    O_STR, &o_screen,	   "touchscreen to inject into: path, name glob, auto, or uinput" },
{ "keys",	    O_STR, &o_keys,	   "keyboards to watch, comma separated: paths, name globs, auto, or none" },
{ "arrows",	    O_STR, &o_arrows,	   "device to send arrow keys through: path, name glob, auto, uinput, or none" },
{ "screen-size",    O_STR, &o_screen_size, "WxH override for the injected coordinate space (default: the screen's own)" },
{ "cursor-modifier",O_STR, &o_cursor_mod,  "key(s) that turn a slide into cursor movement, or none" },
{ "word-delete",    O_STR, &o_word_delete, "chord sent by a right-to-left slide in a text field, or none" },
{ "text-focus-file",O_STR, &o_text_focus,  "file holding 1 while a text field has focus; empty disables word delete" },
{ "backlight",	    O_STR, &o_backlight,   "brightness file that says the display is on: path, auto, or none" },
{ "rel-lift-ms",    O_INT, &o_rel_lift,	   "relative pad: stillness that ends a stroke (ms)" },
{ "rel-width",	    O_INT, &o_rel_w,	   "relative pad: virtual surface width (0 = the screen's)" },
{ "rel-height",	    O_INT, &o_rel_h,	   "relative pad: virtual surface height (0 = the screen's)" },
{ "pad-rotate",	    O_INT, &o_pad_rotate,  "0|90|180|270: how the surface is mounted against the screen" },
{ "pad-invert-x",   O_INT, &o_invert_x,	   "1 to mirror the surface sideways (after pad-rotate)" },
{ "pad-invert-y",   O_INT, &o_invert_y,	   "1 to mirror the surface top to bottom (after pad-rotate)" },
{ "scale",	    O_DBL, &o_scale,	   "screen pixels per pad unit when scrolling (0: screen/pad * gain)" },
{ "scale-x",	    O_DBL, &o_scale_x,	   "the same for sideways drags (0: screen/pad * gain-x)" },
{ "gain",	    O_DBL, &o_gain,	   "multiplier behind an automatic scale" },
{ "gain-x",	    O_DBL, &o_gain_x,	   "multiplier behind an automatic scale-x" },
{ "slop",	    O_INT, &o_slop,	   "pad units before a touch counts as a slide (0: a tenth of the pad)" },
{ "typing-ms",	    O_INT, &o_typing_ms,   "quiet time a key press needs before a touch may scroll" },
{ "horizontal",	    O_INT, &o_horizontal,  "0 to ignore sideways slides" },
{ "cursor-step-x",  O_INT, &o_step_x,	   "pad units per Left/Right key (0: derived from the pad)" },
{ "cursor-step-y",  O_INT, &o_step_y,	   "pad units per Up/Down key (0: derived from the pad)" },
{ "debug",	    O_INT, &o_debug,	   "1 to log every contact and decision" },
};

#define dbg(...) do { if (o_debug) { fprintf(stderr, __VA_ARGS__); fflush(stderr); } } while (0)

static void note(const char *fmt, ...)
{
	va_list ap;
	va_start(ap, fmt);
	vfprintf(stderr, fmt, ap);
	va_end(ap);
	fflush(stderr);
}

/* Keys are written with '-' or '_' and matched either way. */
static void normalise(char *s)
{
	for (; *s; s++)
		if (*s == '_')
			*s = '-';
		else
			*s = tolower((unsigned char)*s);
}

static int opt_set(const char *key, const char *val, const char *where)
{
	char k[64];
	snprintf(k, sizeof k, "%s", key);
	normalise(k);

	for (size_t i = 0; i < sizeof opts / sizeof *opts; i++) {
		if (strcmp(k, opts[i].key))
			continue;
		switch (opts[i].type) {
		case O_STR: *(char **)opts[i].slot = strdup(val); break;
		case O_INT: *(int *)opts[i].slot = atoi(val); break;
		case O_DBL: *(double *)opts[i].slot = atof(val); break;
		}
		return 0;
	}
	note("kbdscroll: %s: unknown setting \"%s\"\n", where, key);
	return -1;
}

static char *trim(char *s)
{
	char *e;
	while (*s && isspace((unsigned char)*s))
		s++;
	e = s + strlen(s);
	while (e > s && isspace((unsigned char)e[-1]))
		*--e = 0;
	return s;
}

/* key = value, one per line; # and ; start a comment. */
static void read_conf(const char *path, int required)
{
	char line[512];
	FILE *f = fopen(path, "r");
	if (!f) {
		if (required)
			note("kbdscroll: %s: %s\n", path, strerror(errno));
		return;
	}
	dbg("reading %s\n", path);
	while (fgets(line, sizeof line, f)) {
		char *eq, *k, *v;
		char *p = strchr(line, '#');
		if (p) *p = 0;
		p = strchr(line, ';');
		if (p) *p = 0;
		if (!(eq = strchr(line, '=')))
			continue;
		*eq = 0;
		k = trim(line);
		v = trim(eq + 1);
		if (*k)
			opt_set(k, v, path);
	}
	fclose(f);
}

static void read_conf_dir(const char *dir)
{
	char pat[PATH_MAX];
	glob_t g;
	snprintf(pat, sizeof pat, "%s/*.conf", dir);
	if (glob(pat, GLOB_NOSORT, NULL, &g) == 0) {
		/* glob() sorts by default; GLOB_NOSORT plus our own pass keeps
		 * the ordering explicit - 10-foo.conf before 90-bar.conf. */
		for (size_t i = 0; i < g.gl_pathc; i++)
			for (size_t j = i + 1; j < g.gl_pathc; j++)
				if (strcmp(g.gl_pathv[i], g.gl_pathv[j]) > 0) {
					char *t = g.gl_pathv[i];
					g.gl_pathv[i] = g.gl_pathv[j];
					g.gl_pathv[j] = t;
				}
		for (size_t i = 0; i < g.gl_pathc; i++)
			read_conf(g.gl_pathv[i], 1);
	}
	globfree(&g);
}

/* KBDSCROLL_SCALE, KBDSCROLL_TEXT_FOCUS_FILE, ... - the option name upper
 * cased. The environment wins over the files, which is what makes a one-off
 * "KBDSCROLL_DEBUG=1 kbdscroll" work against a shipped configuration. */
/* The per-device profile, chosen at runtime rather than at build time.
 *
 * A machine that builds its own image can ship its profile as a drop-in and be
 * done. Most machines no longer do: q25, mp01 and every other Halium target
 * share the generic halium-arm64 rootfs, which by design carries nothing
 * device-specific and cannot know at build time which phone it will boot on. So
 * the profiles for every device ship together and the right one is selected here
 * by codename - the same arrangement luneos-device-config uses for its Tier 1
 * adaptations, and the same reason.
 *
 * Read after the drop-in directory so a hand-edited drop-in still wins.
 */
static void read_conf_codename(const char *dir)
{
	char line[256], cn[64] = "", path[512];
	FILE *f = fopen("/run/luneos-device/device.env", "r");

	if (!f)
		return;		/* no luneos-device-config on this image */
	while (fgets(line, sizeof line, f))
		if (sscanf(line, "LUNEOS_DEVICE_CODENAME=\"%63[^\"]\"", cn) == 1)
			break;
	fclose(f);
	if (!*cn)
		return;
	snprintf(path, sizeof path, "%s/%s.conf", dir, cn);
	if (!access(path, R_OK)) {
		read_conf(path, 0);
		return;
	}
	/* Codenames are not reliably lowercase - ro.product.vendor.device is
	 * "Q25" and "MP01" - while a profile is named after the machine, which
	 * conventionally is. Try that rather than shipping two copies. */
	for (char *p = cn; *p; p++)
		*p = tolower((unsigned char)*p);
	snprintf(path, sizeof path, "%s/%s.conf", dir, cn);
	if (!access(path, R_OK))
		read_conf(path, 0);
}

static void read_env(void)
{
	for (size_t i = 0; i < sizeof opts / sizeof *opts; i++) {
		char e[80] = "KBDSCROLL_", *v;
		size_t n = strlen(e);
		for (const char *p = opts[i].key; *p && n < sizeof e - 1; p++)
			e[n++] = *p == '-' ? '_' : toupper((unsigned char)*p);
		e[n] = 0;
		if ((v = getenv(e)))
			opt_set(opts[i].key, v, e);
	}
}

/* --------------------------------------------------------------- key names */

static const struct { const char *name; int code; } keynames[] = {
	{ "leftalt", KEY_LEFTALT }, { "rightalt", KEY_RIGHTALT },
	{ "alt", KEY_LEFTALT },
	{ "leftctrl", KEY_LEFTCTRL }, { "rightctrl", KEY_RIGHTCTRL },
	{ "ctrl", KEY_LEFTCTRL },
	{ "leftshift", KEY_LEFTSHIFT }, { "rightshift", KEY_RIGHTSHIFT },
	{ "shift", KEY_LEFTSHIFT },
	{ "leftmeta", KEY_LEFTMETA }, { "rightmeta", KEY_RIGHTMETA },
	{ "meta", KEY_LEFTMETA },
	{ "backspace", KEY_BACKSPACE }, { "delete", KEY_DELETE },
	{ "w", KEY_W }, { "h", KEY_H }, { "u", KEY_U },
	{ "left", KEY_LEFT }, { "right", KEY_RIGHT },
	{ "up", KEY_UP }, { "down", KEY_DOWN },
	{ "home", KEY_HOME }, { "end", KEY_END },
};

static int keycode(const char *name)
{
	for (size_t i = 0; i < sizeof keynames / sizeof *keynames; i++)
		if (!strcmp(name, keynames[i].name))
			return keynames[i].code;
	return -1;
}

/* "leftalt+backspace" / "leftalt,rightalt" -> codes. Returns how many. */
static int parse_keys(const char *spec, int *out, int max)
{
	char buf[128], *p, *save = NULL;
	int n = 0;
	if (!spec || !*spec || !strcmp(spec, "none"))
		return 0;
	snprintf(buf, sizeof buf, "%s", spec);
	normalise(buf);
	for (p = strtok_r(buf, "+, ", &save); p && n < max; p = strtok_r(NULL, "+, ", &save)) {
		int c = keycode(p);
		if (c < 0) {
			note("kbdscroll: unknown key name \"%s\"\n", p);
			continue;
		}
		out[n++] = c;
	}
	return n;
}

/* ------------------------------------------------------- input enumeration */

#define BPL		(int)(8 * sizeof(unsigned long))
#define NBITS(x)	(((x) - 1) / BPL + 1)
#define test_bit(b, a)	((a[(b) / BPL] >> ((b) % BPL)) & 1)

struct indev {
	char path[32];
	char name[128];
	int mt;			/* multitouch, protocol B */
	int st;			/* single touch: ABS_X/ABS_Y plus BTN_TOUCH */
	int rel;		/* mouse-like */
	int direct;		/* INPUT_PROP_DIRECT: you touch what you see */
	int has_keys, alpha, arrows, mods, backspace;
	int min_x, max_x, min_y, max_y;
	int slots;		/* ABS_MT_SLOT maximum, -1 if none */
};

static struct indev devs[MAX_DEVS];
static int ndevs, nunreadable;

static void probe(const char *path)
{
	unsigned long ev[NBITS(EV_MAX)], key[NBITS(KEY_MAX)];
	unsigned long abs[NBITS(ABS_MAX)], rel[NBITS(REL_MAX)], prop[NBITS(INPUT_PROP_MAX)];
	struct input_absinfo ai;
	struct indev *d = &devs[ndevs];
	int fd = open(path, O_RDONLY | O_NONBLOCK);

	if (fd < 0) {
		/* Worth saying out loud: run as anything but root and every
		 * device is EACCES, which otherwise looks exactly like a device
		 * that has no touch surface. */
		note("kbdscroll: %s: %s\n", path, strerror(errno));
		nunreadable++;
		return;
	}
	memset(d, 0, sizeof *d);
	memset(ev, 0, sizeof ev);
	memset(key, 0, sizeof key);
	memset(abs, 0, sizeof abs);
	memset(rel, 0, sizeof rel);
	memset(prop, 0, sizeof prop);
	snprintf(d->path, sizeof d->path, "%s", path);
	if (ioctl(fd, EVIOCGNAME(sizeof d->name), d->name) < 0)
		snprintf(d->name, sizeof d->name, "?");
	ioctl(fd, EVIOCGBIT(0, sizeof ev), ev);
	if (test_bit(EV_KEY, ev))
		ioctl(fd, EVIOCGBIT(EV_KEY, sizeof key), key);
	if (test_bit(EV_ABS, ev))
		ioctl(fd, EVIOCGBIT(EV_ABS, sizeof abs), abs);
	if (test_bit(EV_REL, ev))
		ioctl(fd, EVIOCGBIT(EV_REL, sizeof rel), rel);
	ioctl(fd, EVIOCGPROP(sizeof prop), prop);

	d->mt = test_bit(ABS_MT_POSITION_X, abs) && test_bit(ABS_MT_POSITION_Y, abs);
	d->st = !d->mt && test_bit(ABS_X, abs) && test_bit(ABS_Y, abs) &&
		test_bit(BTN_TOUCH, key);
	d->rel = test_bit(REL_X, rel) && test_bit(REL_Y, rel);
	d->direct = test_bit(INPUT_PROP_DIRECT, prop);
	d->alpha = test_bit(KEY_A, key) && test_bit(KEY_Z, key);
	d->arrows = test_bit(KEY_UP, key) && test_bit(KEY_DOWN, key) &&
		    test_bit(KEY_LEFT, key) && test_bit(KEY_RIGHT, key);
	d->mods = test_bit(KEY_LEFTALT, key) || test_bit(KEY_RIGHTALT, key) ||
		  test_bit(KEY_LEFTCTRL, key) || test_bit(KEY_LEFTSHIFT, key);
	d->backspace = test_bit(KEY_BACKSPACE, key);
	d->has_keys = d->alpha || d->arrows || d->mods;
	d->slots = -1;

	/* A relative pad has no coordinate space of its own; main() gives it a
	 * virtual one once the screen is known - see rel_surface(). */
	if (d->mt || d->st) {
		int cx = d->mt ? ABS_MT_POSITION_X : ABS_X;
		int cy = d->mt ? ABS_MT_POSITION_Y : ABS_Y;
		if (ioctl(fd, EVIOCGABS(cx), &ai) == 0) { d->min_x = ai.minimum; d->max_x = ai.maximum; }
		if (ioctl(fd, EVIOCGABS(cy), &ai) == 0) { d->min_y = ai.minimum; d->max_y = ai.maximum; }
		if (d->mt && ioctl(fd, EVIOCGABS(ABS_MT_SLOT), &ai) == 0)
			d->slots = ai.maximum;
	}
	close(fd);
	if (ndevs < MAX_DEVS)
		ndevs++;
}

static void enumerate(void)
{
	glob_t g;
	if (glob("/dev/input/event*", 0, NULL, &g) == 0)
		for (size_t i = 0; i < g.gl_pathc; i++)
			probe(g.gl_pathv[i]);
	globfree(&g);
	if (!ndevs)
		note("kbdscroll: no input device could be opened%s\n",
		     nunreadable ? " (all of them refused: not running as root?)" : "");
}

static int pointer(const struct indev *d) { return d->mt || d->st; }
/* What can drive a gesture: an absolute surface, or a relative pad.
 * Deliberately NOT folded into pointer(), which is also what keeps a pad out
 * of the watched-keyboard list - and where the pad and the QWERTY are one node
 * (the Q25) we want that node in both lists. */
static int padsrc(const struct indev *d) { return pointer(d) || d->rel; }
static int width(const struct indev *d)   { return d->max_x - d->min_x + 1; }
static int height(const struct indev *d)  { return d->max_y - d->min_y + 1; }

/* A surface that sits on or beside the keys rather than on the glass. Only ever
 * asked of a device that is already known to be a pointer, so "keypad" here
 * cannot pick up a plain numeric keypad with no touch surface. */
static int padlike(const struct indev *d)
{
	static const char *hints[] = { "keypad", "key_pad", "keyboard", "kbd",
				       "touchpad", "trackpad", "navpad", "nav_",
				       "_pad", "-pad", "touch_key" };
	char n[128];
	snprintf(n, sizeof n, "%s", d->name);
	for (char *p = n; *p; p++)
		*p = tolower((unsigned char)*p);
	for (size_t i = 0; i < sizeof hints / sizeof *hints; i++)
		if (strstr(n, hints[i]))
			return 1;
	return 0;
}

/* "auto"/"none" are handled by the caller; a value starting with / is a device
 * path, anything else is a glob matched against the device name (fnmatch, so
 * "stmpe_*keypad" works and a bare name has to match in full). */
static int matches(const struct indev *d, const char *spec)
{
	if (*spec == '/')
		return !strcmp(d->path, spec);
	return fnmatch(spec, d->name, FNM_CASEFOLD) == 0;
}

static struct indev *find(const char *spec, int want_pointer)
{
	for (int i = 0; i < ndevs; i++) {
		if (want_pointer && !padsrc(&devs[i]))
			continue;
		if (!want_pointer && !devs[i].has_keys)
			continue;
		if (matches(&devs[i], spec))
			return &devs[i];
	}
	/* Fall back to matching without the role filter, so a misdetected
	 * capability cannot make an explicit setting simply not work. */
	for (int i = 0; i < ndevs; i++)
		if (matches(&devs[i], spec))
			return &devs[i];
	return NULL;
}

/* -------------------------------------------------------------- the devices */

static int scr_fd = -1, scr_w, scr_h, inj_slot = 9;
static int arrow_fd = -1, keys_wfd = -1;
static int uinput_scr_fd = -1, uinput_key_fd = -1;
static long long own_keys_until;

static long long now_ms(void)
{
	struct timespec t;
	clock_gettime(CLOCK_MONOTONIC, &t);
	return t.tv_sec * 1000LL + t.tv_nsec / 1000000;
}

static void msleep(int ms)
{
	struct timespec t = { ms / 1000, (long)(ms % 1000) * 1000000 };
	nanosleep(&t, NULL);
}

/* A device of our own, for the two cases where there is nothing to inject
 * into: a touchscreen whose node refuses writes (screen=uinput), and a
 * keyboard with no arrow keys on it (an ordinary BlackBerry Classic layout). */
static int uinput_dev(const char *name, int touch, int w, int h, int *slot)
{
	struct uinput_setup us;
	int fd = open("/dev/uinput", O_WRONLY | O_NONBLOCK);

	if (fd < 0) {
		note("kbdscroll: /dev/uinput: %s (CONFIG_INPUT_UINPUT?)\n", strerror(errno));
		return -1;
	}
	memset(&us, 0, sizeof us);
	us.id.bustype = BUS_VIRTUAL;
	us.id.vendor = 0x1209;
	us.id.product = 0x4b53;
	snprintf(us.name, sizeof us.name, "%s", name);

	ioctl(fd, UI_SET_EVBIT, EV_KEY);
	ioctl(fd, UI_SET_EVBIT, EV_SYN);
	if (touch) {
		static const int absbits[] = { ABS_MT_SLOT, ABS_MT_TRACKING_ID,
					       ABS_MT_POSITION_X, ABS_MT_POSITION_Y,
					       ABS_MT_TOUCH_MAJOR, ABS_MT_PRESSURE };
		ioctl(fd, UI_SET_EVBIT, EV_ABS);
		ioctl(fd, UI_SET_PROPBIT, INPUT_PROP_DIRECT);
		ioctl(fd, UI_SET_KEYBIT, BTN_TOUCH);
		ioctl(fd, UI_SET_KEYBIT, BTN_TOOL_FINGER);
		for (size_t i = 0; i < sizeof absbits / sizeof *absbits; i++)
			ioctl(fd, UI_SET_ABSBIT, absbits[i]);
		for (size_t i = 0; i < sizeof absbits / sizeof *absbits; i++) {
			struct uinput_abs_setup as;
			memset(&as, 0, sizeof as);
			as.code = absbits[i];
			switch (absbits[i]) {
			case ABS_MT_SLOT:	 as.absinfo.maximum = SLOTS - 1; break;
			case ABS_MT_TRACKING_ID: as.absinfo.maximum = 65535; break;
			case ABS_MT_POSITION_X:	 as.absinfo.maximum = w - 1; break;
			case ABS_MT_POSITION_Y:	 as.absinfo.maximum = h - 1; break;
			default:		 as.absinfo.maximum = 255; break;
			}
			ioctl(fd, UI_ABS_SETUP, &as);
		}
		*slot = 0;	/* our own device: no contention for slots */
	} else {
		for (size_t i = 0; i < sizeof keynames / sizeof *keynames; i++)
			ioctl(fd, UI_SET_KEYBIT, keynames[i].code);
	}
	if (ioctl(fd, UI_DEV_SETUP, &us) < 0 || ioctl(fd, UI_DEV_CREATE) < 0) {
		note("kbdscroll: uinput setup: %s\n", strerror(errno));
		close(fd);
		return -1;
	}
	/* The compositor learns about the device through udev; events written
	 * before libinput has opened it go nowhere. */
	msleep(UINPUT_SETTLE_MS);
	note("kbdscroll: created virtual %s device \"%s\"\n", touch ? "touch" : "key", name);
	return fd;
}

/* ----------------------------------------------------------- event emission */

static void emit(int fd, int type, int code, int value)
{
	struct input_event e;
	memset(&e, 0, sizeof e);
	e.type = type;
	e.code = code;
	e.value = value;
	if (fd >= 0 && write(fd, &e, sizeof e) < 0) { /* nothing useful to do */ }
}

static int inj_down;

static void inj_touch(int x, int y)
{
	if (scr_fd < 0)
		return;
	emit(scr_fd, EV_ABS, ABS_MT_SLOT, inj_slot);
	if (!inj_down) {
		emit(scr_fd, EV_ABS, ABS_MT_TRACKING_ID, INJ_ID);
		emit(scr_fd, EV_ABS, ABS_MT_TOUCH_MAJOR, 8);
		emit(scr_fd, EV_ABS, ABS_MT_PRESSURE, 60);
		/* The kernel drops a value equal to the slot's last one, and a
		 * compositor started since then never saw it (it takes 0): a
		 * touch-down at the previous touch-down's spot would land at the
		 * top-left. Step off the value first so both always get through. */
		emit(scr_fd, EV_ABS, ABS_MT_POSITION_X, x + 1);
		emit(scr_fd, EV_ABS, ABS_MT_POSITION_Y, y + 1);
	}
	emit(scr_fd, EV_ABS, ABS_MT_POSITION_X, x);
	emit(scr_fd, EV_ABS, ABS_MT_POSITION_Y, y);
	if (!inj_down) {
		emit(scr_fd, EV_KEY, BTN_TOUCH, 1);
		emit(scr_fd, EV_KEY, BTN_TOOL_FINGER, 1);
	}
	emit(scr_fd, EV_SYN, SYN_REPORT, 0);
	inj_down = 1;
}

static void inj_up(void)
{
	if (!inj_down || scr_fd < 0)
		return;
	emit(scr_fd, EV_ABS, ABS_MT_SLOT, inj_slot);
	emit(scr_fd, EV_ABS, ABS_MT_TRACKING_ID, -1);
	emit(scr_fd, EV_KEY, BTN_TOUCH, 0);
	emit(scr_fd, EV_KEY, BTN_TOOL_FINGER, 0);
	emit(scr_fd, EV_SYN, SYN_REPORT, 0);
	inj_down = 0;
}

static void tap_key(int fd, int code)
{
	emit(fd, EV_KEY, code, 1);
	emit(fd, EV_SYN, SYN_REPORT, 0);
	emit(fd, EV_KEY, code, 0);
	emit(fd, EV_SYN, SYN_REPORT, 0);
}

static int wd_keys[8], wd_n;

/* The chord goes back into the keyboard the shell is already listening to, so
 * whatever that keyboard's own layout or input-method shim makes of it happens
 * exactly as if it had been typed. It comes back to us on the read side too,
 * hence the echo window. */
static void send_word_delete(void)
{
	int i;
	if (!wd_n || keys_wfd < 0)
		return;
	own_keys_until = now_ms() + WORD_ECHO_MS;
	for (i = 0; i < wd_n - 1; i++)
		emit(keys_wfd, EV_KEY, wd_keys[i], 1);
	emit(keys_wfd, EV_SYN, SYN_REPORT, 0);
	tap_key(keys_wfd, wd_keys[wd_n - 1]);
	for (i = wd_n - 2; i >= 0; i--)
		emit(keys_wfd, EV_KEY, wd_keys[i], 0);
	emit(keys_wfd, EV_SYN, SYN_REPORT, 0);
}

/* ------------------------------------------------------------- environment */

static int text_focus(void)
{
	char b[4] = "";
	int fd;
	if (!*o_text_focus)
		return 0;
	if ((fd = open(o_text_focus, O_RDONLY)) < 0)
		return 0;
	if (read(fd, b, sizeof b - 1) < 0) { b[0] = 0; }
	close(fd);
	return b[0] == '1';
}

static char backlight_path[PATH_MAX];

/* Scrolling a screen nobody can see wakes the whole stack up for nothing, and
 * a pocket resting against a capacitive keyboard does exactly that. Any
 * brightness file will do; a device without one (E Ink, or a panel driven
 * entirely through DRM) is simply treated as always on. */
static void find_backlight(void)
{
	static const char *cands[] = {
		"/sys/class/backlight/*/brightness",
		"/sys/class/leds/lcd-backlight/brightness",
		"/sys/class/leds/*backlight*/brightness",
	};
	if (!strcmp(o_backlight, "none"))
		return;
	if (strcmp(o_backlight, "auto")) {
		snprintf(backlight_path, sizeof backlight_path, "%s", o_backlight);
		return;
	}
	for (size_t i = 0; i < sizeof cands / sizeof *cands; i++) {
		glob_t g;
		if (glob(cands[i], 0, NULL, &g) == 0 && g.gl_pathc) {
			snprintf(backlight_path, sizeof backlight_path, "%s", g.gl_pathv[0]);
			globfree(&g);
			return;
		}
		globfree(&g);
	}
}

static int display_on(void)
{
	char b[16] = "";
	int fd;
	if (!*backlight_path)
		return 1;
	if ((fd = open(backlight_path, O_RDONLY)) < 0)
		return 1;
	if (read(fd, b, sizeof b - 1) < 0) { b[0] = 0; }
	close(fd);
	return atoi(b) > 0;
}

/* ------------------------------------------------------------------ signals */

static volatile sig_atomic_t quitting;
static void on_signal(int sig) { (void)sig; quitting = 1; }

/* ------------------------------------------------------------- pad geometry */

static int pad_w, pad_h;	/* after pad-rotate: the axes we work in */
static int raw_w, raw_h;

static void pad_xform(int rx, int ry, int *ox, int *oy)
{
	int x, y;
	switch (o_pad_rotate) {
	case 90:  x = ry;		y = raw_w - 1 - rx;	break;
	case 180: x = raw_w - 1 - rx;	y = raw_h - 1 - ry;	break;
	case 270: x = raw_h - 1 - ry;	y = rx;			break;
	default:  x = rx;		y = ry;			break;
	}
	if (o_invert_x) x = pad_w - 1 - x;
	if (o_invert_y) y = pad_h - 1 - y;
	*ox = x;
	*oy = y;
}

/* ---------------------------------------------------------------- reporting */

static void list_devices(const struct indev *pad, const struct indev *scr,
			 struct indev **keyd, int nkeyd,
			 const struct indev *arrowd, const struct indev *wordd)
{
	printf("%-17s %-34s %s\n", "DEVICE", "NAME", "CAPABILITIES");
	for (int i = 0; i < ndevs; i++) {
		struct indev *d = &devs[i];
		char caps[256] = "";
		size_t n = 0;
		#define cap(fmt, ...) do { if (n < sizeof caps - 1) \
			n += snprintf(caps + n, sizeof caps - n, fmt, ##__VA_ARGS__); } while (0)
		if (d->mt) cap("multitouch(%d slots) ", d->slots + 1);
		if (d->st) cap("single-touch ");
		if (d->mt || d->st) cap("%dx%d ", width(d), height(d));
		else if (d->rel && d->max_x) cap("%dx%d virtual ", width(d), height(d));
		if (d->direct) cap("direct ");
		if (d->rel) cap("relative ");
		if (d->alpha) cap("alpha ");
		if (d->arrows) cap("arrows ");
		if (d->mods) cap("modifiers ");
		#undef cap
		printf("%-17s %-34s %s\n", d->path, d->name, caps);
	}
	printf("\nchosen:\n");
	printf("  pad      %s\n", pad ? pad->name : "(none: nothing to do on this device)");
	printf("  screen   %s\n", !strcmp(o_screen, "uinput") ? "(virtual, uinput)" :
	       scr ? scr->name : "(none: nowhere to inject)");
	for (int i = 0; i < nkeyd; i++)
		printf("  keys     %s\n", keyd[i]->name);
	if (!nkeyd)
		printf("  keys     (none: typing will not cancel a slide)\n");
	printf("  arrows   %s\n", arrowd ? arrowd->name :
	       strcmp(o_arrows, "none") ? "(virtual, uinput)" : "(none: no cursor mode)");
	printf("  delete   %s\n", wordd && wd_n ? wordd->name : "(off)");
	if (pad) {
		printf("\ngeometry: pad %dx%d", raw_w, raw_h);
		if (o_pad_rotate || o_invert_x || o_invert_y)
			printf(" -> %dx%d (rotate %d%s%s)", pad_w, pad_h, o_pad_rotate,
			       o_invert_x ? " invert-x" : "", o_invert_y ? " invert-y" : "");
		printf(", screen %dx%d, scale %.2f/%.2f, slop %d, cursor step %d/%d\n",
		       scr_w, scr_h, o_scale, o_scale_x, o_slop, o_step_x, o_step_y);
	}
	printf("backlight: %s\n", *backlight_path ? backlight_path : "(none: always treated as on)");
}

static void usage(void)
{
	printf("Usage: kbdscroll [--list] [--debug] [--config FILE] [key=value ...]\n\n"
	       "Turns a slide over a capacitive keyboard, trackpad or navigation pad into\n"
	       "scrolling on the touchscreen, and into arrow keys while the cursor modifier\n"
	       "is held.  Settings come from /etc/kbdscroll.conf, then\n"
	       "/etc/kbdscroll.conf.d/*.conf, then KBDSCROLL_* in the environment, then the\n"
	       "command line.\n\n");
	for (size_t i = 0; i < sizeof opts / sizeof *opts; i++) {
		char def[64];
		switch (opts[i].type) {
		case O_STR: snprintf(def, sizeof def, "\"%s\"", *(char **)opts[i].slot); break;
		case O_INT: snprintf(def, sizeof def, "%d", *(int *)opts[i].slot); break;
		case O_DBL: snprintf(def, sizeof def, "%g", *(double *)opts[i].slot); break;
		}
		printf("  %-17s %-24s %s\n", opts[i].key, def, opts[i].help);
	}
	printf("\n  0 or an empty default means \"work it out from the hardware\".\n");
}

/* --------------------------------------------------------------------- main */

int main(int argc, char **argv)
{
	struct indev *pad = NULL, *scr = NULL, *arrowd = NULL, *wordd = NULL;
	struct indev *keyd[MAX_KEYDEVS];
	int nkeyd = 0, key_fd[MAX_KEYDEVS], listing = 0;
	int cursor_mod[4], ncursor_mod;
	int pad_fd = -1;
	const char *conf = "/etc/kbdscroll.conf";

	/* Configuration, in order of increasing precedence. */
	for (int i = 1; i < argc; i++)
		if (!strcmp(argv[i], "--config") && i + 1 < argc)
			conf = argv[++i];
	read_conf(conf, 0);
	read_conf_dir("/etc/kbdscroll.conf.d");
	read_conf_codename("/etc/kbdscroll.conf.d/by-codename");
	read_env();
	for (int i = 1; i < argc; i++) {
		char *eq;
		if (!strcmp(argv[i], "--list")) { listing = 1; continue; }
		if (!strcmp(argv[i], "--debug")) { o_debug = 1; continue; }
		if (!strcmp(argv[i], "--config")) { i++; continue; }
		if (!strcmp(argv[i], "--help") || !strcmp(argv[i], "-h")) { usage(); return 0; }
		if ((eq = strchr(argv[i], '='))) {
			*eq = 0;
			opt_set(argv[i], eq + 1, "command line");
			continue;
		}
		note("kbdscroll: unexpected argument \"%s\"; try --help\n", argv[i]);
		return 2;
	}
	ncursor_mod = parse_keys(o_cursor_mod, cursor_mod, 4);
	wd_n = parse_keys(o_word_delete, wd_keys, 8);
	find_backlight();

	/* Which devices, and what for. */
	enumerate();
	if (strcmp(o_screen, "auto") && strcmp(o_screen, "uinput")) {
		if (!(scr = find(o_screen, 1)))
			note("kbdscroll: no touchscreen matching \"%s\"\n", o_screen);
	} else {
		for (int i = 0; i < ndevs; i++) {
			struct indev *d = &devs[i];
			if (!d->mt || padlike(d))
				continue;
			if (!scr || (d->direct && !scr->direct) ||
			    (d->direct == scr->direct &&
			     (long)width(d) * height(d) > (long)width(scr) * height(scr)))
				scr = d;
		}
	}
	if (strcmp(o_pad, "auto")) {
		if (!(pad = find(o_pad, 1)))
			note("kbdscroll: no touch surface matching \"%s\"\n", o_pad);
	} else {
		for (int i = 0; i < ndevs; i++) {
			struct indev *d = &devs[i];
			if (!padsrc(d) || d == scr || !padlike(d))
				continue;
			/* A relative pad has no size to compare yet, and is the
			 * last resort among pad-like devices: prefer a real
			 * surface if the device has both. */
			if (!pad) { pad = d; continue; }
			if (pad->rel && !d->rel) { pad = d; continue; }
			if (!pad->rel && d->rel) continue;
			if (!d->rel && (long)width(d) * height(d) < (long)width(pad) * height(pad))
				pad = d;
		}
		/* No device names itself after the keys: take the smallest
		 * absolute pointer that is not the screen, and say so - this is
		 * the guess most likely to need "pad" set by hand. */
		for (int i = 0; !pad && i < ndevs; i++) {
			struct indev *d = &devs[i];
			if (!pointer(d) || d == scr)
				continue;
			pad = d;
			note("kbdscroll: guessing \"%s\" is the touch surface; set "
			     "pad= in /etc/kbdscroll.conf.d if that is wrong\n", d->name);
		}
	}
	if (strcmp(o_keys, "auto") && strcmp(o_keys, "none")) {
		char buf[256], *p, *save = NULL;
		snprintf(buf, sizeof buf, "%s", o_keys);
		for (p = strtok_r(buf, ",", &save); p && nkeyd < MAX_KEYDEVS;
		     p = strtok_r(NULL, ",", &save)) {
			struct indev *d = find(trim(p), 0);
			if (d)
				keyd[nkeyd++] = d;
			else
				note("kbdscroll: no keyboard matching \"%s\"\n", trim(p));
		}
	} else if (!strcmp(o_keys, "auto")) {
		for (int i = 0; i < ndevs && nkeyd < MAX_KEYDEVS; i++)
			if (devs[i].has_keys && !pointer(&devs[i]))
				keyd[nkeyd++] = &devs[i];
	}
	/* Arrow keys: whichever watched keyboard has them. Athena keeps them on
	 * a separate "nav_key" node; a Classic-style layout has none at all, and
	 * then a device of our own is the only way to move a cursor. */
	if (strcmp(o_arrows, "auto") && strcmp(o_arrows, "none") && strcmp(o_arrows, "uinput"))
		arrowd = find(o_arrows, 0);
	else if (!strcmp(o_arrows, "auto"))
		for (int i = 0; i < nkeyd && !arrowd; i++)
			if (keyd[i]->arrows)
				arrowd = keyd[i];
	/* The word-delete chord wants the keyboard that carries the letters. */
	for (int i = 0; i < nkeyd && !wordd; i++)
		if (keyd[i]->alpha && keyd[i]->backspace)
			wordd = keyd[i];
	if (!wordd && nkeyd)
		wordd = keyd[0];

	/* The screen's geometry first: a relative pad's virtual surface is derived
	 * from it, and the pad-unit defaults below are derived from that in turn. */
	if (scr) {
		scr_w = width(scr);
		scr_h = height(scr);
		if (scr->slots >= 0)
			inj_slot = scr->slots;
	}
	if (*o_screen_size)
		sscanf(o_screen_size, "%dx%d", &scr_w, &scr_h);
	if (scr_w <= 1 || scr_h <= 1) { scr_w = 1080; scr_h = 1920; }

	/* A relative pad reports deltas and has no coordinate space of its own, so
	 * give it one. Defaulting to the screen's size makes one pad unit one
	 * screen pixel at scale 1.0, which keeps scale, gain, slop and the cursor
	 * steps below meaning exactly what they mean for an absolute surface. */
	if (pad && pad->rel && !pointer(pad)) {
		pad->min_x = pad->min_y = 0;
		pad->max_x = (o_rel_w > 0 ? o_rel_w : scr_w) - 1;
		pad->max_y = (o_rel_h > 0 ? o_rel_h : scr_h) - 1;
		note("kbdscroll: \"%s\" is a relative pad: %dx%d virtual surface, "
		     "lift after %d ms still\n",
		     pad->name, width(pad), height(pad), o_rel_lift);
	}

	if (pad) {
		raw_w = width(pad);
		raw_h = height(pad);
		pad_w = (o_pad_rotate == 90 || o_pad_rotate == 270) ? raw_h : raw_w;
		pad_h = (o_pad_rotate == 90 || o_pad_rotate == 270) ? raw_w : raw_h;
	}
	/* Everything below is expressed in pad units, so the defaults follow the
	 * hardware: a slide across the whole surface travels the whole screen
	 * (times the gain), a tenth of the surface is a deliberate slide, and a
	 * cursor step is a fifth of the surface's height. Athena's tuned numbers
	 * come out of this within a few percent - see profiles/athena.conf. */
	if (o_scale <= 0)   o_scale = (double)scr_h / (pad_h ? pad_h : scr_h) * o_gain;
	if (o_scale_x <= 0) o_scale_x = (double)scr_w / (pad_w ? pad_w : scr_w) * o_gain_x;
	if (o_slop <= 0)    o_slop = pad_h ? pad_h / 10 : 50;
	if (o_step_y <= 0)  o_step_y = pad_h ? pad_h / 5 : 100;
	if (o_step_x <= 0)  o_step_x = pad_w ? pad_w / 24 : 45;

	if (listing) {
		list_devices(pad, scr, keyd, nkeyd, arrowd, wordd);
		return 0;
	}
	if (!pad) {
		/* Not a failure: the package is installed on every machine that
		 * declares a physical keyboard, and plenty of those have no touch
		 * surface behind it. Exit 0 so systemd does not restart us. */
		note("kbdscroll: no touch surface besides the touchscreen; nothing to do\n");
		return 0;
	}

	/* Open what we chose. */
	pad_fd = open(pad->path, O_RDONLY | O_NONBLOCK);
	if (pad_fd < 0) {
		note("kbdscroll: %s: %s\n", pad->path, strerror(errno));
		return 1;
	}
	/* Injecting into the touchscreen's own node is the tested path: the
	 * contacts arrive in the stream the compositor has already opened, in the
	 * coordinate space it already knows. A virtual device of our own is the
	 * fallback for a node that refuses writes, not the default. */
	if (!scr && strcmp(o_screen, "uinput")) {
		note("kbdscroll: found a touch surface (\"%s\") but no touchscreen to "
		     "inject into; name one with screen=, or use screen=uinput\n", pad->name);
		return 1;
	}
	if (!scr)
		note("kbdscroll: no touchscreen found: the virtual one is %dx%d, "
		     "set screen-size if that is wrong\n", scr_w, scr_h);
	if (scr && strcmp(o_screen, "uinput"))
		scr_fd = open(scr->path, O_WRONLY);
	if (scr_fd < 0) {
		if (scr && strcmp(o_screen, "uinput"))
			note("kbdscroll: %s: %s; falling back to a virtual touchscreen\n",
			     scr->path, strerror(errno));
		scr_fd = uinput_scr_fd = uinput_dev("kbdscroll touch", 1, scr_w, scr_h, &inj_slot);
	}
	if (scr_fd < 0) {
		note("kbdscroll: nowhere to inject touch events\n");
		return 1;
	}
	for (int i = 0; i < nkeyd; i++) {
		key_fd[i] = open(keyd[i]->path, O_RDONLY | O_NONBLOCK);
		if (key_fd[i] < 0)
			note("kbdscroll: %s: %s\n", keyd[i]->path, strerror(errno));
	}
	if (arrowd)
		arrow_fd = open(arrowd->path, O_WRONLY);
	if (arrow_fd < 0 && ncursor_mod && strcmp(o_arrows, "none"))
		arrow_fd = uinput_key_fd = uinput_dev("kbdscroll keys", 0, 0, 0, NULL);
	if (wordd && wd_n && *o_text_focus)
		keys_wfd = open(wordd->path, O_WRONLY);

	note("kbdscroll: pad \"%s\" %dx%d -> screen \"%s\" %dx%d, scale %.2f/%.2f, slop %d\n",
	     pad->name, pad_w, pad_h,
	     uinput_scr_fd >= 0 ? "kbdscroll touch" : scr ? scr->name : "?",
	     scr_w, scr_h, o_scale, o_scale_x, o_slop);
	for (int i = 0; i < nkeyd; i++)
		dbg("watching keys on %s (%s)\n", keyd[i]->path, keyd[i]->name);

	/* Deliberately not SA_RESTART: poll() has to come back with EINTR so the
	 * loop can exit and release the contact it is holding. */
	struct sigaction sa;
	memset(&sa, 0, sizeof sa);
	sa.sa_handler = on_signal;
	sigaction(SIGTERM, &sa, NULL);
	sigaction(SIGINT, &sa, NULL);
	sigaction(SIGHUP, &sa, NULL);

	/* --------------------------------------------------------- the loop */
	int alt_down = 0, was_alt = 0, alt_block = 0;
	int drv = -1, axis = 0, ax = 0, ay = 0;		/* cursor-mode driver contact */
	int id[SLOTS], rx[SLOTS], ry[SLOTS], x[SLOTS], y[SLOTS];
	int sx[SLOTS], sy[SLOTS], fresh[SLOTS];
	int slot = 0;
	long long last_key = 0;
	int tracking = 0, rejected = 0, scrolling = 0;
	/* Relative pads only: whether a synthesised contact is down, and when it
	 * last moved. Stillness is the lift, so the loop has to wake up to notice
	 * it - see the poll timeout below. */
	int rel_active = 0;
	long long rel_last = 0;
	int t_slot = -1, x0 = 0, y0 = 0;
	int anchor_x = 0, anchor_y = 0, pad_anchor = 0, scroll_axis = 'y';
	struct pollfd pf[1 + MAX_KEYDEVS];
	int npf = 0;

	for (int i = 0; i < SLOTS; i++) {
		id[i] = -1; fresh[i] = 0;
		rx[i] = ry[i] = x[i] = y[i] = sx[i] = sy[i] = 0;
	}

	pf[npf].fd = pad_fd; pf[npf].events = POLLIN; pf[npf].revents = 0; npf++;
	for (int i = 0; i < nkeyd; i++)
		if (key_fd[i] >= 0) {
			pf[npf].fd = key_fd[i]; pf[npf].events = POLLIN; pf[npf].revents = 0; npf++;
		}

	while (!quitting) {
		/* A relative pad's stroke ends in silence, so while one is in flight
		 * the loop must not block for ever waiting for an event that will
		 * never come. Half the lift interval, so the lift is noticed
		 * promptly without busy-waiting. */
		int wait = rel_active ? (o_rel_lift / 2 > 0 ? o_rel_lift / 2 : 1) : -1;
		if (poll(pf, npf, wait) < 0) {
			if (errno == EINTR)
				continue;
			note("kbdscroll: poll: %s\n", strerror(errno));
			break;
		}
		/* ---- keyboards: modifier state, and "is someone typing" ---- */
		for (int i = 1; i < npf; i++) {
			struct input_event e;
			if (!(pf[i].revents & POLLIN))
				continue;
			while (read(pf[i].fd, &e, sizeof e) == (ssize_t)sizeof e) {
				int is_mod = 0;
				if (now_ms() < own_keys_until)
					continue;	/* our own chord coming back */
				if (e.type != EV_KEY)
					continue;
				for (int m = 0; m < ncursor_mod; m++)
					if (e.code == cursor_mod[m])
						is_mod = 1;
				if (is_mod) {
					if (e.value != 2)	/* not an autorepeat */
						alt_down = e.value;
					continue;	/* the modifier is not typing */
				}
				if (e.value == 1) {
					last_key = now_ms();
					if (tracking && !scrolling)
						rejected = 1;	/* typing, not a slide */
				}
			}
		}
		/* The relative pad's lift. Feeding one synthetic SYN_REPORT through
		 * the loop below - rather than duplicating the end-of-stroke handling
		 * - is what keeps the two input shapes on exactly one code path. */
		int synth_syn = 0;
		if (rel_active && now_ms() - rel_last >= o_rel_lift) {
			id[0] = -1;
			rel_active = 0;
			synth_syn = 1;
			dbg("relative contact up: %lld ms still\n", now_ms() - rel_last);
		}
		if (!synth_syn && !(pf[0].revents & POLLIN))
			continue;

		struct input_event e;
		/* Initialised: the loop below can now be entered for a synthetic
		 * SYN_REPORT, and the error check after it reads r either way. */
		ssize_t r = 0;
		for (;;) {
			if (synth_syn) {
				synth_syn = 0;
				e.type = EV_SYN;
				e.code = SYN_REPORT;
				e.value = 0;
			} else if ((r = read(pad_fd, &e, sizeof e)) != (ssize_t)sizeof e) {
				break;
			}
		if (e.type == EV_ABS) {
			switch (e.code) {
			case ABS_MT_SLOT:
				if (e.value >= 0 && e.value < SLOTS)
					slot = e.value;
				break;
			case ABS_MT_TRACKING_ID:
				if (e.value >= 0 && id[slot] < 0)
					fresh[slot] = 1;
				id[slot] = e.value;
				break;
			case ABS_MT_POSITION_X:
				rx[slot] = e.value - pad->min_x;
				break;
			case ABS_MT_POSITION_Y:
				ry[slot] = e.value - pad->min_y;
				break;
			/* A single-touch surface reports the plain axes. On a
			 * protocol B device those are a compatibility copy of the
			 * first contact and would land in whichever slot happens
			 * to be current, so they are ignored there. */
			case ABS_X:
				if (pad->st) rx[0] = e.value - pad->min_x;
				break;
			case ABS_Y:
				if (pad->st) ry[0] = e.value - pad->min_y;
				break;
			}
			continue;
		}
		/* A relative pad: accumulate the deltas into slot 0 of the virtual
		 * surface. The first movement after stillness is the contact coming
		 * down - there is no touch-down to report - and it starts in the
		 * middle so a stroke can run in any direction before it clamps. */
		if (e.type == EV_REL && pad->rel && !pointer(pad)) {
			if (e.code != REL_X && e.code != REL_Y)
				continue;
			if (!rel_active) {
				rx[0] = (pad->max_x - pad->min_x) / 2;
				ry[0] = (pad->max_y - pad->min_y) / 2;
				id[0] = 1;
				fresh[0] = 1;
				rel_active = 1;
				dbg("relative contact down at %d,%d\n", rx[0], ry[0]);
			}
			if (e.code == REL_X) rx[0] += e.value;
			else                 ry[0] += e.value;
			if (rx[0] < 0) rx[0] = 0;
			if (ry[0] < 0) ry[0] = 0;
			if (rx[0] > pad->max_x - pad->min_x) rx[0] = pad->max_x - pad->min_x;
			if (ry[0] > pad->max_y - pad->min_y) ry[0] = pad->max_y - pad->min_y;
			rel_last = now_ms();
			continue;
		}
		/* A single-touch surface has no tracking ids: BTN_TOUCH is the
		 * contact, and it always lands in slot 0. */
		if (e.type == EV_KEY && e.code == BTN_TOUCH && pad->st) {
			if (e.value && id[0] < 0) { id[0] = 1; fresh[0] = 1; }
			if (!e.value) id[0] = -1;
			continue;
		}
		if (e.type != EV_SYN || e.code != SYN_REPORT)
			continue;

		int active = 0, first = -1;
		for (int i = 0; i < SLOTS; i++) {
			if (id[i] < 0) continue;
			pad_xform(rx[i], ry[i], &x[i], &y[i]);
			if (fresh[i]) { sx[i] = x[i]; sy[i] = y[i]; fresh[i] = 0; }
			active++;
			if (first < 0) first = i;
		}

		/* ---- the cursor modifier is held: arrow keys ---- */
		if (alt_down && ncursor_mod && arrow_fd >= 0) {
			if (!was_alt) {
				/* whatever moved before the modifier does not count */
				for (int i = 0; i < SLOTS; i++) { sx[i] = x[i]; sy[i] = y[i]; }
				drv = -1;
				was_alt = 1;
				inj_up();
				tracking = scrolling = 0;
				dbg("modifier down, %d contact(s)\n", active);
			}
			if (drv >= 0 && id[drv] < 0) {
				dbg("driver %d lifted\n", drv);
				drv = -1;
			}
			/* The thumb holding the modifier rests on the surface too,
			 * so the cursor follows whichever contact moves first - never
			 * the resting one - and each slide is locked to the axis it
			 * started on, so jitter cannot add stray steps. */
			if (drv < 0) {
				int best = DRIVE_SLOP;
				for (int i = 0; i < SLOTS; i++) {
					int dx, dy, d;
					if (id[i] < 0) continue;
					dx = abs(x[i] - sx[i]);
					dy = abs(y[i] - sy[i]);
					d = dx > dy ? dx : dy;
					if (d >= best) { best = d; drv = i; }
				}
				if (drv >= 0) {
					axis = abs(x[drv] - sx[drv]) >= abs(y[drv] - sy[drv]) ? 'x' : 'y';
					ax = sx[drv];
					ay = sy[drv];
					dbg("driver slot %d axis %c from %d,%d\n", drv, axis, ax, ay);
				}
			}
			if (drv >= 0 && display_on()) {
				if (axis == 'x') {
					while (x[drv] - ax >= o_step_x) { tap_key(arrow_fd, KEY_RIGHT); ax += o_step_x; }
					while (ax - x[drv] >= o_step_x) { tap_key(arrow_fd, KEY_LEFT); ax -= o_step_x; }
				} else {
					while (y[drv] - ay >= o_step_y) { tap_key(arrow_fd, KEY_DOWN); ay += o_step_y; }
					while (ay - y[drv] >= o_step_y) { tap_key(arrow_fd, KEY_UP); ay -= o_step_y; }
				}
			}
			continue;
		}
		if (was_alt) {
			/* Released: the fingers on the surface were steering the
			 * cursor; none of them may turn into a scroll. */
			was_alt = 0;
			drv = -1;
			alt_block = active > 0;
			dbg("modifier up\n");
		}
		if (alt_block) {
			if (active == 0) alt_block = 0;
			continue;
		}

		/* ---- scrolling ---- */
		if (!tracking) {
			if (active == 1) {
				tracking = 1;
				scrolling = 0;
				t_slot = first;
				x0 = x[first];
				y0 = y[first];
				rejected = (now_ms() - last_key < o_typing_ms) || !display_on();
				dbg("touch at %d,%d%s\n", x0, y0, !rejected ? "" :
				    now_ms() - last_key < o_typing_ms ?
				    ": ignored, a key was just pressed" : ": ignored, display off");
			}
			continue;
		}

		/* finger lifted, or a second one arrived: the gesture is over */
		if (active == 0 || id[t_slot] < 0 || active > 1) {
			if (active > 1 && !rejected)
				dbg("second contact: gesture cancelled\n");
			else if (active == 0)
				dbg("lifted%s\n", scrolling ? "" : rejected ?
				    " (was ignored)" : " before moving past the slop");
			inj_up();
			tracking = scrolling = 0;
			if (active > 1) rejected = 1;
			if (active == 0) rejected = 0;
			continue;
		}
		if (rejected)
			continue;

		int dx = x[t_slot] - x0, dy = y[t_slot] - y0;
		if (!scrolling) {
			if (abs(dx) < o_slop && abs(dy) < o_slop)
				continue;
			/* lock the slide to the axis it started on */
			scroll_axis = abs(dx) > abs(dy) ? 'x' : 'y';
			if (scroll_axis == 'x' && !o_horizontal) {
				rejected = 1;
				continue;
			}
			/* In a text field a right-to-left slide deletes the word
			 * before the cursor, the way it does on the hardware this
			 * imitates. Off unless something keeps text-focus-file up
			 * to date - only the input method knows. */
			if (scroll_axis == 'x' && dx < 0 && wd_n && text_focus()) {
				dbg("slide left in a text field: delete word\n");
				send_word_delete();
				rejected = 1;	/* one word per slide */
				continue;
			}
			dbg("slide %s\n", scroll_axis == 'x' ? (dx < 0 ? "left" : "right") :
			    (dy < 0 ? "up" : "down"));
			scrolling = 1;
			/* A sideways drag starts on the side it moves away from, so
			 * it has most of the screen to travel: a pager only changes
			 * page on a slow drag once it has covered half the width. */
			anchor_x = scroll_axis == 'x' ?
				   (dx < 0 ? scr_w - scr_w / SIDE_DIV : scr_w / SIDE_DIV) : scr_w / 2;
			anchor_y = scr_h / 2;
			pad_anchor = scroll_axis == 'x' ? x[t_slot] : y[t_slot];
			inj_touch(anchor_x, anchor_y);
			continue;
		}

		if (scroll_axis == 'x') {
			/* Hold at the screen edge rather than lifting and taking
			 * hold again: a second touch would stop the page change the
			 * first one started. */
			int tx = anchor_x + (int)((x[t_slot] - pad_anchor) * o_scale_x);
			if (tx < scr_w / SIDE_STOP_DIV) tx = scr_w / SIDE_STOP_DIV;
			if (tx > scr_w - scr_w / SIDE_STOP_DIV) tx = scr_w - scr_w / SIDE_STOP_DIV;
			inj_touch(tx, anchor_y);
			continue;
		}

		int ty = anchor_y + (int)((y[t_slot] - pad_anchor) * o_scale);
		if (ty < scr_h / EDGE_DIV || ty > scr_h - scr_h / EDGE_DIV) {
			/* ran out of screen: lift, and take hold again mid-screen */
			inj_up();
			anchor_y = scr_h / 2;
			pad_anchor = y[t_slot];
			inj_touch(scr_w / 2, anchor_y);
			continue;
		}
		inj_touch(scr_w / 2, ty);
		}
		if (r < 0 && errno != EAGAIN && errno != EINTR) {
			note("kbdscroll: %s: %s\n", pad->path, strerror(errno));
			inj_up();
			return 1;	/* the surface went away: let systemd restart us */
		}
	}

	/* Never leave a contact down: the shell would hold a drag for ever. */
	inj_up();
	if (uinput_scr_fd >= 0) { ioctl(uinput_scr_fd, UI_DEV_DESTROY); close(uinput_scr_fd); }
	if (uinput_key_fd >= 0) { ioctl(uinput_key_fd, UI_DEV_DESTROY); close(uinput_key_fd); }
	dbg("exiting\n");
	return 0;
}
