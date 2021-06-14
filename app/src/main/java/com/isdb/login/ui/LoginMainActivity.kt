package com.isdb.login.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.isdb.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import timber.log.Timber

/**
 * The opening activity which gives user the option to either register or log-in.
 */
class LoginMainActivity : AppCompatActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login_main)
    Timber.i("Starting main activity. Layout set.")

    val loginButton = findViewById<ExtendedFloatingActionButton>(R.id.signInButton)
    val registerButton = findViewById<ExtendedFloatingActionButton>(R.id.createAccButton)
    val backButton = findViewById<ImageButton>(R.id.btn_back)

    loginButton.setOnClickListener {
      Timber.i("Starting Login Activity")
      val loginActivity = Intent(this, LoginActivity::class.java)
      startActivity(loginActivity)
    }

    registerButton.setOnClickListener {
      Timber.i("Starting Register Activity")
      val registerActivity = Intent(this, RegisterActivity::class.java)
      startActivity(registerActivity)
    }

    backButton.setOnClickListener {
      Timber.d("User chose to close the application")
      finish()
    }
  }
}