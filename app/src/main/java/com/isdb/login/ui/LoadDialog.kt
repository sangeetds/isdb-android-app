package com.isdb.login.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.isdb.R
import timber.log.Timber

/**
 * Opens a dialog which tells user about the status of their login/register attempt
 * @param context the context from where this is launched
 */
class LoadDialog(context: Context) : Dialog(context) {

  private var statusText: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.loading_dialog)
    Timber.i("Starting load dialog. Layout set.")

    statusText = findViewById(R.id.dialog)
    val cancelButton = findViewById<ImageView>(R.id.cancel_ratings_button)

    cancelButton.setOnClickListener {
      Timber.d("User closed the load dialog.")
      dismiss()
    }
  }

  fun updateProgressText(error: String) {
    Timber.e("Login/Sing-up unsuccessful, error message: $error")
    statusText?.text = error
  }
}
