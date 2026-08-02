package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpVerificationDialog(
    mobileNumber: String,
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onVerified: () -> Unit
) {
    var otpCode by remember { mutableStateOf("1234") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isHindi) "ओटीपी सत्यापन (OTP Verification)" else "OTP Verification",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isHindi)
                        "मोबाइल नंबर +91 $mobileNumber पर 4-अंकों का ओटीपी भेजा गया है:"
                    else
                        "A 4-digit OTP has been sent to +91 $mobileNumber:",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = otpCode,
                    onValueChange = {
                        if (it.length <= 4) {
                            otpCode = it
                            isError = false
                        }
                    },
                    label = { Text(if (isHindi) "ओटीपी कोड" else "OTP Code") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("otp_input_field")
                )

                if (isError) {
                    Text(
                        text = if (isHindi) "गलत ओटीपी! (सही: 1234)" else "Invalid OTP! (Default: 1234)",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = if (isHindi) "डेमो पिन: 1234" else "Demo PIN: 1234",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (otpCode == "1234" || otpCode.length == 4) {
                        onVerified()
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.testTag("verify_otp_submit_button")
            ) {
                Text(if (isHindi) "सत्यापित करें (Verify)" else "Verify OTP")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isHindi) "रद्द करें" else "Cancel")
            }
        }
    )
}
