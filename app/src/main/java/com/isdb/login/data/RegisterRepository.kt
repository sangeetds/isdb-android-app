package com.isdb.login.data

import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.login.data.model.User
import com.isdb.retrofit.LoginService
import com.isdb.retrofit.Retrofit
import com.isdb.retrofit.createAccount
import java.net.SocketTimeoutException

class RegisterRepository {

  var user: User? = null
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