package com.example.login

import com.squareup.moshi.Json

/**
 * User class which stores the credentials of the user.
 *
 * Moshi helps converting the an object of this class into a JSON.
 */
data class User(
    @field:Json(name = "username") val username: String,
    @field:Json(name = "password") var password: String,
    @field:Json(name = "email") val email: String? = null
    ){

}