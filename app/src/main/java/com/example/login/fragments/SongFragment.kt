package com.example.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.adapters.SongAdapter
import com.example.login.models.SongDTO
import com.example.login.service.Retrofit
import com.example.login.service.SongService
import com.example.login.service.getSongsList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SongFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SongFragment : Fragment() {
  // TODO: Rename and change types of parameters
  private var param1: String? = null
  private var param2: String? = null
  private lateinit var songAdapter: SongAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    getSongList()
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val inflate = inflater.inflate(R.layout.fragment_song, container, false)

    this.songAdapter = SongAdapter(context = context!!)
    val recyclerView = inflate.findViewById<RecyclerView>(R.id.song_recycler_view)
    recyclerView.adapter = songAdapter
    recyclerView.layoutManager = LinearLayoutManager(context!!)
    recyclerView.setHasFixedSize(true)

    return inflate
  }

  private fun getSongList() =
    CoroutineScope(Dispatchers.Main).launch {
      var list: List<SongDTO>?
      val retrofitService = Retrofit.getRetrofitClient(
              getString(R.string.baseUrl),
              SongService::class.java
      ) as SongService

      withContext(Dispatchers.IO) {
        list = getSongsList(retrofitService, null).body()
      }

      songAdapter.songList.addAll(list!!)
      songAdapter.notifyDataSetChanged()
    }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(
        param1: String,
        param2: String
    ) =
      SongFragment().apply {
        arguments = Bundle().apply {
          putString(ARG_PARAM1, param1)
          putString(ARG_PARAM2, param2)
        }
      }
  }
}