package com.example.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.login.dialog.RatingsDialog
import com.example.login.models.Song
import com.squareup.picasso.Picasso


class SongAdapter(songs: List<Song>, val context: Context) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var songList = songs
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class SongViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
        val image: ImageView = cardView.findViewById(R.id.song_image)
        val songName: TextView = cardView.findViewById(R.id.song_name)
        val albumName: TextView = cardView.findViewById(R.id.album_name_text)
        val releaseDate: TextView = cardView.findViewById(R.id.release_date_text)
        val criticScore: TextView = cardView.findViewById(R.id.critic_score_text)
        val fanScore: TextView = cardView.findViewById(R.id.fan_score_text)
        val rateButton: Button = cardView.findViewById(R.id.rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.song_item_view, parent, false)

        view.setOnClickListener {

        }

        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = this.songList[position]

        holder.songName.text = song.name
        holder.releaseDate.text = song.releaseDate
        holder.albumName.text = song.album
        holder.criticScore.text = song.criticsRatings.toString()
        holder.fanScore.text = song.userRatings.toString()

        holder.rateButton.setOnClickListener {
            val rateDialog = RatingsDialog(context = this.context)
            rateDialog.show()
        }

        Picasso.get().load(song.image).into(holder.image)
    }

    override fun getItemCount(): Int = this.songList.size
}