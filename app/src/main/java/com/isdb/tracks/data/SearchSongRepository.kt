package com.isdb.tracks.data

import com.isdb.login.data.Result
import com.isdb.login.data.Result.Success
import com.isdb.tracks.data.api.SongService
import com.isdb.tracks.data.dto.UserSongDTO
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class SearchSongRepository @Inject constructor(private val songService: SongService) {

  fun getSongs(
    songName: String,
    userId: String
  ) = flow {
    try {
      songService.getSongs(songName, userId).run {
        when {
          isSuccessful && body() != null -> {
            Timber.i("Login successful with response: ${raw()} ")
            emit(Success(body()!!))
          }
          else -> {
            Timber.e("Error while logging in with error: ${errorBody()}")
            emit(Result.Error(Exception(errorBody().toString())))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Error while logging in with error: $exception")
      Result.Error(Exception("Server Down. Please try again."))
    }
  }

  suspend fun updateRatings(userSongDTO: UserSongDTO) = try {
    Timber.i("Updating ratings successful for $userSongDTO ")
    songService.updateSongRatings(userSongDTO)
  } catch (exception: SocketTimeoutException) {
    Timber.e("Error while Updating ratings for $userSongDTO")
    println("Server Down. Please try again.")
  }
}