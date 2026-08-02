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
import com.example.data.local.JobApplicationEntity
import com.example.data.local.JobPostEntity
import com.example.data.local.UserEntity
import com.example.ui.theme.OrangePrimary

@Composable
fun EmployerDashboardScreen(
    isHindi: Boolean,
    currentUser: UserEntity?,
    postedJobs: List<JobPostEntity>,
    applicants: List<JobApplicationEntity>,
    favouriteWorkers: List<UserEntity>,
    onPostJobClick: () -> Unit,
    onCancelJob: (Int) -> Unit,
    onHireApplicant: (Int) -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Employer Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser.fullName.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = currentUser.fullName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    if (currentUser.companyName.isNotEmpty()) {
                        Text(text = currentUser.companyName, color = OrangePrimary, fontSize = 13.sp)
                    }
                    Text(text = "${currentUser.city}, ${currentUser.state}", style = MaterialTheme.typography.bodySmall)
                }

                IconButton(onClick = onLogoutClick) {
                    Icon(Icons.Default.Logout, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error)
                }
            }
        }

        // Post Job Button CTA
        Button(
            onClick = onPostJobClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("employer_post_job_button"),
            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
        ) {
            Icon(Icons.Default.AddCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isHindi) "+ नया काम पोस्ट करें" else "+ Post New Job",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Posted Jobs Section
        Text(
            text = if (isHindi) "मेरे पोस्ट किए गए काम (${postedJobs.size})" else "My Posted Jobs (${postedJobs.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        if (postedJobs.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = if (isHindi) "आपने अभी तक कोई काम पोस्ट नहीं किया है।" else "No jobs posted yet.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            postedJobs.forEach { job ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
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
                            Text(text = "₹${job.wageAmount.toInt()}/दिन", color = OrangePrimary, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "आवश्यक: ${job.requiredSkill} (${job.workersNeeded} मिस्त्री)", fontSize = 12.sp)
                        Text(text = "स्थान: ${job.siteLocation}", fontSize = 12.sp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { onCancelJob(job.id) },
                                colors = ButtonButtonColorsDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text(if (isHindi) "काम रद्द करें" else "Cancel Job")
                            }
                        }
                    }
                }
            }
        }

        // Applicants Section
        Text(
            text = if (isHindi) "आवेदक मिस्त्री (Applicants / Candidates)" else "Job Applicants",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        if (applicants.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = if (isHindi) "अभी कोई नया आवेदन नहीं है।" else "No applications yet.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            applicants.forEach { app ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = app.mistryName, fontWeight = FontWeight.Bold)
                            Text(text = "कौशल: ${app.mistrySkill}", fontSize = 12.sp, color = OrangePrimary)
                            Text(text = "मांगी गई मजदूरी: ₹${app.dailyWage.toInt()}", fontSize = 12.sp)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${app.mistryPhone}"))
                                context.startActivity(intent)
                            }) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = OrangePrimary)
                            }

                            Button(
                                onClick = { onHireApplicant(app.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                            ) {
                                Text(if (isHindi) "हायर करें" else "Hire")
                            }
                        }
                    }
                }
            }
        }
    }
}

object ButtonButtonColorsDefaults {
    @Composable
    fun textButtonColors(contentColor: Color) = ButtonDefaults.textButtonColors(contentColor = contentColor)
}
