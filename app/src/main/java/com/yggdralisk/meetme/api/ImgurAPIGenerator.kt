package com.yggdralisk.meetme.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Jan Stoltman on 6/10/18.
 */
class ImgurAPIGenerator {
    companion object {
        private const val BASE_URL = "https://api.imgur.com"
        private const val IMGUR_CLIENT_ID = "cee3b0da0b9dedc"
        private const val IMGUR_CLIENT_SECRET = "9da96a801fcd63e27acaac1f40a48ce2a4df7daf"

        private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
                .readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .build()

        private val builder: Retrofit.Builder = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())

        private var retrofit: Retrofit = builder.build()

        private val logging: HttpLoggingInterceptor =
                HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)

        private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        fun <S> createService(serviceClass: Class<S>): S {
            if (!httpClient.interceptors().contains(logging)) {
                httpClient.addInterceptor(logging)
                httpClient.addInterceptor {
                    val original = it.request()
                    val request = original.newBuilder()
                            .header("Authorization", "Client-ID $IMGUR_CLIENT_ID")
                            .method(original.method(), original.body())
                            .build()

                    it.proceed(request)
                }

                builder.client(httpClient.readTimeout(3, TimeUnit.MINUTES)
                        .connectTimeout(3, TimeUnit.MINUTES)
                        .writeTimeout(3, TimeUnit.MINUTES).build())
                retrofit = builder.build()
            }
            return retrofit.create(serviceClass)
        }
    }
}