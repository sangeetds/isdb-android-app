package com.example.isdb.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.isdb.R
import com.example.isdb.dialog.RatingsDialog
import com.example.isdb.models.SongDTO
import com.example.isdb.models.User
import com.squareup.picasso.Picasso

class SearchAdapter(
  val context: Context,
  val user: User?
) :
  RecyclerView.Adapter<SearchAdapter.SongSearchViewHolder>() {

  var songList = mutableListOf<SongDTO>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

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
        RatingsDialog(context = this.context, associatedFunction = null, song = song, user = user!!)
      rateDialog.show()
    }

    holder.songName.text = song.name
    holder.albumName.text = song.album

    val smallestResolutionImage = song.image.minByOrNull { (_, height, width) -> height / width }!!

    Picasso.get().load(smallestResolutionImage.url).into(holder.image)
  }

  override fun getItemCount(): Int = this.songList.size
}