package com.example.login.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongDTO(
    @Json(name = "id") val id: String = "",
    @Json(name = "name") val name: String = "",
    @Json(name = "url") val url: String = "",
    @Json(name = "albumName") val album: String = "",
    @Json(name = "releaseDate") val releaseDate: String = "",
    @Json(name = "image") val image: List<Image> = listOf(),
    @Json(name = "userRatings") val userRatings: Double = 0.0,
    @Json(name = "criticsRatings") val criticsRatings: Double = 0.0,
    @Json(name = "votes") val votes: Int = 0,
    @Json(name = "spotifyId") val spotifyId: String = ""
) : Parcelable