package com.isdb.tracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isdb.tracks.data.SongRepository
import timber.log.Timber

class SongViewModelFactory(val id: String) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
      Timber.i("SongViewModel instantiated successfully")
      return SongViewModel(songRepository = SongRepository(), id = id) as T
    }
    Timber.e("Error creating view model, wrong view model requested.")
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
