package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.MistriBottomNavBar
import com.example.ui.components.MistriTopBar
import com.example.ui.screens.*
import com.example.ui.theme.MistriConnectTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
            val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
            val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

            val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
            val allMistris by viewModel.allMistris.collectAsStateWithLifecycle()
            val filteredMistris by viewModel.filteredMistris.collectAsStateWithLifecycle()
            val allJobs by viewModel.allJobs.collectAsStateWithLifecycle()

            val mistryApplications by viewModel.mistryApplications.collectAsStateWithLifecycle()
            val employerApplications by viewModel.employerApplications.collectAsStateWithLifecycle()

            val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
            val selectedSkill by viewModel.selectedSkill.collectAsStateWithLifecycle()
            val selectedState by viewModel.selectedState.collectAsStateWithLifecycle()
            val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
            val maxPrice by viewModel.maxPrice.collectAsStateWithLifecycle()
            val minExperience by viewModel.minExperience.collectAsStateWithLifecycle()

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: "home"
            val context = LocalContext.current

            MistriConnectTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        MistriTopBar(
                            isHindi = isHindi,
                            isDarkMode = isDarkMode,
                            currentUser = currentUser,
                            onLanguageToggle = { viewModel.toggleLanguage() },
                            onDarkModeToggle = { viewModel.toggleDarkMode() },
                            onAdminClick = { navController.navigate("admin") },
                            onProfileClick = { navController.navigate("dashboard") },
                            onLoginClick = { navController.navigate("login") }
                        )
                    },
                    bottomBar = {
                        // Show bottom nav on main tabs
                        if (currentRoute in listOf("home", "search", "post_job", "dashboard", "admin")) {
                            MistriBottomNavBar(
                                currentRoute = currentRoute,
                                isHindi = isHindi,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // --- HOME ---
                        composable("home") {
                            HomeScreen(
                                isHindi = isHindi,
                                currentUser = currentUser,
                                mistrisList = allMistris,
                                recentJobsList = allJobs,
                                onBecomeMistriClick = { navController.navigate("register?role=MISTRY") },
                                onPostJobClick = {
                                    if (currentUser == null) {
                                        navController.navigate("login")
                                    } else {
                                        navController.navigate("post_job")
                                    }
                                },
                                onLoginClick = { navController.navigate("login") },
                                onRegisterClick = { navController.navigate("register") },
                                onSelectSkill = { skill ->
                                    viewModel.setSelectedSkill(if (skill.isEmpty()) null else skill)
                                    navController.navigate("search")
                                },
                                onWorkerClick = { worker ->
                                    navController.navigate("worker_detail/${worker.id}")
                                },
                                onJobClick = { job ->
                                    Toast.makeText(
                                        context,
                                        if (isHindi) "${job.jobTitle} पर आवेदन करने के लिए लॉगिन करें" else "Login to apply for ${job.jobTitle}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }

                        // --- SEARCH ---
                        composable("search") {
                            SearchScreen(
                                isHindi = isHindi,
                                searchQuery = searchQuery,
                                selectedSkill = selectedSkill,
                                selectedState = selectedState,
                                selectedCity = selectedCity,
                                maxPrice = maxPrice,
                                minExperience = minExperience,
                                filteredMistris = filteredMistris,
                                onQueryChange = { viewModel.setSearchQuery(it) },
                                onSkillSelect = { viewModel.setSelectedSkill(it) },
                                onStateSelect = { viewModel.setSelectedState(it) },
                                onCitySelect = { viewModel.setSelectedCity(it) },
                                onMaxPriceChange = { viewModel.setMaxPrice(it) },
                                onMinExpChange = { viewModel.setMinExperience(it) },
                                onClearFilters = { viewModel.clearFilters() },
                                onWorkerClick = { worker ->
                                    navController.navigate("worker_detail/${worker.id}")
                                }
                            )
                        }

                        // --- POST JOB ---
                        composable("post_job") {
                            PostJobScreen(
                                isHindi = isHindi,
                                currentUser = currentUser,
                                onJobSubmit = { job ->
                                    viewModel.postJob(job) {
                                        Toast.makeText(
                                            context,
                                            if (isHindi) "काम सफलतापूर्वक पोस्ट हो गया!" else "Job posted successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate("dashboard")
                                    }
                                }
                            )
                        }

                        // --- DASHBOARD ---
                        composable("dashboard") {
                            val currentWorkHistories by viewModel.getWorkHistoriesForWorker(currentUser?.id ?: 0)
                                .collectAsStateWithLifecycle(initialValue = emptyList())

                            if (currentUser?.userType == "EMPLOYER") {
                                EmployerDashboardScreen(
                                    isHindi = isHindi,
                                    currentUser = currentUser,
                                    postedJobs = allJobs.filter { it.employerId == currentUser?.id },
                                    applicants = employerApplications,
                                    favouriteWorkers = emptyList(),
                                    onPostJobClick = { navController.navigate("post_job") },
                                    onCancelJob = { jobId -> viewModel.deleteJob(jobId) },
                                    onHireApplicant = { appId -> viewModel.updateApplicationStatus(appId, "HIRED") },
                                    onLogoutClick = {
                                        viewModel.logout()
                                        navController.navigate("home")
                                    }
                                )
                            } else {
                                MistriDashboardScreen(
                                    isHindi = isHindi,
                                    currentUser = currentUser,
                                    applications = mistryApplications,
                                    workHistories = currentWorkHistories,
                                    onOnlineToggle = { isOnline -> viewModel.updateOnlineStatus(isOnline) },
                                    onWageUpdate = { wage -> viewModel.updateDailyWage(wage) },
                                    onToolsUpdate = { tools -> viewModel.updateToolsAndEquipment(tools) },
                                    onAddWorkHistory = { title, desc, loc, dur, photos ->
                                        viewModel.addWorkHistory(title, desc, loc, dur, photos) {
                                            Toast.makeText(
                                                context,
                                                if (isHindi) "प्रोजेक्ट कार्य इतिहास जोड़ा गया!" else "Work history project added!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    onDeleteWorkHistory = { historyId -> viewModel.deleteWorkHistory(historyId) },
                                    onAppStatusChange = { appId, status -> viewModel.updateApplicationStatus(appId, status) },
                                    onLogoutClick = {
                                        viewModel.logout()
                                        navController.navigate("home")
                                    }
                                )
                            }
                        }

                        // --- ADMIN PANEL ---
                        composable("admin") {
                            AdminPanelScreen(
                                isHindi = isHindi,
                                users = allUsers,
                                jobs = allJobs,
                                onToggleVerification = { userId, status -> viewModel.toggleUserVerification(userId, status) },
                                onToggleBlock = { userId, status -> viewModel.toggleUserBlock(userId, status) },
                                onDeleteJob = { jobId -> viewModel.deleteJob(jobId) }
                            )
                        }

                        // --- LOGIN ---
                        composable("login") {
                            LoginScreen(
                                isHindi = isHindi,
                                onLoginSubmit = { mobile, password ->
                                    viewModel.loginWithMobile(
                                        mobile = mobile,
                                        onSuccess = { user ->
                                            Toast.makeText(
                                                context,
                                                if (isHindi) "स्वागत है ${user.fullName}!" else "Welcome ${user.fullName}!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate("dashboard")
                                        },
                                        onError = { err ->
                                            Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                onRegisterClick = { navController.navigate("register") }
                            )
                        }

                        // --- REGISTER ---
                        composable(
                            "register?role={role}",
                            arguments = listOf(navArgument("role") { defaultValue = "MISTRY" })
                        ) { backStack ->
                            val roleArg = backStack.arguments?.getString("role") ?: "MISTRY"
                            RegisterScreen(
                                isHindi = isHindi,
                                defaultRole = roleArg,
                                onRegisterSubmit = { user ->
                                    viewModel.registerUser(user) {
                                        Toast.makeText(
                                            context,
                                            if (isHindi) "रजिस्ट्रेशन सफल!" else "Registration Successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate("dashboard")
                                    }
                                },
                                onLoginClick = { navController.navigate("login") }
                            )
                        }

                        // --- WORKER DETAIL ---
                        composable(
                            "worker_detail/{workerId}",
                            arguments = listOf(navArgument("workerId") { type = NavType.IntType })
                        ) { backStack ->
                            val workerId = backStack.arguments?.getInt("workerId") ?: 0
                            val worker = allUsers.find { it.id == workerId }
                            val reviews by viewModel.repository.getReviewsForMistry(workerId)
                                .collectAsStateWithLifecycle(initialValue = emptyList())
                            val workerHistories by viewModel.getWorkHistoriesForWorker(workerId)
                                .collectAsStateWithLifecycle(initialValue = emptyList())

                            if (worker != null) {
                                WorkerDetailScreen(
                                    worker = worker,
                                    reviews = reviews,
                                    workHistories = workerHistories,
                                    isHindi = isHindi,
                                    onHireSubmit = { mistry, jobTitle, wage ->
                                        viewModel.hireWorkerDirectly(mistry, jobTitle, wage) {
                                            Toast.makeText(
                                                context,
                                                if (isHindi) "हायर अनुरोध सफलतापूर्वक भेजा गया!" else "Hire request sent successfully!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    onAddReview = { rating, comment ->
                                        val reviewer = currentUser?.fullName ?: "Employer"
                                        viewModel.addReview(worker.id, rating, comment, reviewer)
                                        Toast.makeText(
                                            context,
                                            if (isHindi) "समीक्षा सबमिट हो गई!" else "Review submitted!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
