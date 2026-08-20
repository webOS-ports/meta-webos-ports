# Atlas UI scaling on high-DPI panels — findings and proposed fixes

Investigated on a Pixel 3a (sargo, 1080x2220, 441 PPI) running LuneOS with the
Halium 9.0 GSI, August 2026. All figures measured over the browser's own remote
debugging port (`--remote-debugging-port=9999`), not inferred.

## Symptom

Atlas's chrome renders far smaller than the rest of the system: at rest it lays
out across the full 1080 device pixels at `devicePixelRatio: 1`, so a 50px
toolbar icon is ~2.3mm on a 441 PPI panel. Every other LuneOS surface is scaled.

## Why Atlas is unscaled and WebAppMgr is not

`webapp-mgr.sh` passes `--force-device-scale-factor`, taken from
`com.webos.surfacemanager.devicePixelRatio` via configd (2.4 on sargo).
`run_browser_shell` passes no scale flag at all. That is the whole difference.

## Why simply adding the flag does not work

Adding `--force-device-scale-factor` to `run_browser_shell` scales the content
but the compositor then shows only part of it: the buttons to the right of the
address bar disappear off the edge. Verified at 1.5 and 2.75; worse at higher
values.

The two hosts honour the same flag differently:

| | layoutViewport | css viewport | `devicePixelRatio` seen by JS |
|---|---|---|---|
| WebAppMgr @ 2.4 | 1080 x 2048 | 450 x 853 | **1** |
| browser_shell @ 1.5 | 1080 x 2046 | 720 x 1364 | **1.5** |

Both arithmetically fill 1080 device px. But WebAppMgr treats the factor purely
as a rasterisation scale - the page keeps a 450px logical screen and is drawn
2.4x crisper - while browser_shell exposes it as a real DPR and resizes its own
window into CSS units. The Wayland surface is then inconsistent with what LSM
expects, and the surplus is cropped rather than fitted.

Chromium's own geometry is correct throughout: `layoutViewport.clientWidth` is
exactly 1080, and a `Page.captureScreenshot` shows the complete toolbar with all
five buttons. The page is right; the presentation is not.

`--force-device-zoom-level` was tried as an alternative and is silently ignored:
the switch appears on the command line, and `innerWidth`, `devicePixelRatio` and
`zoom` are all unchanged. Chromium zoom is a per-origin preference, not a
startup switch.

**Conclusion: there is no LuneOS-side configuration that fixes this.** No
configd key, no `luneos-device-config` generator. Both fixes below belong in
Atlas.

## Fix 1 — scale the UI with CSS zoom

Setting `document.documentElement.style.zoom` scales layout *without* touching
the surface, so the compositor has nothing to mis-scale. Verified live:

| zoom | URL field width | toolbar right edge | all buttons visible |
|---|---|---|---|
| 1.0 (today) | 1080 css px wide UI | - | yes, but everything ~2.3mm |
| 1.5 | 382 device px | 702 / 1080 | yes |
| 1.75 | 279 | 599 / 1080 | yes |
| 2.0 | 202 | 522 / 1080 | yes |
| 2.4 | 115 | 432 / 1080 | yes |

Unlike the scale factor, the toolbar moves *further inside* the viewport as zoom
rises, so this cannot reproduce the clipping at any value. 2.4 was preferred by
eye: icons well sized, but the URL field is then too narrow ("Ente").

The value should be per-device and declared, not derived. Three independent
surfaces on this device (Calendar, the Enyo apps, Atlas) have now each rejected
a "natural" density/160 ratio.

## Fix 2 — collapse the toolbar while the address bar is active

At a zoom where the icons look right there is not enough room for a usable URL
field, because every child of the toolbar row is `flex: 0 1 auto` with a
hardcoded width - they can shrink but never grow. The fixed 50px buttons refuse
to shrink, so the address bar absorbs the entire shortfall.

Hiding the five trailing buttons while the field is focused, and releasing the
field's width, was measured live at zoom 2.4:

    buttons visible:  address bar 115 device px
    buttons hidden:   address bar 323 device px   (2.8x wider)

This is standard mobile browser behaviour. Note the field only grew once there
was free space *and* `width: auto` / `flex: 1 1 auto` were set on it and its
children - hiding the neighbours alone is not enough, the pinned width has to be
released too. It stopped at 436/1080, so the row itself is also constrained and
would need to be allowed to fill for the full width to be reclaimed.

## Not investigated

Why `export WAYLAND_DEBUG=1` in `run_browser_shell`, six lines above a plain
`exec` with no privilege drop, never reached the process environment - while a
flag added in the same edit did take effect. Something filters that
environment; worth knowing before relying on env vars in that script.
