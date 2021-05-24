package com.isdb.models

data class UserSongDTO(
  val songId: String? = "",
  var userRatings: Double = 0.0,
  val criticsRatings: Double = 0.0,
  var votes: Int = 0,
  val spotifyId: String = "",
  val userId: String = ""
)