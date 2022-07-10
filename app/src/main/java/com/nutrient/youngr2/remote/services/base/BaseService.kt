package com.nutrient.youngr2.remote.services.base

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseService {

    companion object {
        private const val CONNECT_TIMEOUT_SEC = 200000L

        private fun getInterceptor() : HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        private fun getOkHttpClient() : OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(getInterceptor())
            .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .build()

        fun getClient(baseUrl: String): Retrofit? = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }
}