package com.isdb.login.data

import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.login.data.model.User
import com.isdb.retrofit.LoginService
import com.isdb.retrofit.Retrofit
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

  // in-memory cache of the loggedInUser object
  var user: User? = null
    private set

  init {
    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    user = null
  }

  fun login(user: User) = flow {
    // handle login
    val retrofitService =
      Retrofit.getRetrofitClient(LoginService::class.java) as LoginService

    emit(update(retrofitService, user))
  }

  private suspend fun update(
    retrofitService: LoginService,
    user: User
  ): Result<User> = try {
    retrofitService.logInUser(user = user).run {
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