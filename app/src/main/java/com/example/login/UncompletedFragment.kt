package com.example.login

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class UncompletedFragment : Fragment() {

    lateinit var questionAdapter: QuestionAdapter
    private var searchView: SearchView? = null
    private lateinit var questionDataBase: QuestionDataBase
    private lateinit var repository: QuestionRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        questionDataBase = QuestionDataBase.getDatabase(context!!)!!
        repository = QuestionRepository(questionDataBase.questionDao())

        val rootView = inflater.inflate(R.layout.fragment_uncompleted, container, false)
        val questionView = rootView.findViewById<RecyclerView>(R.id.questionList)
        questionView.setHasFixedSize(true)

        val questionList = getListOfNames()
        questionAdapter = QuestionAdapter(context, questionList.toMutableList())
        questionView.adapter = questionAdapter

        questionView.layoutManager = LinearLayoutManager(context)

        questionAdapter.onComplete = {
            val question = it
            question.completed = true
            repository.removeQuestion(it)
            repository.addQuestion(question)
        }

        return rootView
    }

    private fun getListOfNames() = repository.uncompletedQuestions

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val activity = activity!!
        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView!!.setSearchableInfo(
            searchManager.getSearchableInfo(activity.componentName)
        )
        searchView!!.maxWidth = Int.MAX_VALUE

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                questionAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                questionAdapter.filter.filter(query)
                return false
            }
        })
    }

    companion object {
        const val TAG = "Uncomplete"

    }
}