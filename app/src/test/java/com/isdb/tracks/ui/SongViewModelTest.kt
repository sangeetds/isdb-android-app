package com.isdb.tracks.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.isdb.TestCoroutineRule
import com.isdb.login.data.Result
import com.isdb.tracks.data.SongRepository
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
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
class SongViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val testCoroutineRule = TestCoroutineRule()

  private lateinit var songViewModel: SongViewModel
  private val songResultObserver: Observer<Result<List<SongDTO>>> = spyk()
  private val songRepository: SongRepository = mockk()

  @Before
  fun setUp() {
    songViewModel = SongViewModel(songRepository)
  }

  @Test
  fun `update ratings success test`() = testCoroutineRule.runBlockingTest {
    val userSongDTO = UserSongDTO(songId = "1", userId = "userId")
    coJustRun { songRepository.updateRatings(userSongDTO) }

    songViewModel.updateRatings(userSongDTO)

    coVerify { songRepository.updateRatings(userSongDTO) }
  }
}