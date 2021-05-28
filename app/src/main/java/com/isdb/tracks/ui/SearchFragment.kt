package com.isdb.tracks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isdb.R
import com.isdb.tracks.data.models.SongDTO
import com.isdb.login.data.model.User
import com.isdb.retrofit.Retrofit
import com.isdb.retrofit.SongService
import com.isdb.retrofit.getSongsList
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.logging.Logger

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

  private var param1: User? = null
  private lateinit var songSearchAdapter: SearchAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getParcelable(ARG_PARAM1)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val inflate = inflater.inflate(R.layout.fragment_search, container, false)

    songSearchAdapter = SearchAdapter(context = requireContext(), param1)
    val recyclerView = inflate.findViewById<RecyclerView>(R.id.search_recycler_view)
    recyclerView.setHasFixedSize(true)
    recyclerView.adapter = songSearchAdapter
    recyclerView.layoutManager = LinearLayoutManager(requireContext())

    val songSearchView = inflate?.findViewById<EditText>(R.id.song_search_view)
    val searchParentView = inflate?.findViewById<TextInputLayout>(R.id.search_parent)

    songSearchView?.setOnClickListener {
      searchParentView?.startIconDrawable?.setVisible(false, true)
    }

    songSearchView?.doOnTextChanged { text, _, _, _ ->
      if (text!!.isNotEmpty()) {
        recyclerView.visibility = View.VISIBLE
      }

      text.toString().getSongList()
    }

    songSearchView?.setOnEditorActionListener { view, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        if (view.text.isNotEmpty()) {
          recyclerView.visibility = View.VISIBLE
        }

        view.text.toString().getSongList()
      }

      true
    }

    return inflate
  }

  private fun String.getSongList() =
    runBlocking {
      val list: List<SongDTO>
      val retrofitService = Retrofit.getRetrofitClient(
        SongService::class.java
      ) as SongService

      withContext(Dispatchers.IO) {
        list = getSongsList(retrofitService, this@getSongList).body()!!
        Logger.getAnonymousLogger().info("Can we get $list")
      }

      songSearchAdapter.songList.addAll(list)
      songSearchAdapter.notifyDataSetChanged()
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