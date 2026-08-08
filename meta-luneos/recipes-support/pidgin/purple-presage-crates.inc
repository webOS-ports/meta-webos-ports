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
