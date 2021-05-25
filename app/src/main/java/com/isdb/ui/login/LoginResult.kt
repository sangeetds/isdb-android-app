package com.isdb.ui.login

import com.isdb.models.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
  val success: User? = null,
  val error: Int? = null
)