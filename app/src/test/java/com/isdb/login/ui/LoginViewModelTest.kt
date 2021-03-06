package com.isdb.login.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.isdb.R.string
import com.isdb.TestCoroutineRule
import com.isdb.login.data.LoginRepository
import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.login.data.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LoginViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val testCoroutineRule = TestCoroutineRule()

  private lateinit var loginViewModel: LoginViewModel
  private val loginResultObserver: Observer<LoginResult> = spyk()
  private val loginFormObserver: Observer<LoginFormState> = spyk()
  private val loginRepository: LoginRepository = mockk()
  private val user = User(password = "sangeet", email = "sangeet@gmail.com")

  @Before
  fun setUp() {
    loginViewModel = LoginViewModel(loginRepository)
  }

  @Test
  fun `register user when successful registration`() = testCoroutineRule.runBlockingTest {
    coEvery { loginRepository.login(user) } returns Success(user)

    loginViewModel.login(user.email, user.password)
    loginViewModel.loginResult.observeForever(loginResultObserver)

    coVerify { loginRepository.login(user = user) }
    verify { loginResultObserver.onChanged(LoginResult(success = user, error = null)) }

    loginViewModel.loginResult.removeObserver(loginResultObserver)
  }

  @Test
  fun `register user when unsuccessful registration`() = testCoroutineRule.runBlockingTest {
    coEvery { loginRepository.login(user) } returns Error(Exception("Unsuccessful Registration"))

    loginViewModel.login(user.email, user.password)
    loginViewModel.loginResult.observeForever(loginResultObserver)

    coVerify { loginRepository.login(user = user) }
    verify {
      loginResultObserver.onChanged(LoginResult(error = string.login_failed, success = null))
    }

    loginViewModel.loginResult.removeObserver(loginResultObserver)
  }

  @Test
  fun `validate credentials when email not valid`() {
    val invalidUser = User(username = "sangeet", password = "sang", email = "sangeet")
    loginViewModel.loginDataChanged(invalidUser.email, invalidUser.password)
    loginViewModel.loginFormState.observeForever(loginFormObserver)

    verify {
      loginFormObserver.onChanged(
        LoginFormState(isDataValid = false, usernameError = string.invalid_email, passwordError = null))
    }

    loginViewModel.loginFormState.removeObserver(loginFormObserver)
  }

  @Test
  fun `validate credentials when password not valid`() {
    val invalidUser = User(username = "sangeet", email = "sang@gmail.com", password = "sang")
    loginViewModel.loginDataChanged(invalidUser.email, invalidUser.password)

    loginViewModel.loginFormState.observeForever(loginFormObserver)

    verify {
      loginFormObserver.onChanged(LoginFormState(isDataValid = false, usernameError = null,
        passwordError = string.invalid_password))
    }

    loginViewModel.loginFormState.removeObserver(loginFormObserver)
  }

  @Test
  fun `validate credentials when data valid`() {
    loginViewModel.loginDataChanged(user.email, user.password)

    loginViewModel.loginFormState.observeForever(loginFormObserver)

    verify {
      loginFormObserver.onChanged(
        LoginFormState(isDataValid = true, usernameError = null, passwordError = null))
    }

    loginViewModel.loginFormState.removeObserver(loginFormObserver)
  }
}