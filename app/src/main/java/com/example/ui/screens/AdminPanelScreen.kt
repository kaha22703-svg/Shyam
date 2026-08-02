package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.JobPostEntity
import com.example.data.local.UserEntity
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.VerifiedGreen

@Composable
fun AdminPanelScreen(
    isHindi: Boolean,
    users: List<UserEntity>,
    jobs: List<JobPostEntity>,
    onToggleVerification: (Int, Boolean) -> Unit,
    onToggleBlock: (Int, Boolean) -> Unit,
    onDeleteJob: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableIntStateOf(0) }

    val totalMistris = users.count { it.userType == "MISTRY" }
    val totalEmployers = users.count { it.userType == "EMPLOYER" }
    val verifiedCount = users.count { it.isVerified }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isHindi) "एडमिन कंट्रोल पैनल (Admin Panel)" else "Admin Control Panel",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = OrangePrimary
        )

        // Analytics Overview Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$totalMistris", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = if (isHindi) "कुल मिस्त्री" else "Total Mistry", fontSize = 11.sp)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$totalEmployers", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = if (isHindi) "काम देने वाले" else "Employers", fontSize = 11.sp)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${jobs.size}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = if (isHindi) "सक्रिय काम" else "Active Jobs", fontSize = 11.sp)
                }
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(if (isHindi) "यूजर्स प्रबंधित करें" else "Users (${users.size})") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(if (isHindi) "काम प्रबंधित करें" else "Jobs (${jobs.size})") }
            )
        }

        if (selectedTab == 0) {
            // Users Management List
            users.forEach { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_user_card_${user.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${user.fullName} (${user.userType})", fontWeight = FontWeight.Bold)
                            if (user.isBlocked) {
                                Surface(color = MaterialTheme.colorScheme.error, shape = RoundedCornerShape(4.dp)) {
                                    Text(
                                        text = if (isHindi) "ब्लॉक है" else "Blocked",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }

                        Text(text = "फ़ोन: ${user.mobile} | शहर: ${user.city}", fontSize = 12.sp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { onToggleVerification(user.id, user.isVerified) }) {
                                Text(
                                    text = if (user.isVerified)
                                        (if (isHindi) "वेरीफाइड ✓" else "Verified ✓")
                                    else
                                        (if (isHindi) "+ वेरीफाई करें" else "+ Verify Profile"),
                                    color = if (user.isVerified) VerifiedGreen else OrangePrimary,
                                    fontSize = 12.sp
                                )
                            }

                            TextButton(onClick = { onToggleBlock(user.id, user.isBlocked) }) {
                                Text(
                                    text = if (user.isBlocked)
                                        (if (isHindi) "अनब्लॉक करें" else "Unblock")
                                    else
                                        (if (isHindi) "ब्लॉक करें" else "Block"),
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Jobs Management List
            jobs.forEach { job ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = job.jobTitle, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text(text = "₹${job.wageAmount.toInt()}", color = OrangePrimary, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "काम देने वाला: ${job.employerName} (${job.employerPhone})", fontSize = 12.sp)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = { onDeleteJob(job.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
