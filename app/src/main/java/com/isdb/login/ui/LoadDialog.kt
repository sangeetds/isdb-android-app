package com.isdb.login.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.isdb.R

/**
 * Opens a dialog which tells user about the status of their isdb/register attempt
 * @param context the context from where this is launched
 */
class LoadDialog(context: Context) : Dialog(context) {

  private var statusText: TextView? = null

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
  }

  fun updateProgressText(error: String) {
    statusText?.text = error
  }
}
