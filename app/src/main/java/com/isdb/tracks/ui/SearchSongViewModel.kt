package com.isdb.tracks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isdb.tracks.data.SongSearchRepository
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import kotlinx.coroutines.launch

class SearchSongViewModel(private val songSearchRepository: SongSearchRepository) : ViewModel() {

  private val _searchedSongs = MutableLiveData<com.isdb.login.data.Result<List<SongDTO>>>()
  val searchedSongs: LiveData<com.isdb.login.data.Result<List<SongDTO>>> = _searchedSongs

  fun getSongs(songName: String, userId: String) = viewModelScope.launch {
    songSearchRepository.getSongs(songName, userId)
  }

  fun updateRatings(userSongDTO: UserSongDTO) = viewModelScope.launch {
    songSearchRepository.updateRatings(userSongDTO)
  }
}
