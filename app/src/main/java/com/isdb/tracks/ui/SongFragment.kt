package com.isdb.tracks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isdb.R
import com.isdb.tracks.data.models.SongDTO
import com.isdb.login.data.model.User
import com.isdb.retrofit.Retrofit
import com.isdb.retrofit.SongService
import com.isdb.retrofit.getLikedSong
import com.isdb.retrofit.getSongsList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [SongFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SongFragment : Fragment() {
  private var user: User? = null
  private lateinit var songAdapter: SongAdapter
  private lateinit var updateList: () -> Unit

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    updateList = {
      getSongList()
    }
    getSongList()

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
    val inflate = inflater.inflate(R.layout.fragment_song, container, false)

    this.songAdapter = SongAdapter(context = requireContext(), updateList, user)
    val recyclerView = inflate.findViewById<RecyclerView>(R.id.song_recycler_view)
    recyclerView.adapter = songAdapter
    recyclerView.layoutManager = LinearLayoutManager(requireContext())
    recyclerView.setHasFixedSize(true)

    return inflate
  }

  private fun getSongList() =
    CoroutineScope(Dispatchers.Main).launch {
      var list: List<SongDTO>?
      var idList: List<String>?
      val retrofitService = Retrofit.getRetrofitClient(
        SongService::class.java
      ) as SongService

      withContext(Dispatchers.IO) {
        list = getSongsList(retrofitService, null).body()
        idList = getLikedSong(service = retrofitService, id = user!!.id).body()
      }

      idList?.let {
        songAdapter.idList.clear()
        songAdapter.idList.addAll(it)
      }

      list?.let {
        songAdapter.songList = it.toMutableList()
        songAdapter.notifyDataSetChanged()
      }
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