package com.isdb.login.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.google.common.truth.Truth
import com.isdb.R.string
import com.isdb.TestCoroutineRule
import com.isdb.login.data.LoginRepository
import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.login.data.model.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
  private lateinit var loginResult: LiveData<LoginResult>
  private lateinit var loginForm: LiveData<LoginFormState>
  private val loginRepository: LoginRepository = mockk()
  private val user = User(password = "sangeet", email = "sangeet@gmail.com")

  @Before
  fun setUp() {
    loginViewModel = LoginViewModel(loginRepository)
    loginResult = loginViewModel.loginResult
    loginForm = loginViewModel.loginFormState
  }

  @Test
  fun `register user when successful registration`() = testCoroutineRule.runBlockingTest {
    every { loginRepository.login(user) } returns flowOf(Success(user))

    loginViewModel.login(user.email, user.password)

    verify { loginRepository.login(user = user) }

    val loginResultState = loginResult.value
    Truth.assertThat(loginResultState).isNotNull()
    Truth.assertThat(loginResultState?.error).isNull()
    Truth.assertThat(loginResultState?.success).isNotNull()
    Truth.assertThat(loginResultState?.success).isEqualTo(user)
  }

  @Test
  fun `register user when unsuccessful registration`() = testCoroutineRule.runBlockingTest {
    every { loginRepository.login(user) } returns flowOf(
      Error(
        Exception("Unsuccessful Registration")
      )
    )

    loginViewModel.login(user.email, user.password)

    verify { loginRepository.login(user = user) }

    val loginResultState = loginResult.value
    Truth.assertThat(loginResultState).isNotNull()
    Truth.assertThat(loginResultState?.error).isNotNull()
    Truth.assertThat(loginResultState?.error).isEqualTo(string.login_failed)
    Truth.assertThat(loginResultState?.success).isNull()
  }

  @Test
  fun `validate credentials when email not valid`() {
    val invalidUser = User(username = "sangeet", password = "sang", email = "sangeet")
    loginViewModel.loginDataChanged(invalidUser.email, invalidUser.password)

    val loginFormState = loginForm.value
    Truth.assertThat(loginFormState).isNotNull()
    Truth.assertThat(loginFormState?.isDataValid).isFalse()
    Truth.assertThat(loginFormState?.usernameError).isEqualTo(string.invalid_email)
    Truth.assertThat(loginFormState?.passwordError).isNull()
  }

  @Test
  fun `validate credentials when password not valid`() {
    val invalidUser = User(username = "sangeet", email = "sang@gmail.com", password = "sang")
    loginViewModel.loginDataChanged(invalidUser.email, invalidUser.password)

    val registerFormState = loginForm.value
    Truth.assertThat(registerFormState).isNotNull()
    Truth.assertThat(registerFormState?.isDataValid).isFalse()
    Truth.assertThat(registerFormState?.usernameError).isNull()
    Truth.assertThat(registerFormState?.passwordError).isEqualTo(string.invalid_password)
  }
}