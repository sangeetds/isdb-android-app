package com.example.login.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.login.R
import com.example.login.R.string
import com.example.login.enums.Log
import com.example.login.models.User
import com.example.login.service.LoginService
import com.example.login.service.Retrofit
import com.example.login.service.createAccount
import com.example.login.service.logIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Opens a dialog which tells user about the status of their login/register attempt
 * @param context the context from where this is launched
 * @param user the [User] which has login credentials
 * @param type used to make code re-usable so that one class can suffice for different activity
 */
class LoadDialog(
  context: Context,
  private var user: User,
  private val url: String,
  private val type: Log,
  private val finishActivity: (User) -> Unit
) : Dialog(context) {

  var statusText: TextView? = null

  /**
   * On creation of the dialog, it binds the text and button, which is used to
   * exit from the dialog.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.loading_dialog)

    statusText = findViewById(R.id.dialog)
    val cancelButton = findViewById<ImageView>(R.id.cancel_ratings_button)

    cancelButton.setOnClickListener {
      dismiss()
    }

    val retrofitService =
      Retrofit.getRetrofitClient(url, LoginService::class.java) as LoginService
    update(retrofitService)
  }

  /**
   * Function to make API requests asynchronously, which is done with the help of
   * Kotlin coroutines
   *
   * @param retrofitService the API request interface
   */
  private fun update(retrofitService: LoginService) =
    CoroutineScope(Dispatchers.Main).launch {
      try {
        var response: Response<User>
        withContext(Dispatchers.IO) {
          response = when (type) {
            Log.LOGIN -> logIn(retrofitService, user = user)
            else -> createAccount(retrofitService, user = user)
          }
        }

        when (response.code()) {
          200 -> {
            statusText?.text =
              if (type == Log.LOGIN) context.getString(R.string.loggedIn)
              else context.getString(R.string.registerSuccess)
            finishActivity(response.body()!!)
            dismiss()
          }
          400 -> {
            statusText?.text = context.getString(R.string.existAlready)
            dismiss()
          }
          else -> statusText?.text = context.getString(R.string.logInError)
        }
      } catch (exception: SocketTimeoutException) {
        statusText?.text = context.getString(string.server_down)
      }
    }
}
