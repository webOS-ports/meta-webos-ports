# The bundled externals (download.lst) this configuration builds, for
# configure --with-external-tar: fetched by bitbake, not by Makefile.fetch.
# From download.lst of libreoffice-26.8.1.1; regenerate with the version.
SRC_URI += " \
    https://dev-www.libreoffice.org/src/1f5def51ca0026cd192958ef07228b52-rasqal-0.9.33.tar.gz;name=rasqal;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/33e1e61fab06a547851ed308b4ffef42-dejavu-fonts-ttf-2.37.zip;name=font_dejavu;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/368f114c078f94214a308a74c7e991bc-crosextrafonts-20130214.tar.gz;name=font_caladea;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/442b985e604f8143a5a0e7bacd6b2e40-hyphen-2.8.9.tar.gz;name=hyphen;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/48d647fbd8ef8889e5a7f422c1bfda94-clucene-core-2.3.3.4.tar.gz;name=clucene;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/Agdasima-2.002.zip;name=font_agdasima;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/Amiri-1.001.zip;name=font_amiri;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/Bacasime_Antique-2.000.zip;name=font_bacasime_antique;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/Belanosima-2.000.zip;name=font_belanosima;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/Caprasimo-1.001.zip;name=font_caprasimo;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/Lugrasimo-1.001.zip;name=font_lugrasimo;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/Lumanosimo-1.010.zip;name=font_lumanosimo;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/Lunasima-2.009.zip;name=font_lunasima;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoKufiArabic-v2.110.zip;name=font_noto_kufi_arabic;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoNaskhArabic-v2.021.zip;name=font_noto_naskh_arabic;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSans-v2.015.zip;name=font_noto_sans;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSansArabic-v2.010.zip;name=font_noto_sans_arabic;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSansArmenian-v2.008.zip;name=font_noto_sans_armenian;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSansGeorgian-v2.005.zip;name=font_noto_sans_georgian;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSansHebrew-v3.001.zip;name=font_noto_sans_hebrew;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSansLao-v2.003.zip;name=font_noto_sans_lao;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSansLisu-v2.102.zip;name=font_noto_sans_lisu;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSerif-v2.015.zip;name=font_noto_serif;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSerifArmenian-v2.008.zip;name=font_noto_serif_armenian;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSerifGeorgian-v2.003.zip;name=font_noto_serif_georgian;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSerifHebrew-v2.004.zip;name=font_noto_serif_hebrew;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/NotoSerifLao-v2.003.zip;name=font_noto_serif_lao;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/ReemKufi-2.0.zip;name=font_reem;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/Scheherazade-2.100.zip;name=font_scheherazade;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/alef-1.001.tar.gz;name=font_alef;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/boost_1_91_0.tar.xz;name=boost;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/box2d-3.1.1.tar.gz;name=box2d;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/c74b7223abe75949b4af367942d96c7a-crosextrafonts-carlito-20130920.tar.gz;name=font_carlito;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/cairo-1.18.4.tar.xz;name=cairo;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/cppunit-1.15.1.tar.gz;name=cppunit;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/culmus-0.140.tar.gz;name=font_culmus;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/curl-8.22.0.tar.xz;name=curl;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/dragonbox-1.1.3.tar.gz;name=dragonbox;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/e5be03eda13ef68aabab6e42aa67715e-redland-1.0.17.tar.gz;name=redland;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/e7a384790b13c29113e22e596ade9687-LinLibertineG-20120116.zip;name=font_linlibertineg;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/expat-2.8.4.tar.xz;name=expat;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/extern/f543e6e2d7275557a839a164941c0a86e5f2c3f2a0042bfc434c88c6dde9e140-opens___.ttf;name=opensymbol;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/fast_float-8.2.10.tar.gz;name=fast_float;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/frozen-1.2.0.tar.gz;name=frozen;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/glm-1.0.3.zip;name=glm;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/graphite2-minimal-1.3.15.tgz;name=graphite;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/harfbuzz-14.2.1.tar.xz;name=harfbuzz;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/icu4c-78.3-data.zip;name=icu_data;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/icu4c-78.3-sources.tgz;name=icu;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/language-subtag-registry-2026-08-08.tar.bz2;name=langtagreg;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/lcms2-2.19.1.tar.gz;name=lcms2;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libabw-0.1.4.tar.xz;name=abw;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libcdr-0.1.9.tar.xz;name=cdr;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libe-book-0.1.4.tar.xz;name=ebook;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libeot-0.01.tar.bz2;name=libeot;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libepoxy-1.5.10.tar.gz;name=epoxy;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libepubgen-0.1.1.tar.xz;name=epubgen;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/liberation-fonts-ttf-2.1.5.tar.gz;name=font_liberation;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/liberation-narrow-fonts-ttf-1.07.6.tar.gz;name=font_liberation_narrow;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libetonyek-0.1.13.tar.xz;name=etonyek;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libexttextcat-3.4.8.tar.xz;name=libexttextcat;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libfreehand-0.1.4.tar.xz;name=freehand;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libjpeg-turbo-3.1.4.1.tar.gz;name=libjpeg_turbo;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/liblangtag-0.6.8.tar.bz2;name=liblangtag;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libmspub-0.1.5.tar.xz;name=mspub;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libmwaw-0.3.23.tar.xz;name=mwaw;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libnumbertext-1.0.11.tar.xz;name=libnumbertext;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libodfgen-0.1.8.tar.xz;name=odfgen;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/liborcus-0.21.0.tar.xz;name=orcus;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libpagemaker-0.0.4.tar.xz;name=pagemaker;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libpng-1.6.58.tar.xz;name=libpng;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libqxp-0.0.3.tar.xz;name=qxp;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libre-hebrew-1.0.tar.gz;name=font_libre_hebrew;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/librevenge-0.0.6.tar.bz2;name=revenge;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libstaroffice-0.0.8.tar.xz;name=staroffice;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libvisio-0.1.11.tar.xz;name=visio;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libwebp-1.6.0.tar.gz;name=libwebp;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libwpd-0.10.3.tar.xz;name=wpd;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libwpg-0.3.4.tar.xz;name=wpg;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libwps-0.4.14.tar.xz;name=wps;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libxml2-2.15.4.tar.xz;name=libxml;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libxslt-1.1.45.tar.xz;name=libxslt;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/libzmf-0.0.2.tar.xz;name=zmf;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/md4c-release-0.5.3.tar.gz;name=md4c;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/mdds-3.2.1.tar.xz;name=mdds;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/meson-1.8.3.tar.gz;name=meson;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/mythes-1.2.6.tar.xz;name=mythes;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/openssl-3.5.8.tar.gz;name=openssl;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/phc-winner-argon2-20190702.tar.gz;name=argon2;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/pixman-0.46.4.tar.xz;name=pixman;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/raptor2-2.0.16.tar.gz;name=raptor;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/tiff-4.7.2.tar.xz;name=libtiff;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/xmlsec1-1.3.12.tar.gz;name=xmlsec;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/zstd-1.5.7.tar.gz;name=zstd;unpack=0;subdir=externals \
    https://dev-www.libreoffice.org/src/zxcvbn-c-2.6.tar.gz;name=zxcvbn_c;unpack=0;subdir=externals \
"
SRC_URI[rasqal.sha256sum] = "6924c9ac6570bd241a9669f83b467c728a322470bf34f4b2da4f69492ccfd97c"
SRC_URI[font_dejavu.sha256sum] = "7576310b219e04159d35ff61dd4a4ec4cdba4f35c00e002a136f00e96a908b0a"
SRC_URI[font_caladea.sha256sum] = "c48d1c2fd613c9c06c959c34da7b8388059e2408d2bb19845dc3ed35f76e4d09"
SRC_URI[hyphen.sha256sum] = "783743daf477de8c4d16e3c74b4d2827377017718d8e17e2d9440210246f6abe"
SRC_URI[clucene.sha256sum] = "ddfdc433dd8ad31b5c5819cc4404a8d2127472a3b720d3e744e8c51d79732eab"
SRC_URI[font_agdasima.sha256sum] = "b4c17499f8bc183320ffdcf1f8491c778dd68a237adc056dc08e1fcb2da488f3"
SRC_URI[font_amiri.sha256sum] = "cae999a9fc5638cb69cf0812e8bca1437ef1ebbf094f8b3c5b3f0a3ea2ef8c3a"
SRC_URI[font_bacasime_antique.sha256sum] = "81127cebc97d4ee0b950c2bc2d6be0ed29abfe0d5988435eb9a39e382557250a"
SRC_URI[font_belanosima.sha256sum] = "9c30e9e7cdb0797b651113a77f6b789659b8d8598a466b2ba92754e9dec30449"
SRC_URI[font_caprasimo.sha256sum] = "f066d27cfc99b6601083be102d08b4039da8d2834e2795aabd890f16c6fd1b9b"
SRC_URI[font_lugrasimo.sha256sum] = "db24dff31e8ddf6a113556c947a219f9534b072852e9ce2079a08c94854a7503"
SRC_URI[font_lumanosimo.sha256sum] = "d4545ad6afded9a55d9a7728cfc7453cf5637861db87aafe0a730676a0cb960f"
SRC_URI[font_lunasima.sha256sum] = "4302809cf4b95b481bec53de87484d919b391c90a518d065f6c775fd435a5393"
SRC_URI[font_noto_kufi_arabic.sha256sum] = "34390882c196f032811f2a3db8982c1e839f69aae3e4c2c1fdd63203150690df"
SRC_URI[font_noto_naskh_arabic.sha256sum] = "6c050ab9bd087d69b733c505a7576e60c528c2f33cd7b91005a5bd7da4514032"
SRC_URI[font_noto_sans.sha256sum] = "0c34df072a3fa7efbb7cbf34950e1f971a4447cffe365d3a359e2d4089b958f5"
SRC_URI[font_noto_sans_arabic.sha256sum] = "a5a34ac1ea01d0d71c083f99440ebfb1f64224474a0d88bb7ef0e2f8d9a996d2"
SRC_URI[font_noto_sans_armenian.sha256sum] = "eab89b99e134177ca6a3f9f0412a7cb812aafceb13175d686b4c45cb237f64ac"
SRC_URI[font_noto_sans_georgian.sha256sum] = "10e85011008108308e6feab0408242acb07804da61ede3d3ff236461ae07ab1b"
SRC_URI[font_noto_sans_hebrew.sha256sum] = "df0a71814b4e63644cf40fcc4529111b61266b7a2dafbe95068b29a7520cc3cb"
SRC_URI[font_noto_sans_lao.sha256sum] = "5a87c31b1a40ef8147c1e84437e5f0ceba2d4dbbfc0b56a65821ad29870da8c0"
SRC_URI[font_noto_sans_lisu.sha256sum] = "b12a1ff762680681b7ce4d98dd29a7f54d90f5bcadd10c955afc640a27b3a268"
SRC_URI[font_noto_serif.sha256sum] = "0e9a43c8a4b94ac76f55069ed1d7385bbcaf6b99527a94deb5619e032b7e76c1"
SRC_URI[font_noto_serif_armenian.sha256sum] = "b21c198a4c76ae598a304decefb3b5c2a4c2d4c3ae226728eff359185f291c6f"
SRC_URI[font_noto_serif_georgian.sha256sum] = "cfb41a264b97d463bab6807a5be937ba4a6ddcfa93d519a21b98b0ba73ca27d4"
SRC_URI[font_noto_serif_hebrew.sha256sum] = "99523f4f21051495f18cbd5169ed0d1e9b395eefe770fece1844a4a7a00c46da"
SRC_URI[font_noto_serif_lao.sha256sum] = "e96a303d3347790b0ef3db274971a989a736ce766ec9ea1bea0e1458568a80b2"
SRC_URI[font_reem.sha256sum] = "94ae468fd88f37011607f03466bfc3a03fb617682b7bac6419b70de94cac22f0"
SRC_URI[font_scheherazade.sha256sum] = "251c8817ceb87d9b661ce1d5b49e732a0116add10abc046be4b8ba5196e149b5"
SRC_URI[font_alef.sha256sum] = "b98b67602a2c8880a1770f0b9e37c190f29a7e2ade5616784f0b89fbdb75bf52"
SRC_URI[boost.sha256sum] = "2f975c10da79511c2f218189fc8a12eef1a92e3bd18206e9841d406296d065eb"
SRC_URI[box2d.sha256sum] = "fb6ef914b50f4312d7d921a600eabc12318bb3c55a0b8c0b90608fa4488ef2e4"
SRC_URI[font_carlito.sha256sum] = "4bd12b6cbc321c1cf16da76e2c585c925ce956a08067ae6f6c64eff6ccfdaf5a"
SRC_URI[cairo.sha256sum] = "445ed8208a6e4823de1226a74ca319d3600e83f6369f99b14265006599c32ccb"
SRC_URI[cppunit.sha256sum] = "89c5c6665337f56fd2db36bc3805a5619709d51fb136e51937072f63fcc717a7"
SRC_URI[font_culmus.sha256sum] = "6daed104481007752a76905000e71c0093c591c8ef3017d1b18222c277fc52e3"
SRC_URI[curl.sha256sum] = "f7ef3ae8a22e521f289803fe93543eb64c329b58aa73a9e224dfd915a2a5f4f7"
SRC_URI[dragonbox.sha256sum] = "09d63b05e9c594ec423778ab59b7a5aa1d76fdd71d25c7048b0258c4ec9c3384"
SRC_URI[redland.sha256sum] = "de1847f7b59021c16bdc72abb4d8e2d9187cd6124d69156f3326dd34ee043681"
SRC_URI[font_linlibertineg.sha256sum] = "54adcb2bc8cac0927a647fbd9362f45eff48130ce6e2379dc3867643019e08c5"
SRC_URI[expat.sha256sum] = "656ae1cc8da3b4ea513bb4e254f33e6243938084c0ec6239da873376b09985a7"
SRC_URI[opensymbol.sha256sum] = "f543e6e2d7275557a839a164941c0a86e5f2c3f2a0042bfc434c88c6dde9e140"
SRC_URI[fast_float.sha256sum] = "76f958dd97b1cf4d8862d1f0986a47d4bdfa8845252bae15ef0f40de3b95961f"
SRC_URI[frozen.sha256sum] = "ed8339c017d7c5fe019ac2c642477f435278f0dc643c1d69d3f3b1e95915e823"
SRC_URI[glm.sha256sum] = "3aa4347b8f13cba882df1c7b61a6ca910c75a875c56ec3d75d7dc9ae8eac34df"
SRC_URI[graphite.sha256sum] = "10cfc69a6f859ce875b9708d170b8222eba76cdb89b6dbb4ee1e375bc7242409"
SRC_URI[harfbuzz.sha256sum] = "a54a5d8e9380a41fbb762ce367bcbf7704792dfca0d93f1bbca86c5a57902e0e"
SRC_URI[icu_data.sha256sum] = "9d8b3899096aeb83e4e21ef8a40fec9e03b28db18c48452efac882ce25a91e27"
SRC_URI[icu.sha256sum] = "3a2e7a47604ba702f345878308e6fefeca612ee895cf4a5f222e7955fabfe0c0"
SRC_URI[langtagreg.sha256sum] = "48293d95a13372f3e5bae04890d7d54ef4168b2469060e8e49cf60461cfc616f"
SRC_URI[lcms2.sha256sum] = "bfc54f7bab59fbc921012014a8032e4cba4abd46db47d46b76416a8c0b2815c8"
SRC_URI[abw.sha256sum] = "fa2685a3440da6e03a66a778480d93cb95f6064e4541e58e37397680760fd6a0"
SRC_URI[cdr.sha256sum] = "f7bb6abdd7f226820f288a93dd8d07759833c0250d9e202af90f9b312c4665a3"
SRC_URI[ebook.sha256sum] = "104b7e791b6632745898e9b6a0037b7540235771a4d8c3bde2c764466ad912f1"
SRC_URI[libeot.sha256sum] = "cf5091fa8e7dcdbe667335eb90a2cfdd0a3fe8f8c7c8d1ece44d9d055736a06a"
SRC_URI[epoxy.sha256sum] = "a7ced37f4102b745ac86d6a70a9da399cc139ff168ba6b8002b4d8d43c900c15"
SRC_URI[epubgen.sha256sum] = "03e084b994cbeffc8c3dd13303b2cb805f44d8f2c3b79f7690d7e3fc7f6215ad"
SRC_URI[font_liberation.sha256sum] = "7191c669bf38899f73a2094ed00f7b800553364f90e2637010a69c0e268f25d0"
SRC_URI[font_liberation_narrow.sha256sum] = "8879d89b5ff7b506c9fc28efc31a5c0b954bbe9333e66e5283d27d20a8519ea3"
SRC_URI[etonyek.sha256sum] = "032b71cb597edd92a0b270b916188281bc35be55296b263f6817b29adbcb1709"
SRC_URI[libexttextcat.sha256sum] = "93eb89fd4fc8f565806354e100e778b3ac9a24e5fc04c24e6a83fb1e9b6c9d59"
SRC_URI[freehand.sha256sum] = "350b10d24a76d7e8c8ae98b74c2d432a2c8ddec08935d09856d20b695a35e600"
SRC_URI[libjpeg_turbo.sha256sum] = "ecae8008e2cc9ade2f2c1bb9d5e6d4fb73e7c433866a056bd82980741571a022"
SRC_URI[liblangtag.sha256sum] = "a96975b79ddd8fef6d9295c083fe3f1afa1a8898a57235d4069255ade44e5cf2"
SRC_URI[mspub.sha256sum] = "3671095f5a10bee8a755052a30576952c5b16d8b0f2ba9f2fb998338c18cb119"
SRC_URI[mwaw.sha256sum] = "ac3590f691a2904eb8c7dc8b757b8a29f125f592449e421459ae8fa928b399eb"
SRC_URI[libnumbertext.sha256sum] = "5dcb4db3b2340f81f601ce86d8d76b69e34d70f84f804192c901e4b7f84d5fb0"
SRC_URI[odfgen.sha256sum] = "55200027fd46623b9bdddd38d275e7452d1b0ff8aeddcad6f9ae6dc25f610625"
SRC_URI[orcus.sha256sum] = "1c6e473d6b8a63d61d0e6874a1762d6f0e0e78338d763e48ac93126ecde19b37"
SRC_URI[pagemaker.sha256sum] = "66adacd705a7d19895e08eac46d1e851332adf2e736c566bef1164e7a442519d"
SRC_URI[libpng.sha256sum] = "28eb403f51f0f7405249132cecfe82ea5c0ef97f1b32c5a65828814ae0d34775"
SRC_URI[qxp.sha256sum] = "4687a9cc96c32d7406e5072c4da150fca696c563e5cde62b024f82fa53d32332"
SRC_URI[font_libre_hebrew.sha256sum] = "f596257c1db706ce35795b18d7f66a4db99d427725f20e9384914b534142579a"
SRC_URI[revenge.sha256sum] = "52a65e904d255dbdd97a8b7bb28d6574e14f999eb01416aff004502406d0904d"
SRC_URI[staroffice.sha256sum] = "bbca92882e96c55bd1527670866d6bc7529ccb12786da55a2398b60a063378da"
SRC_URI[visio.sha256sum] = "2a6efd40b6d9dbcb70fba3be53112366882ba97b57151df3698dfa478c8d8dd3"
SRC_URI[libwebp.sha256sum] = "e4ab7009bf0629fd11982d4c2aa83964cf244cffba7347ecd39019a9e38c4564"
SRC_URI[wpd.sha256sum] = "2465b0b662fdc5d4e3bebcdc9a79027713fb629ca2bff04a3c9251fdec42dd09"
SRC_URI[wpg.sha256sum] = "b55fda9440d1e070630eb2487d8b8697cf412c214a27caee9df69cec7c004de3"
SRC_URI[wps.sha256sum] = "365b968e270e85a8469c6b160aa6af5619a4e6c995dbb04c1ecc1b4dd13e80de"
SRC_URI[libxml.sha256sum] = "98087fd181d9070724f3fbc65c7377db03038eb92bd882374daff44940138821"
SRC_URI[libxslt.sha256sum] = "9acfe68419c4d06a45c550321b3212762d92f41465062ca4ea19e632ee5d216e"
SRC_URI[zmf.sha256sum] = "27051a30cb057fdb5d5de65a1f165c7153dc76e27fe62251cbb86639eb2caf22"
SRC_URI[md4c.sha256sum] = "353c346f376b87c954a13f3415ede2d51264cc61dc5abcd38ff1d2aa0d059b9e"
SRC_URI[mdds.sha256sum] = "673f5bb94612dbba581fc92b99b5e5dd1a53e29496a5dbc936432f6b0687c112"
SRC_URI[meson.sha256sum] = "f118aa910fc0a137cc2dd0122232dbf82153d9a12fb5b0f5bb64896f6a157abf"
SRC_URI[mythes.sha256sum] = "97bd2ba5738aebdba1bc31f30f1be42f1404386c94105ad5990d839a1311c8a5"
SRC_URI[openssl.sha256sum] = "a8f84a39918ec6415ce765d9b429d313ba97b8143169c172e734b9514464f5b2"
SRC_URI[argon2.sha256sum] = "daf972a89577f8772602bf2eb38b6a3dd3d922bf5724d45e7f9589b5e830442c"
SRC_URI[pixman.sha256sum] = "a098c33924754ad43f981b740f6d576c70f9ed1006e12221b1845431ebce1239"
SRC_URI[raptor.sha256sum] = "089db78d7ac982354bdbf39d973baf09581e6904ac4c92a98c5caadb3de44680"
SRC_URI[libtiff.sha256sum] = "4996f0c4f93094719b1ca5c6279b20e588773ba8a247533e486416fb662ddb88"
SRC_URI[xmlsec.sha256sum] = "24045199af12d93fe5fdbbbf7e386e823e4842071e9432e2b90ac108b889a923"
SRC_URI[zstd.sha256sum] = "eb33e51f49a15e023950cd7825ca74a4a2b43db8354825ac24fc1b7ee09e6fa3"
SRC_URI[zxcvbn_c.sha256sum] = "11e39f6776f9c82c68b2acb94336e32697d4ab6cdb4ac16f9583ccbdd735113a"
