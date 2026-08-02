package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OrangePrimary

@Composable
fun InteractiveLocationPicker(
    currentLocation: String,
    isHindi: Boolean,
    onLocationSelected: (String) -> Unit
) {
    var locText by remember { mutableStateOf(currentLocation.ifEmpty { "26.8467° N, 80.9462° E - लखनऊ, उत्तर प्रदेश" }) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("location_picker_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Map",
                    tint = OrangePrimary
                )
                Text(
                    text = if (isHindi) "गूगल मैप / लाइव लोकेशन (Live GPS Pin)" else "Google Map Location Pin",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Simulated Map Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE2E8F0))
                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Pin",
                        tint = OrangePrimary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = locText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        locText = "28.6139° N, 77.2090° E - नई दिल्ली (GPS Live Location)"
                        onLocationSelected(locText)
                    },
                    modifier = Modifier.testTag("gps_fetch_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = "GPS",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isHindi) "लाइव GPS प्राप्त करें" else "Get GPS Location", fontSize = 12.sp)
                }

                Button(
                    onClick = { onLocationSelected(locText) },
                    modifier = Modifier.testTag("save_location_button")
                ) {
                    Text(if (isHindi) "पिन सेट करें" else "Set Pin", fontSize = 12.sp)
                }
            }
        }
    }
}
