package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow

class MistriRepository(private val appDao: AppDao) {

    val allUsers: Flow<List<UserEntity>> = appDao.getAllUsers()
    val allMistris: Flow<List<UserEntity>> = appDao.getAllMistris()
    val allJobs: Flow<List<JobPostEntity>> = appDao.getAllJobPosts()

    fun getJobsByEmployer(employerId: Int): Flow<List<JobPostEntity>> =
        appDao.getJobsByEmployer(employerId)

    fun getApplicationsByMistry(mistryId: Int): Flow<List<JobApplicationEntity>> =
        appDao.getApplicationsByMistry(mistryId)

    fun getApplicationsByEmployer(employerId: Int): Flow<List<JobApplicationEntity>> =
        appDao.getApplicationsByEmployer(employerId)

    fun getReviewsForMistry(mistryId: Int): Flow<List<ReviewEntity>> =
        appDao.getReviewsForMistry(mistryId)

    fun getFavourites(employerId: Int): Flow<List<Int>> =
        appDao.getFavouriteMistryIds(employerId)

    suspend fun getUserById(id: Int): UserEntity? = appDao.getUserById(id)
    suspend fun getUserByMobile(mobile: String): UserEntity? = appDao.getUserByMobile(mobile)

    suspend fun registerUser(user: UserEntity): Long = appDao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = appDao.updateUser(user)
    suspend fun updateOnlineStatus(userId: Int, isOnline: Boolean) = appDao.updateOnlineStatus(userId, isOnline)
    suspend fun updateDailyWage(userId: Int, dailyWage: Double) = appDao.updateDailyWage(userId, dailyWage)
    suspend fun updateVerificationStatus(userId: Int, isVerified: Boolean) = appDao.updateVerificationStatus(userId, isVerified)
    suspend fun updateBlockStatus(userId: Int, isBlocked: Boolean) = appDao.updateBlockStatus(userId, isBlocked)
    suspend fun updateToolsAndEquipment(userId: Int, tools: String) = appDao.updateToolsAndEquipment(userId, tools)

    fun getWorkHistoriesForWorker(workerId: Int): Flow<List<WorkHistoryEntity>> = appDao.getWorkHistoriesForWorker(workerId)
    suspend fun addWorkHistory(workHistory: WorkHistoryEntity): Long = appDao.insertWorkHistory(workHistory)
    suspend fun deleteWorkHistory(historyId: Int) = appDao.deleteWorkHistoryById(historyId)

    suspend fun postJob(job: JobPostEntity): Long = appDao.insertJob(job)
    suspend fun getJobById(jobId: Int): JobPostEntity? = appDao.getJobById(jobId)
    suspend fun updateJobStatus(jobId: Int, status: String) = appDao.updateJobStatus(jobId, status)
    suspend fun deleteJob(jobId: Int) = appDao.deleteJob(jobId)

    suspend fun applyForJob(application: JobApplicationEntity): Long = appDao.insertApplication(application)
    suspend fun updateApplicationStatus(appId: Int, status: String) = appDao.updateApplicationStatus(appId, status)

    suspend fun addReview(review: ReviewEntity) {
        appDao.insertReview(review)
        // Also update Mistry's total reviews and average rating
        val mistry = appDao.getUserById(review.mistryId)
        if (mistry != null) {
            val newTotal = mistry.totalReviews + 1
            val newRating = ((mistry.rating * mistry.totalReviews) + review.rating) / newTotal
            appDao.updateUser(mistry.copy(rating = newRating, totalReviews = newTotal))
        }
    }

    suspend fun addFavourite(employerId: Int, mistryId: Int) =
        appDao.addFavourite(FavouriteWorkerEntity(employerId, mistryId))

    suspend fun removeFavourite(employerId: Int, mistryId: Int) =
        appDao.removeFavourite(employerId, mistryId)

