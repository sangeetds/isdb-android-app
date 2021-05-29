package com.isdb.tracks.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isdb.R
import com.isdb.tracks.ui.SearchAdapter.SongSearchViewHolder
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.login.data.model.User
import com.isdb.tracks.data.dto.UserSongDTO
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Job

class SearchAdapter(
  val context: Context,
  val user: User?,
  val update: (UserSongDTO) -> Job
) :
  RecyclerView.Adapter<SongSearchViewHolder>() {

  val songList = mutableListOf<SongDTO>()

  class SongSearchViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(R.id.small_song_image)
    val songName: TextView = cardView.findViewById(R.id.search_song_name)
    val albumName: TextView = cardView.findViewById(R.id.search_album_name)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): SongSearchViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.search_song_list_view, parent, false)

    return SongSearchViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: SongSearchViewHolder,
    position: Int
  ) {
    val song = songList[position]

    holder.itemView.setOnClickListener {
      val rateDialog =
        RatingsDialog(
          context = this.context, song = song, associatedFunction = update, user = user!!,
          removeRatingsButton = null
        )
      rateDialog.show()
    }

    holder.songName.text = song.name
    holder.albumName.text = song.album

    val smallestResolutionImage = song.image.minByOrNull { (_, height, width) -> height / width }!!

    Picasso.get().load(smallestResolutionImage.url).into(holder.image)
  }

  override fun getItemCount(): Int = this.songList.size
}