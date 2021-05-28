package com.isdb.login.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.isdb.R
import com.isdb.tracks.ui.HomeScreenActivity
import com.isdb.login.ui.LoadDialog
import com.isdb.login.data.model.User

/**
 * Class where user enters isdb credentials to log in to the service
 */
class LoginActivity : AppCompatActivity() {

  /**
   * View that will hold our isdb, password details along with buttons for logging, returning
   * and showing passwords.
   */
  private lateinit var username: EditText
  private lateinit var password: TextInputEditText
  private lateinit var loginButton: FloatingActionButton
  private lateinit var backButton: ImageButton
  private lateinit var progressDialog: LoadDialog
  private lateinit var loginViewModel: LoginViewModel

  /**
   * On creation of the view, respective bindings are done and the button have been assigned
   * their respective responsibilities.
   */
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    username = findViewById(R.id.input_username)
    password = findViewById(R.id.input_password)
    loginButton = findViewById(R.id.btn_login)
    backButton = findViewById(R.id.btn_back)

    loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
      .get(LoginViewModel::class.java)

    loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
      val loginState = it ?: return@Observer

      // disable login button unless both username / password is valid
      loginButton.isEnabled = loginState.isDataValid

      if (loginState.usernameError != null) {
        username.error = getString(loginState.usernameError)
      }
      if (loginState.passwordError != null) {
        password.error = getString(loginState.passwordError)
      }
    })

    loginViewModel.loginResult.observe(this@LoginActivity, Observer {
      val loginResult = it ?: return@Observer

      if (loginResult.error != null) {
        showLoginFailed(loginResult.error)
      }
      if (loginResult.success != null) {
        updateUiWithUser(loginResult.success)
      }
      setResult(Activity.RESULT_OK)

      //Complete and destroy login activity once successful
      finish()
    })

    username.doOnTextChanged { text, _, _, _ ->
      loginViewModel.loginDataChanged(
        text.toString(),
        password.text.toString()
      )
    }

    password.apply {
      doOnTextChanged { text, _, _, _ ->
        loginViewModel.loginDataChanged(
          username.text.toString(),
          text.toString()
        )
      }

      setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
          EditorInfo.IME_ACTION_DONE -> {
            showLoadDialog()
            loginViewModel.login(
              username.text.toString(),
              password.text.toString()
            )
          }
        }
        false
      }
    }

    loginButton.setOnClickListener {
      showLoadDialog()
      loginViewModel.login(username.text.toString(), password.text.toString())
    }

    backButton.setOnClickListener {
      onBackPressed()
    }
  }

  /**
   * Helper function to display the dialog and check the status of the isdb activity
   */
  private fun showLoadDialog() {
    progressDialog = LoadDialog(this)
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

  private fun updateUiWithUser(model: User) {
    val welcome = getString(R.string.welcome)
    val displayName = model.username

    Toast.makeText(
      applicationContext,
      "$welcome $displayName",
      Toast.LENGTH_LONG
    ).show()

    val songsActivity = Intent(this, HomeScreenActivity::class.java)
    songsActivity.putExtra("user", model)
    startActivity(songsActivity)
  }

  private fun showLoginFailed(@StringRes errorString: Int) {
    Toast.makeText(applicationContext, errorString, Toast.LENGTH_LONG).show()
    progressDialog.updateProgressText(getString(errorString))
    loginButton.isEnabled = true
  }
}