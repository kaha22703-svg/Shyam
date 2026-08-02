package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userType: String, // "MISTRY", "EMPLOYER", "ADMIN"
    val fullName: String,
    val companyName: String = "",
    val mobile: String,
    val email: String = "",
    val passwordHash: String = "",
    val profilePhotoUri: String = "",
    val aadhaarNumber: String = "",
    val experienceYears: Int = 0,
    val dailyWage: Double = 0.0,
    val hourlyRate: Double = 0.0,
    val monthlySalary: Double = 0.0,
    val city: String,
    val district: String,
    val state: String,
    val liveLocation: String = "",
    val workingRadiusKm: Int = 20,
    val isOnline: Boolean = true,
    val isVerified: Boolean = false,
    val skills: String = "", // Comma-separated list of skills
    val bio: String = "",
    val toolsAndEquipment: String = "", // Comma-separated or text listing owned tools and equipment
    val workPhotos: String = "", // Comma-separated photo URIs
    val idProofUri: String = "",
    val bankUpiDetails: String = "",
    val rating: Float = 4.8f,
    val totalReviews: Int = 12,
    val isBlocked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
