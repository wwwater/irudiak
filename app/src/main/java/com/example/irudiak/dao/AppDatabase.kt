package com.example.irudiak.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Word::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}
