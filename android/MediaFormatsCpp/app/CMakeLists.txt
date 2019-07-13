
cmake_minimum_required(VERSION 3.4.1)

#include_directories(src/main/cpp)

aux_source_directory(src/main/cpp SRC_LIST)

add_library(mediaformat SHARED
        ${SRC_LIST}
        )

#find_library(log-lib log )

target_link_libraries( # Specifies the target library.
        mediaformat

        # Links the target library to the log library
        # included in the NDK.
        #${log-lib}
        )