package com.example.isdb.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.isdb.R
import com.example.isdb.models.User

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
  private var user: User? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      user = it.getParcelable(ARG_PARAM2)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val inflate = inflater.inflate(R.layout.fragment_user, container, false)

    val userEmail = inflate.findViewById<TextView>(R.id.user_email)
    userEmail.text = user?.email

    val userName = inflate.findViewById<TextView>(R.id.user_name)
    userName.text = user?.username

    return inflate
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    @JvmStatic
    fun newInstance(
      param2: User
    ) =
      UserFragment().apply {
        arguments = Bundle().apply {
          putParcelable(ARG_PARAM2, param2)
        }
      }
  }
}