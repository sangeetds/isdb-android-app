package com.isdb.tracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isdb.tracks.data.SongSearchRepository

class SongSearchViewModelFactory : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SearchSongViewModel::class.java)) {
      return SearchSongViewModel(songSearchRepository = SongSearchRepository()) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}