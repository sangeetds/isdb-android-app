package com.isdb.tracks.data

import com.isdb.login.data.Result
import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.retrofit.Retrofit.Companion.getRetrofitClient
import com.isdb.retrofit.SongService
import com.isdb.tracks.data.dto.SongDTO
import java.net.SocketTimeoutException

class SongRepository {
  private val songService = getRetrofitClient(SongService::class.java) as SongService

  suspend fun getSongs(songName: String?): Result<List<SongDTO>> = try {
    songService.getSongs(songName).run {
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
}
