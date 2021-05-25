package com.isdb.ui.register

import com.isdb.models.User

data class RegisterResult(
  val success: User? = null,
  val error: Int? = null
)