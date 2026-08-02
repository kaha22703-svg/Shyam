package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StarYellow

@Composable
fun RatingReviewDialog(
    mistryName: String,
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (Float, String) -> Unit
) {
    var rating by remember { mutableFloatStateOf(5.0f) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isHindi) "$mistryName को रेटिंग दें" else "Rate $mistryName",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Interactive Star Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Default.Star else Icons.Outlined.Star,
                            contentDescription = "Star $i",
                            tint = StarYellow,
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { rating = i.toFloat() }
                        )
                    }
                }

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text(if (isHindi) "समीक्षा / टिप्पणी (Review)" else "Write a review") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("review_comment_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(rating, comment) },
                modifier = Modifier.testTag("submit_review_button")
            ) {
                Text(if (isHindi) "सबमिट करें" else "Submit Review")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isHindi) "रद्द करें" else "Cancel")
            }
        }
    )
}
