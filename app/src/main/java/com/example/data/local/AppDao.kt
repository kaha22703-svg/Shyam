package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // --- Users ---
    @Query("SELECT * FROM users ORDER BY isVerified DESC, rating DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE userType = 'MISTRY' AND isBlocked = 0 ORDER BY isVerified DESC, rating DESC")
    fun getAllMistris(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM users WHERE mobile = :mobile LIMIT 1")
    suspend fun getUserByMobile(mobile: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isOnline = :isOnline WHERE id = :userId")
    suspend fun updateOnlineStatus(userId: Int, isOnline: Boolean)

    @Query("UPDATE users SET dailyWage = :dailyWage WHERE id = :userId")
    suspend fun updateDailyWage(userId: Int, dailyWage: Double)

    @Query("UPDATE users SET isVerified = :isVerified WHERE id = :userId")
    suspend fun updateVerificationStatus(userId: Int, isVerified: Boolean)

    @Query("UPDATE users SET isBlocked = :isBlocked WHERE id = :userId")
    suspend fun updateBlockStatus(userId: Int, isBlocked: Boolean)

    @Query("UPDATE users SET toolsAndEquipment = :tools WHERE id = :userId")
    suspend fun updateToolsAndEquipment(userId: Int, tools: String)


    // --- Work Histories ---
    @Query("SELECT * FROM work_histories WHERE workerId = :workerId ORDER BY createdAt DESC")
    fun getWorkHistoriesForWorker(workerId: Int): Flow<List<WorkHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkHistory(workHistory: WorkHistoryEntity): Long

    @Query("DELETE FROM work_histories WHERE id = :historyId")
    suspend fun deleteWorkHistoryById(historyId: Int)


    // --- Jobs ---
    @Query("SELECT * FROM job_posts ORDER BY createdAt DESC")
    fun getAllJobPosts(): Flow<List<JobPostEntity>>

    @Query("SELECT * FROM job_posts WHERE employerId = :employerId ORDER BY createdAt DESC")
    fun getJobsByEmployer(employerId: Int): Flow<List<JobPostEntity>>

    @Query("SELECT * FROM job_posts WHERE id = :jobId")
    suspend fun getJobById(jobId: Int): JobPostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobPostEntity): Long

    @Update
    suspend fun updateJob(job: JobPostEntity)

    @Query("UPDATE job_posts SET status = :status WHERE id = :jobId")
    suspend fun updateJobStatus(jobId: Int, status: String)

    @Query("DELETE FROM job_posts WHERE id = :jobId")
    suspend fun deleteJob(jobId: Int)


    // --- Applications ---
    @Query("SELECT * FROM job_applications WHERE mistryId = :mistryId ORDER BY appliedAt DESC")
    fun getApplicationsByMistry(mistryId: Int): Flow<List<JobApplicationEntity>>

    @Query("SELECT * FROM job_applications WHERE employerId = :employerId ORDER BY appliedAt DESC")
    fun getApplicationsByEmployer(employerId: Int): Flow<List<JobApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: JobApplicationEntity): Long

    @Query("UPDATE job_applications SET status = :status WHERE id = :appId")
    suspend fun updateApplicationStatus(appId: Int, status: String)


    // --- Reviews ---
    @Query("SELECT * FROM reviews WHERE mistryId = :mistryId ORDER BY createdAt DESC")
    fun getReviewsForMistry(mistryId: Int): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long


    // --- Favourites ---
    @Query("SELECT mistryId FROM favourite_workers WHERE employerId = :employerId")
    fun getFavouriteMistryIds(employerId: Int): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(fav: FavouriteWorkerEntity)

    @Query("DELETE FROM favourite_workers WHERE employerId = :employerId AND mistryId = :mistryId")
    suspend fun removeFavourite(employerId: Int, mistryId: Int)
}
