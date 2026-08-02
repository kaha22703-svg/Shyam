package com.example.data.local

import androidx.room.Entity

@Entity(tableName = "favourite_workers", primaryKeys = ["employerId", "mistryId"])
data class FavouriteWorkerEntity(
    val employerId: Int,
    val mistryId: Int
)
