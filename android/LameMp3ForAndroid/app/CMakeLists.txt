
cmake_minimum_required(VERSION 3.4.1)

set(LIB_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs)
add_library(mediaformat SHARED IMPORTED)
set_target_properties(mediaformat PROPERTIES IMPORTED_LOCATION ${LIB_DIR}/${ANDROID_ABI}/libmediaformat.so)

set(SRC_DIR src/main/cpp/lamemp3)

include_directories(src/main/cpp/lamemp3)


aux_source_directory(src/main/cpp/lamemp3 SRC_LIST)
aux_source_directory(src/main/cpp/lamemp3/mpglib SRC_LIST2)

add_library(lamemp3 SHARED
        ${SRC_LIST}
        ${SRC_LIST2}
        src/main/cpp/lame_chordino.cpp
        src/main/cpp/array.cpp
        src/main/cpp/lame_jni.c
        src/main/cpp/SimpleLame.cpp
        )

find_library(log-lib log )

target_link_libraries( # Specifies the target library.
        lamemp3
        mediaformat

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})