package com.example.irudiak.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    fun getAll(): List<Word>

    @Query("SELECT * FROM word WHERE id = :id")
    fun getById(id: Int): Word

    @Query("SELECT * FROM word WHERE nextCheck <= :date LIMIT :count")
    fun findWordsToCheck(date: Int, count: Int): List<Word>

    @Insert
    fun insertAll(words: Array<Word>)

    @Query("UPDATE word SET bucket = :newBucket, nextCheck = :newCheck WHERE id = :id")
    fun update(id: Int, newBucket: Int, newCheck: Int)


}
