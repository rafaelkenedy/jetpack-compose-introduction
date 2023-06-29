package com.example.aula1.network

import android.os.Build.VERSION_CODES.BASE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {

    private const val API_KEY = "YOU_DREAM"
    private const val BASE_URL = "https://api.openai.com/"

    fun provideAiService(): AiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(provideApiKeyInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AiService::class.java)

    private fun provideApiKeyInterceptor(): ApiKeyInterceptor =
        ApiKeyInterceptor(API_KEY)

    private fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(logInterceptor)
            .build()
    }
}