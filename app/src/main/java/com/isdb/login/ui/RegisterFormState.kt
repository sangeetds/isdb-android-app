package com.isdb.login.ui

data class RegisterFormState (
  val emailError: Int? = null,
  val usernameError: Int? = null,
  val passwordError: Int? = null,
  val isDataValid: Boolean = false
)
