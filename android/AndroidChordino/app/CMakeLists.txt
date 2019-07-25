# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(LIB_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs)
# ----------add a pre-build lib
add_library(sndfile SHARED IMPORTED)
add_library(chordino_depend SHARED IMPORTED)
add_library(mediaformat SHARED IMPORTED)
add_library(lame SHARED IMPORTED)
# --------path to find pre-build lib
set_target_properties(sndfile PROPERTIES IMPORTED_LOCATION ${LIB_DIR}/${ANDROID_ABI}/libsndfile.so)
set_target_properties(chordino_depend PROPERTIES IMPORTED_LOCATION ${LIB_DIR}/${ANDROID_ABI}/libchordino_depend.so)
set_target_properties(mediaformat PROPERTIES IMPORTED_LOCATION ${LIB_DIR}/${ANDROID_ABI}/libmediaformat.so)
set_target_properties(lame PROPERTIES IMPORTED_LOCATION ${LIB_DIR}/${ANDROID_ABI}/liblame.so)

include_directories(src/main/cpp)
include_directories(src/main/cpp/lame)
# include_directories(SYSTEM ${ANDROID_NDK}/sysroot/usr/include)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/chordextract.cpp
        src/main/cpp/Chordino.cpp
        src/main/cpp/chromamethods.cpp
        src/main/cpp/nnls.c
        src/main/cpp/NNLSBase.cpp
        src/main/cpp/NNLSChroma.cpp
        src/main/cpp/plugins.cpp
        src/main/cpp/Tuning.cpp
        src/main/cpp/viterbi.cpp

        src/main/cpp/mediamanager.cpp
        src/main/cpp/sndmedia.cpp
        src/main/cpp/formats.cpp
        src/main/cpp/cut_generator.cpp
        src/main/cpp/cut_gen_strategy.cpp
        src/main/cpp/number.cpp

        src/main/cpp/mp3ext/mp3_upperlayyer.cpp
        src/main/cpp/mp3ext/java_env.cpp
        src/main/cpp/android/logger.cpp
        src/main/cpp/android/native-lib.cpp
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib
        mediaformat
        chordino_depend
        sndfile
        lame

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})