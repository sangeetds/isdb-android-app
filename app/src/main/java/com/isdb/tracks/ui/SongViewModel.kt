package com.isdb.tracks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.isdb.login.data.Result
import com.isdb.tracks.data.dto.UserSongDTO
import com.isdb.tracks.data.SongRepository
import com.isdb.tracks.data.dto.SongDTO
import kotlinx.coroutines.launch

class SongViewModel(private val songRepository: SongRepository, id: String) : ViewModel() {
  private val _songs = liveData {
    emit(songRepository.getSongs(null, id))
  }
  val songs: LiveData<Result<List<SongDTO>>> = _songs

  fun updateRatings(userSongDTO: UserSongDTO) = viewModelScope.launch {
    songRepository.updateRatings(userSongDTO)
  }
}