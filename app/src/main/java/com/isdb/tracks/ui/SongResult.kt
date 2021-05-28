package com.isdb.tracks.ui

import com.isdb.tracks.data.models.SongDTO

data class SongResult(
  val success: List<SongDTO>? = null,
  val error: Int? = null
)
