package com.example.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.login.models.Song
import com.google.android.material.card.MaterialCardView

class SongAdapter(songs: List<Song>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var songList = songs
                set(value) {
                    field = value
                    notifyDataSetChanged()
                }

    class SongViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.song_item_view, parent, false) as MaterialCardView

        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = this.songList[position]
    }

    override fun getItemCount(): Int = this.songList.size
}