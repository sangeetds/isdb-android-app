package com.isdb.login.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isdb.R.string
import com.isdb.login.data.RegisterRepository
import com.isdb.login.data.Result
import com.isdb.login.data.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

  private val _registerForm = MutableLiveData<RegisterFormState>()
  val registerFormState: LiveData<RegisterFormState> = _registerForm

  private val _registerResult = MutableLiveData<RegisterResult>()
  val registerResult: LiveData<RegisterResult> = _registerResult

  fun register(
    email: String,
    username: String,
    password: String
  ) =
    viewModelScope.launch {
      val user = User(email = email, username = username, password = password)
      Timber.i("Making request to the server for user: $user")

      registerRepository.register(user).collect { result ->
        if (result is Result.Success) {
          _registerResult.value =
            RegisterResult(success = result.data)
        } else {
          _registerResult.value = RegisterResult(error = string.login_failed)
        }
      }
    }

  fun registerDataChanged(
    email: String,
    username: String,
    password: String
  ) {
    if (!isEmailValid(email)) {
      _registerForm.value = RegisterFormState(emailError = string.invalid_email)
    } else if (!isUserNameValid(username)) {
      _registerForm.value = RegisterFormState(usernameError = string.invalid_username)
    } else if (!isPasswordValid(password)) {
      _registerForm.value = RegisterFormState(passwordError = string.invalid_password)
    } else {
      _registerForm.value = RegisterFormState(isDataValid = true)
    }
  }

  // A placeholder username validation check
  private fun isUserNameValid(username: String) = username.isNotBlank()

  private fun isEmailValid(email: String) =
    Patterns.EMAIL_ADDRESS.matcher(email).matches()

  // A placeholder password validation check
  private fun isPasswordValid(password: String): Boolean {
    return password.length > 5
  }
}