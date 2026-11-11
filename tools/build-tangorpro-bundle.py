#!/usr/bin/env python3
"""Bundle the offline workaround and its sources; never build or flash firmware."""

import hashlib
import json
from pathlib import Path
import subprocess
import tarfile
import zipfile

ROOT = Path(__file__).resolve().parent.parent
RULE = Path("meta-luneos/recipes-core/android-system/android-system/stubbed-services-tangorpro")
FILES = [RULE, Path("meta-luneos/recipes-core/android-system/android-system_1.0.bbappend"),
         Path("docs/tangorpro-trusty-workaround.md"), Path("tools/stage-tangorpro-workaround.py"),
         Path("tools/check-tangorpro-persist.py"), Path("tests/fixtures/50-stub-services"),
         Path("tests/fixtures/stub-sleep"), Path("tests/fixtures/README.md"),
         Path("tests/fixtures/COPYING"), Path("tests/test_tangorpro_trusty.py")]


def main():
    output = ROOT / "dist"
    output.mkdir(exist_ok=True)
    revision = subprocess.check_output(["git", "rev-parse", "HEAD"], cwd=ROOT, text=True).strip()
    manifest = {"kind": "experimental-offline-rootfs-workaround", "source_revision": revision,
                "complete_os_image": False, "tablet_boot_tested": False,
                "vendor_build_observed": "CP3A.260905.009",
                "sha256": {str(p): hashlib.sha256((ROOT / p).read_bytes()).hexdigest() for p in FILES}}
    with zipfile.ZipFile(output / "tangorpro-trusty-workaround.zip", "w", zipfile.ZIP_DEFLATED) as archive:
        for path in FILES:
            archive.write(ROOT / path, str(path))
        archive.writestr("MANIFEST.json", json.dumps(manifest, indent=2) + "\n")
        archive.write(ROOT / "docs/tangorpro-trusty-workaround.md", "README.md")
    with tarfile.open(output / "tangorpro-trusty-overlay.tar.gz", "w:gz") as archive:
        archive.add(ROOT / RULE, arcname="var/lib/lxc/android/stubbed-services.d/tangorpro")
    hashes = "".join(f"{hashlib.sha256(p.read_bytes()).hexdigest()}  {p.name}\n"
                     for p in sorted(output.iterdir()) if p.suffix in (".zip", ".gz"))
    (output / "SHA256SUMS").write_text(hashes)
    print(f"Created experimental overlay bundle in {output}; not a complete OS image")


if __name__ == "__main__":
    main()
