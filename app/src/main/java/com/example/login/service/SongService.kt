package com.example.login.service

import com.example.login.models.Song
import com.example.login.models.SongDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SongService {

  @GET("/tracks")
  fun getSongs(@Query("search") songName: String?): Call<List<SongDTO>>

  @POST("/tracks")
  fun updateSongRatings(@Body songDTO: SongDTO): Call<Song>
}

fun getSongsList(
    service: SongService,
    songName: String?
): Response<List<SongDTO>> =
  service.getSongs(songName = songName).execute()

fun updateSongRatings(
    service: SongService,
    songDto: SongDTO
): Response<Song> =
  service.updateSongRatings(songDTO = songDto).execute()