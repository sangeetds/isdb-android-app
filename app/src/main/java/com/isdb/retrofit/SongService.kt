package com.isdb.retrofit

import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import com.isdb.tracks.data.models.Song
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SongService {

  @GET("/tracks")
  suspend fun getSongs(
    @Query("search") songName: String?,
    @Query("userId") userId: String
  ): Response<List<SongDTO>>

  @POST("/tracks")
  suspend fun updateSongRatings(@Body songDTO: UserSongDTO): Response<Song>
}