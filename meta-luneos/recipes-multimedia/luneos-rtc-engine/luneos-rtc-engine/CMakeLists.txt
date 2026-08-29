cmake_minimum_required(VERSION 3.10)
project(luneos-rtc-engine CXX)

find_package(PkgConfig REQUIRED)
pkg_check_modules(GST REQUIRED gstreamer-1.0 glib-2.0)

add_executable(luneos-rtc-engine src/rtc_engine.cpp)
target_include_directories(luneos-rtc-engine PRIVATE ${GST_INCLUDE_DIRS})
target_link_libraries(luneos-rtc-engine ${GST_LIBRARIES})

install(TARGETS luneos-rtc-engine DESTINATION sbin)
