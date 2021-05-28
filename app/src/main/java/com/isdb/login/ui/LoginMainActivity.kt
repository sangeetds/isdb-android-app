package com.isdb.login.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.isdb.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.isdb.login.ui.login.LoginActivity
import com.isdb.login.ui.register.RegisterActivity

/**
 * The opening activity which gives user the option to either register or log-in.
 */
class LoginMainActivity : AppCompatActivity() {

  /**
   * On creation, it binds the register and isdb buttons.
   */

  // Todo: See rotation changes
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login_main)

    val loginButton = findViewById<ExtendedFloatingActionButton>(R.id.signInButton)
    val registerButton = findViewById<ExtendedFloatingActionButton>(R.id.createAccButton)
    val backButton = findViewById<ImageButton>(R.id.btn_back)

    loginButton.setOnClickListener {
      val loginActivity = Intent(this, LoginActivity::class.java)
      startActivity(loginActivity)
    }

    registerButton.setOnClickListener {
      val registerActivity = Intent(this, RegisterActivity::class.java)
      startActivity(registerActivity)
    }

    backButton.setOnClickListener {
      finish()
    }
  }
}