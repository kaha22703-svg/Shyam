package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mistryId: Int,
    val reviewerName: String,
    val rating: Float,
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)
