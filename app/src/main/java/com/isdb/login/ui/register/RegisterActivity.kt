package com.isdb.login.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.isdb.R
import com.isdb.login.data.model.User
import com.isdb.login.ui.LoadDialog
import com.isdb.tracks.ui.HomeScreenActivity
import timber.log.Timber

/**
 * Class where user enter their login credentials to register to the service
 */
class RegisterActivity : AppCompatActivity() {

  /**
   * View that will hold our username, password and email details along with buttons for sign-up,
   * returning and showing passwords.
   */
  private lateinit var emailText: EditText
  private lateinit var signUpButton: FloatingActionButton
  private lateinit var nameText: EditText
  private lateinit var passwordText: EditText
  private lateinit var backButton: ImageButton
  private lateinit var registerViewModel: RegisterViewModel
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

    registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
      .get(RegisterViewModel::class.java)

    registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
      val registerState = it ?: return@Observer
      Timber.i("Change in register state")

      // disable login button unless both username / password is valid
      signUpButton.isEnabled = registerState.isDataValid

      registerState.emailError?.let {
        Timber.d("Invalid email entered")
        emailText.error = getString(registerState.emailError)
      }

      registerState.usernameError?.let {
        Timber.d("Invalid username entered")
        nameText.error = getString(registerState.usernameError)
      }
      registerState.passwordError?.let {
        Timber.d("Invalid password entered")
        passwordText.error = getString(registerState.passwordError)
      }
    })

    registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
      val registerResult = it ?: return@Observer
      Timber.i("Change in register result")

      if (registerResult.error != null) {
        Timber.e("Error while registering")
        showLoginFailed(registerResult.error)
      }
      if (registerResult.success != null) {
        Timber.i("Registration successful")
        updateUiWithUser(registerResult.success)
      }
      setResult(Activity.RESULT_OK)
    })

    nameText.doOnTextChanged { text, _, _, _ ->
      registerViewModel.registerDataChanged(
        emailText.text.toString(),
        text.toString(),
        passwordText.text.toString()
      )
    }

    passwordText.apply {
      doOnTextChanged { text, _, _, _ ->
        registerViewModel.registerDataChanged(
          emailText.text.toString(),
          nameText.text.toString(),
          text.toString()
        )
      }

      setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
          EditorInfo.IME_ACTION_DONE -> {
            Timber.i("User requested sign up")
            showLoadDialog()
            registerViewModel.register(
              emailText.text.toString(),
              nameText.text.toString(),
              passwordText.text.toString()
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

  /**
   * Helper function to display the dialog and check the status of the sign-up activity
   */
  private fun showLoadDialog() {
    val progressDialog = LoadDialog(context = this)
    progressDialog.show()
    Timber.i("Load dialog requested.")

    progressDialog.setOnDismissListener {
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

    Toast.makeText(
      applicationContext,
      "$welcome $displayName",
      Toast.LENGTH_LONG
    ).show()

    Timber.i("Starting HomeScreenActivity")
    val songsActivity = Intent(this, HomeScreenActivity::class.java)
    songsActivity.putExtra("user", model)
    startActivity(songsActivity)

    finish()
  }

  private fun showLoginFailed(@StringRes errorString: Int) {
    Timber.d("Registration failed.")
    Toast.makeText(applicationContext, errorString, Toast.LENGTH_LONG).show()
    progressDialog.updateProgressText(getString(errorString))
    signUpButton.isEnabled = true
  }
}