package com.example.login.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.login.R
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class RatingsDialog(context: Context) : Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ratings_dialog)

        val cancelButton = findViewById<ImageView>(R.id.cancel_ratings_button)
        val songRating = findViewById<MaterialRatingBar>(R.id.star_ratings)

        cancelButton.setOnClickListener {
            dismiss()
        }

        songRating.setOnRatingChangeListener { _, rating ->
            Toast.makeText(context, "Voted $rating", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}