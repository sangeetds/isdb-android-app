package com.isdb.tracks.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
  val url: String,
  val height: Int = 0,
  val width: Int = 0,
) : Parcelable
