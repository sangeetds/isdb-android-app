package com.example.login.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Int = 0,
    val name: String = "",
    val uri: String = "",
    val album: String = "",
    val releaseDate: String = "",
    val image: String? = "",
    val userRatings: Double = 0.0,
    val criticsRatings: Double = 0.0,
    val votes: Int = 0,
    val spotifyId: String = ""
) : Parcelable