package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
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
import com.example.ui.components.OtpVerificationDialog
import com.example.ui.theme.OrangePrimary

@Composable
fun LoginScreen(
    isHindi: Boolean,
    onLoginSubmit: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var mobile by remember { mutableStateOf("9876543210") }
    var password by remember { mutableStateOf("1234") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var showOtpDialog by remember { mutableStateOf(false) }

    if (showOtpDialog) {
        OtpVerificationDialog(
            mobileNumber = mobile,
            isHindi = isHindi,
            onDismiss = { showOtpDialog = false },
            onVerified = {
                showOtpDialog = false
                onLoginSubmit(mobile, password)
            }
        )
    }

    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = { Text(if (isHindi) "पासवर्ड भूल गए? (Forgot Password)" else "Forgot Password") },
            text = {
                Text(
                    if (isHindi)
                        "आपका नया ओटीपी मोबाइल नंबर $mobile पर भेज दिया गया है। पिन दर्ज करें: 1234"
                    else
                        "A password reset OTP was sent to $mobile. Use OTP: 1234"
                )
            },
            confirmButton = {
                Button(onClick = {
                    showForgotPasswordDialog = false
                    showOtpDialog = true
                }) {
                    Text(if (isHindi) "OTP से लॉगिन करें" else "Login via OTP")
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text(if (isHindi) "बंद करें" else "Close")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isHindi) "लॉगिन करें (Login)" else "Login to Account",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangePrimary
                )

                Text(
                    text = if (isHindi)
                        "अपना रजिस्टर्ड मोबाइल नंबर और पासवर्ड डालें"
                    else
                        "Enter registered mobile number & password",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text(if (isHindi) "मोबाइल नंबर *" else "Mobile Number *") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_mobile_input")
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(if (isHindi) "पासवर्ड *" else "Password *") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_password_input")
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { showForgotPasswordDialog = true }) {
                        Text(
                            text = if (isHindi) "पासवर्ड भूल गए?" else "Forgot Password?",
                            fontSize = 12.sp
                        )
                    }

                    TextButton(onClick = { showOtpDialog = true }) {
                        Text(
                            text = if (isHindi) "OTP से लॉगिन" else "OTP Login",
                            color = OrangePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Button(
                    onClick = {
                        if (mobile.isNotEmpty()) {
                            onLoginSubmit(mobile, password)
                        } else {
                            errorMessage = if (isHindi) "कृपया मोबाइल नंबर दर्ज करें" else "Please enter mobile number"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("login_submit_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text(
                        text = if (isHindi) "लॉगिन करें" else "Login",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Divider()

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = if (isHindi) "अकाउंट नहीं है? " else "Don't have an account? ", fontSize = 13.sp)
                    TextButton(onClick = onRegisterClick) {
                        Text(
                            text = if (isHindi) "रजिस्टर करें" else "Register",
                            color = OrangePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
