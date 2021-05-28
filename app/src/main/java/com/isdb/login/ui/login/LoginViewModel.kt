package com.isdb.login.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isdb.R.string
import com.isdb.login.data.LoginRepository
import com.isdb.login.data.Result
import com.isdb.login.data.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

  private val _loginForm = MutableLiveData<LoginFormState>()
  val loginFormState: LiveData<LoginFormState> = _loginForm

  private val _loginResult = MutableLiveData<LoginResult>()
  val loginResult: LiveData<LoginResult> = _loginResult

  fun login(
    username: String,
    password: String
  ) = viewModelScope.launch {

    // can be launched in a separate asynchronous job
    val user = User(email = username, password = password)
    loginRepository.login(user).collect { result ->
      if (result is Result.Success) {
        _loginResult.value =
          LoginResult(success = result.data)
      } else {
        _loginResult.value = LoginResult(error = string.login_failed)
      }
    }
  }

  fun loginDataChanged(
    username: String,
    password: String
  ) {
    if (!isUserNameValid(username)) {
      _loginForm.value = LoginFormState(usernameError = string.invalid_email)
    } else if (!isPasswordValid(password)) {
      _loginForm.value = LoginFormState(passwordError = string.invalid_password)
    } else {
      _loginForm.value = LoginFormState(isDataValid = true)
    }
  }

  // A placeholder username validation check
  private fun isUserNameValid(username: String): Boolean {
    return if (username.contains('@')) {
      Patterns.EMAIL_ADDRESS.matcher(username).matches()
    } else {
      username.isNotBlank()
    }
  }

  // A placeholder password validation check
  private fun isPasswordValid(password: String): Boolean {
    return password.length > 5
  }
}