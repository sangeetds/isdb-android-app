package com.isdb.tracks.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.isdb.R
import com.isdb.login.data.model.User
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import kotlinx.coroutines.Job
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import timber.log.Timber

class RatingsDialog(
  context: Context,
  private val song: SongDTO,
  val associatedFunction: ((UserSongDTO) -> Job)?,
  val user: User,
  val removeRatingsButton: (() -> Unit)?
) : Dialog(context) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.ratings_dialog)
    Timber.i("Ratings dialog started for ${song.name}. Layout set.")

    val cancelButton = findViewById<ImageView>(R.id.cancel_ratings_button)
    val songRating = findViewById<MaterialRatingBar>(R.id.star_ratings)

    cancelButton.setOnClickListener {
      Timber.i("User didn't rate the song")
      dismiss()
    }

    songRating.setOnRatingChangeListener { _, rating ->
      Timber.i("User rated the song $rating")
      updateRating(rating)
    }
  }

  private fun updateRating(rating: Float) {
    val updateRating = (((song.userRatings * song.votes) + rating) / (song.votes + 1))
    song.userRatings = updateRating
    song.votes += 1
    Timber.i("Rating the song $song")

    val updatedSongDTO = UserSongDTO(
      songId = song.id,
      userRatings = updateRating,
      criticsRatings = song.criticsRatings,
      votes = song.votes,
      spotifyId = song.spotifyId,
      userId = user.id
    )

    associatedFunction?.invoke(updatedSongDTO)
    removeRatingsButton?.invoke()

    Toast.makeText(context, "Voted $rating", Toast.LENGTH_SHORT).show()
    dismiss()
  }
}