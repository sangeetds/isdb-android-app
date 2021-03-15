package com.example.login.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageButton
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.login.dialog.LoadDialog
import com.example.login.R
import com.example.login.enums.Log
import com.example.login.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Class where user enter his/her login credentials to log in to the service
 */
class LoginActivity : AppCompatActivity() {

    /**
     * View that will hold our login, password details along with buttons for logging, returning
     * and showing passwords.
     */
    private lateinit var userNameText: EditText
    private lateinit var passwordText: EditText
    private lateinit var loginButton: FloatingActionButton
    private lateinit var backButton: ImageButton
    private lateinit var showPasswordButton: CheckBox
    private lateinit var loadSongList: () -> Unit

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
        showPasswordButton = findViewById(R.id.check_password_login)

        loginButton.setOnClickListener {
            login()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }
        /**
         * Simple workaround to show/hide password
         */
        showPasswordButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else passwordText.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    /**
     * Helper function to display the dialog and check the status of the login activity
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

        loadSongList = {
            val songsActivity = Intent(this, SongsActivity::class.java)
            Thread.sleep(1000)
            startActivity(songsActivity)
            finish()
        }


        val progressDialog = LoadDialog(this, user, getString(R.string.baseUrl), Log.LOGIN, loadSongList)
        progressDialog.show()
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

data class LoginMessages(val loginMessage: String, val errorMessage: String, val userAlreadyExistsMessage: String)