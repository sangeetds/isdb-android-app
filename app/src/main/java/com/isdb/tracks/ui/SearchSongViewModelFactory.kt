package com.isdb.tracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isdb.tracks.data.SearchSongRepository
import timber.log.Timber

class SearchSongViewModelFactory : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SearchSongViewModel::class.java)) {
      Timber.i("SongSearchViewModel instantiated successfully")
      return SearchSongViewModel(searchSongRepository = SearchSongRepository()) as T
    }
    Timber.e("Error creating view model, wrong view model requested.")
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}