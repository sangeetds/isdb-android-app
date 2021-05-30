package com.isdb.login.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.isdb.R
import com.isdb.login.data.model.User
import com.isdb.login.ui.LoadDialog
import com.isdb.tracks.ui.HomeScreenActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Class where user enters login credentials to log in to the service
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

  /**
   * View that will hold our email and password details along with buttons for logging, returning
   * and showing passwords.
   */
  private val loginViewModel: LoginViewModel by viewModels()
  private lateinit var username: EditText
  private lateinit var password: TextInputEditText
  private lateinit var loginButton: FloatingActionButton
  private lateinit var backButton: ImageButton
  private lateinit var progressDialog: LoadDialog

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    Timber.i("Login activity created. Layout set.")

    username = findViewById(R.id.input_username)
    password = findViewById(R.id.input_password)
    loginButton = findViewById(R.id.btn_login)
    backButton = findViewById(R.id.btn_back)

    setUpObserver()
    setUpButtons()
  }

  private fun setUpObserver() {
    loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
      val loginState = it ?: return@Observer
      Timber.i("Change in login form state.")
      // disable login button unless both username / password is valid
      loginButton.isEnabled = loginState.isDataValid

      loginState.usernameError?.let {
        Timber.d("Username not correct")
        username.error = getString(loginState.usernameError)
      }
      loginState.passwordError?.let {
        Timber.d("Password not correct")
        password.error = getString(loginState.passwordError)
      }
    })

    loginViewModel.loginResult.observe(this@LoginActivity, Observer {
      val loginResult = it ?: return@Observer
      Timber.i("Change in login result state.")

      loginResult.error?.let {
        Timber.e("Login not successful")
        showLoginFailed(loginResult.error)
      }
      loginResult.success?.let {
        Timber.i("Successfully logged in")
        updateUiWithUser(loginResult.success)
      }
      setResult(RESULT_OK)
    })
  }

  private fun setUpButtons() {
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
            Timber.i("User requested log in")
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
      Timber.i("User requested log in")
      showLoadDialog()
      loginViewModel.login(username.text.toString(), password.text.toString())
    }

    backButton.setOnClickListener {
      Timber.i("User chose to close the login screen")
      onBackPressed()
    }
  }

  /**
   * Helper function to display the dialog and check the status of the login activity
   */
  private fun showLoadDialog() {
    progressDialog = LoadDialog(this)
    progressDialog.show()
    Timber.i("Load dialog requested.")

    progressDialog.setOnDismissListener {
      loginButton.isEnabled = true
    }
  }

  /**
   * To go back to the screen
   */
  override fun onBackPressed() {
    Timber.i("User chose to close the screen")
    super.onBackPressed()
    finish()
  }

  private fun updateUiWithUser(model: User) {
    val welcome = getString(R.string.welcome)
    val displayName = model.username
    Timber.i("Login successful for $model")

    Toast.makeText(
      applicationContext,
      "$welcome $displayName",
      Toast.LENGTH_LONG
    ).show()

    Timber.i("Starting HomeScreenActivity")
    val songsActivity = Intent(this, HomeScreenActivity::class.java)
    songsActivity.putExtra("user", model)
    startActivity(songsActivity)

    progressDialog.cancel()
    finish()
  }

  private fun showLoginFailed(@StringRes errorString: Int) {
    Timber.d("Logging failed.")
    Toast.makeText(applicationContext, errorString, Toast.LENGTH_LONG).show()
    progressDialog.updateProgressText(getString(errorString))
    loginButton.isEnabled = true
  }
}