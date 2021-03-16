package com.example.login.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.models.Song
import com.example.login.service.Retrofit
import com.example.login.service.SongService
import com.example.login.service.getSongsList
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchAdapter(val context: Context) :
    ListAdapter<Song, SearchAdapter.SongSearchViewHolder>(SongCallBack()) {

    var songList = mutableListOf<Song>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class SongSearchViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
        val image: ImageView = cardView.findViewById(R.id.small_song_image)
        val songName: TextView = cardView.findViewById(R.id.search_song_name)
        val albumName: TextView = cardView.findViewById(R.id.search_album_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongSearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.song_item_view, parent, false)

        view.setOnClickListener {

        }

        return SongSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongSearchViewHolder, position: Int) {
        val song = songList[position]

        holder.songName.text = song.name
        holder.albumName.text = song.album

        Picasso.get().load(song.image).into(holder.image)
    }

    override fun getItemCount(): Int = this.songList.size

    val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val filteredList: MutableList<Song> = mutableListOf()
            if (constraint != null && constraint.length > 3) {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }

                filteredList.addAll(getSongList(filterPattern))
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            songList.clear()
            songList.addAll(results.values as List<Song>)
            notifyDataSetChanged()
        }
    }

    private fun getSongList(searchString: String): List<Song> {
        val list: MutableList<Song> = mutableListOf()

        CoroutineScope(Dispatchers.Main).launch {
            val retrofitService = Retrofit.getRetrofitClient(
                context.getString(R.string.baseUrl),
                SongService::class.java
            ) as SongService

            withContext(Dispatchers.IO) {
                list.addAll(getSongsList(retrofitService, searchString).body()!!)
            }
        }

        return list
    }
}