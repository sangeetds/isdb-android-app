package com.isdb.tracks.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.isdb.TestCoroutineRule
import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.tracks.data.SearchSongRepository
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import com.isdb.tracks.ui.SearchSongViewModel
import io.mockk.coEvery
import io.mockk.coJustRun
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
class SearchSongViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val testCoroutineRule = TestCoroutineRule()

  private lateinit var searchViewModel: SearchSongViewModel
  private val searchSongResultObserver: Observer<List<SongDTO>> = spyk()
  private val searchSongRepository: SearchSongRepository = mockk()

  @Before
  fun setUp() {
    searchViewModel = SearchSongViewModel(searchSongRepository)
  }

  @Test
  fun `search song success test`() = testCoroutineRule.runBlockingTest {
    val songName = "songName"
    val userId = "userId"
    val songDTOs = listOf(
      SongDTO(name = songName, isUserRated = true))
    coEvery { searchSongRepository.getSongs(songName, userId) } returns Success(songDTOs)

    searchViewModel.searchedSongs.observeForever(searchSongResultObserver)
    searchViewModel.getSongs(songName, userId = userId)

    coVerify { searchSongRepository.getSongs(songName, userId) }
    verify { searchSongResultObserver.onChanged(songDTOs) }

    searchViewModel.searchedSongs.removeObserver(searchSongResultObserver)
  }

  @Test
  fun `search song failure test`() = testCoroutineRule.runBlockingTest {
    val songName = "songName"
    val userId = "userId"

    coEvery { searchSongRepository.getSongs(songName, userId) } returns Error(Exception())

    searchViewModel.searchedSongs.observeForever(searchSongResultObserver)
    searchViewModel.getSongs(songName, userId = userId)

    coVerify { searchSongRepository.getSongs(songName, userId) }
    verify { searchSongResultObserver.onChanged(listOf()) }

    searchViewModel.searchedSongs.removeObserver(searchSongResultObserver)
  }

  @Test
  fun `update songs success test`() = testCoroutineRule.runBlockingTest {
    val userSongDTO = UserSongDTO(songId = "1", userId = "userId")
    coJustRun { searchSongRepository.updateRatings(userSongDTO) }

    searchViewModel.updateRatings(userSongDTO)

    coVerify { searchSongRepository.updateRatings(userSongDTO) }
  }
}