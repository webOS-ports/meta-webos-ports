# Auto-generated from src/rust/Cargo.lock -- do not edit by hand.
# Regenerate with:  bitbake -c update_crates purple-presage
# (cargo-update-recipe-crates.bbclass, already in this layer, writes this file.)
#
# 443 crates.io crates. The 24 git-sourced ones are NOT here: crate:// only
# serves the registry, so each git dependency needs its own SRC_URI entry with a pinned
# revision. They are listed at the bottom for reference.

SRC_URI += " \
    crate://crates.io/aead/0.5.2 \
    crate://crates.io/aes/0.8.4 \
    crate://crates.io/aes-gcm/0.10.3 \
    crate://crates.io/aes-gcm-siv/0.11.1 \
    crate://crates.io/aho-corasick/1.1.4 \
    crate://crates.io/allocator-api2/0.2.21 \
    crate://crates.io/android_system_properties/0.1.5 \
    crate://crates.io/anyhow/1.0.103 \
    crate://crates.io/argon2/0.5.3 \
    crate://crates.io/asn1/0.23.1 \
    crate://crates.io/asn1_derive/0.23.1 \
    crate://crates.io/assert_matches/1.5.0 \
    crate://crates.io/async-trait/0.1.89 \
    crate://crates.io/async-tungstenite/0.28.2 \
    crate://crates.io/atoi/2.0.0 \
    crate://crates.io/atomic-polyfill/1.0.3 \
    crate://crates.io/atomic-waker/1.1.2 \
    crate://crates.io/auto_enums/0.8.10 \
    crate://crates.io/autocfg/1.5.1 \
    crate://crates.io/base64/0.22.1 \
    crate://crates.io/base64ct/1.8.3 \
    crate://crates.io/bincode/1.3.3 \
    crate://crates.io/bindgen/0.72.1 \
    crate://crates.io/bitflags/2.13.0 \
    crate://crates.io/bitstream-io/1.10.0 \
    crate://crates.io/blake2/0.10.6 \
    crate://crates.io/block-buffer/0.10.4 \
    crate://crates.io/block-buffer/0.12.1 \
    crate://crates.io/block-padding/0.3.3 \
    crate://crates.io/bumpalo/3.20.3 \
    crate://crates.io/byteorder/1.5.0 \
    crate://crates.io/bytes/1.12.1 \
    crate://crates.io/cbc/0.1.2 \
    crate://crates.io/cc/1.2.66 \
    crate://crates.io/cesu8/1.1.0 \
    crate://crates.io/cexpr/0.6.0 \
    crate://crates.io/cfg-if/1.0.4 \
    crate://crates.io/cfg_aliases/0.2.1 \
    crate://crates.io/chacha20/0.10.1 \
    crate://crates.io/chacha20/0.9.1 \
    crate://crates.io/chacha20poly1305/0.10.1 \
    crate://crates.io/chrono/0.4.45 \
    crate://crates.io/cipher/0.4.4 \
    crate://crates.io/clang-sys/1.8.1 \
    crate://crates.io/cmake/0.1.58 \
    crate://crates.io/cmov/0.5.4 \
    crate://crates.io/cobs/0.3.0 \
    crate://crates.io/combine/4.6.7 \
    crate://crates.io/concurrent-queue/2.5.0 \
    crate://crates.io/const-oid/0.9.6 \
    crate://crates.io/const-str/1.1.0 \
    crate://crates.io/convert_case/0.10.0 \
    crate://crates.io/core-foundation/0.10.1 \
    crate://crates.io/core-foundation-sys/0.8.7 \
    crate://crates.io/core-models/0.0.5 \
    crate://crates.io/cpufeatures/0.2.17 \
    crate://crates.io/cpufeatures/0.3.0 \
    crate://crates.io/crc/3.4.0 \
    crate://crates.io/crc-catalog/2.5.0 \
    crate://crates.io/critical-section/1.2.0 \
    crate://crates.io/crossbeam-deque/0.8.7 \
    crate://crates.io/crossbeam-epoch/0.9.20 \
    crate://crates.io/crossbeam-queue/0.3.13 \
    crate://crates.io/crossbeam-utils/0.8.22 \
    crate://crates.io/crypto-common/0.1.7 \
    crate://crates.io/crypto-common/0.2.2 \
    crate://crates.io/ctr/0.9.2 \
    crate://crates.io/ctutils/0.4.2 \
    crate://crates.io/data-encoding/2.11.0 \
    crate://crates.io/data-encoding-macro/0.1.20 \
    crate://crates.io/data-encoding-macro-internal/0.1.18 \
    crate://crates.io/der/0.7.10 \
    crate://crates.io/derive-where/1.6.1 \
    crate://crates.io/derive_more/2.1.1 \
    crate://crates.io/derive_more-impl/2.1.1 \
    crate://crates.io/derive_utils/0.16.0 \
    crate://crates.io/digest/0.10.7 \
    crate://crates.io/digest/0.11.3 \
    crate://crates.io/displaydoc/0.2.6 \
    crate://crates.io/dotenvy/0.15.7 \
    crate://crates.io/ed25519/2.2.3 \
    crate://crates.io/ed25519-dalek/2.2.0 \
    crate://crates.io/either/1.16.0 \
    crate://crates.io/embedded-io/0.4.0 \
    crate://crates.io/embedded-io/0.6.1 \
    crate://crates.io/equivalent/1.0.2 \
    crate://crates.io/errno/0.3.14 \
    crate://crates.io/etcetera/0.11.0 \
    crate://crates.io/event-listener/5.4.1 \
    crate://crates.io/fastrand/2.4.1 \
    crate://crates.io/fiat-crypto/0.2.9 \
    crate://crates.io/find-msvc-tools/0.1.9 \
    crate://crates.io/fixedbitset/0.5.7 \
    crate://crates.io/flume/0.12.0 \
    crate://crates.io/fnv/1.0.7 \
    crate://crates.io/foldhash/0.1.5 \
    crate://crates.io/foldhash/0.2.0 \
    crate://crates.io/foreign-types/0.5.0 \
    crate://crates.io/foreign-types-macros/0.2.4 \
    crate://crates.io/foreign-types-shared/0.3.1 \
    crate://crates.io/form_urlencoded/1.2.2 \
    crate://crates.io/fs_extra/1.3.0 \
    crate://crates.io/fslock/0.2.1 \
    crate://crates.io/futures/0.3.32 \
    crate://crates.io/futures-channel/0.3.32 \
    crate://crates.io/futures-core/0.3.32 \
    crate://crates.io/futures-executor/0.3.32 \
    crate://crates.io/futures-intrusive/0.5.0 \
    crate://crates.io/futures-io/0.3.32 \
    crate://crates.io/futures-macro/0.3.32 \
    crate://crates.io/futures-sink/0.3.32 \
    crate://crates.io/futures-task/0.3.32 \
    crate://crates.io/futures-util/0.3.32 \
    crate://crates.io/generic-array/0.14.7 \
    crate://crates.io/getrandom/0.2.17 \
    crate://crates.io/getrandom/0.3.4 \
    crate://crates.io/getrandom/0.4.3 \
    crate://crates.io/ghash/0.5.1 \
    crate://crates.io/glob/0.3.4 \
    crate://crates.io/h2/0.4.15 \
    crate://crates.io/hash32/0.2.1 \
    crate://crates.io/hashbrown/0.15.5 \
    crate://crates.io/hashbrown/0.16.1 \
    crate://crates.io/hashbrown/0.17.1 \
    crate://crates.io/hashlink/0.11.1 \
    crate://crates.io/hax-lib/0.3.6 \
    crate://crates.io/hax-lib-macros/0.3.6 \
    crate://crates.io/hax-lib-macros-types/0.3.6 \
    crate://crates.io/heapless/0.7.17 \
    crate://crates.io/heck/0.5.0 \
    crate://crates.io/hex/0.4.3 \
    crate://crates.io/hkdf/0.12.4 \
    crate://crates.io/hkdf/0.13.0 \
    crate://crates.io/hmac/0.12.1 \
    crate://crates.io/hmac/0.13.0 \
    crate://crates.io/home/0.5.12 \
    crate://crates.io/hpke-rs/0.6.1 \
    crate://crates.io/hpke-rs-crypto/0.6.1 \
    crate://crates.io/http/1.4.2 \
    crate://crates.io/http-body/1.0.1 \
    crate://crates.io/http-body-util/0.1.3 \
    crate://crates.io/httparse/1.10.1 \
    crate://crates.io/httpdate/1.0.3 \
    crate://crates.io/hybrid-array/0.4.13 \
    crate://crates.io/hyper/1.10.1 \
    crate://crates.io/hyper-rustls/0.27.9 \
    crate://crates.io/hyper-util/0.1.20 \
    crate://crates.io/iana-time-zone/0.1.65 \
    crate://crates.io/iana-time-zone-haiku/0.1.2 \
    crate://crates.io/icu_collections/2.2.0 \
    crate://crates.io/icu_locale_core/2.2.0 \
    crate://crates.io/icu_normalizer/2.2.0 \
    crate://crates.io/icu_normalizer_data/2.2.0 \
    crate://crates.io/icu_properties/2.2.0 \
    crate://crates.io/icu_properties_data/2.2.0 \
    crate://crates.io/icu_provider/2.2.0 \
    crate://crates.io/idna/1.1.0 \
    crate://crates.io/idna_adapter/1.2.2 \
    crate://crates.io/indexmap/2.14.0 \
    crate://crates.io/inout/0.1.4 \
    crate://crates.io/ipnet/2.12.0 \
    crate://crates.io/itertools/0.13.0 \
    crate://crates.io/itertools/0.14.0 \
    crate://crates.io/itertools/0.15.0 \
    crate://crates.io/itoa/1.0.18 \
    crate://crates.io/jni/0.21.1 \
    crate://crates.io/jni-sys/0.3.1 \
    crate://crates.io/jni-sys/0.4.1 \
    crate://crates.io/jni-sys-macros/0.4.1 \
    crate://crates.io/js-sys/0.3.103 \
    crate://crates.io/lazy_static/1.5.0 \
    crate://crates.io/libc/0.2.186 \
    crate://crates.io/libcrux-hacl-rs/0.0.4 \
    crate://crates.io/libcrux-hmac/0.0.6 \
    crate://crates.io/libcrux-intrinsics/0.0.6 \
    crate://crates.io/libcrux-macros/0.0.3 \
    crate://crates.io/libcrux-ml-kem/0.0.8 \
    crate://crates.io/libcrux-platform/0.0.3 \
    crate://crates.io/libcrux-secrets/0.0.5 \
    crate://crates.io/libcrux-sha2/0.0.6 \
    crate://crates.io/libcrux-sha3/0.0.8 \
    crate://crates.io/libcrux-traits/0.0.6 \
    crate://crates.io/libloading/0.8.9 \
    crate://crates.io/linked-hash-map/0.5.6 \
    crate://crates.io/linux-raw-sys/0.12.1 \
    crate://crates.io/linux-raw-sys/0.4.15 \
    crate://crates.io/litemap/0.8.2 \
    crate://crates.io/lock_api/0.4.14 \
    crate://crates.io/log/0.4.33 \
    crate://crates.io/lru-cache/0.1.2 \
    crate://crates.io/lru-slab/0.1.2 \
    crate://crates.io/matchers/0.2.0 \
    crate://crates.io/md-5/0.11.0 \
    crate://crates.io/memchr/2.8.3 \
    crate://crates.io/mime/0.3.17 \
    crate://crates.io/mime_guess/2.0.5 \
    crate://crates.io/minimal-lexical/0.2.1 \
    crate://crates.io/mio/1.2.1 \
    crate://crates.io/multimap/0.10.1 \
    crate://crates.io/nom/7.1.3 \
    crate://crates.io/nonzero_ext/0.3.0 \
    crate://crates.io/nu-ansi-term/0.50.3 \
    crate://crates.io/num-bigint/0.4.8 \
    crate://crates.io/num-integer/0.1.46 \
    crate://crates.io/num-traits/0.2.19 \
    crate://crates.io/num_enum/0.7.6 \
    crate://crates.io/num_enum_derive/0.7.6 \
    crate://crates.io/once_cell/1.21.4 \
    crate://crates.io/oncemutex/0.1.1 \
    crate://crates.io/opaque-debug/0.3.1 \
    crate://crates.io/openssl-macros/0.1.1 \
    crate://crates.io/openssl-probe/0.2.1 \
    crate://crates.io/parking/2.2.1 \
    crate://crates.io/parking_lot/0.12.5 \
    crate://crates.io/parking_lot_core/0.9.12 \
    crate://crates.io/partial-default/0.1.0 \
    crate://crates.io/partial-default-derive/0.1.0 \
    crate://crates.io/password-hash/0.5.0 \
    crate://crates.io/pastey/0.2.3 \
    crate://crates.io/percent-encoding/2.3.2 \
    crate://crates.io/petgraph/0.8.3 \
    crate://crates.io/phonenumber/0.3.10+9.0.33 \
    crate://crates.io/pin-project/1.1.13 \
    crate://crates.io/pin-project-internal/1.1.13 \
    crate://crates.io/pin-project-lite/0.2.17 \
    crate://crates.io/pkcs8/0.10.2 \
    crate://crates.io/pkg-config/0.3.33 \
    crate://crates.io/poly1305/0.8.0 \
    crate://crates.io/polyval/0.6.2 \
    crate://crates.io/postcard/1.1.3 \
    crate://crates.io/potential_utf/0.1.5 \
    crate://crates.io/ppv-lite86/0.2.21 \
    crate://crates.io/prettyplease/0.2.37 \
    crate://crates.io/proc-macro-crate/3.5.0 \
    crate://crates.io/proc-macro-error-attr2/2.0.0 \
    crate://crates.io/proc-macro-error2/2.0.1 \
    crate://crates.io/proc-macro2/1.0.106 \
    crate://crates.io/prost/0.14.4 \
    crate://crates.io/prost-build/0.14.4 \
    crate://crates.io/prost-derive/0.14.4 \
    crate://crates.io/prost-types/0.14.4 \
    crate://crates.io/protobuf/3.7.2 \
    crate://crates.io/protobuf-codegen/3.7.2 \
    crate://crates.io/protobuf-parse/3.7.2 \
    crate://crates.io/protobuf-support/3.7.2 \
    crate://crates.io/quick-xml/0.41.0 \
    crate://crates.io/quinn/0.11.11 \
    crate://crates.io/quinn-proto/0.11.16 \
    crate://crates.io/quinn-udp/0.5.15 \
    crate://crates.io/quote/1.0.46 \
    crate://crates.io/r-efi/5.3.0 \
    crate://crates.io/r-efi/6.0.0 \
    crate://crates.io/rand/0.10.2 \
    crate://crates.io/rand/0.8.6 \
    crate://crates.io/rand/0.9.4 \
    crate://crates.io/rand_chacha/0.3.1 \
    crate://crates.io/rand_chacha/0.9.0 \
    crate://crates.io/rand_core/0.10.1 \
    crate://crates.io/rand_core/0.6.4 \
    crate://crates.io/rand_core/0.9.5 \
    crate://crates.io/rand_pcg/0.10.2 \
    crate://crates.io/rangemap/1.7.1 \
    crate://crates.io/rayon/1.12.0 \
    crate://crates.io/rayon-core/1.13.0 \
    crate://crates.io/redox_syscall/0.5.18 \
    crate://crates.io/regex/1.13.0 \
    crate://crates.io/regex-automata/0.4.15 \
    crate://crates.io/regex-cache/0.2.1 \
    crate://crates.io/regex-syntax/0.6.29 \
    crate://crates.io/regex-syntax/0.8.11 \
    crate://crates.io/reqwest/0.12.28 \
    crate://crates.io/reqwest-websocket/0.4.4 \
    crate://crates.io/ring/0.17.14 \
    crate://crates.io/rustc-hash/2.1.3 \
    crate://crates.io/rustc_version/0.4.1 \
    crate://crates.io/rustix/0.38.44 \
    crate://crates.io/rustix/1.1.4 \
    crate://crates.io/rustls/0.23.41 \
    crate://crates.io/rustls-native-certs/0.8.4 \
    crate://crates.io/rustls-pki-types/1.15.0 \
    crate://crates.io/rustls-platform-verifier/0.5.3 \
    crate://crates.io/rustls-platform-verifier-android/0.1.1 \
    crate://crates.io/rustls-webpki/0.103.13 \
    crate://crates.io/rustversion/1.0.23 \
    crate://crates.io/ryu/1.0.23 \
    crate://crates.io/same-file/1.0.6 \
    crate://crates.io/schannel/0.1.29 \
    crate://crates.io/scopeguard/1.2.0 \
    crate://crates.io/security-framework/3.7.0 \
    crate://crates.io/security-framework-sys/2.17.0 \
    crate://crates.io/semver/1.0.28 \
    crate://crates.io/serde/1.0.228 \
    crate://crates.io/serde_core/1.0.228 \
    crate://crates.io/serde_derive/1.0.228 \
    crate://crates.io/serde_json/1.0.150 \
    crate://crates.io/serde_urlencoded/0.7.1 \
    crate://crates.io/sha1/0.10.6 \
    crate://crates.io/sha1/0.11.0 \
    crate://crates.io/sha2/0.10.9 \
    crate://crates.io/sha2/0.11.0 \
    crate://crates.io/sha2-asm/0.6.4 \
    crate://crates.io/sharded-slab/0.1.7 \
    crate://crates.io/shlex/1.3.0 \
    crate://crates.io/shlex/2.0.1 \
    crate://crates.io/signature/2.2.0 \
    crate://crates.io/slab/0.4.12 \
    crate://crates.io/smallvec/1.15.2 \
    crate://crates.io/snow/0.10.0 \
    crate://crates.io/socket2/0.6.4 \
    crate://crates.io/sorted-vec/0.8.10 \
    crate://crates.io/spin/0.9.8 \
    crate://crates.io/spki/0.7.3 \
    crate://crates.io/sqlx/0.9.0 \
    crate://crates.io/sqlx-core/0.9.0 \
    crate://crates.io/sqlx-macros/0.9.0 \
    crate://crates.io/sqlx-macros-core/0.9.0 \
    crate://crates.io/sqlx-mysql/0.9.0 \
    crate://crates.io/sqlx-postgres/0.9.0 \
    crate://crates.io/sqlx-sqlite/0.9.0 \
    crate://crates.io/stable_deref_trait/1.2.1 \
    crate://crates.io/static_assertions/1.1.0 \
    crate://crates.io/stringprep/0.1.5 \
    crate://crates.io/strum/0.27.2 \
    crate://crates.io/strum_macros/0.27.2 \
    crate://crates.io/subtle/2.6.1 \
    crate://crates.io/syn/2.0.118 \
    crate://crates.io/syn/3.0.3 \
    crate://crates.io/sync_wrapper/1.0.2 \
    crate://crates.io/synstructure/0.13.2 \
    crate://crates.io/tempfile/3.27.0 \
    crate://crates.io/thiserror/1.0.69 \
    crate://crates.io/thiserror/2.0.18 \
    crate://crates.io/thiserror-impl/1.0.69 \
    crate://crates.io/thiserror-impl/2.0.18 \
    crate://crates.io/thread_local/1.1.9 \
    crate://crates.io/tinystr/0.8.3 \
    crate://crates.io/tinyvec/1.11.0 \
    crate://crates.io/tinyvec_macros/0.1.1 \
    crate://crates.io/tokio/1.52.3 \
    crate://crates.io/tokio-macros/2.7.0 \
    crate://crates.io/tokio-rustls/0.26.4 \
    crate://crates.io/tokio-socks/0.5.3 \
    crate://crates.io/tokio-stream/0.1.18 \
    crate://crates.io/tokio-tungstenite/0.28.0 \
    crate://crates.io/tokio-util/0.7.18 \
    crate://crates.io/toml_datetime/1.1.1+spec-1.1.0 \
    crate://crates.io/toml_edit/0.25.12+spec-1.1.0 \
    crate://crates.io/toml_parser/1.1.2+spec-1.1.0 \
    crate://crates.io/tonic/0.14.6 \
    crate://crates.io/tower/0.5.3 \
    crate://crates.io/tower-http/0.6.11 \
    crate://crates.io/tower-layer/0.3.3 \
    crate://crates.io/tower-service/0.3.3 \
    crate://crates.io/tracing/0.1.44 \
    crate://crates.io/tracing-attributes/0.1.31 \
    crate://crates.io/tracing-core/0.1.36 \
    crate://crates.io/tracing-futures/0.2.5 \
    crate://crates.io/tracing-log/0.2.0 \
    crate://crates.io/tracing-subscriber/0.3.23 \
    crate://crates.io/try-lock/0.2.5 \
    crate://crates.io/tungstenite/0.24.0 \
    crate://crates.io/tungstenite/0.28.0 \
    crate://crates.io/typenum/1.20.1 \
    crate://crates.io/unicase/2.9.0 \
    crate://crates.io/unicode-bidi/0.3.18 \
    crate://crates.io/unicode-ident/1.0.24 \
    crate://crates.io/unicode-normalization/0.1.25 \
    crate://crates.io/unicode-properties/0.1.4 \
    crate://crates.io/unicode-segmentation/1.13.3 \
    crate://crates.io/unicode-xid/0.2.6 \
    crate://crates.io/universal-hash/0.5.1 \
    crate://crates.io/untrusted/0.9.0 \
    crate://crates.io/url/2.5.8 \
    crate://crates.io/utf-8/0.7.6 \
    crate://crates.io/utf8_iter/1.0.4 \
    crate://crates.io/uuid/1.23.4 \
    crate://crates.io/valuable/0.1.1 \
    crate://crates.io/vcpkg/0.2.15 \
    crate://crates.io/version_check/0.9.5 \
    crate://crates.io/visibility/0.1.1 \
    crate://crates.io/walkdir/2.5.0 \
    crate://crates.io/want/0.3.1 \
    crate://crates.io/wasi/0.11.1+wasi-snapshot-preview1 \
    crate://crates.io/wasip2/1.0.4+wasi-0.2.12 \
    crate://crates.io/wasm-bindgen/0.2.126 \
    crate://crates.io/wasm-bindgen-futures/0.4.76 \
    crate://crates.io/wasm-bindgen-macro/0.2.126 \
    crate://crates.io/wasm-bindgen-macro-support/0.2.126 \
    crate://crates.io/wasm-bindgen-shared/0.2.126 \
    crate://crates.io/wasm-streams/0.4.2 \
    crate://crates.io/web-sys/0.3.103 \
    crate://crates.io/web-time/1.1.0 \
    crate://crates.io/webpki-root-certs/0.26.11 \
    crate://crates.io/webpki-root-certs/1.0.9 \
    crate://crates.io/which/4.4.2 \
    crate://crates.io/whoami/2.1.2 \
    crate://crates.io/winapi/0.3.9 \
    crate://crates.io/winapi-i686-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi-util/0.1.11 \
    crate://crates.io/winapi-x86_64-pc-windows-gnu/0.4.0 \
    crate://crates.io/windows-core/0.62.2 \
    crate://crates.io/windows-implement/0.60.2 \
    crate://crates.io/windows-interface/0.59.3 \
    crate://crates.io/windows-link/0.2.1 \
    crate://crates.io/windows-result/0.4.1 \
    crate://crates.io/windows-strings/0.5.1 \
    crate://crates.io/windows-sys/0.45.0 \
    crate://crates.io/windows-sys/0.52.0 \
    crate://crates.io/windows-sys/0.59.0 \
    crate://crates.io/windows-sys/0.61.2 \
    crate://crates.io/windows-targets/0.42.2 \
    crate://crates.io/windows-targets/0.52.6 \
    crate://crates.io/windows_aarch64_gnullvm/0.42.2 \
    crate://crates.io/windows_aarch64_gnullvm/0.52.6 \
    crate://crates.io/windows_aarch64_msvc/0.42.2 \
    crate://crates.io/windows_aarch64_msvc/0.52.6 \
    crate://crates.io/windows_i686_gnu/0.42.2 \
    crate://crates.io/windows_i686_gnu/0.52.6 \
    crate://crates.io/windows_i686_gnullvm/0.52.6 \
    crate://crates.io/windows_i686_msvc/0.42.2 \
    crate://crates.io/windows_i686_msvc/0.52.6 \
    crate://crates.io/windows_x86_64_gnu/0.42.2 \
    crate://crates.io/windows_x86_64_gnu/0.52.6 \
    crate://crates.io/windows_x86_64_gnullvm/0.42.2 \
    crate://crates.io/windows_x86_64_gnullvm/0.52.6 \
    crate://crates.io/windows_x86_64_msvc/0.42.2 \
    crate://crates.io/windows_x86_64_msvc/0.52.6 \
    crate://crates.io/winnow/1.0.3 \
    crate://crates.io/wit-bindgen/0.57.1 \
    crate://crates.io/writeable/0.6.3 \
    crate://crates.io/x25519-dalek/2.0.1 \
    crate://crates.io/yoke/0.8.3 \
    crate://crates.io/yoke-derive/0.8.2 \
    crate://crates.io/zerocopy/0.8.54 \
    crate://crates.io/zerocopy-derive/0.8.54 \
    crate://crates.io/zerofrom/0.1.8 \
    crate://crates.io/zerofrom-derive/0.1.7 \
    crate://crates.io/zeroize/1.9.0 \
    crate://crates.io/zeroize_derive/1.5.0 \
    crate://crates.io/zerotrie/0.2.4 \
    crate://crates.io/zerovec/0.11.6 \
    crate://crates.io/zerovec-derive/0.11.3 \
    crate://crates.io/zmij/1.0.21 \
"

