package com.isdb.login.ui

import com.isdb.login.data.model.User

data class RegisterResult(
  val success: User? = null,
  val error: Int? = null
)