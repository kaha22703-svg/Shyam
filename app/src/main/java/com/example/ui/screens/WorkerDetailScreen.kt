package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ReviewEntity
import com.example.data.local.UserEntity
import com.example.data.local.WorkHistoryEntity
import com.example.ui.components.RatingReviewDialog
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.StarYellow
import com.example.ui.theme.VerifiedGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WorkerDetailScreen(
    worker: UserEntity,
    reviews: List<ReviewEntity>,
    workHistories: List<WorkHistoryEntity> = emptyList(),
    isHindi: Boolean,
    onHireSubmit: (UserEntity, String, Double) -> Unit,
    onAddReview: (Float, String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var showHireDialog by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var hireJobTitle by remember { mutableStateOf("घर निर्माण कार्य के लिए") }
    var hireWage by remember { mutableStateOf("${worker.dailyWage.toInt()}") }

    val scrollState = rememberScrollState()

    if (showReviewDialog) {
        RatingReviewDialog(
            mistryName = worker.fullName,
            isHindi = isHindi,
            onDismiss = { showReviewDialog = false },
            onSubmit = { rating, comment ->
                onAddReview(rating, comment)
                showReviewDialog = false
            }
        )
    }

    if (showHireDialog) {
        AlertDialog(
            onDismissRequest = { showHireDialog = false },
            title = { Text(if (isHindi) "${worker.fullName} को काम दें" else "Hire ${worker.fullName}") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = hireJobTitle,
                        onValueChange = { hireJobTitle = it },
                        label = { Text(if (isHindi) "काम का शीर्षक / विवरण" else "Job Title / Details") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = hireWage,
                        onValueChange = { hireWage = it },
                        label = { Text(if (isHindi) "दैनिक मजदूरी (₹)" else "Daily Wage (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val wage = hireWage.toDoubleOrNull() ?: worker.dailyWage
                        onHireSubmit(worker, hireJobTitle, wage)
                        showHireDialog = false
                    },
                    modifier = Modifier.testTag("confirm_hire_submit_button")
                ) {
                    Text(if (isHindi) "हायर अनुरोध भेजें" else "Send Hire Request")
                }
            },
            dismissButton = {
                TextButton(onClick = { showHireDialog = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = worker.fullName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(OrangePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = worker.fullName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = worker.fullName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        if (worker.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Verified, contentDescription = "Verified", tint = VerifiedGreen)
                        }
                    }

                    Text(text = worker.skills, color = OrangePrimary, fontWeight = FontWeight.SemiBold)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Rating", tint = StarYellow, modifier = Modifier.size(18.dp))
                            Text(text = "${worker.rating} (${worker.totalReviews})", fontWeight = FontWeight.Bold)
                        }
                        Text(text = "•")
                        Text(text = "${worker.experienceYears} ${if (isHindi) "वर्ष अनुभव" else "Yrs Exp"}")
                    }
                }
            }

            // Wages & Rates Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "₹${worker.dailyWage.toInt()}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = if (isHindi) "प्रति दिन" else "Per Day", fontSize = 11.sp)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "₹${worker.hourlyRate.toInt()}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = if (isHindi) "प्रति घंटा" else "Per Hour", fontSize = 11.sp)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "₹${worker.monthlySalary.toInt()}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = if (isHindi) "प्रति माह" else "Monthly", fontSize = 11.sp)
                    }
                }
            }

            // Location & Radius
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = OrangePrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "${worker.city}, ${worker.district}, ${worker.state}", fontWeight = FontWeight.Bold)
                    }
                    Text(text = "GPS Pin: ${worker.liveLocation}", fontSize = 12.sp)
                    Text(text = "कार्य क्षेत्र त्रिज्या: ${worker.workingRadiusKm} km तक", fontSize = 12.sp, color = OrangePrimary)
                }
            }

            // Bio
            Text(text = if (isHindi) "कार्य अनुभव व विवरण (Bio)" else "Bio & Experience", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = worker.bio, style = MaterialTheme.typography.bodyMedium)

            // --- TOOLS & EQUIPMENT SECTION ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Build, contentDescription = null, tint = OrangePrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "उपलब्ध औजार व उपकरण (Tools & Equipment)" else "Tools & Equipment Owned",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    if (worker.toolsAndEquipment.isNotBlank()) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            worker.toolsAndEquipment.split(",").forEach { tool ->
                                if (tool.isNotBlank()) {
                                    AssistChip(
                                        onClick = { },
                                        label = { Text(tool.trim(), fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                                        leadingIcon = { Icon(Icons.Default.Construction, contentDescription = null, modifier = Modifier.size(14.dp), tint = OrangePrimary) }
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = if (isHindi) "सामान्य बुनियादी औजार उपलब्ध हैं।" else "Basic tools available.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // --- WORK HISTORY / PAST PROJECTS SECTION ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Engineering, contentDescription = null, tint = BluePrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "कार्य इतिहास एवं पिछले प्रोजेक्ट (Work History)" else "Work History & Past Projects",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    if (workHistories.isEmpty()) {
                        Text(
                            text = if (isHindi) "अभी तक कोई पूर्व प्रोजेक्ट फोटो दर्ज नहीं है।" else "No past project photos added yet.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        workHistories.forEach { project ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = project.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

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

                                    // Project Photo Badges
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        listOf("प्रोजेक्ट फोटो 1", "प्रोजेक्ट फोटो 2").forEach { photoTag ->
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = OrangePrimary.copy(alpha = 0.15f),
                                                modifier = Modifier.padding(top = 2.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(Icons.Default.Image, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(12.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(photoTag, fontSize = 10.sp, color = OrangePrimary, fontWeight = FontWeight.Bold)
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

            // Direct Call, WhatsApp & Hire Action Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${worker.mobile}"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        .testTag("call_worker_button")
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call", tint = OrangePrimary)
                }

                IconButton(
                    onClick = {
                        val url = "https://api.whatsapp.com/send?phone=+91${worker.mobile}&text=${Uri.encode("नमस्ते ${worker.fullName}, मिस्त्री कनेक्ट ऐप से आपका संपर्क नंबर मिला है।")}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFF25D366).copy(alpha = 0.2f), CircleShape)
                        .testTag("whatsapp_worker_button")
                ) {
                    Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color(0xFF128C7E))
                }

                Button(
                    onClick = { showHireDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("hire_worker_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text(text = if (isHindi) "काम दें (Hire Worker)" else "Hire Worker", fontWeight = FontWeight.Bold)
                }
            }

            Divider()

            // Reviews Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if (isHindi) "समीक्षाएं (Reviews)" else "Reviews & Ratings", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextButton(onClick = { showReviewDialog = true }) {
                    Text(text = if (isHindi) "+ समीक्षा लिखें" else "+ Write Review", color = OrangePrimary)
                }
            }

            reviews.forEach { rev ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = rev.reviewerName, fontWeight = FontWeight.Bold)
                            Row {
                                Icon(Icons.Default.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(16.dp))
                                Text(text = "${rev.rating}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                        Text(text = rev.comment, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
