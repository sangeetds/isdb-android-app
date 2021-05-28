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
import com.isdb.tracks.ui.HomeScreenActivity
import com.isdb.login.ui.LoadDialog
import com.isdb.login.data.model.User

/**
 * Class where user enter his/her isdb credentials to register to the service
 */
class RegisterActivity : AppCompatActivity() {

  /**
   * View that will hold our isdb, password and email details along with buttons for sign-up,
   * returning and showing passwords.
   */
  private lateinit var emailText: EditText
  private lateinit var signUpButton: FloatingActionButton
  private lateinit var nameText: EditText
  private lateinit var passwordText: EditText
  private lateinit var backButton: ImageButton
  private lateinit var registerViewModel: RegisterViewModel
  private lateinit var progressDialog: LoadDialog

  /**
   * On creation of the view, respective bindings are done and the button have been assigned
   * their respective responsibilities.
   */
  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    signUpButton = findViewById(R.id.btn_signup)
    nameText = findViewById(R.id.input_name)
    emailText = findViewById(R.id.input_email)
    passwordText = findViewById(R.id.input_password)
    backButton = findViewById(R.id.btn_back)

    registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
      .get(RegisterViewModel::class.java)

    registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
      val registerState = it ?: return@Observer

      // disable login button unless both username / password is valid
      signUpButton.isEnabled = registerState.isDataValid

      registerState.emailError?.let {
        emailText.error = getString(registerState.emailError)
      }

      registerState.usernameError?.let {
        nameText.error = getString(registerState.usernameError)
      }
      registerState.passwordError?.let {
        passwordText.error = getString(registerState.passwordError)
      }
    })

    registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
      val loginResult = it ?: return@Observer

      if (loginResult.error != null) {
        showLoginFailed(loginResult.error)
      }
      if (loginResult.success != null) {
        updateUiWithUser(loginResult.success)
      }
      setResult(Activity.RESULT_OK)

      finish()
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
      showLoadDialog()
      registerViewModel.register(
        emailText.text.toString(), nameText.text.toString(), passwordText.text.toString()
      )
    }

    backButton.setOnClickListener { onBackPressed() }
  }

  /**
   * To go back to the screen
   */
  override fun onBackPressed() {
    super.onBackPressed()
    finish()
  }

  /**
   * Helper function to display the dialog and check the status of the sign-up activity
   */
  private fun showLoadDialog() {
    val progressDialog = LoadDialog(context = this)
    progressDialog.show()

    progressDialog.setOnDismissListener {
      signUpButton.isEnabled = true
    }
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
    signUpButton.isEnabled = true
  }
}