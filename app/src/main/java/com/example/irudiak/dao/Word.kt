package com.example.irudiak.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word (
    @PrimaryKey
    var id : Int,
    var meaning: String,
    var bucket : Int,
    var nextCheck : Int
)
