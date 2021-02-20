package com.example.login.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.login.R
import com.example.login.models.Song

class SongDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)

        val currentSong: Song = intent.getParcelableExtra("Properties")!!

        val title = findViewById<TextView>(R.id.question)
        val questionContent = findViewById<TextView>(R.id.questionContent)
        val codingButton = findViewById<Button>(R.id.startCoding)
        val shareButton = findViewById<Button>(R.id.shareQuestion)

        title.text = currentSong.title
        questionContent.text = currentSong.content

        codingButton.setOnClickListener {
            val uri: Uri =
                Uri.parse(currentSong.url) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, currentSong.url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }
}