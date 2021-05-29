package com.isdb.login.data

import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.login.data.model.User
import com.isdb.retrofit.LoginService
import com.isdb.retrofit.Retrofit
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException

class RegisterRepository {

  var user: User? = null
    private set

  init {
    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    user = null
  }

  fun register(user: User) = flow {
    val retrofit = Retrofit.getRetrofitClient(LoginService::class.java) as LoginService
    emit(update(retrofit, user))
  }

  private suspend fun update(
    retrofitService: LoginService,
    user: User
  ) =
    try {
      retrofitService.createUser(user).run {
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