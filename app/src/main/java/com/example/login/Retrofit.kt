package com.example.login

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Retrofit client class which has an companion function that returns the client to make
 * API requests.
 */
class Retrofit {
    companion object {

        fun getRetrofitClient(url: String): LoginService {
            val httpClient = OkHttpClient.Builder().build()

            /**
             * Moshi helps in converting JSON objects into Java Classes and parameters.
             */
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient)
                .build()

            return retrofit.create(LoginService::class.java)
        }
    }
}