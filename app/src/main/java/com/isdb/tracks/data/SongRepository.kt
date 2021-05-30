package com.isdb.tracks.data

import com.isdb.login.data.Result
import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.tracks.data.api.SongService
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class SongRepository @Inject constructor(private val songService: SongService) {

  suspend fun getSongs(
    songName: String?,
    userId: String
  ): Result<List<SongDTO>> = try {
    songService.getSongs(songName, userId).run {
      when {
        isSuccessful && body() != null -> {
          Timber.i("Fetching songs successful with response: ${raw()} ")
          Success(body()!!)
        }
        else -> {
          Timber.e("Error while fetching songs with error: ${errorBody()}")
          Error(Exception(errorBody().toString()))
        }
      }
    }
  } catch (exception: SocketTimeoutException) {
    Timber.e("Error while fetching songs with error: $exception")
    Error(Exception("Server Down. Please try again."))
  }

  suspend fun updateRatings(userSongDTO: UserSongDTO) = try {
    Timber.i("Updating ratings successful for $userSongDTO ")
    songService.updateSongRatings(userSongDTO)
  } catch (exception: SocketTimeoutException) {
    Timber.e("Error while Updating ratings for $userSongDTO")
    println("Server Down. Please try again.")
  }
}
