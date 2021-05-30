package com.isdb.tracks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isdb.login.data.Result
import com.isdb.tracks.data.SearchSongRepository
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchSongViewModel @Inject constructor(private val searchSongRepository: SearchSongRepository) :
  ViewModel() {

  private val _searchedSongs = MutableLiveData<List<SongDTO>>()
  val searchedSongs: LiveData<List<SongDTO>> = _searchedSongs

  fun getSongs(
    songName: String,
    userId: String
  ) = viewModelScope.launch {
    Timber.i("Making request to the server for user: $userId")

    searchSongRepository.getSongs(songName, userId).collect { result ->
      if (result is Result.Success) {
        _searchedSongs.value = result.data
      } else {
        _searchedSongs.value = listOf()
      }
    }
  }

  fun updateRatings(userSongDTO: UserSongDTO) = viewModelScope.launch {
    Timber.i("Updating ratings for $userSongDTO")
    searchSongRepository.updateRatings(userSongDTO)
  }
}
