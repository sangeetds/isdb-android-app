package com.isdb.tracks.ui

import com.isdb.tracks.data.dto.SongDTO

data class SongResult(
  val success: List<SongDTO>? = null,
  val error: Int? = null
)
