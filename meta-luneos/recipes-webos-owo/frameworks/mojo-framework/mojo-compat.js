/*--------------------------------------------------------------------------
*  Mojo compatibility shim for modern Blink/Chromium
*
*  Loaded by mojo.js immediately after the framework blob. Everything here
*  compensates for behaviour the framework relied on in LunaSysMgr's WebKit
*  (circa 2011) that current Chromium implements differently.
*--------------------------------------------------------------------------*/
/*globals Mojo */

(function () {
	"use strict";

	/*
	 * -webkit-border-image without border-style
	 * ------------------------------------------
	 * Practically every framed surface in Mojo - grouped lists, buttons,
	 * dialogs, menus, selection highlights - is drawn with
	 *
	 *     border-width: 40px 18px 18px 18px;
	 *     -webkit-border-image: url(palm-group.png) 40 18 18 18 repeat repeat;
	 *
	 * and never sets border-style. The 2011 WebKit painted the border image
	 * regardless. CSS says border-width computes to 0 when border-style is
	 * none, so Blink resolves those widths to 0 and paints nothing at all:
	 * the chrome disappears and only bare text is left on the background.
	 *
	 * Blink still accepts the -webkit-border-image shorthand and resolves it
	 * to the standard border-image-* longhands, so the images themselves are
	 * fine. Only the missing border-style needs supplying.
	 *
	 * This is done over the CSSOM rather than by rewriting the framework
	 * stylesheets because apps ship their own CSS in exactly the same style
	 * (overriding the images while reusing the framework's classes), and
	 * those files are outside anything we package.
	 *
	 * border-color is forced transparent so that if an image ever fails to
	 * load the result is the old invisible border rather than a solid slab
	 * of the inherited text colour.
	 */
	var patchedSheets = typeof WeakSet === "function" ? new WeakSet() : null;

	/*
	 * "" when the rule says nothing about a border image, "none" when it
	 * explicitly turns one off, otherwise the image value.
	 */
	function declaredBorderImage(style) {
		return style.getPropertyValue("border-image-source") ||
			style.getPropertyValue("-webkit-border-image") || "";
	}

	function hasBorderImage(style) {
		var source = declaredBorderImage(style);
		return !!source && source !== "none" && source !== "initial";
	}

	function clearsBorderImage(style) {
		var source = declaredBorderImage(style);
		return source === "none" || source === "initial";
	}

	function patchRuleList(rules) {
		if (!rules) {
			return 0;
		}
		var patched = 0;
		for (var i = 0; i < rules.length; i++) {
			var rule = rules[i];

			// @import pulls in a whole further stylesheet, reached through
			// .styleSheet rather than .cssRules. Nearly all of Mojo's styling
			// arrives this way - global.css is little more than nine imports
			// (menus, lists, buttons, textfields, ...) - so skipping these
			// silently leaves most of the framework unpatched.
			if (rule.styleSheet) {
				patched += patchStyleSheet(rule.styleSheet);
				continue;
			}

			// @media / @supports and friends nest further rules.
			if (rule.cssRules && !rule.style) {
				patched += patchRuleList(rule.cssRules);
				continue;
			}
			if (!rule.style) {
				continue;
			}

			/*
			 * The legacy shorthand worked both ways: setting an image made
			 * the border paint, and "-webkit-border-image: none" put the
			 * border back to nothing. Apps lean on the second half - the
			 * Device Info override replaces the page header's image with a
			 * plain background:
			 *
			 *     .palm-page-header {
			 *       background: url(toolbar_light_top.png) bottom left repeat-x;
			 *       -webkit-border-image: none;
			 *     }
			 *
			 * Restoring border-style on the framework rule without honouring
			 * this leaves the header with real 35px/13px transparent borders
			 * that nothing paints, and its title is pushed out of view.
			 */
			if (clearsBorderImage(rule.style)) {
				if (!rule.style.getPropertyValue("border-style")) {
					rule.style.setProperty("border-style", "none");
				}
				if (!rule.style.getPropertyValue("border-width")) {
					rule.style.setProperty("border-width", "0");
				}
				patched++;
				continue;
			}

			if (!hasBorderImage(rule.style)) {
				continue;
			}
			// Respect an author who did specify a style; only fill the gap.
			if (rule.style.getPropertyValue("border-style")) {
				continue;
			}
			rule.style.setProperty("border-style", "solid");
			if (!rule.style.getPropertyValue("border-color")) {
				rule.style.setProperty("border-color", "transparent");
			}
			patched++;
		}
		return patched;
	}

	function patchStyleSheet(sheet) {
		if (!sheet || (patchedSheets && patchedSheets.has(sheet))) {
			return 0;
		}
		var rules;
		try {
			rules = sheet.cssRules;
		} catch (e) {
			// Opaque sheet (should not happen for file:// but be safe).
			return 0;
		}
		if (!rules) {
			return 0;			// still loading; the load event brings us back
		}
		if (patchedSheets) {
			patchedSheets.add(sheet);
		}
		return patchRuleList(rules);
	}

	function patchAllStyleSheets() {
		var total = 0;
		var sheets = document.styleSheets;
		for (var i = 0; i < sheets.length; i++) {
			total += patchStyleSheet(sheets[i]);
		}
		return total;
	}

	/*
	 * Mojo pulls scene and app stylesheets in after startup (and each stage
	 * gets its own document), so one pass at load time is not enough. Watch
	 * for new style/link nodes and patch each as it becomes readable.
	 */
	function watchForNewStyleSheets() {
		if (typeof MutationObserver !== "function") {
			return;
		}
		var observer = new MutationObserver(function (records) {
			for (var i = 0; i < records.length; i++) {
				var added = records[i].addedNodes;
				for (var j = 0; j < added.length; j++) {
					var node = added[j];
					if (!node.tagName) {
						continue;
					}
					var tag = node.tagName.toLowerCase();
					if (tag !== "link" && tag !== "style") {
						continue;
					}
					if (node.sheet) {
						patchStyleSheet(node.sheet);
					} else {
						node.addEventListener("load", function () {
							patchStyleSheet(this.sheet);
						});
					}
				}
			}
		});
		observer.observe(document.documentElement, {childList: true, subtree: true});
	}

	function run() {
		var patched = patchAllStyleSheets();
		if (window.Mojo && Mojo.Log && Mojo.Log.info) {
			Mojo.Log.info("Mojo compat: applied border-style to " + patched + " border-image rules");
		}
	}

	// External stylesheets referenced in <head> are not necessarily parsed
	// when this script runs, so patch at every point where more may have
	// arrived, and keep watching afterwards.
	run();
	watchForNewStyleSheets();
	document.addEventListener("DOMContentLoaded", run, false);
	window.addEventListener("load", run, false);

	/*
	 * PalmSystem members LunaSysMgr had and WebAppMgr does not
	 * ---------------------------------------------------------
	 * Mojo calls some of these without checking for them first, so a missing
	 * one is not a degraded feature but a TypeError that aborts whatever was
	 * in progress - scene transitions and the app menu among them.
	 *
	 * Mojo carries its own desktop-host stand-in for PalmSystem (the object
	 * with version "mojo-host"), which is Palm's own statement of what each
	 * of these should do when the compositor cannot. Those semantics are
	 * reused here, and anything WebAppMgr can genuinely back is wired up
	 * rather than stubbed.
	 *
	 * Deliberately NOT defined, because Mojo already guards them and reports
	 * their absence honestly:
	 *   encrypt / decrypt        - Mojo.Model logs "not implemented"
	 *   crossAppSceneActive      - cross-app scenes need compositor support
	 *   runCrossAppTransition    - Mojo falls back to no transition
	 */
	/* Duration and curve chosen to sit close to the card transition sysmgr
	 * used to run; long enough to read as movement, short enough that it
	 * never delays a tap. */
	var TRANSITION_MS = 220;
	var TRANSITION_EASING = "cubic-bezier(0.25, 0.1, 0.25, 1)";
	var currentAnimation = null;

	function reduceMotion() {
		return !!(window.matchMedia &&
			window.matchMedia("(prefers-reduced-motion: reduce)").matches);
	}

	/*
	 * Animate whichever scene has just become the top one. Deferred by a
	 * frame because Mojo runs the transition before the scene stack settles.
	 */
	function animateIncomingScene(fromScale) {
		if (typeof window.requestAnimationFrame !== "function") {
			return;
		}
		window.requestAnimationFrame(function () {
			var element = topSceneContainer();
			if (!element || typeof element.animate !== "function") {
				return;
			}
			if (currentAnimation) {
				// A second push landing mid-flight: drop the old one rather
				// than leaving two transforms fighting over one element.
				currentAnimation.cancel();
			}
			try {
				currentAnimation = element.animate([
					{opacity: 0, transform: "scale(" + fromScale + ")"},
					{opacity: 1, transform: "scale(1)"}
				], {
					duration: TRANSITION_MS,
					easing: TRANSITION_EASING
					// fill defaults to "none", so the element is left with
					// exactly the styles Mojo gave it once this finishes.
				});
				currentAnimation.onfinish = function () { currentAnimation = null; };
				currentAnimation.oncancel = function () { currentAnimation = null; };
			} catch (e) {
				currentAnimation = null;
			}
		});
	}

	function topSceneContainer() {
		try {
			var stage = window.Mojo && Mojo.Controller && Mojo.Controller.stageController;
			var scene = stage && stage.topScene && stage.topScene();
			var element = scene && scene.sceneElement;
			// The scene div sits inside its scroller; animating the scroller
			// moves the scene's chrome along with its content.
			return (element && element.parentNode) || element;
		} catch (e) {
			return null;
		}
	}

	/*
	 * PalmSystem.deviceInfo is missing the layout metrics
	 * ----------------------------------------------------
	 * LunaSysMgr reported maximumCardWidth, maximumCardHeight, touchableRows,
	 * keyboardAvailable and keyboardSlider alongside the model and version.
	 * WebAppMgr reports only the model, the platform version and a
	 * screenWidth/screenHeight pair that are both 0.
	 *
	 * Mojo derives touchableRows from maximumCardHeight when the host does
	 * not supply it:
	 *
	 *     touchableRows = Math.floor((maximumCardHeight - 28) / 48)
	 *
	 * With maximumCardHeight undefined that is NaN, and the menu widget
	 * writes the result straight into a class name - the app menu comes up as
	 * "palm-popup-container palm-touch-rows-NaN". That class is what carries
	 * the menu's frame:
	 *
	 *     #palm-app-menu.palm-touch-rows-7 {
	 *       -webkit-border-image: url(system-menu-background-solid.png) 30 28 35 28 ...
	 *     }
	 *
	 * so with NaN the menu loses its 30px top and 35px bottom frame while
	 * .palm-popup-content keeps its matching 31px/26px insets, and the rows
	 * spill past both ends of the popup - the top and bottom items drop off.
	 *
	 * touchableRows is pinned to 7 rather than computed: submission 506 ships
	 * styling for palm-touch-rows-7 and nothing else, so any other value
	 * leaves the menu unstyled no matter how large the display is. Seven rows
	 * (capped at 286px, then scrolling) is what the framework's own artwork is
	 * cut for.
	 */
	var TOUCHABLE_ROWS = 7;

	function installDeviceInfoShim() {
		var ps = window.PalmSystem;
		if (!ps) {
			return;
		}
		var raw = ps.deviceInfo;
		var info;
		try {
			info = JSON.parse(raw);
		} catch (e) {
			return;			// unrecognisable; leave it alone
		}

		// Resolved on read: Mojo fetches deviceInfo lazily, by which point
		// the window has its real size.
		function augmented() {
			var out = {};
			for (var key in info) {
				if (Object.prototype.hasOwnProperty.call(info, key)) {
					out[key] = info[key];
				}
			}
			if (!out.screenWidth) {
				out.screenWidth = window.screen ? window.screen.width : window.innerWidth;
			}
			if (!out.screenHeight) {
				out.screenHeight = window.screen ? window.screen.height : window.innerHeight;
			}
			// The card, not the display: this is the area an app is given.
			if (!out.maximumCardWidth) {
				out.maximumCardWidth = window.innerWidth || out.screenWidth;
			}
			if (!out.maximumCardHeight) {
				out.maximumCardHeight = window.innerHeight || out.screenHeight;
			}
			if (!out.touchableRows) {
				out.touchableRows = TOUCHABLE_ROWS;
			}
			return JSON.stringify(out);
		}

		try {
			Object.defineProperty(ps, "deviceInfo", {
				get: augmented, configurable: true, enumerable: true
			});
		} catch (e) {
			// Not redefinable; nothing further we can do here.
		}
	}

	function installPalmSystemShim() {
		var ps = window.PalmSystem;
		if (!ps) {
			return;			// desktop host; Mojo installs its own
		}

		function define(name, value) {
			if (ps[name] === undefined) {
				try {
					ps[name] = value;
				} catch (e) {
					// Some builds expose PalmSystem members as accessors on a
					// frozen prototype; fall back to an own property.
					Object.defineProperty(ps, name, {
						value: value, writable: true, configurable: true
					});
				}
			}
		}

		var noop = function () {};

		/* --- scene transitions -------------------------------------------
		 * Legacy transitions ran in the compositor: prepareSceneTransition()
		 * had sysmgr snapshot the card, and runSceneTransition() animated
		 * from that snapshot to the live content. WebAppMgr offers nothing
		 * equivalent, and Mojo calls both unguarded.
		 *
		 * We cannot snapshot, but we do not need to: both scenes are
		 * ordinary elements in one document. By the time Mojo calls
		 * runSceneTransition() the incoming scene is already display:block
		 * and the outgoing one is display:none (on a pop it is gone from the
		 * DOM entirely), so there is nothing to animate *out* - animating
		 * the incoming scene in is the whole effect that survives.
		 *
		 * The scene stack has not caught up at call time (on a push the new
		 * scene is not in getScenes() yet), so the element is picked up one
		 * frame later, when topScene() is the scene that just arrived.
		 *
		 * Mojo does not wait for us: it calls finish.defer() immediately
		 * after. That is fine - the animation is decorative and runs
		 * alongside whatever the scene's activate handler does.
		 */
		define("prepareSceneTransition", noop);

		define("runSceneTransition", function (type, isPop) {
			if (type === "none" || reduceMotion()) {
				return;
			}
			// Direction reads as depth: a pushed scene settles back from
			// slightly too close, a pop rises from slightly too far.
			var from = (type === "cross-fade") ? 1 : (isPop ? 0.94 : 1.06);
			animateIncomingScene(from);
		});

		define("cancelSceneTransition", function () {
			if (currentAnimation) {
				currentAnimation.cancel();
				currentAnimation = null;
			}
		});
		define("cancelCrossAppScene", noop);

		/* --- window properties --------------------------------------------
		 * WebAppMgr exposes the singular setWindowProperty(key, value);
		 * Mojo wants the plural bulk form.
		 */
		define("setWindowProperties", function (props) {
			if (!props || typeof ps.setWindowProperty !== "function") {
				return;
			}
			for (var key in props) {
				if (Object.prototype.hasOwnProperty.call(props, key)) {
					ps.setWindowProperty(key, String(props[key]));
				}
			}
		});

		/* --- smart-zoom animation driver -----------------------------------
		 * PalmSystem drove this loop natively. requestAnimationFrame gives
		 * the same shape: step callbacks with an eased 0..1 progress, then a
		 * completion callback.
		 */
		define("runAnimationLoop", function (target, stepMethod, completeMethod,
		                                     curve, duration, from, to) {
			var start = null;
			var ms = (duration || 0.3) * 1000;
			var easeOut = function (t) { return 1 - Math.pow(1 - t, 3); };
			var ease = (curve === "linear") ? function (t) { return t; } : easeOut;

			function frame(now) {
				if (start === null) {
					start = now;
				}
				var t = Math.min(1, (now - start) / ms);
				var value = from + (to - from) * ease(t);
				try {
					if (target && typeof target[stepMethod] === "function") {
						target[stepMethod](value);
					}
					if (t >= 1) {
						if (target && typeof target[completeMethod] === "function") {
							target[completeMethod]();
						}
						return;
					}
				} catch (e) {
					return;
				}
				window.requestAnimationFrame(frame);
			}
			window.requestAnimationFrame(frame);
		});

		/* --- text and sound ------------------------------------------------ */
		// Called unguarded when the app menu opens.
		define("hideSpellingWidget", noop);
		// Mojo.Format.runTextIndexer expects the text back unchanged.
		define("runTextIndexer", function (text) { return text; });
		define("receivePageUpDownInLandscape", noop);
		define("playSoundNotification", noop);
		define("setAlertSound", noop);

		/* --- identity ------------------------------------------------------- */
		define("version", (function () {
			try {
				return JSON.parse(ps.deviceInfo).platformVersion || "webappmgr";
			} catch (e) {
				return "webappmgr";
			}
		}()));
		// Falsy on a real device; Mojo uses it to pick the emulator input path.
		define("simulated", false);
	}

	installDeviceInfoShim();
	installPalmSystemShim();

	// Exposed so a scene that injects styles by hand can re-run the pass.
	window.MojoCompat = {patchStyleSheets: patchAllStyleSheets};
}());
