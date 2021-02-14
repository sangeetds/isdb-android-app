package com.example.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class QuestionAdapter(
    private val context: Context?,
    private val questions: MutableList<Question>,
) : RecyclerView.Adapter<QuestionAdapter.Card>(), Filterable {

    private var filteredQuestions: MutableList<Question> = questions
    lateinit var onComplete: (Question) -> Unit
    lateinit var onFavorite: (Question) -> Unit

    inner class Card(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val questionName: TextView = itemView.findViewById(R.id.questionTitle)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }
    }

    fun changeContent() {

    }

    override fun getItemCount() = this.filteredQuestions.size

    override fun onBindViewHolder(holder: Card, position: Int) {
        val questionItem = filteredQuestions[position]
        holder.questionName.text = questionItem.title

        holder.itemView.setOnClickListener {
            val detailIntent = Intent(context, QuestionDetailActivity::class.java)
            detailIntent.putExtra("Properties", questionItem)
            context!!.startActivity(detailIntent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Card {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.questionlist_item, parent, false)
        return Card(inflatedView)
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence): FilterResults? {
                val charString = constraint.toString()

                filteredQuestions = if (charString.isEmpty()) {
                    questions
                } else {
                    questions.filter { it.title.contains(constraint, true) }.toMutableList()
                }

                val filterResults = FilterResults()
                filterResults.values = filteredQuestions
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                filteredQuestions = filterResults.values as MutableList<Question>
                notifyDataSetChanged()
            }
        }
    }

    private fun removeItem(holder: Card) {
        val actualPosition: Int = holder.adapterPosition
        filteredQuestions.removeAt(actualPosition)
        notifyItemRemoved(actualPosition)
        notifyItemRangeChanged(actualPosition, questions.size)
    }

}



