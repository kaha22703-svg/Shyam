package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.data.model.SkillCategories
import com.example.ui.components.InteractiveLocationPicker
import com.example.ui.components.OtpVerificationDialog
import com.example.ui.theme.OrangePrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
    isHindi: Boolean,
    defaultRole: String = "MISTRY", // "MISTRY" or "EMPLOYER"
    onRegisterSubmit: (UserEntity) -> Unit,
    onLoginClick: () -> Unit
) {
    var userRole by remember { mutableStateOf(defaultRole) }

    // Common Fields
    var fullName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var isMobileVerified by remember { mutableStateOf(false) }
    var showOtpDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Location Fields
    var state by remember { mutableStateOf("उत्तर प्रदेश (Uttar Pradesh)") }
    var district by remember { mutableStateOf("लखनऊ") }
    var city by remember { mutableStateOf("लखनऊ") }
    var liveLocationPin by remember { mutableStateOf("26.8467° N, 80.9462° E - लखनऊ") }

    // Mistri Specific Fields
    var aadhaar by remember { mutableStateOf("") }
    var experienceYears by remember { mutableStateOf("5") }
    var dailyWage by remember { mutableStateOf("800") }
    var hourlyRate by remember { mutableStateOf("100") }
    var monthlySalary by remember { mutableStateOf("22000") }
    var workRadiusKm by remember { mutableStateOf("25") }
    var isAvailableOnline by remember { mutableStateOf(true) }
    var bio by remember { mutableStateOf("") }
    var toolsAndEquipment by remember { mutableStateOf("") }
    var bankUpi by remember { mutableStateOf("") }

    // 21 Skills Selected Checkboxes
    val selectedSkills = remember { mutableStateListOf<String>("इलेक्ट्रिशियन") }

    val scrollState = rememberScrollState()

    if (showOtpDialog) {
        OtpVerificationDialog(
            mobileNumber = mobile,
            isHindi = isHindi,
            onDismiss = { showOtpDialog = false },
            onVerified = {
                showOtpDialog = false
                isMobileVerified = true
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
        Text(
            text = if (isHindi) "नया अकाउंट रजिस्टर करें" else "Create New Account",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = OrangePrimary
        )

        // --- ROLE SELECTION BUTTONS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = userRole == "MISTRY",
                onClick = { userRole = "MISTRY" },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Engineering, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isHindi) "मैं मिस्त्री हूँ" else "I am a Mistry",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("role_mistry_chip")
            )

            FilterChip(
                selected = userRole == "EMPLOYER",
                onClick = { userRole = "EMPLOYER" },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Business, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isHindi) "मैं काम देने वाला हूँ" else "I am an Employer",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("role_employer_chip")
            )
        }

        // --- COMMON BASIC INFO ---
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(if (isHindi) "पूरा नाम *" else "Full Name *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("full_name_input")
        )

        if (userRole == "EMPLOYER") {
            OutlinedTextField(
                value = companyName,
                onValueChange = { companyName = it },
                label = { Text(if (isHindi) "कंपनी / ठेकेदार का नाम" else "Company / Business Name") },
                leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("company_name_input")
            )
        }

        // Mobile & OTP Verification
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = { Text(if (isHindi) "मोबाइल नंबर *" else "Mobile Number *") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .weight(1f)
                    .testTag("mobile_number_input")
            )

            Button(
                onClick = { showOtpDialog = true },
                enabled = mobile.length >= 10,
                modifier = Modifier.testTag("send_otp_button")
            ) {
                Text(
                    text = if (isMobileVerified)
                        (if (isHindi) "वेरीफाइड ✓" else "Verified ✓")
                    else
                        (if (isHindi) "OTP भेजें" else "Send OTP")
                )
            }
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(if (isHindi) "ईमेल (ऐच्छिक)" else "Email (Optional)") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("email_input")
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(if (isHindi) "पासवर्ड *" else "Password *") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password_input")
        )

        // --- LOCATION PICKER ---
        Text(
            text = if (isHindi) "लोकेशन विवरण" else "Location Details",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
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
                value = district,
                onValueChange = { district = it },
                label = { Text(if (isHindi) "जिला" else "District") },
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = state,
            onValueChange = { state = it },
            label = { Text(if (isHindi) "राज्य" else "State") },
            modifier = Modifier.fillMaxWidth()
        )

        InteractiveLocationPicker(
            currentLocation = liveLocationPin,
            isHindi = isHindi,
            onLocationSelected = { liveLocationPin = it }
        )

        // --- MISTRY FORM SPECIFICS ---
        if (userRole == "MISTRY") {
            Divider()

            Text(
                text = if (isHindi) "कौशल चुनें (कम से कम एक) *" else "Select Skills (Select at least 1) *",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = OrangePrimary
            )

            // 21 Skills Checkboxes Grid
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SkillCategories.ALL_SKILLS.forEach { skill ->
                    val isChecked = selectedSkills.contains(skill.hindiName)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                if (isChecked) selectedSkills.remove(skill.hindiName)
                                else selectedSkills.add(skill.hindiName)
                            }
                            .padding(4.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                if (checked) selectedSkills.add(skill.hindiName)
                                else selectedSkills.remove(skill.hindiName)
                            }
                        )
                        Text(
                            text = if (isHindi) skill.hindiName else skill.englishName,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Divider()

            Text(
                text = if (isHindi) "मजदूरी एवं अनुभव विवरण" else "Wages & Experience Details",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = OrangePrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = dailyWage,
                    onValueChange = { dailyWage = it },
                    label = { Text(if (isHindi) "1 दिन की मजदूरी ₹" else "Daily Wage ₹") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = hourlyRate,
                    onValueChange = { hourlyRate = it },
                    label = { Text(if (isHindi) "प्रति घंटा रेट ₹" else "Hourly Rate ₹") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = monthlySalary,
                    onValueChange = { monthlySalary = it },
                    label = { Text(if (isHindi) "मासिक सैलरी ₹" else "Monthly Salary ₹") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = experienceYears,
                    onValueChange = { experienceYears = it },
                    label = { Text(if (isHindi) "अनुभव (वर्ष)" else "Experience (Yrs)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = workRadiusKm,
                onValueChange = { workRadiusKm = it },
                label = { Text(if (isHindi) "कितनी दूरी तक काम करेंगे (km)" else "Working Radius (km)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isHindi) "अभी काम के लिए उपलब्ध हैं (Online)" else "Currently Available (Online)",
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = isAvailableOnline,
                    onCheckedChange = { isAvailableOnline = it },
                    modifier = Modifier.testTag("online_availability_switch")
                )
            }

            OutlinedTextField(
                value = aadhaar,
                onValueChange = { aadhaar = it },
                label = { Text(if (isHindi) "आधार कार्ड नंबर (ऐच्छिक)" else "Aadhaar Number (Optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text(if (isHindi) "बायोडाटा / काम का अनुभव" else "Bio / Work Experience") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- TOOLS & EQUIPMENT FIELD ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (isHindi) "उपलब्ध औजार व उपकरण (Tools & Equipment)" else "Available Tools & Equipment",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = OrangePrimary
                )
                Text(
                    text = if (isHindi)
                        "उदाहरण: सीढ़ी, हैवी पावर ड्रिल, वेल्डिंग मशीन, सुरक्षा किट"
                    else
                        "e.g. own ladder, proficient with welding machine, has own power drill",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Quick Select Chips
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val suggestedTools = listOf(
                        "अपनी सीढ़ी (Ladder)",
                        "पावर ड्रिल मशीन (Power Drill)",
                        "वेल्डिंग मशीन (Welding Machine)",
                        "मार्बल कटर (Tile Cutter)",
                        "सुरक्षा किट व हेलमेट (Safety Gear)",
                        "पाइप रेंच व टूलकिट (Toolkit)"
                    )
                    suggestedTools.forEach { tool ->
                        val isSelected = toolsAndEquipment.contains(tool)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    toolsAndEquipment = toolsAndEquipment.split(", ")
                                        .filter { it != tool && it.isNotBlank() }
                                        .joinToString(", ")
                                } else {
                                    toolsAndEquipment = if (toolsAndEquipment.isBlank()) tool else "$toolsAndEquipment, $tool"
                                }
                            },
                            label = { Text(text = tool, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = toolsAndEquipment,
                    onValueChange = { toolsAndEquipment = it },
                    label = { Text(if (isHindi) "औजारों की सूची दर्ज करें *" else "List Tools & Equipment *") },
                    placeholder = { Text(if (isHindi) "जैसे: ड्रिल मशीन, सीढ़ी, वेल्डिंग मशीन" else "e.g., power drill, ladder, welding kit") },
                    leadingIcon = { Icon(Icons.Default.Build, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tools_and_equipment_input")
                )
            }

            OutlinedTextField(
                value = bankUpi,
                onValueChange = { bankUpi = it },
                label = { Text(if (isHindi) "बैंक खाते / UPI आईडी" else "Bank / UPI Details") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val user = UserEntity(
                    userType = userRole,
                    fullName = fullName.ifEmpty { "User ${mobile.takeLast(4)}" },
                    companyName = companyName,
                    mobile = mobile.ifEmpty { "9876543210" },
                    email = email,
                    passwordHash = password,
                    aadhaarNumber = aadhaar,
                    experienceYears = experienceYears.toIntOrNull() ?: 5,
                    dailyWage = dailyWage.toDoubleOrNull() ?: 800.0,
                    hourlyRate = hourlyRate.toDoubleOrNull() ?: 100.0,
                    monthlySalary = monthlySalary.toDoubleOrNull() ?: 22000.0,
                    city = city.ifEmpty { "लखनऊ" },
                    district = district.ifEmpty { "लखनऊ" },
                    state = state.ifEmpty { "उत्तर प्रदेश" },
                    liveLocation = liveLocationPin,
                    workingRadiusKm = workRadiusKm.toIntOrNull() ?: 25,
                    isOnline = isAvailableOnline,
                    isVerified = true,
                    skills = selectedSkills.joinToString(", ").ifEmpty { "इलेक्ट्रिशियन" },
                    bio = bio.ifEmpty { "कुशल और विश्वसनीय कार्यकर्ता।" },
                    toolsAndEquipment = toolsAndEquipment.ifEmpty { "अपनी सीढ़ी (Ladder), पावर ड्रिल, टूल बॉक्स" },
                    bankUpiDetails = bankUpi
                )
                onRegisterSubmit(user)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_registration_button"),
            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
        ) {
            Text(
                text = if (isHindi) "रजिस्ट्रेशन पूरा करें" else "Complete Registration",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (isHindi) "पहले से अकाउंट है? " else "Already have an account? ")
            TextButton(onClick = onLoginClick) {
                Text(text = if (isHindi) "लॉगिन करें" else "Login", color = OrangePrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}
