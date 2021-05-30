package com.isdb.tracks.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.isdb.R
import com.isdb.login.data.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {

  private var toolbar: Toolbar? = null
  private lateinit var user: User

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home_screen)

    user = intent.extras?.getParcelable("user")!!

    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    toolbar = findViewById(R.id.toolbar)
    setSupportActionBar(toolbar)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    openFragment(SongFragment.newInstance(user))
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

  private fun openFragment(fragment: Fragment) {
    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_container, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
  }

  private var navigationItemSelectedListener =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.nav_home -> {
          openFragment(SongFragment.newInstance(user))
          return@OnNavigationItemSelectedListener true
        }
        R.id.nav_search -> {
          openFragment(SearchFragment.newInstance(user))
          return@OnNavigationItemSelectedListener true
        }
        R.id.nav_user -> {
          openFragment(UserFragment.newInstance(user))
          return@OnNavigationItemSelectedListener true
        }
      }
      false
    }
}