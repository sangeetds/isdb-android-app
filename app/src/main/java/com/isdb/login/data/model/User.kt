package com.isdb.login.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * User class which stores the credentials of the user.
 *
 * Moshi helps converting the an object of this class into a JSON.
 */
@Parcelize
data class User(
  @Json(name = "id") val id: String = "",
  @Json(name = "username") val username: String = "",
  @Json(name = "password") var password: String = "",
  @Json(name = "email") val email: String = "",
  @Json(name = "ratedSongIds") val ratedSongsIds: MutableList<String> = mutableListOf()
) : Parcelable