package com.isdb.login.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.isdb.TestCoroutineRule
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
class RegisterRepositoryTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val testCoroutineRule = TestCoroutineRule()

  private lateinit var registerRepository: RegisterRepository
  private val loginService: LoginService = mockk()
  private val user = User(username = "Sangeet", password = "sangeet", email = "sangeet@gmail.com")

  @Before
  fun setup() {
    registerRepository = RegisterRepository(loginService)
  }

  @Test
  fun `login user when success network call`() = testCoroutineRule.runBlockingTest {
    coEvery { loginService.logInUser(user) } returns Response.success(user)

    val register = registerRepository.register(user)

    Truth.assertThat(register).isNotNull()
    Truth.assertThat(register).isInstanceOf(Success::class.java)
    Truth.assertThat(register).isEqualTo(Success(user))
  }

  @Test
  fun `login user when failed network call`() = testCoroutineRule.runBlockingTest {
    coEvery { loginService.logInUser(user) } returns Response.error(
      400,
      "{\"key\":[\"somestuff\"]}"
        .toResponseBody("application/json".toMediaTypeOrNull())
    )

    val register = registerRepository.register(user)

    Truth.assertThat(register).isNotNull()
    Truth.assertThat(register).isInstanceOf(Error::class.java)
  }

  @Test
  fun `login user when failed network call throws exception`() = testCoroutineRule.runBlockingTest {
    coEvery { loginService.logInUser(user) } throws SocketTimeoutException()

    val login = registerRepository.register(user)

    Truth.assertThat(login).isNotNull()
    Truth.assertThat(login).isInstanceOf(Error::class.java)
  }
}