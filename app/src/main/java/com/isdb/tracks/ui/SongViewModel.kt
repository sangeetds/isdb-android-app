package com.isdb.tracks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.isdb.login.data.Result
import com.isdb.tracks.data.SongRepository
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(private val songRepository: SongRepository) : ViewModel() {
  private val _songs = liveData {
    Timber.i("Fetching all rated songs")
    emit(songRepository.getSongs(null, "1"))
  }
  val songs: LiveData<Result<List<SongDTO>>> = _songs

  fun updateRatings(userSongDTO: UserSongDTO) = viewModelScope.launch {
    Timber.i("Updating ratings for $userSongDTO")
    songRepository.updateRatings(userSongDTO)
  }
}