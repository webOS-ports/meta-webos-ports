# Display scaling on high-DPI panels — findings

Investigated on a Pixel 3a (sargo, 1080x2220, 441 PPI) running LuneOS with the
Halium 9.0 GSI, August 2026. Every figure below was measured over the Chromium
remote debugging ports (WebAppMgr on 9998, browser_shell on 9999), not inferred
from appearance.

## Two symptoms, one cause

  - Atlas renders its chrome far too small: toolbar icons about 2.3mm on a
    441 PPI panel.
  - Enyo apps in WebAppMgr - Calendar and friends - look oversized *and* have
    their right-hand side cut off.

These look like opposite problems. They are the same one seen from two sides.

**Any surface rendered with a device scale factor greater than 1 is magnified
and cropped by the compositor.** The page is always laid out correctly; only
the presentation is wrong. Atlas escapes it only because nothing scales it.

## Evidence

Calendar at devicePixelRatio 2.0. Its own render, captured with
`Page.captureScreenshot`, is intact: "Get started with your HP webOS account:"
in full, the "LuneOS" button complete and centred, nothing truncated. A device
screenshot taken at the same moment shows "HP webOS" and "Lune" chopped at the
right edge.

Sweeping the viewport with `Emulation.setDeviceMetricsOverride` from 450 to
1080 css px finds **zero** overflowing elements at every width. Calendar's
layout is fully fluid and never overflows, at any scale.

The same holds for Atlas once a scale factor is applied: at 1.5 its page render
shows the complete toolbar with all five buttons ending exactly at 1080 device
px, while the screen shows roughly the left two thirds and the trailing buttons
are gone.

## Why the two hosts differ

They are different processes with different launch paths - not, as it appears,
one framework behaving inconsistently. Both Atlas and Calendar are Enyo 1 apps.

| | process | launched by | scale flag |
|---|---|---|---|
| Calendar, Just Type, Mail, Status Bar | `/usr/bin/WebAppMgr` | `webapp-mgr.sh` | `--force-device-scale-factor` from configd `com.webos.surfacemanager.devicePixelRatio` |
| Atlas | `/usr/bin/browser-shell/browser_shell` | `sam` via `run_browser_shell` | none |

So `devicePixelRatio = 2.4` has been magnifying and cropping every Enyo app all
along, and Atlas has been left unscaled. 1.0 is the only value that presents a
complete surface.

The two hosts also report the factor differently to JS - WebAppMgr keeps
`devicePixelRatio` at 1 and gives the page a 450px logical screen, browser_shell
exposes the real DPR and resizes its window into css units - but that difference
does not protect either of them from the crop.

## What does not work

  - **`--force-device-scale-factor` on browser_shell.** Crops, at 1.5 and worse
    at 2.75.
  - **`--force-device-zoom-level`.** Silently ignored: the switch appears on the
    command line and `innerWidth`, `devicePixelRatio` and `zoom` are all
    unchanged. Chromium zoom is a per-origin preference, not a startup switch.
  - **Tuning the per-device value.** There is no value that is both large enough
    to be legible and small enough to avoid the crop, because the crop scales
    with the factor.

## What does work, as a workaround

CSS `zoom` scales content *within* an unscaled surface, so the compositor never
sees a scaled surface to crop. Verified live on Atlas:

| zoom | URL field | toolbar right edge | all buttons visible |
|---|---|---|---|
| 1.5 | 382 device px | 702 / 1080 | yes |
| 1.75 | 279 | 599 / 1080 | yes |
| 2.0 | 202 | 522 / 1080 | yes |
| 2.4 | 115 | 432 / 1080 | yes |

Unlike the scale factor, the toolbar moves *further inside* the viewport as zoom
rises, so this cannot clip at any value. 2.4 was preferred by eye for icon size,
but leaves the URL field too narrow.

## The actual fix

One bug in LSM's surface scaling, not per-app layout and not a per-device
configd value. Fixing it would let 2.4 work as intended in both hosts and make
the zoom workaround unnecessary. `luneos-device-config` has nothing to
contribute here: no generator can produce a value that avoids the crop.

## Atlas, separately

Independent of the crop, Atlas has room to improve at any scale:

  - `ActionBar.js:59` already wires `onAddressInputFocused: "hideButtons"` and
    `onAddressInputBlurred: "showButtons"`, so collapsing the toolbar while
    typing is implemented - it just does not appear to happen. Worth checking
    whether the handlers fire before writing anything new.
  - The same line declares `flex: 1` on the address bar, so it is meant to grow,
    yet it resolves to a fixed 382px. Hiding the trailing buttons and releasing
    the width took it from 115 to 323 device px at zoom 2.4, so the growth works
    once something lets it happen.
  - `.addressbar` has no width in CSS at all, only `margin: 0 10px`; the width
    comes from Enyo's HFlexBox layout.

## Not investigated

`export WAYLAND_DEBUG=1` in `run_browser_shell`, six lines above a plain `exec`
with no privilege drop, never reached the process environment - while a flag
added in the same edit did take effect. Something filters that environment.
Worth knowing before relying on env vars in that script.
