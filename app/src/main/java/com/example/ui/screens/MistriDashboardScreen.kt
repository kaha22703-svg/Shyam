package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.JobApplicationEntity
import com.example.data.local.UserEntity
import com.example.data.local.WorkHistoryEntity
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.StarYellow
import com.example.ui.theme.VerifiedGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MistriDashboardScreen(
    isHindi: Boolean,
    currentUser: UserEntity?,
    applications: List<JobApplicationEntity>,
    workHistories: List<WorkHistoryEntity> = emptyList(),
    onOnlineToggle: (Boolean) -> Unit,
    onWageUpdate: (Double) -> Unit,
    onToolsUpdate: (String) -> Unit = {},
    onAddWorkHistory: (title: String, description: String, location: String, duration: String, photoUris: String) -> Unit = { _, _, _, _, _ -> },
    onDeleteWorkHistory: (Int) -> Unit = {},
    onAppStatusChange: (Int, String) -> Unit,
    onLogoutClick: () -> Unit
) {
    if (currentUser == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(48.dp), tint = OrangePrimary)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isHindi) "डैशबोर्ड देखने के लिए लॉगिन करें" else "Please login to view dashboard",
                fontWeight = FontWeight.Bold
            )
        }
        return
    }

    var editWageText by remember { mutableStateOf("${currentUser.dailyWage.toInt()}") }
    var showEditWageDialog by remember { mutableStateOf(false) }

    var editToolsText by remember { mutableStateOf(currentUser.toolsAndEquipment) }
    var showEditToolsDialog by remember { mutableStateOf(false) }

    var showAddWorkHistoryDialog by remember { mutableStateOf(false) }
    var projectTitle by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var projectLocation by remember { mutableStateOf("${currentUser.city}, ${currentUser.state}") }
    var projectDuration by remember { mutableStateOf("") }
    var projectPhotoCount by remember { mutableStateOf(2) } // Simulated photo selection

    val scrollState = rememberScrollState()

    if (showEditWageDialog) {
        AlertDialog(
            onDismissRequest = { showEditWageDialog = false },
            title = { Text(if (isHindi) "अपनी दैनिक मजदूरी बदलें" else "Change Daily Wage") },
            text = {
                OutlinedTextField(
                    value = editWageText,
                    onValueChange = { editWageText = it },
                    label = { Text(if (isHindi) "एक दिन की मजदूरी (₹)" else "Daily Wage (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    val newWage = editWageText.toDoubleOrNull() ?: currentUser.dailyWage
                    onWageUpdate(newWage)
                    showEditWageDialog = false
                }) {
                    Text(if (isHindi) "अपडेट करें" else "Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditWageDialog = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }

    if (showEditToolsDialog) {
        AlertDialog(
            onDismissRequest = { showEditToolsDialog = false },
            title = { Text(if (isHindi) "उपलब्ध औजार व उपकरण दर्ज करें" else "Update Tools & Equipment") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isHindi) "आपके पास कौन-कौन से औजार और मशीनें उपलब्ध हैं?" else "List tools and equipment you possess:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    val suggestedTools = listOf(
                        "अपनी सीढ़ी (Ladder)",
                        "पावर ड्रिल मशीन (Power Drill)",
                        "वेल्डिंग मशीन (Welding Machine)",
                        "मार्बल कटर (Tile Cutter)",
                        "सुरक्षा किट (Safety Gear)"
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        suggestedTools.forEach { tool ->
                            val isSelected = editToolsText.contains(tool)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        editToolsText = editToolsText.split(", ")
                                            .filter { it != tool && it.isNotBlank() }
                                            .joinToString(", ")
                                    } else {
                                        editToolsText = if (editToolsText.isBlank()) tool else "$editToolsText, $tool"
                                    }
                                },
                                label = { Text(text = tool, fontSize = 10.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = editToolsText,
                        onValueChange = { editToolsText = it },
                        label = { Text(if (isHindi) "औजार एवं मशीनें" else "Tools & Equipment List") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Build, contentDescription = null) }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onToolsUpdate(editToolsText)
                        showEditToolsDialog = false
                    },
                    modifier = Modifier.testTag("save_tools_button")
                ) {
                    Text(if (isHindi) "सेव करें" else "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditToolsDialog = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }

    if (showAddWorkHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showAddWorkHistoryDialog = false },
            title = { Text(if (isHindi) "नया पिछला काम / प्रोजेक्ट जोड़ें" else "Add Past Work History Project") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = projectTitle,
                        onValueChange = { projectTitle = it },
                        label = { Text(if (isHindi) "प्रोजेक्ट का शीर्षक *" else "Project Title *") },
                        placeholder = { Text("e.g. 3 BHK Flat House Wiring") },
                        modifier = Modifier.fillMaxWidth().testTag("project_title_input")
                    )

                    OutlinedTextField(
                        value = projectDescription,
                        onValueChange = { projectDescription = it },
                        label = { Text(if (isHindi) "किए गए काम का विवरण *" else "Brief Work Description *") },
                        placeholder = { Text("e.g. Completed concealed piping, switch boards, and ceiling lights.") },
                        modifier = Modifier.fillMaxWidth().testTag("project_description_input")
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = projectLocation,
                            onValueChange = { projectLocation = it },
                            label = { Text(if (isHindi) "स्थान / शहर *" else "Location *") },
                            modifier = Modifier.weight(1f).testTag("project_location_input")
                        )

                        OutlinedTextField(
                            value = projectDuration,
                            onValueChange = { projectDuration = it },
                            label = { Text(if (isHindi) "समय / अवधि *" else "Duration *") },
                            placeholder = { Text("e.g. 15 Days") },
                            modifier = Modifier.weight(1f).testTag("project_duration_input")
                        )
                    }

                    // Photo Selector Simulation
                    Text(
                        text = if (isHindi) "प्रोजेक्ट की तस्वीरें अपलोड करें" else "Upload Project Photos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OrangePrimary
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { projectPhotoCount = (projectPhotoCount + 1).coerceAtMost(5) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (isHindi) "+ फोटो चुनें ($projectPhotoCount)" else "+ Select Photos ($projectPhotoCount)", fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (projectTitle.isNotBlank() && projectDescription.isNotBlank()) {
                            val dummyPhotos = "photo_1,photo_2"
                            onAddWorkHistory(
                                projectTitle,
                                projectDescription,
                                projectLocation.ifBlank { "${currentUser.city}, ${currentUser.state}" },
                                projectDuration.ifBlank { "10 Days" },
                                dummyPhotos
                            )
                            projectTitle = ""
                            projectDescription = ""
                            projectDuration = ""
                            showAddWorkHistoryDialog = false
                        }
                    },
                    modifier = Modifier.testTag("confirm_add_work_history_button")
                ) {
                    Text(if (isHindi) "प्रोजेक्ट जोड़ें" else "Add Project")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddWorkHistoryDialog = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- PROFILE HEADER ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(OrangePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.fullName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentUser.fullName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            if (currentUser.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, contentDescription = "Verified", tint = VerifiedGreen, modifier = Modifier.size(18.dp))
                            }
                        }

                        Text(
                            text = currentUser.skills,
                            color = OrangePrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "${currentUser.city}, ${currentUser.state}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error)
                    }
                }

                Divider()

                // Online / Offline Availability Switcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentUser.isOnline)
                                (if (isHindi) "स्थिति: ऑनलाइन (काम के लिए उपलब्ध)" else "Status: Online (Available)")
                            else
                                (if (isHindi) "स्थिति: ऑफलाइन (व्यस्त)" else "Status: Offline (Busy)"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (currentUser.isOnline) VerifiedGreen else MaterialTheme.colorScheme.error
                        )
                    }

                    Switch(
                        checked = currentUser.isOnline,
                        onCheckedChange = onOnlineToggle,
                        modifier = Modifier.testTag("dashboard_online_switch")
                    )
                }
            }
        }

        // --- STATS & EARNINGS CARDS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showEditWageDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Payments, contentDescription = null, tint = OrangePrimary)
                    Text(
                        text = "₹${currentUser.dailyWage.toInt()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = if (isHindi) "दैनिक मजदूरी (बदलें)" else "Daily Wage (Edit)",
                        fontSize = 11.sp
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = StarYellow)
                        Text(
                            text = "${currentUser.rating}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Text(
                        text = "${currentUser.totalReviews} ${if (isHindi) "समीक्षाएं" else "Reviews"}",
                        fontSize = 11.sp
                    )
                }
            }
        }

        // --- TOOLS & EQUIPMENT SECTION ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Build, contentDescription = null, tint = OrangePrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "उपलब्ध औजार व उपकरण (Tools)" else "Tools & Equipment",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    TextButton(onClick = { showEditToolsDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isHindi) "बदलें" else "Edit")
                    }
                }

                if (currentUser.toolsAndEquipment.isNotBlank()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        currentUser.toolsAndEquipment.split(",").forEach { tool ->
                            if (tool.isNotBlank()) {
                                AssistChip(
                                    onClick = { },
                                    label = { Text(tool.trim(), fontSize = 12.sp) },
                                    leadingIcon = { Icon(Icons.Default.Construction, contentDescription = null, modifier = Modifier.size(14.dp), tint = OrangePrimary) }
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = if (isHindi) "कोई औजार दर्ज नहीं है। 'बदलें' पर क्लिक करके औजार जोड़ें।" else "No tools listed yet. Click 'Edit' to add.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // --- WORK HISTORY & PAST PROJECTS SECTION ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Engineering, contentDescription = null, tint = BluePrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHindi) "कार्य इतिहास एवं पुराने प्रोजेक्ट" else "Work History & Projects",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Text(
                            text = if (isHindi) "अपने पुराने काम की फोटो और जानकारी दिखाएं" else "Showcase your past completed work & photos",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showAddWorkHistoryDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("add_work_history_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isHindi) "+ जोड़ें" else "+ Add", fontSize = 12.sp)
                    }
                }

                if (workHistories.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi) "अभी तक कोई कार्य इतिहास दर्ज नहीं किया है।" else "No work history added yet.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (isHindi) "प्रोजेक्ट जोड़ने से काम मिलने की संभावना बढ़ती है!" else "Adding work history increases your hiring chances!",
                                fontSize = 11.sp,
                                color = OrangePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    workHistories.forEach { project ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = project.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    IconButton(
                                        onClick = { onDeleteWorkHistory(project.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(project.location, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Schedule, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(project.duration, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Text(
                                    text = project.description,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                // Photo Preview Badges
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    listOf("साइट फोटो 1", "साइट फोटो 2").forEach { photoLabel ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = OrangePrimary.copy(alpha = 0.15f),
                                            modifier = Modifier.padding(top = 2.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.Image, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(photoLabel, fontSize = 10.sp, color = OrangePrimary, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- JOB REQUESTS & ACCEPTED JOBS ---
        Text(
            text = if (isHindi) "प्राप्त काम के अनुरोध (Job Requests)" else "Job Requests Received",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        if (applications.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = if (isHindi) "अभी कोई काम का अनुरोध प्राप्त नहीं हुआ है।" else "No job requests received yet.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            applications.forEach { app ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = app.jobTitle, fontWeight = FontWeight.Bold)
                        Text(text = "मजदूरी: ₹${app.dailyWage.toInt()}", fontSize = 12.sp)
                        Text(text = "स्थिति: ${app.status}", fontSize = 12.sp, color = OrangePrimary, fontWeight = FontWeight.Bold)

                        if (app.status == "REQUESTED") {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { onAppStatusChange(app.id, "ACCEPTED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen)
                                ) {
                                    Text(if (isHindi) "स्वीकार करें" else "Accept")
                                }
                                OutlinedButton(
                                    onClick = { onAppStatusChange(app.id, "REJECTED") }
                                ) {
                                    Text(if (isHindi) "अस्वीकार करें" else "Reject")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
