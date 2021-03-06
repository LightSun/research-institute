# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#include_directories(src/main/jni/lua)

aux_source_directory(src/main/cpp/clib SRC_CLIB)
aux_source_directory(src/main/cpp/common SRC_COMMON)
aux_source_directory(src/main/cpp/cmp SRC_CMP)
aux_source_directory(src/main/cpp/vm SRC_VM)
aux_source_directory(src/main/cpp/test SRC_TEST)

add_library(
        dummylua

        SHARED

        ${SRC_CLIB}
        ${SRC_COMMON}
        ${SRC_CMP}
        ${SRC_VM}
        ${SRC_TEST}
        src/main/cpp/main.c
        src/main/cpp/test/setjmp_test.c
        src/main/cpp/test/setjmp_test2.c
        src/main/cpp/android/LuaDesigner.cpp
)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)


target_link_libraries( # Specifies the target library.
        # Links the target library to the log library
        # included in the NDK.
        dummylua
        ${log-lib}
        )
