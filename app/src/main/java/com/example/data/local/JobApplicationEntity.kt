package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_applications")
data class JobApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val jobId: Int,
    val mistryId: Int,
    val employerId: Int,
    val jobTitle: String,
    val mistryName: String,
    val mistryPhone: String,
    val mistrySkill: String,
    val dailyWage: Double,
    val status: String = "REQUESTED", // "REQUESTED", "ACCEPTED", "REJECTED", "HIRED", "COMPLETED"
    val notes: String = "",
    val appliedAt: Long = System.currentTimeMillis()
)
