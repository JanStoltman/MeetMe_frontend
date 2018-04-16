package com.yggdralisk.meetme.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Jan Stoltman on 4/7/18.
 */
class APIGenerator {
    companion object {
        const val BASE_URL: String = "http://zpimeetme.gear.host"

        private val builder: Retrofit.Builder = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())

        private var retrofit: Retrofit = builder.build()

        private val logging : HttpLoggingInterceptor =
                HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)

        private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        fun <S> createService(serviceClass: Class<S>): S {
            if(!httpClient.interceptors().contains(logging)){
                httpClient.addInterceptor(logging)
                builder.client(httpClient.build())
                retrofit = builder.build()
            }
            return retrofit.create(serviceClass)
        }
    }
}