package com.example.irudiak

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class ShowImageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        val extras = intent.getBundleExtra(MainActivity.WORDS_BUNDLE) ?: Bundle.EMPTY

        val arrayIdx = extras.getInt(MainActivity.CURRENT_ARRAY_INDEX)
        val wordIds = extras.getIntegerArrayList(MainActivity.WORD_ID_ARRAY).orEmpty()
        val wordId = wordIds[arrayIdx]
        val img = findViewById<ImageView>(R.id.wordImage)

        val drawableId = resources.getIdentifier("word_$wordId", "drawable", packageName)
        val res = ContextCompat.getDrawable(applicationContext, drawableId)

        img.setImageDrawable(res)
    }

    fun goToCheckWord(view: View) {
        val toCheck = Intent(this, CheckWordActivity::class.java)
        toCheck.putExtra(MainActivity.WORDS_BUNDLE, intent.getBundleExtra(MainActivity.WORDS_BUNDLE))
        toCheck.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(toCheck)
        overridePendingTransition(0, 0)
    }
}
