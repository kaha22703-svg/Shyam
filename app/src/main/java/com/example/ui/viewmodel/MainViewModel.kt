package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.repository.MistriRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SearchFilterState(
    val query: String = "",
    val skill: String? = null,
    val state: String? = null,
    val city: String? = null,
    val price: Float = 2000f,
    val exp: Int = 0
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val repository = MistriRepository(db.appDao())

    // --- State Flows ---
    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allMistris: StateFlow<List<UserEntity>> = repository.allMistris.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allJobs: StateFlow<List<JobPostEntity>> = repository.allJobs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Logged in User
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Settings
    private val _isHindi = MutableStateFlow(true)
    val isHindi: StateFlow<Boolean> = _isHindi.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Search & Filter State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSkill = MutableStateFlow<String?>(null)
    val selectedSkill: StateFlow<String?> = _selectedSkill.asStateFlow()

    private val _selectedState = MutableStateFlow<String?>(null)
    val selectedState: StateFlow<String?> = _selectedState.asStateFlow()

    private val _selectedDistrict = MutableStateFlow<String?>(null)
    val selectedDistrict: StateFlow<String?> = _selectedDistrict.asStateFlow()

    private val _selectedCity = MutableStateFlow<String?>(null)
    val selectedCity: StateFlow<String?> = _selectedCity.asStateFlow()

    private val _maxPrice = MutableStateFlow(2000f)
    val maxPrice: StateFlow<Float> = _maxPrice.asStateFlow()

    private val _minExperience = MutableStateFlow(0)
    val minExperience: StateFlow<Int> = _minExperience.asStateFlow()

    // Filtered Workers list
    val filteredMistris: StateFlow<List<UserEntity>> = combine(
        allMistris, searchQuery, selectedSkill, selectedState, maxPrice
    ) { list, query, skill, state, price ->
        val city = selectedCity.value
        val exp = minExperience.value
        list.filter { user ->
            val matchQuery = query.isEmpty() ||
                    user.fullName.contains(query, ignoreCase = true) ||
                    user.skills.contains(query, ignoreCase = true) ||
                    user.city.contains(query, ignoreCase = true)
            val matchSkill = skill == null || user.skills.contains(skill, ignoreCase = true)
            val matchState = state == null || user.state.contains(state, ignoreCase = true)
            val matchCity = city == null || user.city.contains(city, ignoreCase = true)
            val matchPrice = user.dailyWage <= price
            val matchExp = user.experienceYears >= exp

            matchQuery && matchSkill && matchState && matchCity && matchPrice && matchExp
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Favourites
    val favouriteIds: StateFlow<List<Int>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getFavourites(user.id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Received applications for Mistry / Posted jobs applications for Employer
    val mistryApplications: StateFlow<List<JobApplicationEntity>> = currentUser.flatMapLatest { user ->
        if (user != null && user.userType == "MISTRY") repository.getApplicationsByMistry(user.id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val employerApplications: StateFlow<List<JobApplicationEntity>> = currentUser.flatMapLatest { user ->
        if (user != null && user.userType == "EMPLOYER") repository.getApplicationsByEmployer(user.id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun toggleLanguage() {
        _isHindi.value = !_isHindi.value
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setSelectedSkill(skill: String?) { _selectedSkill.value = skill }
    fun setSelectedState(state: String?) { _selectedState.value = state }
    fun setSelectedDistrict(district: String?) { _selectedDistrict.value = district }
    fun setSelectedCity(city: String?) { _selectedCity.value = city }
    fun setMaxPrice(price: Float) { _maxPrice.value = price }
    fun setMinExperience(exp: Int) { _minExperience.value = exp }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedSkill.value = null
        _selectedState.value = null
        _selectedDistrict.value = null
        _selectedCity.value = null
        _maxPrice.value = 2000f
        _minExperience.value = 0
    }

    // Login simulation
    fun loginWithMobile(mobile: String, onSuccess: (UserEntity) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByMobile(mobile)
            if (user != null) {
                _currentUser.value = user
                onSuccess(user)
            } else {
                onError("इस मोबाइल नंबर से कोई यूजर नहीं मिला। (No user found with this mobile)")
            }
        }
    }

    fun registerUser(user: UserEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            val id = repository.registerUser(user)
            val registered = repository.getUserById(id.toInt())
            _currentUser.value = registered
            onComplete()
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun updateOnlineStatus(isOnline: Boolean) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateOnlineStatus(user.id, isOnline)
            _currentUser.value = user.copy(isOnline = isOnline)
        }
    }

    fun updateDailyWage(wage: Double) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateDailyWage(user.id, wage)
            _currentUser.value = user.copy(dailyWage = wage)
        }
    }

    fun updateToolsAndEquipment(tools: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateToolsAndEquipment(user.id, tools)
            _currentUser.value = user.copy(toolsAndEquipment = tools)
        }
    }

    fun getWorkHistoriesForWorker(workerId: Int): Flow<List<WorkHistoryEntity>> {
        return repository.getWorkHistoriesForWorker(workerId)
    }

    fun addWorkHistory(
        title: String,
        description: String,
        location: String,
        duration: String,
        photoUris: String,
        onComplete: () -> Unit
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val history = WorkHistoryEntity(
                workerId = user.id,
                title = title,
                description = description,
                location = location,
                duration = duration,
                photoUris = photoUris
            )
            repository.addWorkHistory(history)
            onComplete()
        }
    }

    fun deleteWorkHistory(historyId: Int) {
        viewModelScope.launch {
            repository.deleteWorkHistory(historyId)
        }
    }

    fun postJob(job: JobPostEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.postJob(job)
            onComplete()
        }
    }

    fun applyForJob(job: JobPostEntity, notes: String, onComplete: () -> Unit) {
        val mistry = _currentUser.value ?: return
        viewModelScope.launch {
            val application = JobApplicationEntity(
                jobId = job.id,
                mistryId = mistry.id,
                employerId = job.employerId,
                jobTitle = job.jobTitle,
                mistryName = mistry.fullName,
                mistryPhone = mistry.mobile,
                mistrySkill = mistry.skills.split(",").firstOrNull() ?: mistry.skills,
                dailyWage = mistry.dailyWage,
                notes = notes
            )
            repository.applyForJob(application)
            onComplete()
        }
    }

    fun hireWorkerDirectly(mistry: UserEntity, jobTitle: String, wage: Double, onComplete: () -> Unit) {
        val employer = _currentUser.value ?: return
        viewModelScope.launch {
            val application = JobApplicationEntity(
                jobId = 0,
                mistryId = mistry.id,
                employerId = employer.id,
                jobTitle = jobTitle,
                mistryName = mistry.fullName,
                mistryPhone = mistry.mobile,
                mistrySkill = mistry.skills.split(",").firstOrNull() ?: mistry.skills,
                dailyWage = wage,
                status = "HIRED",
                notes = "डायरेक्ट हायर (Direct Hire by Employer)"
            )
            repository.applyForJob(application)
            onComplete()
        }
    }

    fun updateApplicationStatus(appId: Int, status: String) {
        viewModelScope.launch {
            repository.updateApplicationStatus(appId, status)
        }
    }

    fun toggleFavourite(mistryId: Int) {
        val employer = _currentUser.value ?: return
        viewModelScope.launch {
            if (favouriteIds.value.contains(mistryId)) {
                repository.removeFavourite(employer.id, mistryId)
            } else {
                repository.addFavourite(employer.id, mistryId)
            }
        }
    }

    fun addReview(mistryId: Int, rating: Float, comment: String, reviewerName: String) {
        viewModelScope.launch {
            repository.addReview(ReviewEntity(mistryId = mistryId, reviewerName = reviewerName, rating = rating, comment = comment))
        }
    }

    // Admin Actions
    fun toggleUserVerification(userId: Int, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.updateVerificationStatus(userId, !currentStatus)
        }
    }

    fun toggleUserBlock(userId: Int, currentBlockStatus: Boolean) {
        viewModelScope.launch {
            repository.updateBlockStatus(userId, !currentBlockStatus)
        }
    }

    fun deleteJob(jobId: Int) {
        viewModelScope.launch {
            repository.deleteJob(jobId)
        }
    }
}
