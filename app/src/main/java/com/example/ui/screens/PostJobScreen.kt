package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.JobPostEntity
import com.example.data.local.UserEntity
import com.example.data.model.SkillCategories
import com.example.ui.components.InteractiveLocationPicker
import com.example.ui.theme.OrangePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostJobScreen(
    isHindi: Boolean,
    currentUser: UserEntity?,
    onJobSubmit: (JobPostEntity) -> Unit
) {
    var jobTitle by remember { mutableStateOf("") }
    var selectedSkill by remember { mutableStateOf("इलेक्ट्रिशियन") }
    var workersNeeded by remember { mutableStateOf("3") }
    var wageAmount by remember { mutableStateOf("850") }
    var wageType by remember { mutableStateOf("PER_DAY") } // "PER_DAY", "PER_HOUR", "CONTRACT"
    var hasAccommodation by remember { mutableStateOf(true) } // रहने की सुविधा
    var hasFood by remember { mutableStateOf(true) } // खाने की सुविधा
    var durationDays by remember { mutableStateOf("10") }
    var startDate by remember { mutableStateOf("तुरंत (Immediately)") }
    var city by remember { mutableStateOf(currentUser?.city ?: "लखनऊ") }
    var district by remember { mutableStateOf(currentUser?.district ?: "लखनऊ") }
    var state by remember { mutableStateOf(currentUser?.state ?: "उत्तर प्रदेश") }
    var siteLocation by remember { mutableStateOf("हजरतगंज, लखनऊ") }
    var mapLocationPin by remember { mutableStateOf("26.8467° N, 80.9462° E") }
    var additionalInfo by remember { mutableStateOf("") }

    var isDropdownExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isHindi) "नया काम पोस्ट करें" else "Post a New Job",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = OrangePrimary
        )

        OutlinedTextField(
            value = jobTitle,
            onValueChange = { jobTitle = it },
            label = { Text(if (isHindi) "काम का नाम / विवरण *" else "Job Title / Headline *") },
            placeholder = { Text(if (isHindi) "उदा. विला प्रोजेक्ट के लिए 3 मेसन चाहिए" else "e.g. Need 3 Masons for Villa project") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("post_job_title_input")
        )

        // Category Dropdown Selection
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedSkill,
                onValueChange = {},
                readOnly = true,
                label = { Text(if (isHindi) "किस प्रकार का मिस्त्री चाहिए *" else "Required Worker Skill *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("post_job_skill_dropdown")
            )

            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                SkillCategories.ALL_SKILLS.forEach { skill ->
                    DropdownMenuItem(
                        text = { Text(if (isHindi) skill.hindiName else skill.englishName) },
                        onClick = {
                            selectedSkill = skill.hindiName
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = workersNeeded,
                onValueChange = { workersNeeded = it },
                label = { Text(if (isHindi) "कितने मिस्त्री *" else "Workers Needed *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .testTag("post_job_workers_count_input")
            )

            OutlinedTextField(
                value = wageAmount,
                onValueChange = { wageAmount = it },
                label = { Text(if (isHindi) "मजदूरी ₹ *" else "Wage ₹ *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .testTag("post_job_wage_input")
            )
        }

        // Wage Type Selection
        Text(
            text = if (isHindi) "मजदूरी का प्रकार:" else "Wage Type:",
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = wageType == "PER_DAY",
                onClick = { wageType = "PER_DAY" },
                label = { Text(if (isHindi) "प्रति दिन" else "Per Day") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = wageType == "PER_HOUR",
                onClick = { wageType = "PER_HOUR" },
                label = { Text(if (isHindi) "प्रति घंटा" else "Per Hour") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = wageType == "CONTRACT",
                onClick = { wageType = "CONTRACT" },
                label = { Text(if (isHindi) "कॉन्ट्रैक्ट" else "Contract") },
                modifier = Modifier.weight(1f)
            )
        }

        // Facilities Checkboxes (रहने/खाने की सुविधा)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (isHindi) "सुविधाएं (Facilities):" else "Facilities Provided:",
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hasAccommodation,
                        onCheckedChange = { hasAccommodation = it }
                    )
                    Text(if (isHindi) "रहने की सुविधा उपलब्ध है" else "Accommodation Provided")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hasFood,
                        onCheckedChange = { hasFood = it }
                    )
                    Text(if (isHindi) "खाने की सुविधा उपलब्ध है" else "Food Provided")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = durationDays,
                onValueChange = { durationDays = it },
                label = { Text(if (isHindi) "कितने दिन का काम है" else "Duration (Days)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = { Text(if (isHindi) "काम शुरू होने की तारीख" else "Start Date") },
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = if (isHindi) "साइट Location details" else "Site Location Details",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        OutlinedTextField(
            value = siteLocation,
            onValueChange = { siteLocation = it },
            label = { Text(if (isHindi) "साइट का पता *" else "Site Address *") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text(if (isHindi) "शहर" else "City") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = state,
                onValueChange = { state = it },
                label = { Text(if (isHindi) "राज्य" else "State") },
                modifier = Modifier.weight(1f)
            )
        }

        InteractiveLocationPicker(
            currentLocation = mapLocationPin,
            isHindi = isHindi,
            onLocationSelected = { mapLocationPin = it }
        )

        OutlinedTextField(
            value = additionalInfo,
            onValueChange = { additionalInfo = it },
            label = { Text(if (isHindi) "अतिरिक्त जानकारी / निर्देश" else "Additional Info / Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        )

        Button(
            onClick = {
                val newJob = JobPostEntity(
                    employerId = currentUser?.id ?: 99,
                    employerName = currentUser?.fullName ?: "Employer",
                    employerPhone = currentUser?.mobile ?: "9988776655",
                    jobTitle = jobTitle.ifEmpty { "प्रोजेक्ट के लिए $selectedSkill की आवश्यकता" },
                    requiredSkill = selectedSkill,
                    workersNeeded = workersNeeded.toIntOrNull() ?: 1,
                    wageAmount = wageAmount.toDoubleOrNull() ?: 800.0,
                    wageType = wageType,
                    hasAccommodation = hasAccommodation,
                    hasFood = hasFood,
                    durationDays = durationDays.toIntOrNull() ?: 5,
                    startDate = startDate,
                    city = city,
                    district = district,
                    state = state,
                    siteLocation = siteLocation,
                    mapLocationPin = mapLocationPin,
                    additionalInfo = additionalInfo
                )
                onJobSubmit(newJob)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_job_post_button"),
            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
        ) {
            Text(
                text = if (isHindi) "काम प्रकाशित करें (Post Job)" else "Post Job Now",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
