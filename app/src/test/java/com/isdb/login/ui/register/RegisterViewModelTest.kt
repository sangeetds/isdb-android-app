package com.isdb.login.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.isdb.R.string
import com.isdb.TestCoroutineRule
import com.isdb.login.data.RegisterRepository
import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.login.data.model.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
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
  private val registerResultObserver: Observer<RegisterResult> = spyk()
  private val registerFormObserver: Observer<RegisterFormState> = spyk()
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
    registerViewModel.registerResult.observeForever(registerResultObserver)

    verify { registerRepository.register(user = user) }
    verify { registerResultObserver.onChanged(RegisterResult(success = user, error = null)) }

    registerViewModel.registerResult.removeObserver(registerResultObserver)
  }

  @Test
  fun `register user when unsuccessful registration`() = testCoroutineRule.runBlockingTest {
    every { registerRepository.register(user) } returns flowOf(
      Error(
        Exception("Unsuccessful Registration")
      )
    )

    registerViewModel.register(user.email, user.username, user.password)
    registerViewModel.registerResult.observeForever(registerResultObserver)

    verify { registerRepository.register(user = user) }
    verify {
      registerResultObserver.onChanged(RegisterResult(error = string.login_failed, success = null))
    }

    registerViewModel.registerResult.removeObserver(registerResultObserver)
  }

  @Test
  fun `validate credentials when email not valid`() {
    val invalidUser = User(username = "sangeet", password = "sang", email = "sangeet")
    registerViewModel.registerDataChanged(invalidUser.email, invalidUser.username,
      invalidUser.password)
    registerViewModel.registerFormState.observeForever(registerFormObserver)

    verify {
      registerFormObserver.onChanged(
        RegisterFormState(emailError = string.invalid_email, isDataValid = false,
          usernameError = null, passwordError = null))
    }

    registerViewModel.registerFormState.removeObserver(registerFormObserver)
  }

  @Test
  fun `validate credentials when username not valid`() {
    val invalidUser = User(username = "", email = "sang@gmail.com", password = "sangeet")
    registerViewModel.registerDataChanged(invalidUser.email, invalidUser.username,
      invalidUser.password)

    registerViewModel.registerFormState.observeForever(registerFormObserver)

    verify {
      registerFormObserver.onChanged(
        RegisterFormState(emailError = null, isDataValid = false,
          usernameError = string.invalid_username, passwordError = null))
    }

    registerViewModel.registerFormState.removeObserver(registerFormObserver)
  }

  @Test
  fun `validate credentials when password not valid`() {
    val invalidUser = User(username = "sangeet", email = "sang@gmail.com", password = "sang")
    registerViewModel.registerDataChanged(invalidUser.email, invalidUser.username,
      invalidUser.password)

    registerViewModel.registerFormState.observeForever(registerFormObserver)

    verify {
      registerFormObserver.onChanged(
        RegisterFormState(emailError = null, isDataValid = false,
          usernameError = null, passwordError = string.invalid_password))
    }

    registerViewModel.registerFormState.removeObserver(registerFormObserver)
  }

  @Test
  fun `validate credentials when data valid`() {
    registerViewModel.registerDataChanged(user.email, user.username, user.password)

    registerViewModel.registerFormState.observeForever(registerFormObserver)

    verify {
      registerFormObserver.onChanged(
        RegisterFormState(emailError = null, isDataValid = true,
          usernameError = null, passwordError = null))
    }

    registerViewModel.registerFormState.removeObserver(registerFormObserver)
  }
}