package com.example.login

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.login.models.Song
import kotlinx.coroutines.*
import java.net.URL


class SongAdapter(songs: List<Song>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var songList = songs
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        println("Songs at adapter")
        println(songList)
    }

    class SongViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
        val image: ImageView = cardView.findViewById(R.id.song_image)
        val songName: TextView = cardView.findViewById(R.id.song_name)
        val releaseDate: TextView = cardView.findViewById(R.id.release_date)
        val criticScore: TextView = cardView.findViewById(R.id.critic_score)
        val fanScore: TextView = cardView.findViewById(R.id.fan_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.song_item_view, parent, false)

        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = this.songList[position]
        val res = holder.itemView.context.resources

        val icon = getIcon(song.image)

        holder.songName.text = song.name
        holder.releaseDate.text = song.releaseDate
        holder.criticScore.text = song.criticsRatings.toString()
        holder.fanScore.text = song.userRatings.toString()
        holder.image.setImageBitmap(icon)
    }

    private fun getIcon(url: String?): Bitmap? {
        var icon: Bitmap? = null

        CoroutineScope(Dispatchers.IO).launch {
            val newUrl = URL(url)
            icon = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream())
        }

        return icon
    }

    override fun getItemCount(): Int = this.songList.size
}