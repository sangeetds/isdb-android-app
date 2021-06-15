package com.isdb.tracks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.isdb.TestCoroutineRule
import com.isdb.login.data.LoginRepository
import com.isdb.login.ui.LoginFormState
import com.isdb.login.ui.LoginResult
import com.isdb.login.ui.LoginViewModel
import com.isdb.tracks.data.SongRepository
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.ui.SearchSongViewModel
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SearchSongViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val testCoroutineRule = TestCoroutineRule()

  private lateinit var searchViewModel: SearchSongViewModel
  private lateinit var loginResult: LiveData<List<SongDTO>>
  private val loginResultObserver: Observer<List<SongDTO>> = spyk()
  private val songRepository: SongRepository = mockk()
}