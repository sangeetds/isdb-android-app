package com.isdb.tracks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.isdb.login.data.Result
import com.isdb.tracks.data.SongRepository
import com.isdb.tracks.data.models.SongDTO

class SongViewModel(songRepository: SongRepository) : ViewModel() {
  private val _songs = liveData {
    emit(songRepository.getSongs(null))
  }
  val songs: LiveData<Result<List<SongDTO>>> = _songs
}