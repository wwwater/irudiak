package com.example.irudiak.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word (
    var meaning: String,
    var picture: String,
    var bucket : Int,
    var nextCheck : Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
