package com.example.login

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SongsActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
//    lateinit var questionAdapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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