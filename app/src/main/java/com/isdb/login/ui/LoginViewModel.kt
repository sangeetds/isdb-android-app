package com.isdb.login.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isdb.R.string
import com.isdb.login.data.LoginRepository
import com.isdb.login.data.Result
import com.isdb.login.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

  private val _loginForm = MutableLiveData<LoginFormState>()
  val loginFormState: LiveData<LoginFormState> = _loginForm

  private val _loginResult = MutableLiveData<LoginResult>()
  val loginResult: LiveData<LoginResult> = _loginResult

  fun login(username: String, password: String) = viewModelScope.launch {
    val user = User(email = username, password = password)
    Timber.i("Making request to the server for user: $user")

    loginRepository.login(user).collect { result ->
      _loginResult.value = if (result is Result.Success) {
        LoginResult(success = result.data)
      } else {
        LoginResult(error = string.login_failed)
      }
    }
  }

  fun loginDataChanged(username: String, password: String) {
    _loginForm.value = if (!isUserNameValid(username)) {
       LoginFormState(usernameError = string.invalid_email)
    } else if (!isPasswordValid(password)) {
       LoginFormState(passwordError = string.invalid_password)
    } else {
       LoginFormState(isDataValid = true)
    }
  }

  // A placeholder username validation check
  private fun isUserNameValid(username: String) =
      Patterns.EMAIL_ADDRESS.matcher(username).matches()

  // A placeholder password validation check
  private fun isPasswordValid(password: String) = password.length > 5
}