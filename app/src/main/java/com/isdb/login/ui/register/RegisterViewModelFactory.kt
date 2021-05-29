package com.isdb.login.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isdb.login.data.RegisterRepository
import timber.log.Timber

class RegisterViewModelFactory : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
      Timber.i("RegisterViewModel instantiated successfully")
      return RegisterViewModel(registerRepository = RegisterRepository()) as T
    }
    Timber.e("Error creating view model, wrong view model requested.")
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}