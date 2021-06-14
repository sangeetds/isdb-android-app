package com.isdb.login.ui

import com.isdb.login.data.model.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
  val success: User? = null,
  val error: Int? = null
)