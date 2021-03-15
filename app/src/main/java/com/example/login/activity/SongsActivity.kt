package com.example.login.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.SongAdapter
import com.example.login.models.Song
import com.example.login.service.Retrofit
import com.example.login.service.SongService
import com.example.login.service.getSongsList
import kotlinx.coroutines.*
import java.util.logging.Logger


class SongsActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        getSongList()
        this.songAdapter = SongAdapter(context = this)
        val recyclerView = findViewById<View>(R.id.recyclerview) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = songAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Title"

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getSongList() =
        CoroutineScope(Dispatchers.Main).launch {
            var list: List<Song>?
            val retrofitService = Retrofit.getRetrofitClient(
                getString(R.string.baseUrl),
                SongService::class.java
            ) as SongService

            withContext(Dispatchers.IO) {
                list = getSongsList(retrofitService, null).body()
            }

            songAdapter.songList.addAll(list!!)
            songAdapter.notifyDataSetChanged()
        }
}