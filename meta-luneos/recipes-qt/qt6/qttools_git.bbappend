# Fails with clang-19
# http://gecko.lge.com:8000/Errors/Details/953040
# qttools-native/6.7.2/git/src/linguist/lupdate/clangtoolastreader.cpp:785:73: error: const class clang::FileEntry has no member named getName
# FileEntry::GetName() was deprecated since clang-17 and removed in clang-19 with:
# https://github.com/llvm/llvm-project/commit/6c1dbd5359c4336d03b11faeaea8459b421f2c5c
PACKAGECONFIG:remove = "clang"
