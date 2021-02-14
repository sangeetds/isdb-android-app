package com.example.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuestionDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)

        val currentQuestion: Question = intent.getParcelableExtra("Properties")!!

        val title = findViewById<TextView>(R.id.question)
        val questionContent = findViewById<TextView>(R.id.questionContent)
        val codingButton = findViewById<Button>(R.id.startCoding)
        val shareButton = findViewById<Button>(R.id.shareQuestion)

        title.text = currentQuestion.title
        questionContent.text = currentQuestion.content

        codingButton.setOnClickListener {
            val uri: Uri =
                Uri.parse(currentQuestion.url) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, currentQuestion.url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }
}