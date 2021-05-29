package com.isdb.tracks.data

import com.isdb.login.data.Result
import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.retrofit.Retrofit
import com.isdb.retrofit.Retrofit.Companion.getRetrofitClient
import com.isdb.retrofit.SongService
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import com.isdb.tracks.data.models.Song
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException

class SongRepository {
  private val songService = getRetrofitClient(SongService::class.java) as SongService

  suspend fun getSongs(songName: String?, userId: String): Result<List<SongDTO>> = try {
    songService.getSongs(songName, userId).run {
      when {
        isSuccessful && body() != null -> {
          Success(body()!!)
        }
        else -> {
          Error(Exception(errorBody().toString()))
        }
      }
    }
  } catch (exception: SocketTimeoutException) {
    Error(Exception("Server Down. Please try again."))
  }

  suspend fun updateRatings(userSongDTO: UserSongDTO) = try {
    songService.updateSongRatings(userSongDTO)
  } catch (exception: SocketTimeoutException) {
    println("Server Down. Please try again.")
  }
}
