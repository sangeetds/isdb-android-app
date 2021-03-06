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
import com.isdb.R
import com.isdb.login.data.model.User
import com.isdb.tracks.ui.HomeScreenActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Class where user enter their login credentials to register to the service
 */
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

  /**
   * View that will hold our username, password and email details along with buttons for sign-up,
   * returning and showing passwords.
   */
  private val registerViewModel: RegisterViewModel by viewModels()
  private lateinit var emailText: EditText
  private lateinit var signUpButton: FloatingActionButton
  private lateinit var nameText: EditText
  private lateinit var passwordText: EditText
  private lateinit var backButton: ImageButton
  private lateinit var progressDialog: LoadDialog

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    Timber.i("Register Activity created. Layout set.")

    signUpButton = findViewById(R.id.btn_signup)
    nameText = findViewById(R.id.input_name)
    emailText = findViewById(R.id.input_email)
    passwordText = findViewById(R.id.input_password)
    backButton = findViewById(R.id.btn_back)

    setUpObserver()
    setUpButtons()
  }

  private fun setUpButtons() {
    nameText.doOnTextChanged { text, _, _, _ ->
      registerViewModel.registerDataChanged(emailText.text.toString(), text.toString(),
        passwordText.text.toString())
    }

    passwordText.apply {
      doOnTextChanged { text, _, _, _ ->
        registerViewModel.registerDataChanged(emailText.text.toString(), nameText.text.toString(),
          text.toString()
        )
      }

      setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
          EditorInfo.IME_ACTION_DONE -> {
            Timber.i("User requested sign up")
            showLoadDialog()
            registerViewModel.register(
              emailText.text.toString(), nameText.text.toString(), passwordText.text.toString()
            )
          }
        }
        false
      }
    }

    signUpButton.setOnClickListener {
      Timber.i("User requested sign up")
      showLoadDialog()
      registerViewModel.register(
        emailText.text.toString(), nameText.text.toString(), passwordText.text.toString()
      )
    }

    backButton.setOnClickListener {
      Timber.i("User chose to close the sign up screen")
      onBackPressed()
    }
  }

  private fun setUpObserver() {
    registerViewModel.apply {
      registerFormState.observe(this@RegisterActivity, Observer {
        val registerState = it ?: return@Observer

        // disable login button unless both username / password is valid
        signUpButton.isEnabled = registerState.isDataValid

        registerState.apply {
          emailError?.let {
            emailText.error = getString(emailError)
          }
          usernameError?.let {
            nameText.error = getString(usernameError)
          }
          passwordError?.let {
            passwordText.error = getString(passwordError)
          }
        }
      })

      registerResult.observe(this@RegisterActivity, Observer {
        val registerResult = it ?: return@Observer
        Timber.i("Change in register result")

        registerResult.apply {
          error?.let {
            Timber.e("Error while registering")
            showLoginFailed(error)
          }
          success?.let {
            Timber.i("Registration successful")
            updateUiWithUser(success)
          }
          setResult(RESULT_OK)
        }
      })
    }
  }

  /**
   * Helper function to display the dialog and check the status of the sign-up activity
   */
  private fun showLoadDialog() {
    val progressDialog = LoadDialog()
    progressDialog.show(supportFragmentManager.beginTransaction(), "Register Dialog")
    Timber.i("Load dialog requested.")

    progressDialog.dialog?.setOnDismissListener {
      signUpButton.isEnabled = true
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

  /**
   * Updating the UI on a successful registration
   */
  private fun updateUiWithUser(model: User) {
    val welcome = getString(R.string.welcome)
    val displayName = model.username
    Timber.i("Signup successful for $model")

    Toast.makeText(applicationContext, "$welcome $displayName", Toast.LENGTH_LONG).show()

    Timber.i("Starting HomeScreenActivity")
    val songsActivity = Intent(this, HomeScreenActivity::class.java)
    songsActivity.putExtra("user", model)
    startActivity(songsActivity)

    progressDialog.dismiss()
    finish()
  }

  private fun showLoginFailed(@StringRes errorString: Int) {
    Timber.d("Registration failed.")
    Toast.makeText(applicationContext, errorString, Toast.LENGTH_LONG).show()
    progressDialog.updateProgressText(getString(errorString))
    signUpButton.isEnabled = true
  }
}