package com.isdb.login.data.api

import com.isdb.login.data.model.User
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
  suspend fun createUser(@Body user: User): Response<User>

  /**
   * Function to make a post request which returns a response from the logging request.
   *
   * @param user which takes in [User] with the logging credentials
   */
  @POST("users/login")
  suspend fun logInUser(@Body user: User): Response<User>
}







