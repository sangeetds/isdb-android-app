package com.isdb.login.data

import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.login.data.api.LoginService
import com.isdb.login.data.model.User
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(private val loginService: LoginService) {

  suspend fun login(user: User) =
    try {
      this.loginService.logInUser(user = user).run {
        when {
          isSuccessful && body() != null -> {
            Timber.i("Login successful with response: ${raw()} ")
            Success(body()!!)
          }
          else -> {
            Timber.e("Error while logging in with error: ${errorBody()}")
            Error(Exception(errorBody().toString()))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Error while logging in with error: $exception")
      Error(Exception("Server Down. Please try again."))
    }
}