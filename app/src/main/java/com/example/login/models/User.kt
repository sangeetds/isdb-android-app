package com.example.login.models

import com.squareup.moshi.Json

/**
 * User class which stores the credentials of the user.
 *
 * Moshi helps converting the an object of this class into a JSON.
 */
data class User(
    @Json(name = "username") val username: String = "",
    @Json(name = "password") var password: String = "",
    @Json(name = "email") val email: String = ""
)