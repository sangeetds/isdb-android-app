package com.isdb.data

import com.isdb.data.Result.Error
import com.isdb.data.Result.Success
import com.isdb.data.model.LoggedInUser
import com.isdb.models.User
import com.isdb.service.LoginService
import com.isdb.service.Retrofit
import com.isdb.service.createAccount
import java.net.SocketTimeoutException

class RegisterRepository {

  var user: LoggedInUser? = null
    private set

  init {
    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    user = null
  }

  fun register(user: User): Result<User> {
    val retrofit = Retrofit.getRetrofitClient(LoginService::class.java) as LoginService

    return update(retrofit, user)
  }

  private fun update(retrofitService: LoginService, user: User): Result<User> = try {
    val response = createAccount(retrofitService, user = user)
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