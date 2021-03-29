package com.example.login.service

import com.example.login.models.SongDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SongService {

    @GET("/tracks")
    fun getSongs(@Query("search") songName: String?): Call<List<SongDTO>>
}

fun getSongsList(service: SongService, songName: String?): Response<List<SongDTO>> =
    service.getSongs(songName = songName).execute()