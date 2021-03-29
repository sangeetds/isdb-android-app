package com.example.login.adapters

import android.content.Context
import android.transition.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.dialog.RatingsDialog
import com.example.login.models.SongDTO
import com.squareup.picasso.Picasso

class SongAdapter(val context: Context) :
  ListAdapter<SongDTO, SongAdapter.SongViewHolder>(SongCallBack()) {

  var songList = mutableListOf<SongDTO>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  class SongViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(R.id.song_image)
    val songName: TextView = cardView.findViewById(R.id.song_name)
    val fanScore: TextView = cardView.findViewById(R.id.fan_score_text)
    val rateButton: Button = cardView.findViewById(R.id.rate)
  }

  override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int
  ): SongViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.song_item_view, parent, false)

    return SongViewHolder(view)
  }

  override fun onBindViewHolder(
      holder: SongViewHolder,
      position: Int
  ) {
    val song = this.songList[position]

    holder.songName.text = song.name
    holder.fanScore.text = song.userRatings.toString()

    val images = song.image
    val highestResolutionImage = images.maxByOrNull { (_, height, width) -> height / width }!!

    val removeRatingsButton = {
      holder.rateButton.visibility = View.GONE
    }

    holder.rateButton.setOnClickListener {
      val rateDialog = RatingsDialog(context = this.context, associatedFunction = removeRatingsButton, song = song)
      rateDialog.show()
    }

    Picasso.get().load(highestResolutionImage.url).into(holder.image)
  }

  override fun getItemCount(): Int = this.songList.size
}

class SongCallBack : DiffUtil.ItemCallback<SongDTO>() {
  override fun areItemsTheSame(
      oldItem: SongDTO,
      newItem: SongDTO
  ) =
    oldItem.id == newItem.id

  override fun areContentsTheSame(
      oldItem: SongDTO,
      newItem: SongDTO
  ) =
    oldItem == newItem
}

