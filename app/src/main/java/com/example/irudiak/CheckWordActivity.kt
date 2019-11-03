package com.example.irudiak

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.irudiak.dao.AppDatabase
import com.example.irudiak.dao.DatabaseClient

class CheckWordActivity : AppCompatActivity() {

    lateinit var extras : Bundle
    var arrayIdx: Int = 0
    lateinit var wordIds: List<Int>
    lateinit var words: List<String>
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_word)

        db = DatabaseClient.getInstance(applicationContext).appDatabase

        extras = intent.getBundleExtra(MainActivity.WORDS_BUNDLE) ?: Bundle.EMPTY

        arrayIdx = extras.getInt(MainActivity.CURRENT_ARRAY_INDEX)
        wordIds = extras.getIntegerArrayList(MainActivity.WORD_ID_ARRAY).orEmpty()
        words = extras.getStringArrayList(MainActivity.WORD_ARRAY).orEmpty()
        findViewById<TextView>(R.id.word).text = words[arrayIdx]

        val wordId = wordIds[arrayIdx]
        val img = findViewById<ImageView>(R.id.wordImage)
        val drawableId = resources.getIdentifier("word_$wordId", "drawable", packageName)
        val res = ContextCompat.getDrawable(applicationContext, drawableId)

        img.setImageDrawable(res)
    }

    fun onNo(view: View) {
        UpdateTask(false).execute()
    }

    fun onYes(view: View) {
        UpdateTask(true).execute()
    }

    internal inner class UpdateTask (private val success: Boolean) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            val w = db.wordDao().getById(wordIds[arrayIdx])
            val newBucket = if (success) Math.min(w.bucket + 1, Bucket.BUCKETS.size - 1) else 0
            val daysToAddInSec = Bucket.BUCKETS[newBucket] * Bucket.DAY_IN_SECONDS
            val newCheckNext = w.nextCheck + daysToAddInSec
            db.wordDao().update(wordIds[arrayIdx], newBucket, newCheckNext)
            val w2 = db.wordDao().getById(wordIds[arrayIdx])
            Log.i("kokoko updated", w2.toString())
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            if (arrayIdx + 1 < wordIds.size) {
                extras.putInt(MainActivity.CURRENT_ARRAY_INDEX, arrayIdx + 1)
                val toShowImage = Intent(applicationContext, ShowImageActivity::class.java)
                toShowImage.putExtra(MainActivity.WORDS_BUNDLE, extras)
                toShowImage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(toShowImage)
                overridePendingTransition(0, 0)
            } else {
                val toMain = Intent(applicationContext, MainActivity::class.java)
                toMain.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(toMain)
            }
        }
    }

}
