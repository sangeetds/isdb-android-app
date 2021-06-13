package com.isdb.login.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.google.common.truth.Truth.assertThat
import com.isdb.R.string
import com.isdb.TestCoroutineRule
import com.isdb.login.data.RegisterRepository
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
class RegisterViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val testCoroutineRule = TestCoroutineRule()

  private lateinit var registerViewModel: RegisterViewModel
  private lateinit var registerResult: LiveData<RegisterResult>
  private lateinit var registerForm: LiveData<RegisterFormState>
  private val registerRepository: RegisterRepository = mockk()
  private val user = User(username = "Sangeet", password = "sangeet", email = "sangeet@gmail.com")

  @Before
  fun setUp() {
    registerViewModel = RegisterViewModel(registerRepository)
    registerResult = registerViewModel.registerResult
    registerForm = registerViewModel.registerFormState
  }

  @Test
  fun `register user when successful registration`() = testCoroutineRule.runBlockingTest {
    every { registerRepository.register(user) } returns flowOf(Success(user))

    registerViewModel.register(user.email, user.username, user.password)

    verify { registerRepository.register(user = user) }

    val registerResultState = registerResult.value
    assertThat(registerResultState).isNotNull()
    assertThat(registerResultState?.error).isNull()
    assertThat(registerResultState?.success).isNotNull()
    assertThat(registerResultState?.success).isEqualTo(user)
  }

  @Test
  fun `register user when unsuccessful registration`() = testCoroutineRule.runBlockingTest {
    every { registerRepository.register(user) } returns flowOf(
      Error(
        Exception("Unsuccessful Registration")
      )
    )

    registerViewModel.register(user.email, user.username, user.password)

    verify { registerRepository.register(user = user) }

    val registerResultState = registerResult.value
    assertThat(registerResultState).isNotNull()
    assertThat(registerResultState?.error).isNotNull()
    assertThat(registerResultState?.error).isEqualTo(string.login_failed)
    assertThat(registerResultState?.success).isNull()
  }

  @Test
  fun `validate credentials when email not valid`() {
    val invalidUser = User(username = "sangeet", password = "sang", email = "sangeet")
    registerViewModel.registerDataChanged(invalidUser.email, invalidUser.username, invalidUser.password)

    val registerFormState = registerForm.value
    assertThat(registerFormState).isNotNull()
    assertThat(registerFormState?.isDataValid).isFalse()
    assertThat(registerFormState?.emailError).isEqualTo(string.invalid_email)
    assertThat(registerFormState?.usernameError).isNull()
    assertThat(registerFormState?.passwordError).isNull()
  }

  @Test
  fun `validate credentials when username not valid`() {
    val invalidUser = User(username = "", email = "sang@gmail.com", password = "sangeet")
    registerViewModel.registerDataChanged(invalidUser.email, invalidUser.username, invalidUser.password)

    val registerFormState = registerForm.value
    assertThat(registerFormState).isNotNull()
    assertThat(registerFormState?.isDataValid).isFalse()
    assertThat(registerFormState?.emailError).isNull()
    assertThat(registerFormState?.usernameError).isEqualTo(string.invalid_username)
    assertThat(registerFormState?.passwordError).isNull()
  }

  @Test
  fun `validate credentials when password not valid`() {
    val invalidUser = User(username = "sangeet", email = "sang@gmail.com", password = "sang")
    registerViewModel.registerDataChanged(invalidUser.email, invalidUser.username, invalidUser.password)

    val registerFormState = registerForm.value
    assertThat(registerFormState).isNotNull()
    assertThat(registerFormState?.isDataValid).isFalse()
    assertThat(registerFormState?.emailError).isNull()
    assertThat(registerFormState?.usernameError).isNull()
    assertThat(registerFormState?.passwordError).isEqualTo(string.invalid_password)
  }
}