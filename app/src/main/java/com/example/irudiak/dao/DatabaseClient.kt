package com.example.irudiak.dao

import android.content.Context

import androidx.room.Room

class DatabaseClient private constructor(mCtx: Context) {

    var appDatabase: AppDatabase =
        Room.databaseBuilder(mCtx, AppDatabase::class.java, "words-db-5").build()

    companion object {
        var mInstance: DatabaseClient? = null

        @Synchronized
        fun getInstance(mCtx: Context): DatabaseClient {
            if (mInstance == null) {
                mInstance = DatabaseClient(mCtx)
            }
            return mInstance!!
        }
    }
}