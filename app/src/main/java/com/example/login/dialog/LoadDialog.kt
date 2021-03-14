package com.example.login.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.login.R
import com.example.login.enums.Log
import com.example.login.enums.Status
import com.example.login.models.User
import com.example.login.service.LoginService
import com.example.login.service.Retrofit
import com.example.login.service.createAccount
import com.example.login.service.logIn
import kotlinx.coroutines.*
import retrofit2.await
import java.util.logging.Logger

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
        setContentView(R.layout.loading_dialog)

        val cancelButton = findViewById<ImageView>(R.id.cancel_ratings_button)

        cancelButton.setOnClickListener {
            dismiss()
        }

        val retrofitService =
            Retrofit.getRetrofitClient(url, LoginService::class.java) as LoginService

        GlobalScope.launch {
            update(retrofitService)
        }
    }

    /**
     * Function to make API requests asynchronously, which is done with the help of
     * Kotlin coroutines
     *
     * @param retrofitService the API request interface
     */
    private suspend fun update(retrofitService: LoginService) {
        var status: Status? = null

        val job = GlobalScope.launch {
            status = when (type) {
                Log.LOGIN -> logIn(retrofitService, user = user).body()
                else -> createAccount(retrofitService, user = user).body()
            }
        }

        job.join()

        Logger.getAnonymousLogger().info("asdadasd")
        Logger.getAnonymousLogger().info("$status")

        when(status) {
            Status.SUCCESS -> statusText?.text = "Successfully Logged In"
            Status.USER_ALREADY_EXISTS -> statusText?.text = "Exists"
            else -> statusText?.text = "Error"
        }
    }
}
