package com.example.login.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") val name: String = "",
    @Json(name = "uri") val uri: String = "",
    @Json(name = "album") val album: String = "",
    @Json(name = "releaseDate") val releaseDate: String = "",
    @Json(name = "image") val image: String? = "",
    @Json(name = "userRatings") val userRatings: Double = 0.0,
    @Json(name = "criticsRatings") val criticsRatings: Double = 0.0,
    @Json(name = "votes") val votes: Int = 0,
    @Json(name = "spotifyId") val spotifyId: String = ""
) : Parcelable