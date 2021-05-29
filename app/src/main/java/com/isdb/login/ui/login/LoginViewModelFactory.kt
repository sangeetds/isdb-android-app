package com.isdb.login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isdb.login.data.LoginRepository
import timber.log.Timber

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
      Timber.i("LoginViewModel instantiated successfully")
      return LoginViewModel(loginRepository = LoginRepository()) as T
    }
    Timber.e("Error creating view model, wrong view model requested.")
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}