package com.isdb.retrofit

import com.isdb.tracks.data.models.Song
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.login.data.dto.UserSongDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SongService {

  @GET("/tracks")
  suspend fun getSongs(@Query("search") songName: String?): Response<List<SongDTO>>

  @GET("/users/songs/{id}")
  suspend fun getLikedSongs(@Path("id") id: String): Response<List<String>>

  @POST("/tracks")
  suspend fun updateSongRatings(@Body songDTO: UserSongDTO): Response<Song>
}