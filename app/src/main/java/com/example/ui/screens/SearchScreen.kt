package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.data.model.SkillCategories
import com.example.ui.theme.OrangePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    isHindi: Boolean,
    searchQuery: String,
    selectedSkill: String?,
    selectedState: String?,
    selectedCity: String?,
    maxPrice: Float,
    minExperience: Int,
    filteredMistris: List<UserEntity>,
    onQueryChange: (String) -> Unit,
    onSkillSelect: (String?) -> Unit,
    onStateSelect: (String?) -> Unit,
    onCitySelect: (String?) -> Unit,
    onMaxPriceChange: (Float) -> Unit,
    onMinExpChange: (Int) -> Unit,
    onClearFilters: () -> Unit,
    onWorkerClick: (UserEntity) -> Unit
) {
    val context = LocalContext.current
    var showFilterSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                placeholder = { Text(if (isHindi) "मिस्त्री या शहर खोजें..." else "Search mistry or city...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("search_text_input")
            )

            IconButton(
                onClick = { showFilterSheet = true },
                modifier = Modifier.testTag("filter_options_button")
            ) {
                BadgedBox(badge = {
                    if (selectedSkill != null || selectedState != null || maxPrice < 2000f) {
                        Badge { Text("!") }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter",
                        tint = OrangePrimary
                    )
                }
            }
        }

        // Active Filter Chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedSkill != null) {
                AssistChip(
                    onClick = { onSkillSelect(null) },
                    label = { Text(selectedSkill) },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp)) }
                )
            }
            if (selectedState != null) {
                AssistChip(
                    onClick = { onStateSelect(null) },
                    label = { Text(selectedState) },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp)) }
                )
            }
            if (selectedSkill != null || selectedState != null) {
                TextButton(onClick = onClearFilters) {
                    Text(if (isHindi) "फ़िल्टर हटाएं" else "Clear All", fontSize = 12.sp)
                }
            }
        }

        Text(
            text = if (isHindi)
                "कुल ${filteredMistris.size} मिस्त्री उपलब्ध हैं:"
            else
                "${filteredMistris.size} Workers Available:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredMistris) { worker ->
                WorkerCardItem(
                    worker = worker,
                    isHindi = isHindi,
                    onClick = { onWorkerClick(worker) },
                    onCallClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${worker.mobile}"))
                        context.startActivity(intent)
                    },
                    onWhatsAppClick = {
                        val url = "https://api.whatsapp.com/send?phone=+91${worker.mobile}&text=${Uri.encode("नमस्ते ${worker.fullName}, मिस्त्री कनेक्ट ऐप से आपका नंबर प्राप्त हुआ है।")}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }

    // --- FILTER BOTTOM SHEET ---
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "फ़िल्टर विकल्प" else "Filter Options",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = OrangePrimary
                    )
                    TextButton(onClick = onClearFilters) {
                        Text(if (isHindi) "रीसेट" else "Reset")
                    }
                }

                Text(if (isHindi) "कौशल चुनें (Skill):" else "Select Skill:", fontWeight = FontWeight.SemiBold)
                ScrollableTabRow(selectedTabIndex = 0, edgePadding = 0.dp) {
                    Tab(
                        selected = selectedSkill == null,
                        onClick = { onSkillSelect(null) },
                        text = { Text(if (isHindi) "सभी (All)" else "All") }
                    )
                    SkillCategories.ALL_SKILLS.take(10).forEach { skill ->
                        Tab(
                            selected = selectedSkill == skill.hindiName,
                            onClick = { onSkillSelect(skill.hindiName) },
                            text = { Text(if (isHindi) skill.hindiName else skill.englishName) }
                        )
                    }
                }

                Text(
                    text = if (isHindi) "अधिकतम दैनिक मजदूरी: ₹${maxPrice.toInt()}" else "Max Daily Wage: ₹${maxPrice.toInt()}",
                    fontWeight = FontWeight.SemiBold
                )
                Slider(
                    value = maxPrice,
                    onValueChange = onMaxPriceChange,
                    valueRange = 300f..3000f,
                    steps = 27
                )

                Text(
                    text = if (isHindi) "न्यूनतम अनुभव: $minExperience वर्ष" else "Min Experience: $minExperience Yrs",
                    fontWeight = FontWeight.SemiBold
                )
                Slider(
                    value = minExperience.toFloat(),
                    onValueChange = { onMinExpChange(it.toInt()) },
                    valueRange = 0f..20f,
                    steps = 20
                )

                Button(
                    onClick = { showFilterSheet = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("apply_filter_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text(if (isHindi) "फ़िल्टर लागू करें" else "Apply Filters", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