    suspend fun seedInitialDataIfEmpty() {
        // Pre-populate verified Mistry profiles and initial sample jobs if database is empty
        val sampleUser = appDao.getUserByMobile("9876543210")
        if (sampleUser == null) {
            val mistry1 = UserEntity(
                userType = "MISTRY",
                fullName = "रमेश शर्मा (Ramesh Sharma)",
                mobile = "9876543210",
                email = "ramesh.mistry@gmail.com",
                experienceYears = 8,
                dailyWage = 850.0,
                hourlyRate = 120.0,
                monthlySalary = 24000.0,
                city = "लखनऊ",
                district = "लखनऊ",
                state = "उत्तर प्रदेश (Uttar Pradesh)",
                liveLocation = "26.8467° N, 80.9462° E - हजरतगंज, लखनऊ",
                workingRadiusKm = 25,
                isOnline = true,
                isVerified = true,
                skills = "इलेक्ट्रिशियन, हेल्पर इलेक्ट्रिशियन, मेसन",
                bio = "8 वर्षों का अनुभवी इलेक्ट्रिशियन। घर, ऑफिस और बड़ी साइटों की वायरिंग में माहिर।",
                toolsAndEquipment = "अपनी सीढ़ी (Own Ladder), हैवी पावर ड्रिल (Power Drill), डिजिटल मल्टीमीटर, इंसुलेटेड वायर स्ट्रिपर",
                rating = 4.9f,
                totalReviews = 28,
                bankUpiDetails = "ramesh@upi"
            )

            val mistry2 = UserEntity(
                userType = "MISTRY",
                fullName = "सुरेश कुमार राजमिस्त्री (Suresh Kumar)",
                mobile = "9876543211",
                email = "suresh.mason@gmail.com",
                experienceYears = 12,
                dailyWage = 950.0,
                hourlyRate = 140.0,
                monthlySalary = 27000.0,
                city = "पटना",
                district = "पटना",
                state = "बिहार (Bihar)",
                liveLocation = "25.5941° N, 85.1376° E - कंकड़बाग, पटना",
                workingRadiusKm = 30,
                isOnline = true,
                isVerified = true,
                skills = "मेसन, फर्श-टाइल या IPS, स्टोन मेसन, कारपेंटर शटरिंग",
                bio = "12 साल का अनुभव, आरसीसी, ब्रिक वर्क और मार्बल/टाइल फिटिंग के विशेषज्ञ मेसन।",
                rating = 4.8f,
                totalReviews = 42,
                bankUpiDetails = "suresh@upi"
            )

            val mistry3 = UserEntity(
                userType = "MISTRY",
                fullName = "मोहम्मद सलीम प्लम्बर (Md Salim)",
                mobile = "9876543212",
                email = "salim.plumbing@gmail.com",
                experienceYears = 6,
                dailyWage = 750.0,
                hourlyRate = 100.0,
                monthlySalary = 21000.0,
                city = "नई दिल्ली",
                district = "दक्षिण दिल्ली",
                state = "दिल्ली NCR (Delhi NCR)",
                liveLocation = "28.5355° N, 77.2610° E - नेहरू प्लेस, दिल्ली",
                workingRadiusKm = 20,
                isOnline = true,
                isVerified = true,
                skills = "प्लम्बर, हेल्पर प्लम्बर, वेल्डर",
                bio = "सैनिटरी फिटिंग, वाटर टैंक लीक रिपेयर और जीआई/सीपीसी पाइपलाइन कार्य में विशेषज्ञ।",
                rating = 4.7f,
                totalReviews = 19,
                bankUpiDetails = "salim@upi"
            )

            val mistry4 = UserEntity(
                userType = "MISTRY",
                fullName = "विक्रम सिंह पेंटर (Vikram Singh)",
                mobile = "9876543213",
                email = "vikram.painter@gmail.com",
                experienceYears = 7,
                dailyWage = 800.0,
                hourlyRate = 110.0,
                monthlySalary = 22000.0,
                city = "जयपुर",
                district = "जयपुर",
                state = "राजस्थान (Rajasthan)",
                liveLocation = "26.9124° N, 75.7873° E - वैशाली नगर, जयपुर",
                workingRadiusKm = 25,
                isOnline = false,
                isVerified = true,
                skills = "पेंटर, कारपेंटर फर्नीचर, एल्युमिनियम कांच",
                bio = "एशियन पेंट, पुट्टी, टेक्सचर पेंटिंग और लकड़ी की पॉलिश में विशेषज्ञता।",
                rating = 4.9f,
                totalReviews = 31
            )

            val mistry5 = UserEntity(
                userType = "MISTRY",
                fullName = "अमित यादव JCB ऑपरेटर (Amit Yadav)",
                mobile = "9876543214",
                experienceYears = 10,
                dailyWage = 1200.0,
                hourlyRate = 200.0,
                monthlySalary = 35000.0,
                city = "इंदौर",
                district = "इंदौर",
                state = "मध्य प्रदेश (Madhya Pradesh)",
                liveLocation = "22.7196° N, 75.8577° E - विजय नगर, इंदौर",
                workingRadiusKm = 50,
                isOnline = true,
                isVerified = true,
                skills = "JCB ऑपरेटर, क्रेन ऑपरेटर, RMC ऑपरेटर",
                bio = "10 साल का भारी मशीनरी ड्राइविंग अनुभव। कमर्शियल साइट खुदाई में माहिर।",
                rating = 5.0f,
                totalReviews = 15
            )

            val emp1 = UserEntity(
                userType = "EMPLOYER",
                fullName = "अनिल गुप्ता (Gupta Constructions)",
                companyName = "Gupta Builders & Contractors",
                mobile = "9988776655",
                email = "info@guptabuilders.com",
                city = "लखनऊ",
                district = "लखनऊ",
                state = "उत्तर प्रदेश (Uttar Pradesh)",
                liveLocation = "26.8467° N, 80.9462° E - गोमती नगर, लखनऊ",
                isVerified = true
            )

            val m1Id = appDao.insertUser(mistry1).toInt()
            val m2Id = appDao.insertUser(mistry2).toInt()
            appDao.insertUser(mistry3)
            appDao.insertUser(mistry4)
            appDao.insertUser(mistry5)
            val empId = appDao.insertUser(emp1).toInt()

            // Seed sample jobs
            val job1 = JobPostEntity(
                employerId = empId,
                employerName = "Gupta Builders",
                employerPhone = "9988776655",
                jobTitle = "कमर्शियल बिल्डिंग के लिए 5 राजमिस्त्री (Mason) चाहिए",
                requiredSkill = "मेसन",
                workersNeeded = 5,
                wageAmount = 900.0,
                wageType = "PER_DAY",
                hasAccommodation = true,
                hasFood = true,
                durationDays = 15,
                startDate = "कल से (From Tomorrow)",
                city = "लखनऊ",
                district = "लखनऊ",
                state = "उत्तर प्रदेश (Uttar Pradesh)",
                siteLocation = "गोमती नगर एक्सटेंशन, साइट नंबर 4, लखनऊ",
                additionalInfo = "रहने और खाने की सुविधा कंपनी की तरफ से मुफ्त दी जाएगी। समय पर पेमेंट।"
            )

            val job2 = JobPostEntity(
                employerId = empId,
                employerName = "Gupta Builders",
                employerPhone = "9988776655",
                jobTitle = "अस्पताल प्रोजेक्ट के लिए 3 इलेक्ट्रिशियन चाहिए",
                requiredSkill = "इलेक्ट्रिशियन",
                workersNeeded = 3,
                wageAmount = 850.0,
                wageType = "PER_DAY",
                hasAccommodation = false,
                hasFood = true,
                durationDays = 10,
                startDate = "तुरंत (Immediately)",
                city = "लखनऊ",
                district = "लखनऊ",
                state = "उत्तर प्रदेश (Uttar Pradesh)",
                siteLocation = "हजरतगंज, सहारा अस्पताल के पास",
                additionalInfo = "कंसील्ड पाइपिंग और वायर पुलिंग का काम है।"
            )

            val job3 = JobPostEntity(
                employerId = empId,
                employerName = "शर्मा इंटीरियर डिकोर्स",
                employerPhone = "9876123456",
                jobTitle = "विला इंटीरियर कार्य हेतु 2 कारपेंटर फर्नीचर",
                requiredSkill = "कारपेंटर फर्नीचर",
                workersNeeded = 2,
                wageAmount = 1000.0,
                wageType = "PER_DAY",
                hasAccommodation = true,
                hasFood = false,
                durationDays = 20,
                startDate = "2 दिन में",
                city = "नई दिल्ली",
                district = "दक्षिण दिल्ली",
                state = "दिल्ली NCR (Delhi NCR)",
                siteLocation = "वसंत कुंज, ब्लॉक C, नई दिल्ली",
                additionalInfo = "मॉड्यूलर किचन और अलमारी निर्माण का फिनिशिंग वर्क।"
            )

            val j1Id = appDao.insertJob(job1).toInt()
            appDao.insertJob(job2)
            appDao.insertJob(job3)

            // Seed reviews
            appDao.insertReview(ReviewEntity(mistryId = m1Id, reviewerName = "राजेश वर्मा", rating = 5.0f, comment = "बहुत ही अच्छा और साफ-सुथरा काम किया। पूरे समय पर वायरिंग पूरी की।"))
            appDao.insertReview(ReviewEntity(mistryId = m1Id, reviewerName = "अमित शुक्ला", rating = 4.8f, comment = "विश्वसनीय मिस्त्री हैं, रेट भी वाजिब था।"))
            appDao.insertReview(ReviewEntity(mistryId = m2Id, reviewerName = "विकास सिंह", rating = 5.0f, comment = "टाइल फिटिंग बहुत ही फिनिशिंग के साथ की।"))

            // Seed sample Work History for Mistris
            appDao.insertWorkHistory(
                WorkHistoryEntity(
                    workerId = m1Id,
                    title = "3 बीएचके फ्लैट कम्प्लीट वायरिंग",
                    description = "पूरे फ्लैट में कंसील्ड पीवीसी पाइपिंग, इन्वर्टर लाइन वायरिंग, 12-मॉड्यूलर बोर्ड्स और फैंसी लाइट फिटिंग सफलता पूर्वक की।",
                    location = "गोमती नगर एक्सटेंशन, लखनऊ",
                    duration = "15 दिन (Jan 2024)"
                )
            )
            appDao.insertWorkHistory(
                WorkHistoryEntity(
                    workerId = m1Id,
                    title = "कमर्शियल शोरूम इलेक्ट्रिकल सेट-अप",
                    description = "3000 sq ft शोरूम में 3-फेज डीबी बोर्ड, एसी पॉइंट्स, एलईडी ट्रैक लाइट्स और अर्थिंग कार्य।",
                    location = "हजरतगंज, लखनऊ",
                    duration = "3 सप्ताह (March 2024)"
                )
            )
            appDao.insertWorkHistory(
                WorkHistoryEntity(
                    workerId = m2Id,
                    title = "डुप्लेक्स विला इटैलियन टाइल एवं आरसीसी कार्य",
                    description = "4000 sq ft विला में नींव ढलाई, कॉलम शटरिंग, और लिविंग एरिया में इटैलियन मार्बल फ्लोरिंग कार्य।",
                    location = "कंकड़बाग, पटना",
                    duration = "2 महीने (Feb - Mar 2024)"
                )
            )
        }
    }
}
