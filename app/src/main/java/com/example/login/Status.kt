package com.example.login

import com.squareup.moshi.Json

/**
 * Simple enum class to reduce dependency on hard-coded strings.
 * Used for checking the status of the request.
 *
 * Moshi helps converting the JSON body response into an object of this class
 */
enum class Status {
    @field:Json(name = "SUCCESS")SUCCESS,
    @field:Json(name = "USER_ALREADY_EXISTS")USER_ALREADY_EXISTS,
    @field:Json(name = "FAILURE")FAILURE
}