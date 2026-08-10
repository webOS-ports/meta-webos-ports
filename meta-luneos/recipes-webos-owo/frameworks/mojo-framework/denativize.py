#!/usr/bin/env python3
"""Strip V8 "natives syntax" from legacy webOS builtin framework blobs.

The blobs under /usr/palm/frameworks/mojo/builtins were compiled into
LunaSysMgr's WebKit as V8 natives, so their trailers use privileged
runtime intrinsics (%SetProperty, %ToFastProperties, %FunctionSetPrototype)
and the natives-context `global` object. Neither is valid in an ordinary
script, so Chromium refuses to parse the file at all.

Every blob follows the same shape: plain JavaScript that declares the payload
as a top-level function/object, followed by a small Setup* trailer that
publishes it onto `global`. Loading the file as a classic script already
publishes top-level declarations onto `window`, so the trailer only needs to
be replaced with an explicit global assignment.
"""
import re
import sys
from pathlib import Path

# `%SetProperty(global, "<name>", <value>, <attrs>)` names the global the blob
# is meant to publish, and what it publishes.
SET_PROPERTY = re.compile(
    r'%SetProperty\(\s*global\s*,\s*["\'](?P<name>[\w$]+)["\']\s*,\s*(?P<value>[\w$]+)\s*,')


def denativize(src: str, path: Path) -> str:
    m = SET_PROPERTY.search(src)
    if not m:
        raise SystemExit(f"{path}: no %SetProperty(global, ...) trailer found")
    name, value = m.group("name"), m.group("value")

    # The trailer begins at the last top-level statement before the Setup
    # function that we must not keep: either `const $<name> = <name>;` or the
    # `function Setup...` declaration itself, whichever comes first.
    alias = re.search(r'^\s*(?:const|var)\s+\$%s\s*=' % re.escape(name), src, re.M)
    setup = re.search(r'^\s*function\s+Setup\w*\s*\(', src, re.M)
    starts = [m2.start() for m2 in (alias, setup) if m2]
    if not starts:
        raise SystemExit(f"{path}: found %SetProperty but no recognisable trailer start")
    cut = min(starts)

    body = src[:cut].rstrip()

    # `$<name>` is the natives-side alias for the payload; outside the natives
    # context the payload is reachable under its own declared name. The blobs
    # that publish an `exports` object build it inside the Setup* function, so
    # for those the whole wrapper has to be retained and merely re-published.
    published = value[1:] if value.startswith("$") else value
    if not re.search(r'^\s*(?:function|var|const|let)\s+%s\b' % re.escape(published), body, re.M):
        raise SystemExit(
            f"{path}: trailer publishes {value!r}, but no top-level declaration of "
            f"{published!r} survives the cut - needs manual handling")

    return (body + "\n\n"
            "/* de-nativized for Chromium: the original trailer published this via\n"
            " * %SetProperty(global, ...), a V8 natives intrinsic unavailable to\n"
            " * ordinary scripts. Classic-script evaluation already puts the\n"
            " * declaration on the global object; assign explicitly to be safe. */\n"
            f"if (typeof window !== 'undefined') {{ window.{name} = {published}; }}\n")


def main():
    if len(sys.argv) < 3:
        raise SystemExit(f"usage: {sys.argv[0]} <src.js> <dest.js>")
    src, dest = Path(sys.argv[1]), Path(sys.argv[2])
    out = denativize(src.read_text(encoding="utf-8", errors="surrogateescape"), src)
    dest.write_text(out, encoding="utf-8", errors="surrogateescape")
    print(f"{src.name}: {src.stat().st_size} -> {len(out.encode('utf-8', 'surrogateescape'))} bytes")


if __name__ == "__main__":
    main()
