package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_posts")
data class JobPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employerId: Int,
    val employerName: String,
    val employerPhone: String,
    val jobTitle: String,
    val requiredSkill: String,
    val workersNeeded: Int = 1,
    val wageAmount: Double = 800.0,
    val wageType: String = "PER_DAY", // "PER_DAY", "PER_HOUR", "CONTRACT"
    val hasAccommodation: Boolean = false, // रहने की सुविधा
    val hasFood: Boolean = false, // खाने की सुविधा
    val durationDays: Int = 5,
    val startDate: String = "तुरंत (Immediately)",
    val city: String,
    val district: String,
    val state: String,
    val siteLocation: String,
    val mapLocationPin: String = "",
    val photoUri: String = "",
    val additionalInfo: String = "",
    val status: String = "OPEN", // "OPEN", "IN_PROGRESS", "COMPLETED", "CANCELLED"
    val createdAt: Long = System.currentTimeMillis()
)
