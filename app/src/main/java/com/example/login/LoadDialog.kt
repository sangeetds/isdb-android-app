package com.example.login

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.login.enums.Log
import com.example.login.enums.Status
import com.example.login.models.User
import com.example.login.service.LoginService
import com.example.login.service.Retrofit
import com.example.login.service.createAccount
import com.example.login.service.logIn
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Opens a dialog which tells user about the status of their login/register attempt
 * @param context the context from where this is launched
 * @param user the [User] which has login credentials
 * @param type used to make code re-usable so that one class can suffice for different activity
 */
class LoadDialog(
    context: Context,
    private val user: User,
    private val url: String,
    private val type: Log
) : Dialog(context) {


    var statusText: TextView? = null

    /**
     * On creation of the dialog, it binds the text and button, which is used to
     * exit from the dialog.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        statusText = findViewById<TextView>(R.id.statusText)
        val cancelButton = findViewById<ImageView>(R.id.cancelButton)

        cancelButton.setOnClickListener {
            dismiss()
        }

        Thread.sleep(1000)
//        statusText?.text = "Logged in"
        val retrofitService = Retrofit.getRetrofitClient(url, LoginService::class.java) as LoginService

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
//        GlobalScope.launch {
            val text =
//                Status.SUCCESS
                if (type == Log.LOGIN) {
                    Thread.sleep(1000)
//                    logIn(retrofitService, user)
                    Status.SUCCESS
                }
                else {
                    Thread.sleep(1000)
//                    createAccount(retrofitService, user)
                    Status.SUCCESS
                }

//            if (!text.isSuccessful || text.errorBody() != null) {
//                statusText.text = context.getString(R.string.logInError)
//            }
//            else {
            when (text) {
                Status.SUCCESS -> {
                    statusText.text =
                        if (type == Log.LOGIN) {
                            context.getString(R.string.loggedIn)
                        } else context.getString(R.string.created)
                }
                Status.FAILURE -> statusText.text = context.getString(R.string.logInError)
                else -> statusText.text = context.getString(R.string.existAlready)
            }
//            }
        }
//    }
}
