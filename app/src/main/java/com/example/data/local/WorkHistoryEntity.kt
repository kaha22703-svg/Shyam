package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_histories")
data class WorkHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workerId: Int,
    val title: String,
    val description: String,
    val location: String,
    val duration: String,
    val photoUris: String = "", // Comma-separated photo URIs
    val createdAt: Long = System.currentTimeMillis()
)
