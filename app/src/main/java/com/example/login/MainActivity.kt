package com.example.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

/**
 * The opening activity which gives user the option to either register or log-in.
 */
class MainActivity : AppCompatActivity() {

    /**
     * On creation, it binds the register and login buttons.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<ExtendedFloatingActionButton>(R.id.signInButton)
        val registerButton = findViewById<ExtendedFloatingActionButton>(R.id.createAccButton)

        loginButton.setOnClickListener {
            val loginActivity = Intent(this, LoginActivity::class.java)
            startActivity(loginActivity)
        }

        registerButton.setOnClickListener {
            val registerActivity = Intent(this, RegisterActivity::class.java)
            startActivity(registerActivity)
        }
    }
}