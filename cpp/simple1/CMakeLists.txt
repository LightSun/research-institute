cmake_minimum_required(VERSION 3.12)
project(xxx)

set(CMAKE_CXX_STANDARD 14)

add_library(xxx SHARED xxx.cpp test_fanxing_limit.cpp)
add_executable(yyy yyy.cpp)
target_link_libraries(yyy xxx)