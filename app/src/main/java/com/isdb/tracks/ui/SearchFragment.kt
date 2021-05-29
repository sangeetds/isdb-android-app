package com.isdb.tracks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.isdb.R
import com.isdb.login.data.model.User
import com.isdb.tracks.data.dto.UserSongDTO
import timber.log.Timber

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

  private var user: User? = null
  private lateinit var viewModel: SearchSongViewModel
  private lateinit var songSearchAdapter: SearchAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      user = it.getParcelable(ARG_PARAM1)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val inflate = inflater.inflate(R.layout.fragment_search, container, false)
    Timber.i("Switched to user fragment for $user")

    val update = { userSongDTO: UserSongDTO ->
      viewModel.updateRatings(userSongDTO)
    }
    songSearchAdapter = SearchAdapter(context = requireContext(), user, update)
    val recyclerView = inflate.findViewById<RecyclerView>(R.id.search_recycler_view)
    recyclerView.adapter = songSearchAdapter
    recyclerView.layoutManager = LinearLayoutManager(requireContext())
    recyclerView.setHasFixedSize(true)
    Timber.i("Recycler view laid out.")

    val songSearchView = inflate.findViewById<EditText>(R.id.song_search_view)
    val searchParentView = inflate.findViewById<TextInputLayout>(R.id.search_parent)

    songSearchView?.setOnClickListener {
      searchParentView?.startIconDrawable?.setVisible(false, true)
    }

    songSearchView?.doOnTextChanged { text, _, _, _ ->
      if (text!!.isNotEmpty() && text.length > 1) {
        recyclerView.visibility = View.VISIBLE
        viewModel.getSongs(text.toString(), userId = user!!.id)
      }
    }

    songSearchView?.setOnEditorActionListener { view, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE && view.text.isNotEmpty()) {
        recyclerView.visibility = View.VISIBLE
        viewModel.getSongs(view.text.toString(), userId = user!!.id)
      }

      true
    }

    return inflate
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    viewModel =
      ViewModelProvider(this, SearchSongViewModelFactory()).get(SearchSongViewModel::class.java)

    viewModel.searchedSongs.observe(viewLifecycleOwner, Observer {
      val song = it ?: return@Observer
      Timber.i("New songs loaded up.")

      if (song.isNotEmpty()) {
        Timber.i("New songs load up: ${song.map { s -> s.name }}")
        songSearchAdapter.songList = song.toMutableList()
        songSearchAdapter.notifyDataSetChanged()
      }
    })
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter.
     * @return A new instance of fragment SearchFragment.
     */
    @JvmStatic
    fun newInstance(
      user: User
    ) =
      SearchFragment().apply {
        arguments = Bundle().apply {
          putParcelable(ARG_PARAM1, user)
        }
      }
  }
}