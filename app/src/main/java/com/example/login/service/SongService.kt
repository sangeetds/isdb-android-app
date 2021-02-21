package com.example.login.service

import com.example.login.models.Song
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SongService {

    @GET("/tracks")
    suspend fun getSongs(@Query("search") songName: String?): Call<List<Song>>
}

suspend fun getSongsList(service: SongService, songName: String?): Response<List<Song>> =
    service.getSongs(songName = songName).execute()