package com.isdb.tracks.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.isdb.TestCoroutineRule
import com.isdb.login.data.Result.Error
import com.isdb.login.data.Result.Success
import com.isdb.tracks.data.api.SongService
import com.isdb.tracks.data.dto.SongDTO
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
class SearchSongRepositoryTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val testCoroutineRule = TestCoroutineRule()

  private lateinit var searchSongRepository: SearchSongRepository
  private val songService: SongService = mockk()

  @Before
  fun setup() {
    searchSongRepository = SearchSongRepository(songService)
  }

  @Test
  fun `login user when success network call`() = testCoroutineRule.runBlockingTest {
    val songName = "songName"
    val userId = "userId"
    val songDTOs = listOf(
      SongDTO(name = songName, isUserRated = true))
    coEvery { songService.getSongs(songName, userId) } returns Response.success(songDTOs)

    val login = searchSongRepository.getSongs(songName, userId)

    Truth.assertThat(login).isNotNull()
    Truth.assertThat(login).isInstanceOf(Success::class.java)
    Truth.assertThat(login).isEqualTo(Success(songDTOs))
  }

  @Test
  fun `login user when failed network call`() = testCoroutineRule.runBlockingTest {
    val songName = "songName"
    val userId = "userId"
    coEvery { songService.getSongs(songName, userId) } returns Response.error(
      400,
      "{\"key\":[\"somestuff\"]}"
        .toResponseBody("application/json".toMediaTypeOrNull())
    )

    val login = searchSongRepository.getSongs(songName, userId)

    Truth.assertThat(login).isNotNull()
    Truth.assertThat(login).isInstanceOf(Error::class.java)
  }

  @Test
  fun `login user when failed network call throws exception`() = testCoroutineRule.runBlockingTest {
    val songName = "songName"
    val userId = "userId"
    coEvery { songService.getSongs(songName, userId) } throws SocketTimeoutException()

    val login = searchSongRepository.getSongs(songName, userId)

    Truth.assertThat(login).isNotNull()
    Truth.assertThat(login).isInstanceOf(Error::class.java)
  }
}