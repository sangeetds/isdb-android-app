package com.isdb.tracks.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.isdb.R
import com.isdb.login.data.model.User
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import com.isdb.tracks.ui.SongAdapter.SongViewHolder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Job
import timber.log.Timber

class SongAdapter(
  val context: Context,
  private val update: (UserSongDTO) -> Job,
  val user: User?,
) :
  ListAdapter<SongDTO, SongViewHolder>(SongCallBack()) {

  val songList = mutableListOf<SongDTO>()

  class SongViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(R.id.song_image)
    val songName: TextView = cardView.findViewById(R.id.song_name)
    val fanScore: TextView = cardView.findViewById(R.id.fan_score_text)
    val rateButton: Button = cardView.findViewById(R.id.rate)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): SongViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.song_item_view, parent, false)

    return SongViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: SongViewHolder,
    position: Int,
  ) {
    val song = this.songList[position]

    holder.songName.text = song.name
    holder.fanScore.text = String.format("%.2f", song.userRatings)

    if (!song.isUserRated) {
      holder.rateButton.visibility = View.GONE
    }

    val removeRatingsButton = {
      holder.rateButton.visibility = View.GONE
    }

    holder.rateButton.setOnClickListener {
      Timber.i("Rating song ${song.name} and opening up the rating dialog")
      val rateDialog =
        RatingsDialog(
          song = song, associatedFunction = update, user = user!!, removeRatingsButton
        )
      rateDialog.show((context as AppCompatActivity).supportFragmentManager.beginTransaction(),
        "RatingDialog")
    }

    val highestResolutionImage = song.image.maxByOrNull { (_, height, width) -> height / width }!!
    Picasso.get().load(highestResolutionImage.url).into(holder.image)
  }

  override fun getItemCount(): Int = this.songList.size
}

class SongCallBack : DiffUtil.ItemCallback<SongDTO>() {
  override fun areItemsTheSame(
    oldItem: SongDTO,
    newItem: SongDTO,
  ) =
    oldItem.id == newItem.id

  override fun areContentsTheSame(
    oldItem: SongDTO,
    newItem: SongDTO,
  ) =
    oldItem == newItem
}

