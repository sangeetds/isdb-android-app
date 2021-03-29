package com.example.login.service

import com.example.login.models.Song
import com.example.login.models.SongDTO
import com.example.login.models.UserSongDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SongService {

  @GET("/tracks")
  fun getSongs(@Query("search") songName: String?): Call<List<SongDTO>>

  @GET("/users/songs/{id}")
  fun getLikedSongs(@Path("id") id: String): Call<List<String>>

  @POST("/tracks")
  fun updateSongRatings(@Body songDTO: UserSongDTO): Call<Song>
}

fun getSongsList(
  service: SongService,
  songName: String?
): Response<List<SongDTO>> =
  service.getSongs(songName = songName).execute()

fun updateSongRatings(
  service: SongService,
  songDto: UserSongDTO
): Response<Song> =
  service.updateSongRatings(songDTO = songDto).execute()

fun getLikedSong(
  service: SongService,
  id: String
): Response<List<String>> =
  service.getLikedSongs(id = id).execute()