package com.example.login.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.login.R
import com.example.login.SongAdapter
import com.example.login.models.Song


class SongsActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        val songs = listOf(Song(id = 0, name = "Hello World Song", album = "Sangeet's album", releaseDate = "2017", userRatings = 4.8, criticsRatings = 3.6))
        this.songAdapter = SongAdapter(songs = songs)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Title"

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val questionView = findViewById<RecyclerView>(R.id.questionList)
//        questionView.setHasFixedSize(true)
//
//        val questionList = listOf(Question())
//        questionAdapter = QuestionAdapter(this, questionList.toMutableList())
//        questionView.adapter = questionAdapter
//        questionView.layoutManager = LinearLayoutManager(this)
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu, menu)
//
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = menu.findItem(R.id.action_search)
//            .actionView as SearchView
//        searchView.setSearchableInfo(
//            searchManager
//                .getSearchableInfo(componentName)
//        )
//        searchView.maxWidth = Int.MAX_VALUE
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                questionAdapter.filter.filter(query)
//                return false
//            }
//
//            override fun onQueryTextChange(query: String?): Boolean {
//                questionAdapter.filter.filter(query)
//                return false
//            }
//        })
//
//        return true
//    }
}