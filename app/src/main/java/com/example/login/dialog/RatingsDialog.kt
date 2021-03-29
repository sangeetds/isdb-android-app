package com.example.login.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.login.R
import com.example.login.models.SongDTO
import com.example.login.service.Retrofit
import com.example.login.service.SongService
import com.example.login.service.updateSongRatings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class RatingsDialog(
  context: Context,
  private val song: SongDTO,
  val associatedFunction: (() -> Unit)?
) : Dialog(context) {

  private val url = context.getString(R.string.baseUrl)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.ratings_dialog)

    val cancelButton = findViewById<ImageView>(R.id.cancel_ratings_button)
    val songRating = findViewById<MaterialRatingBar>(R.id.star_ratings)
    val retrofitService =
      Retrofit.getRetrofitClient(url, SongService::class.java) as SongService

    cancelButton.setOnClickListener {
      dismiss()
    }

    songRating.setOnRatingChangeListener { _, rating ->
      updateRating(rating, retrofitService)
    }
  }

  private fun updateRating(
    rating: Float,
    retrofitService: SongService
  ) {
    associatedFunction?.let {
      CoroutineScope(Dispatchers.Main).launch {
        val updatedSongDTO = SongDTO(
          id = song.id,
          name = song.name,
          url = song.url,
          album = song.album,
          releaseDate = song.releaseDate,
          image = song.image,
          userRatings = (((song.userRatings * song.votes) + rating) / song.votes + 1),
          criticsRatings = song.criticsRatings,
          votes = song.votes + 1,
          spotifyId = song.spotifyId
        )

        withContext(Dispatchers.IO) {
          updateSongRatings(service = retrofitService, songDto = updatedSongDTO)
        }
      }

      it()
    }

    Toast.makeText(context, "Voted $rating", Toast.LENGTH_SHORT).show()
    dismiss()
  }
}