# --- git-sourced dependencies, for reference ---------------------------------------
# These come from signalapp/whisperfish forks that are not published to crates.io, so
# they must be fetched as git repos and wired in with a cargo config patch section.
#   attest 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   boring 5.0.2
#     https://github.com/signalapp/boring  rev=2c157897feea0c54f5eca93407a157fa90703b5f
#   boring-sys 5.0.2
#     https://github.com/signalapp/boring  rev=2c157897feea0c54f5eca93407a157fa90703b5f
#   curve25519-dalek 4.1.3
#     https://github.com/signalapp/curve25519-dalek  rev=7c6d34756355a3566a704da84dce7b1c039a6572
#   curve25519-dalek-derive 0.1.1
#     https://github.com/signalapp/curve25519-dalek  rev=7c6d34756355a3566a704da84dce7b1c039a6572
#   libsignal-account-keys 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   libsignal-core 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   libsignal-debug 0.94.4
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   libsignal-keytrans 0.0.1
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   libsignal-net 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   libsignal-net-infra 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   libsignal-protocol 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   libsignal-service 0.1.0
#     https://github.com/whisperfish/libsignal-service-rs  rev=fd481655d481388ebfe3a1fdb85a668b4876d592
#   libsignal-svrb 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   libsqlite3-sys 0.36.0
#     https://github.com/whisperfish/rusqlite  rev=2a42b3354c9194700d08aa070f70a131a470e7dc
#   mime-sniffer 0.1.2
#     https://github.com/kamadorueda/rust-mime-sniffer  rev=0053c4330ca159db118e57435ea781bdf46158ed
#   poksho 0.7.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   presage-store-sqlite 0.8.0-dev
#     https://github.com/whisperfish/presage  rev=63482efd0cbdc0780baf0650517c7d55f1cac05d
#   signal-crypto 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   spqr 1.5.1
#     https://github.com/signalapp/SparsePostQuantumRatchet.git  rev=f2589fef855c10f39d72634dab3d14654dd410bf
#   tokio-boring 5.0.2
#     https://github.com/signalapp/boring  rev=2c157897feea0c54f5eca93407a157fa90703b5f
#   usernames 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   zkcredential 0.1.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f
#   zkgroup 0.9.0
#     https://github.com/signalapp/libsignal  rev=46d867c986f66201e34e7ae20ce423eec742bf3f

