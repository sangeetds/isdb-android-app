package com.example.login

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.*

/**
 * Opens a dialog which tells user about the status of their login/register attempt
 * @param context the context from where this is launched
 * @param user the [User] which has login credentials
 * @param type used to make code re-usable so that one class can suffice for different activity
 */
class LoadDialog(context: Context, private val user: User, private val url: String, private val type: Log) : Dialog(context) {

    /**
     * On creation of the dialog, it binds the text and button, which is used to
     * exit from the dialog.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val statusText = findViewById<TextView>(R.id.statusText)
        val cancelButton = findViewById<ImageView>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dismiss()
        }

        val retrofitService = Retrofit.getRetrofitClient(url)

        update(statusText!!, retrofitService)
    }

    /**
     * Function to make API requests asynchronously, which is done with the help of
     * Kotlin coroutines
     *
     * @param statusText the text to keep track of the login/register status
     * @param retrofitService the API request interface
     */
    private fun update(statusText: TextView, retrofitService: LoginService) {
        GlobalScope.launch {
            val text = if (type == Log.LOGIN) logIn(retrofitService, user).body() else createAccount(retrofitService, user).body()

            when(text) {
                Status.SUCCESS -> {
                    statusText.text = if (type == Log.LOGIN)context.getString(R.string.loggedIn)
                        else context.getString(R.string.created)
                }
                Status.FAILURE -> statusText.text = context.getString(R.string.logInError)
                else -> statusText.text = context.getString(R.string.existAlready)
            }
        }
    }
}
