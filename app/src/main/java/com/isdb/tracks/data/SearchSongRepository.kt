package com.isdb.tracks.data

import com.isdb.login.data.Result
import com.isdb.login.data.Result.Success
import com.isdb.retrofit.Retrofit.Companion.getRetrofitClient
import com.isdb.retrofit.SongService
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import kotlinx.coroutines.flow.flow
import java.lang.Error
import java.net.SocketTimeoutException

class SearchSongRepository {
  private val songService = getRetrofitClient(SongService::class.java) as SongService

  fun getSongs(songName: String, userId: String) = flow {
    try {
      songService.getSongs(songName, userId).run {
        when {
          isSuccessful && body() != null -> {
            emit(Success(body()!!))
          }
          else -> {
            emit(Result.Error(Exception(errorBody().toString())))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Result.Error(Exception("Server Down. Please try again."))
    }
  }

  suspend fun updateRatings(userSongDTO: UserSongDTO) = try {
    songService.updateSongRatings(userSongDTO)
  } catch (exception: SocketTimeoutException) {
    println("Server Down. Please try again.")
  }
}