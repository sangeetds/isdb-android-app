package com.isdb.login.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isdb.R
import timber.log.Timber

/**
 * Opens a dialog which tells user about the status of their login/register attempt
 */
class LoadDialog : DialogFragment() {

  private var statusText: TextView? = null
  private var dialogView: View? = null

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return MaterialAlertDialogBuilder(requireContext(), theme).apply {
      dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
      dialogView?.let { onViewCreated(it, savedInstanceState) }
      setView(dialogView)
    }.create()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    val inflate = inflater.inflate(R.layout.loading_dialog, container, false)
    Timber.i("Starting load dialog. Layout set.")

    statusText = inflate.findViewById(R.id.dialog)
    val cancelButton = inflate.findViewById<ImageView>(R.id.cancel_ratings_button)

    cancelButton.setOnClickListener {
      Timber.d("User closed the load dialog.")
      dismiss()
    }
    return inflate
  }

  override fun onResume() {
    super.onResume()
    dialog?.window?.setLayout(900, 1200);
  }

  fun updateProgressText(error: String) {
    Timber.e("Login/Sing-up unsuccessful, error message: $error")
    statusText?.text = error
  }

  override fun getView(): View? {
    return dialogView
  }
}
