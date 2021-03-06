package com.isdb.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.isdb.R
import com.isdb.login.data.model.User
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
    loginViewModel.apply {
      loginFormState.observe(this@LoginActivity, Observer {
        val loginState = it ?: return@Observer
        // disable login button unless both username / password is valid
        loginButton.isEnabled = loginState.isDataValid

        loginState.apply {
          usernameError?.let {
            username.error = getString(usernameError)
          }
          passwordError?.let {
            password.error = getString(passwordError)
          }
        }
      })

      loginResult.observe(this@LoginActivity, Observer {
        val loginResult = it ?: return@Observer
        Timber.i("Change in login result state.")

        loginResult.apply {
          error?.let {
            Timber.e("Login not successful")
            showLoginFailed(error)
          }
          success?.let {
            Timber.i("Successfully logged in")
            updateUiWithUser(success)
          }
          setResult(RESULT_OK)
        }
      })
    }
  }

  private fun setUpButtons() {
    username.doOnTextChanged { text, _, _, _ ->
      loginViewModel.loginDataChanged(text.toString(), password.text.toString())
    }

    password.apply {
      doOnTextChanged { text, _, _, _ ->
        loginViewModel.loginDataChanged(username.text.toString(), text.toString())
      }

      setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
          EditorInfo.IME_ACTION_DONE -> {
            Timber.i("User requested log in")
            showLoadDialog()
            loginViewModel.login(username.text.toString(), password.text.toString())
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
    progressDialog = LoadDialog()
    progressDialog.show(supportFragmentManager.beginTransaction(), "LoginDialog")
    Timber.i("Load dialog requested.")

    progressDialog.dialog?.setOnDismissListener {
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

    Toast.makeText(applicationContext, "$welcome $displayName", Toast.LENGTH_LONG).show()

    Timber.i("Starting HomeScreenActivity")
    val songsActivity = Intent(this, HomeScreenActivity::class.java)
    songsActivity.putExtra("user", model)
    startActivity(songsActivity)

    progressDialog.dismiss()
    finish()
  }

  private fun showLoginFailed(@StringRes errorString: Int) {
    Timber.d("Logging failed.")
    Toast.makeText(applicationContext, errorString, Toast.LENGTH_LONG).show()
    progressDialog.updateProgressText(getString(errorString))
    loginButton.isEnabled = true
  }
}