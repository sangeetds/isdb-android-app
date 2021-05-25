package com.isdb.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.isdb.R
import com.isdb.models.SongDTO
import com.isdb.models.User
import com.isdb.models.UserSongDTO
import com.isdb.service.Retrofit
import com.isdb.service.SongService
import com.isdb.service.updateSongRatings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class RatingsDialog(
  context: Context,
  private val song: SongDTO,
  val associatedFunction: (() -> Unit)?,
  val user: User
) : Dialog(context) {

  private val url = context.getString(R.string.base_url)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.ratings_dialog)

    val cancelButton = findViewById<ImageView>(R.id.cancel_ratings_button)
    val songRating = findViewById<MaterialRatingBar>(R.id.star_ratings)
    val retrofitService =
      Retrofit.getRetrofitClient(SongService::class.java) as SongService

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
    val updateRating = (((song.userRatings * song.votes) + rating) / (song.votes + 1))
    song.userRatings = updateRating
    song.votes += 1

    CoroutineScope(Dispatchers.Main).launch {
      val updatedSongDTO = UserSongDTO(
        songId = song.id,
        userRatings = updateRating,
        criticsRatings = song.criticsRatings,
        votes = song.votes + 1,
        spotifyId = song.spotifyId,
        userId = user.id
      )

      withContext(Dispatchers.IO) {
        updateSongRatings(service = retrofitService, songDto = updatedSongDTO)
      }
    }

    associatedFunction?.invoke()

    Toast.makeText(context, "Voted $rating", Toast.LENGTH_SHORT).show()
    dismiss()
  }
}