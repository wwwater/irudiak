package com.example.irudiak

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.irudiak.dao.AppDatabase
import com.example.irudiak.dao.DatabaseClient
import com.example.irudiak.dao.Word


class MainActivity : AppCompatActivity() {
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = DatabaseClient.getInstance(applicationContext).appDatabase
        GetAllWords().execute()
    }


    fun startCheckingWords(view: View) {
        StartCheckWords(20).execute()
    }

    internal inner class GetAllWords : AsyncTask<Void, Void, List<Word>>() {

        override fun doInBackground(vararg voids: Void): List<Word> {
            var ws = db.wordDao().getAll()
            if (ws.isEmpty()) {
                val now = (System.currentTimeMillis() / 1000).toInt()
                val words = InitialWords.WORDS.map { pair ->
                    Word (pair.first, pair.second, 0 , now) }.toTypedArray()
                db.wordDao().insertAll(words)
                ws = db.wordDao().getAll()
            }
            Log.i("kokoko", ws.toString())
            return ws
        }

        override fun onPostExecute(words: List<Word>) {
            super.onPostExecute(words)
            val now = (System.currentTimeMillis() / 1000).toInt()
            val ripingWords = words.filter { w -> w.nextCheck > now }
            val readyWords = words.filter { w -> w.nextCheck <= now }
            arrayOf(R.id.bucket2_riping, R.id.bucket3_riping, R.id.bucket4_riping, R.id.bucket5_riping)
                .forEachIndexed { index, b ->
                    findViewById<TextView>(b).text =
                        ripingWords.filter { w -> w.bucket == index + 1}.count().toString()

                }
            arrayOf(R.id.bucket1, R.id.bucket2, R.id.bucket3, R.id.bucket4, R.id.bucket5)
                .forEachIndexed { index, b ->
                    findViewById<TextView>(b).text =
                        readyWords.filter { w -> w.bucket == index }.count().toString()

                }
            findViewById<TextView>(R.id.wordsReady).text = readyWords.count().toString()
        }
    }

    internal inner class StartCheckWords(private val amount: Int) : AsyncTask<Void, Void, List<Word>>()  {


        override fun doInBackground(vararg voids: Void): List<Word> {
            val now = (System.currentTimeMillis() / 1000).toInt()
            val ws = db.wordDao().findWordsToCheck(now, amount)
            return ws
        }

        override fun onPostExecute(words: List<Word>) {
            super.onPostExecute(words)
            if (words.isEmpty()) {
                val myToast = Toast.makeText(applicationContext, "Nothing to learn", Toast.LENGTH_SHORT)
                myToast.show()
                return
            }
            val extras = Bundle(3)
            extras.putInt(CURRENT_ARRAY_INDEX, 0)
            extras.putIntegerArrayList(WORD_ID_ARRAY, ArrayList(words.map { w -> w.id }))
            extras.putStringArrayList(WORD_ARRAY, ArrayList(words.map { w -> w.meaning }))
            val toShowImage = Intent(applicationContext, ShowImageActivity::class.java)
            toShowImage.putExtra(WORDS_BUNDLE, extras)
            toShowImage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(toShowImage)
        }
    }

    companion object {
        const val WORD_ID_ARRAY = "word_ids"
        const val WORD_ARRAY = "words"
        const val CURRENT_ARRAY_INDEX = "current_idx"
        const val WORDS_BUNDLE = "words_bundle"
    }
}
