cmake_minimum_required(VERSION 3.22.1)

project("movietmdb")

add_library(
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp)
