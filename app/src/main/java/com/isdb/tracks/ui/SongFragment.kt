package com.isdb.tracks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isdb.R
import com.isdb.login.data.Result.Success
import com.isdb.login.data.model.User
import com.isdb.tracks.data.dto.SongDTO
import com.isdb.tracks.data.dto.UserSongDTO
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [SongFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SongFragment : Fragment() {
  private var user: User? = null
  private lateinit var songAdapter: SongAdapter
  private val viewModel: SongViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      user = it.getParcelable(ARG_PARAM1)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    // Inflate the layout for this fragment
    val inflate = inflater.inflate(R.layout.fragment_song, container, false)
    Timber.i("Switched to user fragment for $user")

    val update = { userSongDTO: UserSongDTO ->
      viewModel.updateRatings(userSongDTO)
    }

    this.songAdapter = SongAdapter(context = requireContext(), update, user)
    val recyclerView = inflate.findViewById<RecyclerView>(R.id.song_recycler_view)
    recyclerView.adapter = songAdapter
    recyclerView.layoutManager = LinearLayoutManager(requireContext())
    recyclerView.setHasFixedSize(true)
    Timber.i("Recycler view laid out.")

    return inflate
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.songs.observe(viewLifecycleOwner, Observer {
      val song = it ?: return@Observer
      Timber.i("New songs loaded up.")

      if (song is Success<List<SongDTO>> && song.data.isNotEmpty()) {
        Timber.i("New songs load up: ${song.data.map { s -> s.name }}")
        songAdapter.submitList(song.data)
      }
    })
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SongFragment.
     */
    @JvmStatic
    fun newInstance(
      param1: User,
    ) =
      SongFragment().apply {
        arguments = Bundle().apply {
          putParcelable(ARG_PARAM1, param1)
        }
      }
  }
}