package com.isdb.login.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isdb.R.string
import com.isdb.login.data.RegisterRepository
import com.isdb.login.data.Result
import com.isdb.login.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) :
  ViewModel() {

  private val _registerForm = MutableLiveData<RegisterFormState>()
  val registerFormState: LiveData<RegisterFormState> = _registerForm

  private val _registerResult = MutableLiveData<RegisterResult>()
  val registerResult: LiveData<RegisterResult> = _registerResult

  fun register(email: String, username: String, password: String) =
    viewModelScope.launch {
      val user = User(email = email, username = username, password = password)
      Timber.i("Making request to the server for user: $user")

      val result = registerRepository.register(user)
      _registerResult.value = if (result is Result.Success) {
          RegisterResult(success = result.data)
        } else {
          RegisterResult(error = string.login_failed)
        }

    }

  fun registerDataChanged(email: String, username: String, password: String) {
    _registerForm.value = when {
      !isEmailValid(email) -> RegisterFormState(emailError = string.invalid_email)
      !isUserNameValid(username) -> RegisterFormState(usernameError = string.invalid_username)
      !isPasswordValid(password) -> RegisterFormState(passwordError = string.invalid_password)
      else -> RegisterFormState(isDataValid = true)
    }
  }

  // A placeholder username validation check
  private fun isUserNameValid(username: String) = username.isNotBlank()

  private fun isEmailValid(email: String) =
    Patterns.EMAIL_ADDRESS.matcher(email).matches()

  // A placeholder password validation check
  private fun isPasswordValid(password: String) = password.length > 5
}