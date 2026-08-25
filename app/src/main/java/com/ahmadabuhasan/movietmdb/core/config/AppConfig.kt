package com.ahmadabuhasan.movietmdb.core.config

object AppConfig {

    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String
}
