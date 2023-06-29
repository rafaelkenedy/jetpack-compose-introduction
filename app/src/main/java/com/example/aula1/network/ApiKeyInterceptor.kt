package com.example.aula1.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apikey: String): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val newRequest = originRequest.newBuilder()
            .addHeader("Authorization", "Bearer $apikey")
            .build()
        return chain.proceed(newRequest)
    }
}