package com.example.isdb.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.isdb.R
import com.example.isdb.dialog.LoadDialog
import com.example.isdb.enums.Log
import com.example.isdb.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

/**
 * Class where user enter his/her isdb credentials to log in to the service
 */
class LoginActivity : AppCompatActivity() {

  /**
   * View that will hold our isdb, password details along with buttons for logging, returning
   * and showing passwords.
   */
  private lateinit var userNameText: EditText
  private lateinit var passwordText: TextInputEditText
  private lateinit var loginButton: FloatingActionButton
  private lateinit var backButton: ImageButton
  private lateinit var loadSongList: (User) -> Unit

  /**
   * On creation of the view, respective bindings are done and the button have been assigned
   * their respective responsibilities.
   */
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    userNameText = findViewById(R.id.input_username)
    passwordText = findViewById(R.id.input_password)
    loginButton = findViewById(R.id.btn_login)
    backButton = findViewById(R.id.btn_back)

    loginButton.setOnClickListener {
      login()
    }

    backButton.setOnClickListener {
      onBackPressed()
    }
  }

  /**
   * Helper function to display the dialog and check the status of the isdb activity
   */
  private fun login() {
    /**
     * A simple validation of the credentials
     */
    if (!validate()) {
      onLoginFailed()
      return
    }

    loginButton.isEnabled = false

    val username = userNameText.text.toString()
    val password = passwordText.text.toString()
    val user = User(email = username, password = password)

    loadSongList = { registeredUser ->
      val songsActivity = Intent(this, HomeScreenActivity::class.java)
      songsActivity.putExtra("user", registeredUser)
      startActivity(songsActivity)
      finish()
    }

    val progressDialog =
      LoadDialog(this, user, getString(R.string.base_url), Log.LOGIN, loadSongList)
    progressDialog.show()

    progressDialog.setOnDismissListener {
      loginButton.isEnabled = true
    }
  }

  /**
   * To go back to the screen
   */
  override fun onBackPressed() {
    super.onBackPressed()
    finish()
  }

  private fun onLoginFailed() {
    Toast.makeText(baseContext, "Check credentials", Toast.LENGTH_LONG).show()
    loginButton.isEnabled = true
  }

  /**
   * Validates the credentials according to some pre-defined rules.
   */
  private fun validate(): Boolean {
    var valid = true
    val email = userNameText.text.toString()
    val password = passwordText.text.toString()

    if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      userNameText.error = "enter a valid email address"
      valid = false
    } else {
      userNameText.error = null
    }
    if (password.isEmpty() || password.length < 4 || password.length > 10) {
      passwordText.error = "between 4 and 10 alphanumeric characters"
      valid = false
    } else {
      passwordText.error = null
    }

    return valid
  }
}