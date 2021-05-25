package com.isdb.data

import com.isdb.data.Result.Error
import com.isdb.data.Result.Success
import com.isdb.data.model.LoggedInUser
import com.isdb.models.User
import com.isdb.service.LoginService
import com.isdb.service.Retrofit
import com.isdb.service.logIn
import java.net.SocketTimeoutException

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

  // in-memory cache of the loggedInUser object
  var user: LoggedInUser? = null
    private set

  val isLoggedIn: Boolean
    get() = user != null

  init {
    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    user = null
  }

  fun logout() {
    user = null
  }

  fun login(user: User): Result<User> {
    // handle login
    val retrofitService =
      Retrofit.getRetrofitClient(LoginService::class.java) as LoginService

    return update(retrofitService, user)
  }

  private fun update(retrofitService: LoginService, user: User): Result<User> = try {
    val response = logIn(retrofitService, user = user)
    when (response.code()) {
      200 -> {
        Success(response.body()!!)
      }
      else -> {
        Error(Exception(response.errorBody().toString()))
      }
    }
  } catch (exception: SocketTimeoutException) {
    Error(Exception("Server Down. Please try again."))
  }
}