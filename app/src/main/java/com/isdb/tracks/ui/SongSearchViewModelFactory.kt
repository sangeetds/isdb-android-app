package com.isdb.tracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isdb.tracks.data.SearchSongRepository

class SongSearchViewModelFactory : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SearchSongViewModel::class.java)) {
      return SearchSongViewModel(searchSongRepository = SearchSongRepository()) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}