package com.isdb.login.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.isdb.TestCoroutineRule
import com.isdb.login.data.api.LoginService
import com.isdb.login.data.model.User
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response

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
  }
}