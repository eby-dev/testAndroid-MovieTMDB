package com.ahmadabuhasan.movietmdb.core.network

import com.ahmadabuhasan.movietmdb.core.config.AppConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${AppConfig.apiKey()}")
            .build()
        return chain.proceed(request)
    }
}
