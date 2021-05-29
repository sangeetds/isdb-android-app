package com.isdb.tracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isdb.tracks.data.SongRepository

class SongViewModelFactory(val id: String) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
      return SongViewModel(songRepository = SongRepository(), id = id) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
