package com.isdb.di

import com.isdb.login.data.api.LoginService
import com.isdb.tracks.data.api.SongService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

  @Provides
  fun getBaseUrl() = "https://pure-ridge-30175.herokuapp.com"

  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

  @Provides
  @Singleton
  fun provideMoshi(): Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

  @Provides
  @Singleton
  fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String,
    moshi: Moshi
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(okHttpClient)
      .build()

  @Provides
  @Singleton
  fun provideLoginService(retrofit: Retrofit): LoginService =
    retrofit.create(LoginService::class.java)

  @Provides
  @Singleton
  fun providesSongService(retrofit: Retrofit): SongService =
    retrofit.create(SongService::class.java)
}