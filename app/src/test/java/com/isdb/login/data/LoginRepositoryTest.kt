package com.isdb.login.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.isdb.TestCoroutineRule
import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.login.data.api.LoginService
import com.isdb.login.data.model.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response
import java.net.SocketTimeoutException

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LoginRepositoryTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val testCoroutineRule = TestCoroutineRule()

  private lateinit var loginRepository: LoginRepository
  private val loginService: LoginService = mockk()
  private val user = User(username = "Sangeet", password = "sangeet", email = "sangeet@gmail.com")

  @Before
  fun setup() {
    loginRepository = LoginRepository(loginService)
  }

  @Test
  fun `login user when success network call`() = testCoroutineRule.runBlockingTest {
    coEvery { loginService.logInUser(user) } returns Response.success(user)

    val login = loginRepository.login(user)

    assertThat(login).isNotNull()
    assertThat(login).isInstanceOf(Success::class.java)
    assertThat(login).isEqualTo(Success(user))
  }

  @Test
  fun `login user when failed network call`() = testCoroutineRule.runBlockingTest {
    coEvery { loginService.logInUser(user) } returns Response.error(
      400,
      "{\"key\":[\"somestuff\"]}"
        .toResponseBody("application/json".toMediaTypeOrNull())
    )

    val login = loginRepository.login(user)

    assertThat(login).isNotNull()
    assertThat(login).isInstanceOf(Error::class.java)
  }

  @Test
  fun `login user when failed network call throws exception`() = testCoroutineRule.runBlockingTest {
    coEvery { loginService.logInUser(user) } throws SocketTimeoutException()

    val login = loginRepository.login(user)

    assertThat(login).isNotNull()
    assertThat(login).isInstanceOf(Error::class.java)
  }
}