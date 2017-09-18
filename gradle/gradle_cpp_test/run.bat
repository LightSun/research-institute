cd e: && cd E:\study\github\research-institute\gradle\gradle_cpp_test
call gradlew greeterStaticLibrary && call gradlew greeterSharedLibrary && call gradlew mainExecutable

set LD_LIBRARY_PATH=build/libs/greeter/shared/ start build/exe/main/main

PAUSE