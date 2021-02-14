package com.example.login

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service implementation interface used in making API requests
 */
interface LoginService {

    /**
     * Function to make a post request which returns a response from the register request
     *
     * @param user which takes in [User] with the registering credentials
     */
    @POST("/users/register")
    fun createUser(
        @Body user: User
    ): Call<Status>

    /**
     * Function to make a post request which returns a response from the logging request.
     *
     * @param user which takes in [User] with the logging credentials
     */
    @POST("users/login")
    fun logInUser(
        @Body user: User
    ): Call<Status>
}

/**
 * Helper function which implements the LoginService interface methods to make API register request.
 * Returns the execution of the call.
 *
 * @param service the interface for making API requests
 * @param user the [User] with the credentials
 */
fun createAccount(service: LoginService, user: User): Response<Status> = service
    .createUser(user)
    .execute()

/**
 * Helper function which implements the LoginService interface methods to make API login request.
 * Returns the execution of the call.
 *
 * @param service the interface for making API requests
 * @param user the [User] with the credentials
 */
fun logIn(service: LoginService, user: User): Response<Status> = service
    .logInUser(user)
    .execute()





