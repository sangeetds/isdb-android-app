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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SongsActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        val songs =
            getSongList()
//            listOf(
//            Song(
//                id = 1,
//                name = "Horses (with PnB Rock, Kodak Black & A Boogie Wit da Hoodie)",
//                album = "Horses",
//                image = "https://i.scdn.co/image/ab67616d0000b273a2c31c39d559355168b4cd2e",
//                releaseDate = "2017",
//                criticsRatings = 4.0,
//                userRatings = 4.0,
//                votes = 1
//            ), Song(
//                id = 2,
//                name = "A Horse with No Name",
//                album = "Horses",
//                image = "https://i.scdn.co/image/ab67616d0000b273afb855e6461310dff4046c56",
//                releaseDate = "2007",
//                criticsRatings = 4.0,
//                userRatings = 3.0,
//                votes = 1
//            ), Song(
//                id = 1,
//                name = "PnB Rock, Kodak Black",
//                album = "Horses",
//                image = "https://i.scdn.co/image/ab67616d0000b273a2c31c39d559355168b4cd2e",
//                releaseDate = "2017",
//                criticsRatings = 2.0,
//                userRatings = 4.0,
//                votes = 1
//            )
//        )
        println(songs)
        this.songAdapter = SongAdapter(songs = songs, context = this)
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

    private fun getSongList(): List<Song> {
        val retrofitService = Retrofit.getRetrofitClient(
            getString(R.string.baseUrl),
            SongService::class.java
        ) as SongService
        val songList = mutableListOf<Song>()

        GlobalScope.launch {
            val songRequest = async {  getSongsList(retrofitService, null) }
            val songResponse = songRequest.await()

            songList.addAll(songResponse.body()!!)
        }

        return songList
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })

        return true
    }
}