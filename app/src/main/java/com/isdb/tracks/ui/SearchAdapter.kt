package com.isdb.tracks.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.isdb.tracks.ui.SearchAdapter.SongSearchViewHolder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Job
import timber.log.Timber

class SearchAdapter(
  val context: Context,
  val user: User?,
  private val update: (UserSongDTO) -> Job,
) :
  ListAdapter<SongDTO, SongSearchViewHolder>(SongItemDiffCallback()) {

  class SongSearchViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(R.id.small_song_image)
    val songName: TextView = cardView.findViewById(R.id.search_song_name)
    val albumName: TextView = cardView.findViewById(R.id.search_album_name)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): SongSearchViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.search_song_list_view, parent, false)

    return SongSearchViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: SongSearchViewHolder,
    position: Int,
  ) {
    val song = getItem(position)

    holder.itemView.setOnClickListener {
      Timber.i("Rating song ${song.name} and opening up the rating dialog")
      val rateDialog =
        RatingsDialog(
          song = song, associatedFunction = update, user = user!!, removeRatingsButton = null
        )
      rateDialog.show((context as AppCompatActivity).supportFragmentManager.beginTransaction(),
        "RatingDialog")
    }

    holder.songName.text = song.name
    holder.albumName.text = song.album

    val smallestResolutionImage = song.image.minByOrNull { (_, height, width) -> height / width }
    Picasso.get().load(smallestResolutionImage?.url).into(holder.image)
  }
}

class SongItemDiffCallback : DiffUtil.ItemCallback<SongDTO>() {

  override fun areItemsTheSame(
    oldItem: SongDTO,
    newItem: SongDTO,
  ): Boolean = oldItem == newItem

  override fun areContentsTheSame(
    oldItem: SongDTO,
    newItem: SongDTO,
  ): Boolean = oldItem == newItem
}