package com.example.login.models

import com.squareup.moshi.Json

data class Song(
  @Json(name = "id") val id: String?,
  @Json(name = "name") val name: String = "",
  @Json(name = "votes") val votes: Int = 0
)

