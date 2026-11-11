# Android pre-start fixtures

`50-stub-services` and `stub-sleep` are unmodified copies from
[`Herrie82/meta-smartphone`, commit `7131c1e809b900b9a98a960f1f2769af6fe28a99`](https://github.com/Herrie82/meta-smartphone/tree/7131c1e809b900b9a98a960f1f2769af6fe28a99/meta-android/recipes-core/android-system/android-system).
Their recipe declares GPL-3.0-only. They are test inputs, not installed by this layer.

The tests redirect the hook's `/var/lib/lxc/android` paths into a temporary
directory. Its selection and bind-mount logic otherwise runs unchanged.
The Linux integration tests use real bind mounts, including a read-only vendor
mount, inside a private mount namespace. They do not run Android or Trusty.
