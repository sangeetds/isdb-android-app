package com.isdb.data

import com.isdb.data.Result.Error
import com.isdb.data.Result.Success
import com.isdb.data.model.LoggedInUser
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

  fun login(
    username: String,
    password: String
  ): Result<LoggedInUser> {
    try {
      // TODO: handle loggedInUser authentication
      val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
      return Success(fakeUser)
    } catch (e: Throwable) {
      return Error(IOException("Error logging in", e))
    }
  }

  fun logout() {
    // TODO: revoke authentication
  }
}