# Checksums for the crates above. crate:// entries need an explicit sha256sum or do_fetch
# refuses with "There was some missing checksums in the recipe". These are the values
# bitbake itself printed on that first failed fetch.
SRC_URI[aead-0.5.2.sha256sum] = "d122413f284cf2d62fb1b7db97e02edb8cda96d769b16e443a4f6195e35662b0"
SRC_URI[aes-0.8.4.sha256sum] = "b169f7a6d4742236a0a00c541b845991d0ac43e546831af1249753ab4c3aa3a0"
SRC_URI[aes-gcm-0.10.3.sha256sum] = "831010a0f742e1209b3bcea8fab6a8e149051ba6099432c8cb2cc117dec3ead1"
SRC_URI[aes-gcm-siv-0.11.1.sha256sum] = "ae0784134ba9375416d469ec31e7c5f9fa94405049cf08c5ce5b4698be673e0d"
SRC_URI[aho-corasick-1.1.4.sha256sum] = "ddd31a130427c27518df266943a5308ed92d4b226cc639f5a8f1002816174301"
SRC_URI[allocator-api2-0.2.21.sha256sum] = "683d7910e743518b0e34f1186f92494becacb047c7b6bf616c96772180fef923"
SRC_URI[android_system_properties-0.1.5.sha256sum] = "819e7219dbd41043ac279b19830f2efc897156490d7fd6ea916720117ee66311"
SRC_URI[anyhow-1.0.103.sha256sum] = "2a4385e2e34eb35d6b3efe798b9eb88096925d87726c0798709bf56d9ed84af3"
SRC_URI[argon2-0.5.3.sha256sum] = "3c3610892ee6e0cbce8ae2700349fcf8f98adb0dbfbee85aec3c9179d29cc072"
SRC_URI[asn1-0.23.1.sha256sum] = "ca7444bb097e58fd76ae2b3929d8f5bdb3748000eb51b79141ed7c7b6a9dcdbc"
SRC_URI[asn1_derive-0.23.1.sha256sum] = "d0dcd11549aa9d043141c7d54839771cbf9a8d38084808c6fa00e62b46b114f0"
SRC_URI[assert_matches-1.5.0.sha256sum] = "9b34d609dfbaf33d6889b2b7106d3ca345eacad44200913df5ba02bfd31d2ba9"
SRC_URI[async-trait-0.1.89.sha256sum] = "9035ad2d096bed7955a320ee7e2230574d28fd3c3a0f186cbea1ff3c7eed5dbb"
SRC_URI[async-tungstenite-0.28.2.sha256sum] = "1c348fb0b6d132c596eca3dcd941df48fb597aafcb07a738ec41c004b087dc99"
SRC_URI[atoi-2.0.0.sha256sum] = "f28d99ec8bfea296261ca1af174f24225171fea9664ba9003cbebee704810528"
SRC_URI[atomic-polyfill-1.0.3.sha256sum] = "8cf2bce30dfe09ef0bfaef228b9d414faaf7e563035494d7fe092dba54b300f4"
SRC_URI[atomic-waker-1.1.2.sha256sum] = "1505bd5d3d116872e7271a6d4e16d81d0c8570876c8de68093a09ac269d8aac0"
SRC_URI[auto_enums-0.8.10.sha256sum] = "3091d68264354f211516b91dce6f71046e444fab1867716035f736667243affb"
SRC_URI[autocfg-1.5.1.sha256sum] = "f2032f911046de80f0a198e0901378627c33f59ea0ac00e363d481118bd70a53"
SRC_URI[base64-0.22.1.sha256sum] = "72b3254f16251a8381aa12e40e3c4d2f0199f8c6508fbecb9d91f575e0fbb8c6"
SRC_URI[base64ct-1.8.3.sha256sum] = "2af50177e190e07a26ab74f8b1efbfe2ef87da2116221318cb1c2e82baf7de06"
SRC_URI[bincode-1.3.3.sha256sum] = "b1f45e9417d87227c7a56d22e471c6206462cba514c7590c09aff4cf6d1ddcad"
SRC_URI[bindgen-0.72.1.sha256sum] = "993776b509cfb49c750f11b8f07a46fa23e0a1386ffc01fb1e7d343efc387895"
SRC_URI[bitflags-2.13.0.sha256sum] = "b4388bee8683e3d04af747c73422af53102d2bd24d9eadb6cbc100baef4b43f8"
SRC_URI[bitstream-io-1.10.0.sha256sum] = "e445576659fd04a57b44cbd00aa37aaa815ebefa0aa3cb677a6b5e63d883074f"
SRC_URI[blake2-0.10.6.sha256sum] = "46502ad458c9a52b69d4d4d32775c788b7a1b85e8bc9d482d92250fc0e3f8efe"
SRC_URI[block-buffer-0.10.4.sha256sum] = "3078c7629b62d3f0439517fa394996acacc5cbc91c5a20d8c658e77abd503a71"
SRC_URI[block-buffer-0.12.1.sha256sum] = "d2f6c7dbe95a6ed67ad9f18e57daf93a2f034c524b99fd2b76d18fdfeb6660aa"
SRC_URI[block-padding-0.3.3.sha256sum] = "a8894febbff9f758034a5b8e12d87918f56dfc64a8e1fe757d65e29041538d93"
SRC_URI[bumpalo-3.20.3.sha256sum] = "72f5acc6cb2ba439de613abc23857ec3d78374d8ed5ac84e9d11336e87da8649"
SRC_URI[byteorder-1.5.0.sha256sum] = "1fd0f2584146f6f2ef48085050886acf353beff7305ebd1ae69500e27c67f64b"
SRC_URI[bytes-1.12.1.sha256sum] = "fc652a48c352aef3ea3aed32080501cf3ef6ed5da78602a020c991775b0aff04"
SRC_URI[cbc-0.1.2.sha256sum] = "26b52a9543ae338f279b96b0b9fed9c8093744685043739079ce85cd58f289a6"
SRC_URI[cc-1.2.66.sha256sum] = "f5d6cac793997bd970000024b2934968efe83b382de4fdcf4fcb46b6ee4ad996"
SRC_URI[cesu8-1.1.0.sha256sum] = "6d43a04d8753f35258c91f8ec639f792891f748a1edbd759cf1dcea3382ad83c"
SRC_URI[cexpr-0.6.0.sha256sum] = "6fac387a98bb7c37292057cffc56d62ecb629900026402633ae9160df93a8766"
SRC_URI[cfg-if-1.0.4.sha256sum] = "9330f8b2ff13f34540b44e946ef35111825727b38d33286ef986142615121801"
SRC_URI[cfg_aliases-0.2.1.sha256sum] = "613afe47fcd5fac7ccf1db93babcb082c5994d996f20b8b159f2ad1658eb5724"
SRC_URI[chacha20-0.10.1.sha256sum] = "d524456ba66e72eb8b115ff89e01e497f8e6d11d78b70b1aa13c0fbd97540a81"
SRC_URI[chacha20-0.9.1.sha256sum] = "c3613f74bd2eac03dad61bd53dbe620703d4371614fe0bc3b9f04dd36fe4e818"
SRC_URI[chacha20poly1305-0.10.1.sha256sum] = "10cd79432192d1c0f4e1a0fef9527696cc039165d729fb41b3f4f4f354c2dc35"
SRC_URI[chrono-0.4.45.sha256sum] = "1aa79e62e7697b8e29b513a68abacf485adcd1fe8284a4316c5ae868e6633327"
SRC_URI[cipher-0.4.4.sha256sum] = "773f3b9af64447d2ce9850330c473515014aa235e6a783b02db81ff39e4a3dad"
SRC_URI[clang-sys-1.8.1.sha256sum] = "0b023947811758c97c59bf9d1c188fd619ad4718dcaa767947df1cadb14f39f4"
SRC_URI[cmake-0.1.58.sha256sum] = "c0f78a02292a74a88ac736019ab962ece0bc380e3f977bf72e376c5d78ff0678"
SRC_URI[cmov-0.5.4.sha256sum] = "0c9ea0ac24bc397ab3c98583a3c9ba74fa56b09a4449bbe172b9b1ddb016027a"
SRC_URI[cobs-0.3.0.sha256sum] = "0fa961b519f0b462e3a3b4a34b64d119eeaca1d59af726fe450bbba07a9fc0a1"
SRC_URI[combine-4.6.7.sha256sum] = "ba5a308b75df32fe02788e748662718f03fde005016435c444eea572398219fd"
SRC_URI[concurrent-queue-2.5.0.sha256sum] = "4ca0197aee26d1ae37445ee532fefce43251d24cc7c166799f4d46817f1d3973"
SRC_URI[const-oid-0.9.6.sha256sum] = "c2459377285ad874054d797f3ccebf984978aa39129f6eafde5cdc8315b612f8"
SRC_URI[const-str-1.1.0.sha256sum] = "18f12cc9948ed9604230cdddc7c86e270f9401ccbe3c2e98a4378c5e7632212f"
SRC_URI[convert_case-0.10.0.sha256sum] = "633458d4ef8c78b72454de2d54fd6ab2e60f9e02be22f3c6104cdc8a4e0fceb9"
SRC_URI[core-foundation-0.10.1.sha256sum] = "b2a6cd9ae233e7f62ba4e9353e81a88df7fc8a5987b8d445b4d90c879bd156f6"
SRC_URI[core-foundation-sys-0.8.7.sha256sum] = "773648b94d0e5d620f64f280777445740e61fe701025087ec8b57f45c791888b"
SRC_URI[core-models-0.0.5.sha256sum] = "657f625ff361906f779745d08375ae3cc9fef87a35fba5f22874cf773010daf4"
SRC_URI[cpufeatures-0.2.17.sha256sum] = "59ed5838eebb26a2bb2e58f6d5b5316989ae9d08bab10e0e6d103e656d1b0280"
SRC_URI[cpufeatures-0.3.0.sha256sum] = "8b2a41393f66f16b0823bb79094d54ac5fbd34ab292ddafb9a0456ac9f87d201"
SRC_URI[crc-3.4.0.sha256sum] = "5eb8a2a1cd12ab0d987a5d5e825195d372001a4094a0376319d5a0ad71c1ba0d"
SRC_URI[crc-catalog-2.5.0.sha256sum] = "217698eaf96b4a3f0bc4f3662aaa55bdf913cd54d7204591faa790070c6d0853"
SRC_URI[critical-section-1.2.0.sha256sum] = "790eea4361631c5e7d22598ecd5723ff611904e3344ce8720784c93e3d83d40b"
SRC_URI[crossbeam-deque-0.8.7.sha256sum] = "5181e0de7b61eb03a81e347d6dd8797bae9da5146707b51077e2d71a54ec0ceb"
SRC_URI[crossbeam-epoch-0.9.20.sha256sum] = "2d6914041f254d6e9176c01941b21115dcfb7089e55135a35411081bd106ef3f"
SRC_URI[crossbeam-queue-0.3.13.sha256sum] = "803d13fb3b09d88be9f4dbc29062c66b19bf7170867ceb746d2a8689bf6c7a26"
SRC_URI[crossbeam-utils-0.8.22.sha256sum] = "61803da095bee82a81bb1a452ecc25d3b2f1416d1897eb86430c6159ef717c17"
SRC_URI[crypto-common-0.1.7.sha256sum] = "78c8292055d1c1df0cce5d180393dc8cce0abec0a7102adb6c7b1eef6016d60a"
SRC_URI[crypto-common-0.2.2.sha256sum] = "ce6e4c961d6cd6c9a86db418387425e8bdeaf05b3c8bc1411e6dca4c252f1453"
SRC_URI[ctr-0.9.2.sha256sum] = "0369ee1ad671834580515889b80f2ea915f23b8be8d0daa4bbaf2ac5c7590835"
SRC_URI[ctutils-0.4.2.sha256sum] = "7d5515a3834141de9eafb9717ad39eea8247b5674e6066c404e8c4b365d2a29e"
SRC_URI[data-encoding-2.11.0.sha256sum] = "a4ae5f15dda3c708c0ade84bfee31ccab44a3da4f88015ed22f63732abe300c8"
SRC_URI[data-encoding-macro-0.1.20.sha256sum] = "3259c913752a86488b501ed8680446a5ed2d5aeac6e596cb23ba3800768ea32c"
SRC_URI[data-encoding-macro-internal-0.1.18.sha256sum] = "ccc2776f0c61eca1ca32528f85548abd1a4be8fb53d1b21c013e4f18da1e7090"
SRC_URI[der-0.7.10.sha256sum] = "e7c1832837b905bbfb5101e07cc24c8deddf52f93225eee6ead5f4d63d53ddcb"
SRC_URI[derive-where-1.6.1.sha256sum] = "d08b3a0bcc0d079199cd476b2cae8435016ec11d1c0986c6901c5ac223041534"
SRC_URI[derive_more-2.1.1.sha256sum] = "d751e9e49156b02b44f9c1815bcb94b984cdcc4396ecc32521c739452808b134"
SRC_URI[derive_more-impl-2.1.1.sha256sum] = "799a97264921d8623a957f6c3b9011f3b5492f557bbb7a5a19b7fa6d06ba8dcb"
SRC_URI[derive_utils-0.16.0.sha256sum] = "dc05a5d33db20c784f873e84934ad94bb209a090987ac5f62fede2c178234f23"
SRC_URI[digest-0.10.7.sha256sum] = "9ed9a281f7bc9b7576e61468ba615a66a5c8cfdff42420a70aa82701a3b1e292"
SRC_URI[digest-0.11.3.sha256sum] = "f1dd6dbb5841937940781866fa1281a1ff7bd3bf827091440879f9994983d5c2"
SRC_URI[displaydoc-0.2.6.sha256sum] = "1ac70aa55017e108007fbaf5aa0f54b021c98f92ff8af59d42eda9da96e3dd4f"
SRC_URI[dotenvy-0.15.7.sha256sum] = "1aaf95b3e5c8f23aa320147307562d361db0ae0d51242340f558153b4eb2439b"
SRC_URI[ed25519-2.2.3.sha256sum] = "115531babc129696a58c64a4fef0a8bf9e9698629fb97e9e40767d235cfbcd53"
SRC_URI[ed25519-dalek-2.2.0.sha256sum] = "70e796c081cee67dc755e1a36a0a172b897fab85fc3f6bc48307991f64e4eca9"
SRC_URI[either-1.16.0.sha256sum] = "91622ff5e7162018101f2fea40d6ebf4a78bbe5a49736a2020649edf9693679e"
SRC_URI[embedded-io-0.4.0.sha256sum] = "ef1a6892d9eef45c8fa6b9e0086428a2cca8491aca8f787c534a3d6d0bcb3ced"
SRC_URI[embedded-io-0.6.1.sha256sum] = "edd0f118536f44f5ccd48bcb8b111bdc3de888b58c74639dfb034a357d0f206d"
SRC_URI[equivalent-1.0.2.sha256sum] = "877a4ace8713b0bcf2a4e7eec82529c029f1d0619886d18145fea96c3ffe5c0f"
SRC_URI[errno-0.3.14.sha256sum] = "39cab71617ae0d63f51a36d69f866391735b51691dbda63cf6f96d042b63efeb"
SRC_URI[etcetera-0.11.0.sha256sum] = "de48cc4d1c1d97a20fd819def54b890cadde72ed3ad0c614822a0a433361be96"
SRC_URI[event-listener-5.4.1.sha256sum] = "e13b66accf52311f30a0db42147dadea9850cb48cd070028831ae5f5d4b856ab"
SRC_URI[fastrand-2.4.1.sha256sum] = "9f1f227452a390804cdb637b74a86990f2a7d7ba4b7d5693aac9b4dd6defd8d6"
SRC_URI[fiat-crypto-0.2.9.sha256sum] = "28dea519a9695b9977216879a3ebfddf92f1c08c05d984f8996aecd6ecdc811d"
SRC_URI[find-msvc-tools-0.1.9.sha256sum] = "5baebc0774151f905a1a2cc41989300b1e6fbb29aff0ceffa1064fdd3088d582"
SRC_URI[fixedbitset-0.5.7.sha256sum] = "1d674e81391d1e1ab681a28d99df07927c6d4aa5b027d7da16ba32d1d21ecd99"
SRC_URI[flume-0.12.0.sha256sum] = "5e139bc46ca777eb5efaf62df0ab8cc5fd400866427e56c68b22e414e53bd3be"
SRC_URI[fnv-1.0.7.sha256sum] = "3f9eec918d3f24069decb9af1554cad7c880e2da24a9afd88aca000531ab82c1"
SRC_URI[foldhash-0.1.5.sha256sum] = "d9c4f5dac5e15c24eb999c26181a6ca40b39fe946cbe4c263c7209467bc83af2"
SRC_URI[foldhash-0.2.0.sha256sum] = "77ce24cb58228fbb8aa041425bb1050850ac19177686ea6e0f41a70416f56fdb"
SRC_URI[foreign-types-0.5.0.sha256sum] = "d737d9aa519fb7b749cbc3b962edcf310a8dd1f4b67c91c4f83975dbdd17d965"
SRC_URI[foreign-types-macros-0.2.4.sha256sum] = "ea5190182e6915eb873ddbc16e23b711b6eb1f9c00a0d0a3a91b5f6228475225"
SRC_URI[foreign-types-shared-0.3.1.sha256sum] = "aa9a19cbb55df58761df49b23516a86d432839add4af60fc256da840f66ed35b"
SRC_URI[form_urlencoded-1.2.2.sha256sum] = "cb4cb245038516f5f85277875cdaa4f7d2c9a0fa0468de06ed190163b1581fcf"
SRC_URI[fs_extra-1.3.0.sha256sum] = "42703706b716c37f96a77aea830392ad231f44c9e9a67872fa5548707e11b11c"
SRC_URI[fslock-0.2.1.sha256sum] = "04412b8935272e3a9bae6f48c7bfff74c2911f60525404edfdd28e49884c3bfb"
SRC_URI[futures-0.3.32.sha256sum] = "8b147ee9d1f6d097cef9ce628cd2ee62288d963e16fb287bd9286455b241382d"
SRC_URI[futures-channel-0.3.32.sha256sum] = "07bbe89c50d7a535e539b8c17bc0b49bdb77747034daa8087407d655f3f7cc1d"
SRC_URI[futures-core-0.3.32.sha256sum] = "7e3450815272ef58cec6d564423f6e755e25379b217b0bc688e295ba24df6b1d"
SRC_URI[futures-executor-0.3.32.sha256sum] = "baf29c38818342a3b26b5b923639e7b1f4a61fc5e76102d4b1981c6dc7a7579d"
SRC_URI[futures-intrusive-0.5.0.sha256sum] = "1d930c203dd0b6ff06e0201a4a2fe9149b43c684fd4420555b26d21b1a02956f"
SRC_URI[futures-io-0.3.32.sha256sum] = "cecba35d7ad927e23624b22ad55235f2239cfa44fd10428eecbeba6d6a717718"
SRC_URI[futures-macro-0.3.32.sha256sum] = "e835b70203e41293343137df5c0664546da5745f82ec9b84d40be8336958447b"
SRC_URI[futures-sink-0.3.32.sha256sum] = "c39754e157331b013978ec91992bde1ac089843443c49cbc7f46150b0fad0893"
SRC_URI[futures-task-0.3.32.sha256sum] = "037711b3d59c33004d3856fbdc83b99d4ff37a24768fa1be9ce3538a1cde4393"
SRC_URI[futures-util-0.3.32.sha256sum] = "389ca41296e6190b48053de0321d02a77f32f8a5d2461dd38762c0593805c6d6"
SRC_URI[generic-array-0.14.7.sha256sum] = "85649ca51fd72272d7821adaf274ad91c288277713d9c18820d8499a7ff69e9a"
SRC_URI[getrandom-0.2.17.sha256sum] = "ff2abc00be7fca6ebc474524697ae276ad847ad0a6b3faa4bcb027e9a4614ad0"
SRC_URI[getrandom-0.3.4.sha256sum] = "899def5c37c4fd7b2664648c28120ecec138e4d395b459e5ca34f9cce2dd77fd"
SRC_URI[getrandom-0.4.3.sha256sum] = "300e883d756b2e4ec94e02791f39b04b522276138852cfc41d9fb7e904106099"
SRC_URI[ghash-0.5.1.sha256sum] = "f0d8a4362ccb29cb0b265253fb0a2728f592895ee6854fd9bc13f2ffda266ff1"
SRC_URI[glob-0.3.4.sha256sum] = "e4eba85ea1d0a966a983acd07deee566e67395d2d96b6fb39e62b5a833f1eb0b"
SRC_URI[h2-0.4.15.sha256sum] = "6cb093c84e8bd9b188d4c4a8cb6579fc016968d14c99882163cd3ff402a4f155"
SRC_URI[hash32-0.2.1.sha256sum] = "b0c35f58762feb77d74ebe43bdbc3210f09be9fe6742234d573bacc26ed92b67"
SRC_URI[hashbrown-0.15.5.sha256sum] = "9229cfe53dfd69f0609a49f65461bd93001ea1ef889cd5529dd176593f5338a1"
SRC_URI[hashbrown-0.16.1.sha256sum] = "841d1cc9bed7f9236f321df977030373f4a4163ae1a7dbfe1a51a2c1a51d9100"
SRC_URI[hashbrown-0.17.1.sha256sum] = "ed5909b6e89a2db4456e54cd5f673791d7eca6732202bbf2a9cc504fe2f9b84a"
SRC_URI[hashlink-0.11.1.sha256sum] = "824e001ac4f3012dd16a264bec811403a67ca9deb6c102fc5049b32c4574b35f"
SRC_URI[hax-lib-0.3.6.sha256sum] = "543f93241d32b3f00569201bfce9d7a93c92c6421b23c77864ac929dc947b9fc"
SRC_URI[hax-lib-macros-0.3.6.sha256sum] = "f8755751e760b11021765bb04cb4a6c4e24742688d9f3aa14c2079638f537b0f"
SRC_URI[hax-lib-macros-types-0.3.6.sha256sum] = "f177c9ae8ea456e2f71ff3c1ea47bf4464f772a05133fcbba56cd5ba169035a2"
SRC_URI[heapless-0.7.17.sha256sum] = "cdc6457c0eb62c71aac4bc17216026d8410337c4126773b9c5daba343f17964f"
SRC_URI[heck-0.5.0.sha256sum] = "2304e00983f87ffb38b55b444b5e3b60a884b5d30c0fca7d82fe33449bbe55ea"
SRC_URI[hex-0.4.3.sha256sum] = "7f24254aa9a54b5c858eaee2f5bccdb46aaf0e486a595ed5fd8f86ba55232a70"
SRC_URI[hkdf-0.12.4.sha256sum] = "7b5f8eb2ad728638ea2c7d47a21db23b7b58a72ed6a38256b8a1849f15fbbdf7"
SRC_URI[hkdf-0.13.0.sha256sum] = "4aaa26c720c68b866f2c96ef5c1264b3e6f473fe5d4ce61cd44bbe913e553018"
SRC_URI[hmac-0.12.1.sha256sum] = "6c49c37c09c17a53d937dfbb742eb3a961d65a994e6bcdcf37e7399d0cc8ab5e"
SRC_URI[hmac-0.13.0.sha256sum] = "6303bc9732ae41b04cb554b844a762b4115a61bfaa81e3e83050991eeb56863f"
SRC_URI[home-0.5.12.sha256sum] = "cc627f471c528ff0c4a49e1d5e60450c8f6461dd6d10ba9dcd3a61d3dff7728d"
SRC_URI[hpke-rs-0.6.1.sha256sum] = "b6ad6a58eb3e0ee30be8bfc7a9770ae98adcfa1d9bc820a5847732ce84f70837"
SRC_URI[hpke-rs-crypto-0.6.1.sha256sum] = "0a73a99d9008010d73289f41335a3f6e14fb8c04eaf60e9111b450463b1bbc7f"
SRC_URI[http-1.4.2.sha256sum] = "6970f50e31d6fc17d3fa27329444bfa74e196cf62e95052a3f6fee181dba6425"
SRC_URI[http-body-1.0.1.sha256sum] = "1efedce1fb8e6913f23e0c92de8e62cd5b772a67e7b3946df930a62566c93184"
SRC_URI[http-body-util-0.1.3.sha256sum] = "b021d93e26becf5dc7e1b75b1bed1fd93124b374ceb73f43d4d4eafec896a64a"
SRC_URI[httparse-1.10.1.sha256sum] = "6dbf3de79e51f3d586ab4cb9d5c3e2c14aa28ed23d180cf89b4df0454a69cc87"
SRC_URI[httpdate-1.0.3.sha256sum] = "df3b46402a9d5adb4c86a0cf463f42e19994e3ee891101b1841f30a545cb49a9"
SRC_URI[hybrid-array-0.4.13.sha256sum] = "818356c5132c1fede50f837ca96afbe78ff42413047f4abb886217845e1b6c8c"
SRC_URI[hyper-1.10.1.sha256sum] = "55281c53a1894c864990125767da440a4e630446785086f52523b20033b74498"
SRC_URI[hyper-rustls-0.27.9.sha256sum] = "33ca68d021ef39cf6463ab54c1d0f5daf03377b70561305bb89a8f83aab66e0f"
SRC_URI[hyper-util-0.1.20.sha256sum] = "96547c2556ec9d12fb1578c4eaf448b04993e7fb79cbaad930a656880a6bdfa0"
SRC_URI[iana-time-zone-0.1.65.sha256sum] = "e31bc9ad994ba00e440a8aa5c9ef0ec67d5cb5e5cb0cc7f8b744a35b389cc470"
SRC_URI[iana-time-zone-haiku-0.1.2.sha256sum] = "f31827a206f56af32e590ba56d5d2d085f558508192593743f16b2306495269f"
SRC_URI[icu_collections-2.2.0.sha256sum] = "2984d1cd16c883d7935b9e07e44071dca8d917fd52ecc02c04d5fa0b5a3f191c"
SRC_URI[icu_locale_core-2.2.0.sha256sum] = "92219b62b3e2b4d88ac5119f8904c10f8f61bf7e95b640d25ba3075e6cac2c29"
SRC_URI[icu_normalizer-2.2.0.sha256sum] = "c56e5ee99d6e3d33bd91c5d85458b6005a22140021cc324cea84dd0e72cff3b4"
SRC_URI[icu_normalizer_data-2.2.0.sha256sum] = "da3be0ae77ea334f4da67c12f149704f19f81d1adf7c51cf482943e84a2bad38"
SRC_URI[icu_properties-2.2.0.sha256sum] = "bee3b67d0ea5c2cca5003417989af8996f8604e34fb9ddf96208a033901e70de"
SRC_URI[icu_properties_data-2.2.0.sha256sum] = "8e2bbb201e0c04f7b4b3e14382af113e17ba4f63e2c9d2ee626b720cbce54a14"
SRC_URI[icu_provider-2.2.0.sha256sum] = "139c4cf31c8b5f33d7e199446eff9c1e02decfc2f0eec2c8d71f65befa45b421"
SRC_URI[idna-1.1.0.sha256sum] = "3b0875f23caa03898994f6ddc501886a45c7d3d62d04d2d90788d47be1b1e4de"
SRC_URI[idna_adapter-1.2.2.sha256sum] = "cb68373c0d6620ef8105e855e7745e18b0d00d3bdb07fb532e434244cdb9a714"
SRC_URI[indexmap-2.14.0.sha256sum] = "d466e9454f08e4a911e14806c24e16fba1b4c121d1ea474396f396069cf949d9"
SRC_URI[inout-0.1.4.sha256sum] = "879f10e63c20629ecabbb64a8010319738c66a5cd0c29b02d63d272b03751d01"
SRC_URI[ipnet-2.12.0.sha256sum] = "d98f6fed1fde3f8c21bc40a1abb88dd75e67924f9cffc3ef95607bad8017f8e2"
SRC_URI[itertools-0.13.0.sha256sum] = "413ee7dfc52ee1a4949ceeb7dbc8a33f2d6c088194d9f922fb8318faf1f01186"
SRC_URI[itertools-0.14.0.sha256sum] = "2b192c782037fadd9cfa75548310488aabdbf3d2da73885b31bd0abd03351285"
SRC_URI[itertools-0.15.0.sha256sum] = "8b4baf93f58d4425749ca49a51c50ebab072c5df6994d08fed93541c331481dc"
SRC_URI[itoa-1.0.18.sha256sum] = "8f42a60cbdf9a97f5d2305f08a87dc4e09308d1276d28c869c684d7777685682"
SRC_URI[jni-0.21.1.sha256sum] = "1a87aa2bb7d2af34197c04845522473242e1aa17c12f4935d5856491a7fb8c97"
SRC_URI[jni-sys-0.3.1.sha256sum] = "41a652e1f9b6e0275df1f15b32661cf0d4b78d4d87ddec5e0c3c20f097433258"
SRC_URI[jni-sys-0.4.1.sha256sum] = "c6377a88cb3910bee9b0fa88d4f42e1d2da8e79915598f65fb0c7ee14c878af2"
SRC_URI[jni-sys-macros-0.4.1.sha256sum] = "38c0b942f458fe50cdac086d2f946512305e5631e720728f2a61aabcd47a6264"
SRC_URI[js-sys-0.3.103.sha256sum] = "53b44bfcdb3f8d5837a46dae1ca9660a837176eee74a28b229bc626816589102"
SRC_URI[lazy_static-1.5.0.sha256sum] = "bbd2bcb4c963f2ddae06a2efc7e9f3591312473c50c6685e1f298068316e66fe"
SRC_URI[libc-0.2.186.sha256sum] = "68ab91017fe16c622486840e4c83c9a37afeff978bd239b5293d61ece587de66"
SRC_URI[libcrux-hacl-rs-0.0.4.sha256sum] = "2637dc87d158e1f1b550fd9b226443e84153fded4de69028d897b534d16d22e6"
SRC_URI[libcrux-hmac-0.0.6.sha256sum] = "8a7a242707d65960770bd7e14e4f18a92bdf0b967777dd404887db8d087a643b"
SRC_URI[libcrux-intrinsics-0.0.6.sha256sum] = "b1b5db005ff8001e026b73a6842ee81bbef8ec5ff0e1915a67ae65fd2a9fafa5"
SRC_URI[libcrux-macros-0.0.3.sha256sum] = "ffd6aa2dcd5be681662001b81d493f1569c6d49a32361f470b0c955465cd0338"
SRC_URI[libcrux-ml-kem-0.0.8.sha256sum] = "a14ab3e477de9df6ee1273a114018ff62c4996ca9220070c4e5cb1743f94a67d"
SRC_URI[libcrux-platform-0.0.3.sha256sum] = "1d9e21d7ed31a92ac539bd69a8c970b183ee883872d2d19ce27036e24cb8ecc4"
SRC_URI[libcrux-secrets-0.0.5.sha256sum] = "1ce650f3041b44ba40d4263852347d007cd2cd9d1cc856a6f6c8b2e10c3fd40b"
SRC_URI[libcrux-sha2-0.0.6.sha256sum] = "e9d253473f259fc74a280c43f29c464f7e374abdf28b4942234dc707f529d4b7"
SRC_URI[libcrux-sha3-0.0.8.sha256sum] = "b1ae0b7d0e1cc4793a609fd0ff2ca3b3a3fabae523770c619a3d4bc86417b0d7"
SRC_URI[libcrux-traits-0.0.6.sha256sum] = "812e4fa89f3f5e34b47f928b22b1b78395a0d4ec23b1f583db635f128159d65f"
SRC_URI[libloading-0.8.9.sha256sum] = "d7c4b02199fee7c5d21a5ae7d8cfa79a6ef5bb2fc834d6e9058e89c825efdc55"
SRC_URI[linked-hash-map-0.5.6.sha256sum] = "0717cef1bc8b636c6e1c1bbdefc09e6322da8a9321966e8928ef80d20f7f770f"
SRC_URI[linux-raw-sys-0.12.1.sha256sum] = "32a66949e030da00e8c7d4434b251670a91556f4144941d37452769c25d58a53"
SRC_URI[linux-raw-sys-0.4.15.sha256sum] = "d26c52dbd32dccf2d10cac7725f8eae5296885fb5703b261f7d0a0739ec807ab"
SRC_URI[litemap-0.8.2.sha256sum] = "92daf443525c4cce67b150400bc2316076100ce0b3686209eb8cf3c31612e6f0"
SRC_URI[lock_api-0.4.14.sha256sum] = "224399e74b87b5f3557511d98dff8b14089b3dadafcab6bb93eab67d3aace965"
SRC_URI[log-0.4.33.sha256sum] = "0ceec5bc11778974d1bcb055b18002eba7f4b3518b6a0081b3af5f21666da9ad"
SRC_URI[lru-cache-0.1.2.sha256sum] = "31e24f1ad8321ca0e8a1e0ac13f23cb668e6f5466c2c57319f6a5cf1cc8e3b1c"
SRC_URI[lru-slab-0.1.2.sha256sum] = "112b39cec0b298b6c1999fee3e31427f74f676e4cb9879ed1a121b43661a4154"
SRC_URI[matchers-0.2.0.sha256sum] = "d1525a2a28c7f4fa0fc98bb91ae755d1e2d1505079e05539e35bc876b5d65ae9"
SRC_URI[md-5-0.11.0.sha256sum] = "69b6441f590336821bb897fb28fc622898ccceb1d6cea3fde5ea86b090c4de98"
SRC_URI[memchr-2.8.3.sha256sum] = "cf8baf1c55e62ffcace7a9f06f4bd9cd3f0c4beb022d3b367256b91b87513d98"
SRC_URI[mime-0.3.17.sha256sum] = "6877bb514081ee2a7ff5ef9de3281f14a4dd4bceac4c09388074a6b5df8a139a"
SRC_URI[mime_guess-2.0.5.sha256sum] = "f7c44f8e672c00fe5308fa235f821cb4198414e1c77935c1ab6948d3fd78550e"
SRC_URI[minimal-lexical-0.2.1.sha256sum] = "68354c5c6bd36d73ff3feceb05efa59b6acb7626617f4962be322a825e61f79a"
SRC_URI[mio-1.2.1.sha256sum] = "02bd0af71c67b473010cbbc60715ee815645a4dc942899111f494b4b737d6fda"
SRC_URI[multimap-0.10.1.sha256sum] = "1d87ecb2933e8aeadb3e3a02b828fed80a7528047e68b4f424523a0981a3a084"
SRC_URI[nom-7.1.3.sha256sum] = "d273983c5a657a70a3e8f2a01329822f3b8c8172b73826411a55751e404a0a4a"
SRC_URI[nonzero_ext-0.3.0.sha256sum] = "38bf9645c8b145698bb0b18a4637dcacbc421ea49bef2317e4fd8065a387cf21"
SRC_URI[nu-ansi-term-0.50.3.sha256sum] = "7957b9740744892f114936ab4a57b3f487491bbeafaf8083688b16841a4240e5"
SRC_URI[num-bigint-0.4.8.sha256sum] = "c89e69e7e0f03bea5ef08013795c25018e101932225a656383bd384495ecc367"
SRC_URI[num-integer-0.1.46.sha256sum] = "7969661fd2958a5cb096e56c8e1ad0444ac2bbcd0061bd28660485a44879858f"
SRC_URI[num-traits-0.2.19.sha256sum] = "071dfc062690e90b734c0b2273ce72ad0ffa95f0c74596bc250dcfd960262841"
SRC_URI[num_enum-0.7.6.sha256sum] = "5d0bca838442ec211fa11de3a8b0e0e8f3a4522575b5c4c06ed722e005036f26"
SRC_URI[num_enum_derive-0.7.6.sha256sum] = "680998035259dcfcafe653688bf2aa6d3e2dc05e98be6ab46afb089dc84f1df8"
SRC_URI[once_cell-1.21.4.sha256sum] = "9f7c3e4beb33f85d45ae3e3a1792185706c8e16d043238c593331cc7cd313b50"
SRC_URI[oncemutex-0.1.1.sha256sum] = "44d11de466f4a3006fe8a5e7ec84e93b79c70cb992ae0aa0eb631ad2df8abfe2"
SRC_URI[opaque-debug-0.3.1.sha256sum] = "c08d65885ee38876c4f86fa503fb49d7b507c2b62552df7c70b2fce627e06381"
SRC_URI[openssl-macros-0.1.1.sha256sum] = "a948666b637a0f465e8564c73e89d4dde00d72d4d473cc972f390fc3dcee7d9c"
SRC_URI[openssl-probe-0.2.1.sha256sum] = "7c87def4c32ab89d880effc9e097653c8da5d6ef28e6b539d313baaacfbafcbe"
SRC_URI[parking-2.2.1.sha256sum] = "f38d5652c16fde515bb1ecef450ab0f6a219d619a7274976324d5e377f7dceba"
SRC_URI[parking_lot-0.12.5.sha256sum] = "93857453250e3077bd71ff98b6a65ea6621a19bb0f559a85248955ac12c45a1a"
SRC_URI[parking_lot_core-0.9.12.sha256sum] = "2621685985a2ebf1c516881c026032ac7deafcda1a2c9b7850dc81e3dfcb64c1"
SRC_URI[partial-default-0.1.0.sha256sum] = "124dc3c21ffb6fb3a0562d129929a8a54998766ef7adc1ba09ddc467d092c14b"
SRC_URI[partial-default-derive-0.1.0.sha256sum] = "7459127d7a18cb202d418e4b7df1103ffd6d82a106e9b2091c250624c2ace70d"
SRC_URI[password-hash-0.5.0.sha256sum] = "346f04948ba92c43e8469c1ee6736c7563d71012b17d40745260fe106aac2166"
SRC_URI[pastey-0.2.3.sha256sum] = "2ee67f1008b1ba2321834326597b8e186293b049a023cdef258527550b9935b4"
SRC_URI[percent-encoding-2.3.2.sha256sum] = "9b4f627cb1b25917193a259e49bdad08f671f8d9708acfd5fe0a8c1455d87220"
SRC_URI[petgraph-0.8.3.sha256sum] = "8701b58ea97060d5e5b155d383a69952a60943f0e6dfe30b04c287beb0b27455"
SRC_URI[phonenumber-0.3.10+9.0.33.sha256sum] = "66d85d3cc5477bfe2f357939e7f02a5a566632508633af1a2c37577d0bef87d9"
SRC_URI[pin-project-1.1.13.sha256sum] = "2466b2336ed02bcdca6b294417127b90ec92038d1d5c4fbeac971a922e0e0924"
SRC_URI[pin-project-internal-1.1.13.sha256sum] = "c96395f0a926bc13b1c17622aaddda1ecb55d49c8f1bf9777e4d877800a43f8b"
SRC_URI[pin-project-lite-0.2.17.sha256sum] = "a89322df9ebe1c1578d689c92318e070967d1042b512afbe49518723f4e6d5cd"
SRC_URI[pkcs8-0.10.2.sha256sum] = "f950b2377845cebe5cf8b5165cb3cc1a5e0fa5cfa3e1f7f55707d8fd82e0a7b7"
SRC_URI[pkg-config-0.3.33.sha256sum] = "19f132c84eca552bf34cab8ec81f1c1dcc229b811638f9d283dceabe58c5569e"
SRC_URI[poly1305-0.8.0.sha256sum] = "8159bd90725d2df49889a078b54f4f79e87f1f8a8444194cdca81d38f5393abf"
SRC_URI[polyval-0.6.2.sha256sum] = "9d1fe60d06143b2430aa532c94cfe9e29783047f06c0d7fd359a9a51b729fa25"
SRC_URI[postcard-1.1.3.sha256sum] = "6764c3b5dd454e283a30e6dfe78e9b31096d9e32036b5d1eaac7a6119ccb9a24"
SRC_URI[potential_utf-0.1.5.sha256sum] = "0103b1cef7ec0cf76490e969665504990193874ea05c85ff9bab8b911d0a0564"
SRC_URI[ppv-lite86-0.2.21.sha256sum] = "85eae3c4ed2f50dcfe72643da4befc30deadb458a9b590d720cde2f2b1e97da9"
SRC_URI[prettyplease-0.2.37.sha256sum] = "479ca8adacdd7ce8f1fb39ce9ecccbfe93a3f1344b3d0d97f20bc0196208f62b"
SRC_URI[proc-macro-crate-3.5.0.sha256sum] = "e67ba7e9b2b56446f1d419b1d807906278ffa1a658a8a5d8a39dcb1f5a78614f"
SRC_URI[proc-macro-error-attr2-2.0.0.sha256sum] = "96de42df36bb9bba5542fe9f1a054b8cc87e172759a1868aa05c1f3acc89dfc5"
SRC_URI[proc-macro-error2-2.0.1.sha256sum] = "11ec05c52be0a07b08061f7dd003e7d7092e0472bc731b4af7bb1ef876109802"
SRC_URI[proc-macro2-1.0.106.sha256sum] = "8fd00f0bb2e90d81d1044c2b32617f68fcb9fa3bb7640c23e9c748e53fb30934"
SRC_URI[prost-0.14.4.sha256sum] = "528ac67416ff8646872a3c02cad9cc4ee5dc9f9540c9b10771855c95cb2e5ae1"
SRC_URI[prost-build-0.14.4.sha256sum] = "03da047801ff44bb6a4d407d4860c05fd70bb81714e6b2f3812603d5b145b042"
SRC_URI[prost-derive-0.14.4.sha256sum] = "b570b25f7617e43d59005d0990ccb79e950a423952cea19671b7a876da390adf"
SRC_URI[prost-types-0.14.4.sha256sum] = "f94967dc7688f3054c7fac87473ffae4cc4c3904800e2d9f5b857246d8963b0a"
SRC_URI[protobuf-3.7.2.sha256sum] = "d65a1d4ddae7d8b5de68153b48f6aa3bba8cb002b243dbdbc55a5afbc98f99f4"
SRC_URI[protobuf-codegen-3.7.2.sha256sum] = "5d3976825c0014bbd2f3b34f0001876604fe87e0c86cd8fa54251530f1544ace"
SRC_URI[protobuf-parse-3.7.2.sha256sum] = "b4aeaa1f2460f1d348eeaeed86aea999ce98c1bded6f089ff8514c9d9dbdc973"
SRC_URI[protobuf-support-3.7.2.sha256sum] = "3e36c2f31e0a47f9280fb347ef5e461ffcd2c52dd520d8e216b52f93b0b0d7d6"
SRC_URI[quick-xml-0.41.0.sha256sum] = "e660451e55124f798a69a5af3f49ccfbefbd41910eefd25caf2393e1f3473ec1"
SRC_URI[quinn-0.11.11.sha256sum] = "0c1a41e437b6bbd489372cd4971de128e85c855f56c57f283d20ff016cf7c0a8"
SRC_URI[quinn-proto-0.11.16.sha256sum] = "2f4bfc015262b9df63c8845072ce59068853ff5872180c2ce2f13038b970e560"
SRC_URI[quinn-udp-0.5.15.sha256sum] = "35a133f956daabe89a61a685c2649f13d82d5aa4bd5d12d1277e1072a21c0694"
SRC_URI[quote-1.0.46.sha256sum] = "dfbc457d0c7a0759a614551b11a6409e5951f6c7537be1f1b7682b9ae9230368"
SRC_URI[r-efi-5.3.0.sha256sum] = "69cdb34c158ceb288df11e18b4bd39de994f6657d83847bdffdbd7f346754b0f"
SRC_URI[r-efi-6.0.0.sha256sum] = "f8dcc9c7d52a811697d2151c701e0d08956f92b0e24136cf4cf27b57a6a0d9bf"
SRC_URI[rand-0.10.2.sha256sum] = "c7f5fa3a058cd35567ef9bfa5e75732bee0f9e4c55fa90477bef2dfcdbc4be80"
SRC_URI[rand-0.8.6.sha256sum] = "5ca0ecfa931c29007047d1bc58e623ab12e5590e8c7cc53200d5202b69266d8a"
SRC_URI[rand-0.9.4.sha256sum] = "44c5af06bb1b7d3216d91932aed5265164bf384dc89cd6ba05cf59a35f5f76ea"
SRC_URI[rand_chacha-0.3.1.sha256sum] = "e6c10a63a0fa32252be49d21e7709d4d4baf8d231c2dbce1eaa8141b9b127d88"
SRC_URI[rand_chacha-0.9.0.sha256sum] = "d3022b5f1df60f26e1ffddd6c66e8aa15de382ae63b3a0c1bfc0e4d3e3f325cb"
SRC_URI[rand_core-0.10.1.sha256sum] = "63b8176103e19a2643978565ca18b50549f6101881c443590420e4dc998a3c69"
SRC_URI[rand_core-0.6.4.sha256sum] = "ec0be4795e2f6a28069bec0b5ff3e2ac9bafc99e6a9a7dc3547996c5c816922c"
SRC_URI[rand_core-0.9.5.sha256sum] = "76afc826de14238e6e8c374ddcc1fa19e374fd8dd986b0d2af0d02377261d83c"
SRC_URI[rand_pcg-0.10.2.sha256sum] = "caa0f4137e1c0a72f4c651489402276c8e8e1cf081f3b0ba156d2cbeef09e86a"
SRC_URI[rangemap-1.7.1.sha256sum] = "973443cf09a9c8656b574a866ab68dfa19f0867d0340648c7d2f6a71b8a8ea68"
SRC_URI[rayon-1.12.0.sha256sum] = "fb39b166781f92d482534ef4b4b1b2568f42613b53e5b6c160e24cfbfa30926d"
SRC_URI[rayon-core-1.13.0.sha256sum] = "22e18b0f0062d30d4230b2e85ff77fdfe4326feb054b9783a3460d8435c8ab91"
SRC_URI[redox_syscall-0.5.18.sha256sum] = "ed2bf2547551a7053d6fdfafda3f938979645c44812fbfcda098faae3f1a362d"
SRC_URI[regex-1.13.0.sha256sum] = "2a0e75113e14dc5acb068cd0786884f214f1312650a3d36d269f5c4f3cdee8a2"
SRC_URI[regex-automata-0.4.15.sha256sum] = "1f388202e4b80542a0921078cc23b6333bcf1409c1e3f86404cae4766a6131db"
SRC_URI[regex-cache-0.2.1.sha256sum] = "2f7b62d69743b8b94f353b6b7c3deb4c5582828328bcb8d5fedf214373808793"
SRC_URI[regex-syntax-0.6.29.sha256sum] = "f162c6dd7b008981e4d40210aca20b4bd0f9b60ca9271061b07f78537722f2e1"
SRC_URI[regex-syntax-0.8.11.sha256sum] = "d6f6ff9a378485b298a5286656da665ba74413d36db0979633275d2e708145d4"
SRC_URI[reqwest-0.12.28.sha256sum] = "eddd3ca559203180a307f12d114c268abf583f59b03cb906fd0b3ff8646c1147"
SRC_URI[reqwest-websocket-0.4.4.sha256sum] = "f477f800f86d8f5c320e19d8b2b1ef0b1e773ea7c75eec6c7f442e7ec3f06d7e"
SRC_URI[ring-0.17.14.sha256sum] = "a4689e6c2294d81e88dc6261c768b63bc4fcdb852be6d1352498b114f61383b7"
SRC_URI[rustc-hash-2.1.3.sha256sum] = "6b1e7f9a428571be2dc5bc0505c13fb6bf936822b894ec87abf8a08a4e51742d"
SRC_URI[rustc_version-0.4.1.sha256sum] = "cfcb3a22ef46e85b45de6ee7e79d063319ebb6594faafcf1c225ea92ab6e9b92"
SRC_URI[rustix-0.38.44.sha256sum] = "fdb5bc1ae2baa591800df16c9ca78619bf65c0488b41b96ccec5d11220d8c154"
SRC_URI[rustix-1.1.4.sha256sum] = "b6fe4565b9518b83ef4f91bb47ce29620ca828bd32cb7e408f0062e9930ba190"
SRC_URI[rustls-0.23.41.sha256sum] = "6b92b125634d9b795e7beca796cc790df15a7fb38323bf3196fda83292d06b1f"
SRC_URI[rustls-native-certs-0.8.4.sha256sum] = "dab5152771c58876a2146916e53e35057e1a4dfa2b9df0f0305b07f611fdea4d"
SRC_URI[rustls-pki-types-1.15.0.sha256sum] = "764899a24af3980067ee14bc143654f297b22eaebfe3c7b6b211920a5a59b046"
SRC_URI[rustls-platform-verifier-0.5.3.sha256sum] = "19787cda76408ec5404443dc8b31795c87cd8fec49762dc75fa727740d34acc1"
SRC_URI[rustls-platform-verifier-android-0.1.1.sha256sum] = "f87165f0995f63a9fbeea62b64d10b4d9d8e78ec6d7d51fb2125fda7bb36788f"
SRC_URI[rustls-webpki-0.103.13.sha256sum] = "61c429a8649f110dddef65e2a5ad240f747e85f7758a6bccc7e5777bd33f756e"
SRC_URI[rustversion-1.0.23.sha256sum] = "cf54715a573b99ac80df0bc206da022bcd442c974952c7b9720069370852e21f"
SRC_URI[ryu-1.0.23.sha256sum] = "9774ba4a74de5f7b1c1451ed6cd5285a32eddb5cccb8cc655a4e50009e06477f"
SRC_URI[same-file-1.0.6.sha256sum] = "93fc1dc3aaa9bfed95e02e6eadabb4baf7e3078b0bd1b4d7b6b0b68378900502"
SRC_URI[schannel-0.1.29.sha256sum] = "91c1b7e4904c873ef0710c1f407dde2e6287de2bebc1bbbf7d430bb7cbffd939"
SRC_URI[scopeguard-1.2.0.sha256sum] = "94143f37725109f92c262ed2cf5e59bce7498c01bcc1502d7b9afe439a4e9f49"
SRC_URI[security-framework-3.7.0.sha256sum] = "b7f4bc775c73d9a02cde8bf7b2ec4c9d12743edf609006c7facc23998404cd1d"
SRC_URI[security-framework-sys-2.17.0.sha256sum] = "6ce2691df843ecc5d231c0b14ece2acc3efb62c0a398c7e1d875f3983ce020e3"
SRC_URI[semver-1.0.28.sha256sum] = "8a7852d02fc848982e0c167ef163aaff9cd91dc640ba85e263cb1ce46fae51cd"
SRC_URI[serde-1.0.228.sha256sum] = "9a8e94ea7f378bd32cbbd37198a4a91436180c5bb472411e48b5ec2e2124ae9e"
SRC_URI[serde_core-1.0.228.sha256sum] = "41d385c7d4ca58e59fc732af25c3983b67ac852c1a25000afe1175de458b67ad"
SRC_URI[serde_derive-1.0.228.sha256sum] = "d540f220d3187173da220f885ab66608367b6574e925011a9353e4badda91d79"
SRC_URI[serde_json-1.0.150.sha256sum] = "e8014e44b4736ed0538adeecded0fce2a272f22dc9578a7eb6b2d9993c74cfb9"
SRC_URI[serde_urlencoded-0.7.1.sha256sum] = "d3491c14715ca2294c4d6a88f15e84739788c1d030eed8c110436aafdaa2f3fd"
SRC_URI[sha1-0.10.6.sha256sum] = "e3bf829a2d51ab4a5ddf1352d8470c140cadc8301b2ae1789db023f01cedd6ba"
SRC_URI[sha1-0.11.0.sha256sum] = "aacc4cc499359472b4abe1bf11d0b12e688af9a805fa5e3016f9a386dc2d0214"
SRC_URI[sha2-0.10.9.sha256sum] = "a7507d819769d01a365ab707794a4084392c824f54a7a6a7862f8c3d0892b283"
SRC_URI[sha2-0.11.0.sha256sum] = "446ba717509524cb3f22f17ecc096f10f4822d76ab5c0b9822c5f9c284e825f4"
SRC_URI[sha2-asm-0.6.4.sha256sum] = "b845214d6175804686b2bd482bcffe96651bb2d1200742b712003504a2dac1ab"
SRC_URI[sharded-slab-0.1.7.sha256sum] = "f40ca3c46823713e0d4209592e8d6e826aa57e928f09752619fc696c499637f6"
SRC_URI[shlex-1.3.0.sha256sum] = "0fda2ff0d084019ba4d7c6f371c95d8fd75ce3524c3cb8fb653a3023f6323e64"
SRC_URI[shlex-2.0.1.sha256sum] = "f8fadd59c855ef2080decdef8ff161eb6661b86933c9d82e5ba29dc602a55aba"
SRC_URI[signature-2.2.0.sha256sum] = "77549399552de45a898a580c1b41d445bf730df867cc44e6c0233bbc4b8329de"
SRC_URI[slab-0.4.12.sha256sum] = "0c790de23124f9ab44544d7ac05d60440adc586479ce501c1d6d7da3cd8c9cf5"
SRC_URI[smallvec-1.15.2.sha256sum] = "8ed6a63f02c8539c91a8685a86f4099661ba3da017932f6ebbea6de3f0fa7c90"
SRC_URI[snow-0.10.0.sha256sum] = "599b506ccc4aff8cf7844bc42cf783009a434c1e26c964432560fb6d6ad02d82"
SRC_URI[socket2-0.6.4.sha256sum] = "52d1cfed4120b4d927bf7c0f86d2087a4a7d6027c906d9f9d525a80573b9be51"
SRC_URI[sorted-vec-0.8.10.sha256sum] = "19f58d7b0190c7f12df7e8be6b79767a0836059159811b869d5ab55721fe14d0"
SRC_URI[spin-0.9.8.sha256sum] = "6980e8d7511241f8acf4aebddbb1ff938df5eebe98691418c4468d0b72a96a67"
SRC_URI[spki-0.7.3.sha256sum] = "d91ed6c858b01f942cd56b37a94b3e0a1798290327d1236e4d9cf4eaca44d29d"
SRC_URI[sqlx-0.9.0.sha256sum] = "378620ccc25c62c89d8be1c819e76a88d59bdcc3304733330788948e619bfd71"
SRC_URI[sqlx-core-0.9.0.sha256sum] = "05b44e85bf579a8eeb4ceaa77a3a523baf2bf0e9bac7e40f405d537b5d2d5ccb"
SRC_URI[sqlx-macros-0.9.0.sha256sum] = "bd2b84f2bc39a5705ef27ec785a11c934a41bbd4a24941e257927cddc26b60bf"
SRC_URI[sqlx-macros-core-0.9.0.sha256sum] = "fb8d96de5fdc85a5c4ec813432b523ec637e80ba98f046555f75f7908ddac7c3"
SRC_URI[sqlx-mysql-0.9.0.sha256sum] = "90b8020fe17c5f2c245bfa2505d7ef59c5604839527c740266ad2214acebea27"
SRC_URI[sqlx-postgres-0.9.0.sha256sum] = "87a2bdd6e83f6b3ea525ca9fee568030508b58355a43d0b2c1674d5f79dcd65e"
SRC_URI[sqlx-sqlite-0.9.0.sha256sum] = "488e99c397a62007e4229aec669a179816339afc6d2620ca6fa420dbee2e982c"
SRC_URI[stable_deref_trait-1.2.1.sha256sum] = "6ce2be8dc25455e1f91df71bfa12ad37d7af1092ae736f3a6cd0e37bc7810596"
SRC_URI[static_assertions-1.1.0.sha256sum] = "a2eb9349b6444b326872e140eb1cf5e7c522154d69e7a0ffb0fb81c06b37543f"
SRC_URI[stringprep-0.1.5.sha256sum] = "7b4df3d392d81bd458a8a621b8bffbd2302a12ffe288a9d931670948749463b1"
SRC_URI[strum-0.27.2.sha256sum] = "af23d6f6c1a224baef9d3f61e287d2761385a5b88fdab4eb4c6f11aeb54c4bcf"
SRC_URI[strum_macros-0.27.2.sha256sum] = "7695ce3845ea4b33927c055a39dc438a45b059f7c1b3d91d38d10355fb8cbca7"
SRC_URI[subtle-2.6.1.sha256sum] = "13c2bddecc57b384dee18652358fb23172facb8a2c51ccc10d74c157bdea3292"
SRC_URI[syn-2.0.118.sha256sum] = "1b9ae57f904213ebb649ce6895b8a66c66f0203b9319718f69a5612a065b1422"
SRC_URI[syn-3.0.3.sha256sum] = "53e9bae58849f64dfa4f5d5ae372c8341f7305f82a3868709269343628b659a3"
SRC_URI[sync_wrapper-1.0.2.sha256sum] = "0bf256ce5efdfa370213c1dabab5935a12e49f2c58d15e9eac2870d3b4f27263"
SRC_URI[synstructure-0.13.2.sha256sum] = "728a70f3dbaf5bab7f0c4b1ac8d7ae5ea60a4b5549c8a5914361c99147a709d2"
SRC_URI[tempfile-3.27.0.sha256sum] = "32497e9a4c7b38532efcdebeef879707aa9f794296a4f0244f6f69e9bc8574bd"
SRC_URI[thiserror-1.0.69.sha256sum] = "b6aaf5339b578ea85b50e080feb250a3e8ae8cfcdff9a461c9ec2904bc923f52"
SRC_URI[thiserror-2.0.18.sha256sum] = "4288b5bcbc7920c07a1149a35cf9590a2aa808e0bc1eafaade0b80947865fbc4"
SRC_URI[thiserror-impl-1.0.69.sha256sum] = "4fee6c4efc90059e10f81e6d42c60a18f76588c3d74cb83a0b242a2b6c7504c1"
SRC_URI[thiserror-impl-2.0.18.sha256sum] = "ebc4ee7f67670e9b64d05fa4253e753e016c6c95ff35b89b7941d6b856dec1d5"
SRC_URI[thread_local-1.1.9.sha256sum] = "f60246a4944f24f6e018aa17cdeffb7818b76356965d03b07d6a9886e8962185"
SRC_URI[tinystr-0.8.3.sha256sum] = "c8323304221c2a851516f22236c5722a72eaa19749016521d6dff0824447d96d"
SRC_URI[tinyvec-1.11.0.sha256sum] = "3e61e67053d25a4e82c844e8424039d9745781b3fc4f32b8d55ed50f5f667ef3"
SRC_URI[tinyvec_macros-0.1.1.sha256sum] = "1f3ccbac311fea05f86f61904b462b55fb3df8837a366dfc601a0161d0532f20"
SRC_URI[tokio-1.52.3.sha256sum] = "8fc7f01b389ac15039e4dc9531aa973a135d7a4135281b12d7c1bc79fd57fffe"
SRC_URI[tokio-macros-2.7.0.sha256sum] = "385a6cb71ab9ab790c5fe8d67f1645e6c450a7ce006a33de03daa956cf70a496"
SRC_URI[tokio-rustls-0.26.4.sha256sum] = "1729aa945f29d91ba541258c8df89027d5792d85a8841fb65e8bf0f4ede4ef61"
SRC_URI[tokio-socks-0.5.3.sha256sum] = "a7e2948f60dbe26b35f2c7fb74ac2854c1fddded0fe9d7548fcc674a246f7615"
SRC_URI[tokio-stream-0.1.18.sha256sum] = "32da49809aab5c3bc678af03902d4ccddea2a87d028d86392a4b1560c6906c70"
SRC_URI[tokio-tungstenite-0.28.0.sha256sum] = "d25a406cddcc431a75d3d9afc6a7c0f7428d4891dd973e4d54c56b46127bf857"
SRC_URI[tokio-util-0.7.18.sha256sum] = "9ae9cec805b01e8fc3fd2fe289f89149a9b66dd16786abd8b19cfa7b48cb0098"
SRC_URI[toml_datetime-1.1.1+spec-1.1.0.sha256sum] = "3165f65f62e28e0115a00b2ebdd37eb6f3b641855f9d636d3cd4103767159ad7"
SRC_URI[toml_edit-0.25.12+spec-1.1.0.sha256sum] = "d2153edc6955a6c354fad8f5efd38b6a8769bdccf9fe50f8e1329f81b0baa5d7"
SRC_URI[toml_parser-1.1.2+spec-1.1.0.sha256sum] = "a2abe9b86193656635d2411dc43050282ca48aa31c2451210f4202550afb7526"
SRC_URI[tonic-0.14.6.sha256sum] = "ac2a5518c70fa84342385732db33fb3f44bc4cc748936eb5833d2df34d6445ef"
SRC_URI[tower-0.5.3.sha256sum] = "ebe5ef63511595f1344e2d5cfa636d973292adc0eec1f0ad45fae9f0851ab1d4"
SRC_URI[tower-http-0.6.11.sha256sum] = "4cfcf7e2740e6fc6d4d688b4ef00650406bb94adf4731e43c096c3a19fe40840"
SRC_URI[tower-layer-0.3.3.sha256sum] = "121c2a6cda46980bb0fcd1647ffaf6cd3fc79a013de288782836f6df9c48780e"
SRC_URI[tower-service-0.3.3.sha256sum] = "8df9b6e13f2d32c91b9bd719c00d1958837bc7dec474d94952798cc8e69eeec3"
SRC_URI[tracing-0.1.44.sha256sum] = "63e71662fa4b2a2c3a26f570f037eb95bb1f85397f3cd8076caed2f026a6d100"
SRC_URI[tracing-attributes-0.1.31.sha256sum] = "7490cfa5ec963746568740651ac6781f701c9c5ea257c58e057f3ba8cf69e8da"
SRC_URI[tracing-core-0.1.36.sha256sum] = "db97caf9d906fbde555dd62fa95ddba9eecfd14cb388e4f491a66d74cd5fb79a"
SRC_URI[tracing-futures-0.2.5.sha256sum] = "97d095ae15e245a057c8e8451bab9b3ee1e1f68e9ba2b4fbc18d0ac5237835f2"
SRC_URI[tracing-log-0.2.0.sha256sum] = "ee855f1f400bd0e5c02d150ae5de3840039a3f54b025156404e34c23c03f47c3"
SRC_URI[tracing-subscriber-0.3.23.sha256sum] = "cb7f578e5945fb242538965c2d0b04418d38ec25c79d160cd279bf0731c8d319"
SRC_URI[try-lock-0.2.5.sha256sum] = "e421abadd41a4225275504ea4d6566923418b7f05506fbc9c0fe86ba7396114b"
SRC_URI[tungstenite-0.24.0.sha256sum] = "18e5b8366ee7a95b16d32197d0b2604b43a0be89dc5fac9f8e96ccafbaedda8a"
SRC_URI[tungstenite-0.28.0.sha256sum] = "8628dcc84e5a09eb3d8423d6cb682965dea9133204e8fb3efee74c2a0c259442"
SRC_URI[typenum-1.20.1.sha256sum] = "b6f5e870be6c3b371b77fe0ee0bafb859fa4964b4404c27de1d380043c4dda20"
SRC_URI[unicase-2.9.0.sha256sum] = "dbc4bc3a9f746d862c45cb89d705aa10f187bb96c76001afab07a0d35ce60142"
SRC_URI[unicode-bidi-0.3.18.sha256sum] = "5c1cb5db39152898a79168971543b1cb5020dff7fe43c8dc468b0885f5e29df5"
SRC_URI[unicode-ident-1.0.24.sha256sum] = "e6e4313cd5fcd3dad5cafa179702e2b244f760991f45397d14d4ebf38247da75"
SRC_URI[unicode-normalization-0.1.25.sha256sum] = "5fd4f6878c9cb28d874b009da9e8d183b5abc80117c40bbd187a1fde336be6e8"
SRC_URI[unicode-properties-0.1.4.sha256sum] = "7df058c713841ad818f1dc5d3fd88063241cc61f49f5fbea4b951e8cf5a8d71d"
SRC_URI[unicode-segmentation-1.13.3.sha256sum] = "c6f5d3c3b1bf09027a88a6bc961fc00497d651009560b5463668dc81b0fa87a8"
SRC_URI[unicode-xid-0.2.6.sha256sum] = "ebc1c04c71510c7f702b52b7c350734c9ff1295c464a03335b00bb84fc54f853"
SRC_URI[universal-hash-0.5.1.sha256sum] = "fc1de2c688dc15305988b563c3854064043356019f97a4b46276fe734c4f07ea"
SRC_URI[untrusted-0.9.0.sha256sum] = "8ecb6da28b8a351d773b68d5825ac39017e680750f980f3a1a85cd8dd28a47c1"
SRC_URI[url-2.5.8.sha256sum] = "ff67a8a4397373c3ef660812acab3268222035010ab8680ec4215f38ba3d0eed"
SRC_URI[utf-8-0.7.6.sha256sum] = "09cc8ee72d2a9becf2f2febe0205bbed8fc6615b7cb429ad062dc7b7ddd036a9"
SRC_URI[utf8_iter-1.0.4.sha256sum] = "b6c140620e7ffbb22c2dee59cafe6084a59b5ffc27a8859a5f0d494b5d52b6be"
SRC_URI[uuid-1.23.4.sha256sum] = "bf80a72845275afea99e7f2b434723d3bc7e38470fcd1c7ed39a599c73319a53"
SRC_URI[valuable-0.1.1.sha256sum] = "ba73ea9cf16a25df0c8caa16c51acb937d5712a8429db78a3ee29d5dcacd3a65"
SRC_URI[vcpkg-0.2.15.sha256sum] = "accd4ea62f7bb7a82fe23066fb0957d48ef677f6eeb8215f372f52e48bb32426"
SRC_URI[version_check-0.9.5.sha256sum] = "0b928f33d975fc6ad9f86c8f283853ad26bdd5b10b7f1542aa2fa15e2289105a"
SRC_URI[visibility-0.1.1.sha256sum] = "d674d135b4a8c1d7e813e2f8d1c9a58308aee4a680323066025e53132218bd91"
SRC_URI[walkdir-2.5.0.sha256sum] = "29790946404f91d9c5d06f9874efddea1dc06c5efe94541a7d6863108e3a5e4b"
SRC_URI[want-0.3.1.sha256sum] = "bfa7760aed19e106de2c7c0b581b509f2f25d3dacaf737cb82ac61bc6d760b0e"
SRC_URI[wasi-0.11.1+wasi-snapshot-preview1.sha256sum] = "ccf3ec651a847eb01de73ccad15eb7d99f80485de043efb2f370cd654f4ea44b"
SRC_URI[wasip2-1.0.4+wasi-0.2.12.sha256sum] = "b67efb37e106e55ce722a510d6b5f9c17f083e5fc79afc2badeb12cc313d9487"
SRC_URI[wasm-bindgen-0.2.126.sha256sum] = "4b067c0c11094aef6b7a801c1e34a26affafdf3d051dba08456b868789aaf9a4"
SRC_URI[wasm-bindgen-futures-0.4.76.sha256sum] = "c62df1340f32221cb9c54d6a27b030e3dba64361d4a95bed55f9aacb44da291d"
SRC_URI[wasm-bindgen-macro-0.2.126.sha256sum] = "167ce5e579f6bcf889c4f7175a8a5a585de84e8ff93976ce393efa5f2837aab1"
SRC_URI[wasm-bindgen-macro-support-0.2.126.sha256sum] = "f3997c7839262f4ef12cf90b818d6340c18e80f263f1a94bf157d0ec4420380e"
SRC_URI[wasm-bindgen-shared-0.2.126.sha256sum] = "dc1b4cb0cc549fcf58d7dfc081778139b3d283a081644e833e84682ad71cea24"
SRC_URI[wasm-streams-0.4.2.sha256sum] = "15053d8d85c7eccdbefef60f06769760a563c7f0a9d6902a13d35c7800b0ad65"
SRC_URI[web-sys-0.3.103.sha256sum] = "8622dcb61c0bcc9fffa6938bed81210af2da9a7e4a1a834b2e37a59b6dfb6141"
SRC_URI[web-time-1.1.0.sha256sum] = "5a6580f308b1fad9207618087a65c04e7a10bc77e02c8e84e9b00dd4b12fa0bb"
SRC_URI[webpki-root-certs-0.26.11.sha256sum] = "75c7f0ef91146ebfb530314f5f1d24528d7f0767efbfd31dce919275413e393e"
SRC_URI[webpki-root-certs-1.0.9.sha256sum] = "b96554aa2acc8ccdb7e1c9a58a7a68dd5d13bccc69cd124cb09406db612a1c9b"
SRC_URI[which-4.4.2.sha256sum] = "87ba24419a2078cd2b0f2ede2691b6c66d8e47836da3b6db8265ebad47afbfc7"
SRC_URI[whoami-2.1.2.sha256sum] = "998767ef88740d1f5b0682a9c53c24431453923962269c2db68ee43788c5a40d"
SRC_URI[winapi-0.3.9.sha256sum] = "5c839a674fcd7a98952e593242ea400abe93992746761e38641405d28b00f419"
SRC_URI[winapi-i686-pc-windows-gnu-0.4.0.sha256sum] = "ac3b87c63620426dd9b991e5ce0329eff545bccbbb34f3be09ff6fb6ab51b7b6"
SRC_URI[winapi-util-0.1.11.sha256sum] = "c2a7b1c03c876122aa43f3020e6c3c3ee5c05081c9a00739faf7503aeba10d22"
SRC_URI[winapi-x86_64-pc-windows-gnu-0.4.0.sha256sum] = "712e227841d057c1ee1cd2fb22fa7e5a5461ae8e48fa2ca79ec42cfc1931183f"
SRC_URI[windows-core-0.62.2.sha256sum] = "b8e83a14d34d0623b51dce9581199302a221863196a1dde71a7663a4c2be9deb"
SRC_URI[windows-implement-0.60.2.sha256sum] = "053e2e040ab57b9dc951b72c264860db7eb3b0200ba345b4e4c3b14f67855ddf"
SRC_URI[windows-interface-0.59.3.sha256sum] = "3f316c4a2570ba26bbec722032c4099d8c8bc095efccdc15688708623367e358"
SRC_URI[windows-link-0.2.1.sha256sum] = "f0805222e57f7521d6a62e36fa9163bc891acd422f971defe97d64e70d0a4fe5"
SRC_URI[windows-result-0.4.1.sha256sum] = "7781fa89eaf60850ac3d2da7af8e5242a5ea78d1a11c49bf2910bb5a73853eb5"
SRC_URI[windows-strings-0.5.1.sha256sum] = "7837d08f69c77cf6b07689544538e017c1bfcf57e34b4c0ff58e6c2cd3b37091"
SRC_URI[windows-sys-0.45.0.sha256sum] = "75283be5efb2831d37ea142365f009c02ec203cd29a3ebecbc093d52315b66d0"
SRC_URI[windows-sys-0.52.0.sha256sum] = "282be5f36a8ce781fad8c8ae18fa3f9beff57ec1b52cb3de0789201425d9a33d"
SRC_URI[windows-sys-0.59.0.sha256sum] = "1e38bc4d79ed67fd075bcc251a1c39b32a1776bbe92e5bef1f0bf1f8c531853b"
SRC_URI[windows-sys-0.61.2.sha256sum] = "ae137229bcbd6cdf0f7b80a31df61766145077ddf49416a728b02cb3921ff3fc"
SRC_URI[windows-targets-0.42.2.sha256sum] = "8e5180c00cd44c9b1c88adb3693291f1cd93605ded80c250a75d472756b4d071"
SRC_URI[windows-targets-0.52.6.sha256sum] = "9b724f72796e036ab90c1021d4780d4d3d648aca59e491e6b98e725b84e99973"
SRC_URI[windows_aarch64_gnullvm-0.42.2.sha256sum] = "597a5118570b68bc08d8d59125332c54f1ba9d9adeedeef5b99b02ba2b0698f8"
SRC_URI[windows_aarch64_gnullvm-0.52.6.sha256sum] = "32a4622180e7a0ec044bb555404c800bc9fd9ec262ec147edd5989ccd0c02cd3"
SRC_URI[windows_aarch64_msvc-0.42.2.sha256sum] = "e08e8864a60f06ef0d0ff4ba04124db8b0fb3be5776a5cd47641e942e58c4d43"
SRC_URI[windows_aarch64_msvc-0.52.6.sha256sum] = "09ec2a7bb152e2252b53fa7803150007879548bc709c039df7627cabbd05d469"
SRC_URI[windows_i686_gnu-0.42.2.sha256sum] = "c61d927d8da41da96a81f029489353e68739737d3beca43145c8afec9a31a84f"
SRC_URI[windows_i686_gnu-0.52.6.sha256sum] = "8e9b5ad5ab802e97eb8e295ac6720e509ee4c243f69d781394014ebfe8bbfa0b"
SRC_URI[windows_i686_gnullvm-0.52.6.sha256sum] = "0eee52d38c090b3caa76c563b86c3a4bd71ef1a819287c19d586d7334ae8ed66"
SRC_URI[windows_i686_msvc-0.42.2.sha256sum] = "44d840b6ec649f480a41c8d80f9c65108b92d89345dd94027bfe06ac444d1060"
SRC_URI[windows_i686_msvc-0.52.6.sha256sum] = "240948bc05c5e7c6dabba28bf89d89ffce3e303022809e73deaefe4f6ec56c66"
SRC_URI[windows_x86_64_gnu-0.42.2.sha256sum] = "8de912b8b8feb55c064867cf047dda097f92d51efad5b491dfb98f6bbb70cb36"
SRC_URI[windows_x86_64_gnu-0.52.6.sha256sum] = "147a5c80aabfbf0c7d901cb5895d1de30ef2907eb21fbbab29ca94c5b08b1a78"
SRC_URI[windows_x86_64_gnullvm-0.42.2.sha256sum] = "26d41b46a36d453748aedef1486d5c7a85db22e56aff34643984ea85514e94a3"
SRC_URI[windows_x86_64_gnullvm-0.52.6.sha256sum] = "24d5b23dc417412679681396f2b49f3de8c1473deb516bd34410872eff51ed0d"
SRC_URI[windows_x86_64_msvc-0.42.2.sha256sum] = "9aec5da331524158c6d1a4ac0ab1541149c0b9505fde06423b02f5ef0106b9f0"
SRC_URI[windows_x86_64_msvc-0.52.6.sha256sum] = "589f6da84c646204747d1270a2a5661ea66ed1cced2631d546fdfb155959f9ec"
SRC_URI[winnow-1.0.3.sha256sum] = "0592e1c9d151f854e6fd382574c3a0855250e1d9b2f99d9281c6e6391af352f1"
SRC_URI[wit-bindgen-0.57.1.sha256sum] = "1ebf944e87a7c253233ad6766e082e3cd714b5d03812acc24c318f549614536e"
SRC_URI[writeable-0.6.3.sha256sum] = "1ffae5123b2d3fc086436f8834ae3ab053a283cfac8fe0a0b8eaae044768a4c4"
SRC_URI[x25519-dalek-2.0.1.sha256sum] = "c7e468321c81fb07fa7f4c636c3972b9100f0346e5b6a9f2bd0603a52f7ed277"
SRC_URI[yoke-0.8.3.sha256sum] = "709fe23a0424b6a435d82152b1bd3fdfb0833487d5fa90d05d42762a9891fef5"
SRC_URI[yoke-derive-0.8.2.sha256sum] = "de844c262c8848816172cef550288e7dc6c7b7814b4ee56b3e1553f275f1858e"
SRC_URI[zerocopy-0.8.54.sha256sum] = "b7cbbc0a705a0fd05cc3676525980d2bf5a9bc4adac6d6475209a7887cf59d19"
SRC_URI[zerocopy-derive-0.8.54.sha256sum] = "e2e817b7b52d0c7358d3246da9d69935ebb18116b2b102b4230dac079b4862f5"
SRC_URI[zerofrom-0.1.8.sha256sum] = "0ec05a11813ea801ff6d75110ad09cd0824ddba17dfe17128ea0d5f68e6c5272"
SRC_URI[zerofrom-derive-0.1.7.sha256sum] = "11532158c46691caf0f2593ea8358fed6bbf68a0315e80aae9bd41fbade684a1"
SRC_URI[zeroize-1.9.0.sha256sum] = "e13c156562582aa81c60cb29407084cdb54c4164760106ab78e6c5b0858cf64e"
SRC_URI[zeroize_derive-1.5.0.sha256sum] = "3c50655cbb0fe3fc43170059e702f1ce5e19b84cec58dc87b037a09935c2f328"
SRC_URI[zerotrie-0.2.4.sha256sum] = "0f9152d31db0792fa83f70fb2f83148effb5c1f5b8c7686c3459e361d9bc20bf"
SRC_URI[zerovec-0.11.6.sha256sum] = "90f911cbc359ab6af17377d242225f4d75119aec87ea711a880987b18cd7b239"
SRC_URI[zerovec-derive-0.11.3.sha256sum] = "625dc425cab0dca6dc3c3319506e6593dcb08a9f387ea3b284dbd52a92c40555"
SRC_URI[zmij-1.0.21.sha256sum] = "b8848ee67ecc8aedbaf3e4122217aff892639231befc6a1b58d29fff4c2cabaa"
