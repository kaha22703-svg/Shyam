package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.JobPostEntity
import com.example.data.local.UserEntity
import com.example.data.model.SkillCategories
import com.example.data.model.SkillItem
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    isHindi: Boolean,
    currentUser: UserEntity?,
    mistrisList: List<UserEntity>,
    recentJobsList: List<JobPostEntity>,
    onBecomeMistriClick: () -> Unit,
    onPostJobClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onSelectSkill: (String) -> Unit,
    onWorkerClick: (UserEntity) -> Unit,
    onJobClick: (JobPostEntity) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- HERO BANNER (BOLD TYPOGRAPHY THEME) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(BluePrimary)
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Badge
                Surface(
                    color = BluePrimaryVariant,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = if (isHindi) "भारत का भरोसा" else "BHARAT'S TRUSTED PLATFORM",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                // Bold Headline with Orange Highlight
                Text(
                    text = if (isHindi)
                        "विश्वसनीय मिस्त्री और काम देने वालों का एक प्लेटफॉर्म"
                    else
                        "A Trusted Platform for Skilled Workers & Employers",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = (-0.5).sp
                )

                Text(
                    text = if (isHindi)
                        "हजारों कुशल कारीगर और बड़ी कंपनियाँ आज ही जुड़ें।"
                    else
                        "Thousands of skilled workers and companies connecting daily.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Hero Action Buttons (Login & Register)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("hero_login_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = BluePrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (isHindi) "लॉगिन" else "Login",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("hero_register_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary, contentColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (isHindi) "रजिस्टर करें" else "Register",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // --- MAIN ACTION CARDS (WORKER vs EMPLOYER) ---
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Worker Card ("मैं मिस्त्री हूँ")
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("worker_role_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, SlateBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(OrangeLightContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Construction,
                                    contentDescription = null,
                                    tint = OrangePrimaryVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isHindi) "मैं मिस्त्री हूँ" else "I'm a Worker",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = if (isHindi) "प्रोफाइल बनाएं और काम पाएं" else "Create profile & get work",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onBecomeMistriClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .testTag("become_mistri_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDarkBg, contentColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (isHindi) "मिस्त्री बनें" else "Join as Worker",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Employer Card ("काम देना है")
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("employer_role_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, SlateBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(BlueLightContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Work,
                                    contentDescription = null,
                                    tint = BluePrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isHindi) "काम देना है" else "Hire Workers",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = if (isHindi) "मिस्त्री खोजें या विज्ञापन डालें" else "Find workers or post jobs",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onPostJobClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .testTag("post_job_hero_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary, contentColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (isHindi) "काम पोस्ट करें" else "Post a Job",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // --- TRUST INDICATORS BADGE BAR ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("trust_indicators_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlueLightContainer),
                border = BorderStroke(1.dp, BlueLightContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "50k+",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = BluePrimary
                        )
                        Text(
                            text = if (isHindi) "वेरीफाइड मिस्त्री" else "VERIFIED WORKERS",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = BluePrimary.copy(alpha = 0.7f)
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .height(28.dp)
                            .width(1.dp),
                        color = BluePrimary.copy(alpha = 0.2f)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "12k+",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = BluePrimary
                        )
                        Text(
                            text = if (isHindi) "सक्रिय काम" else "ACTIVE JOBS",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = BluePrimary.copy(alpha = 0.7f)
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .height(28.dp)
                            .width(1.dp),
                        color = BluePrimary.copy(alpha = 0.2f)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "4.8/5",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = BluePrimary
                        )
                        Text(
                            text = if (isHindi) "यूजर रेटिंग" else "USER RATING",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = BluePrimary.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        // --- SKILL CATEGORIES (21 CHECKBOXES / CARDS) ---
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "कौशल के आधार पर मिस्त्री खोजें" else "Browse Workers by Skill",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                TextButton(onClick = { onSelectSkill("") }) {
                    Text(text = if (isHindi) "सभी देखें (21)" else "View All (21)", color = OrangePrimary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Grid of 6 Quick Skills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(SkillCategories.ALL_SKILLS) { skill ->
                    SkillChipItem(
                        skill = skill,
                        isHindi = isHindi,
                        onClick = { onSelectSkill(skill.hindiName) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- FEATURED VERIFIED MISTRIS ---
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isHindi) "टॉप वेरीफाइड मिस्त्री" else "Top Verified Workers",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            mistrisList.take(6).forEach { worker ->
                WorkerCardItem(
                    worker = worker,
                    isHindi = isHindi,
                    onClick = { onWorkerClick(worker) },
                    onCallClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${worker.mobile}"))
                        context.startActivity(intent)
                    },
                    onWhatsAppClick = {
                        val url = "https://api.whatsapp.com/send?phone=+91${worker.mobile}&text=${Uri.encode("नमस्ते ${worker.fullName}, मिस्त्री कनेक्ट ऐप से आपका संपर्क नंबर मिला है।")}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- RECENT POSTED JOBS ---
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = if (isHindi) "हाल ही में पोस्ट किए गए काम" else "Recent Job Posts",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            recentJobsList.take(4).forEach { job ->
                JobCardItem(
                    job = job,
                    isHindi = isHindi,
                    onClick = { onJobClick(job) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun SkillChipItem(
    skill: SkillItem,
    isHindi: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(130.dp)
            .clickable { onClick() }
            .testTag("skill_chip_${skill.englishName}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(OrangePrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = skill.icon,
                    contentDescription = skill.englishName,
                    tint = OrangePrimary
                )
            }
            Text(
                text = if (isHindi) skill.hindiName else skill.englishName,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun WorkerCardItem(
    worker: UserEntity,
    isHindi: Boolean,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("worker_card_${worker.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Initial Avatar
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(OrangePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = worker.fullName.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = worker.fullName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (worker.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = VerifiedGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = worker.skills,
                    color = OrangePrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = "Rating", tint = StarYellow, modifier = Modifier.size(14.dp))
                        Text(text = "${worker.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "•", fontSize = 12.sp)
                    Text(text = "₹${worker.dailyWage.toInt()}/${if (isHindi) "दिन" else "day"}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "•", fontSize = 12.sp)
                    Text(text = "${worker.city}", fontSize = 12.sp, maxLines = 1)
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onCallClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = OrangePrimary, modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = onWhatsAppClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF25D366).copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color(0xFF128C7E), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun JobCardItem(
    job: JobPostEntity,
    isHindi: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("job_card_${job.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                Surface(
                    color = OrangePrimary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = job.requiredSkill,
                        color = OrangePrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "₹${job.wageAmount.toInt()}/${if (isHindi) "दिन" else "day"}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }

            Text(
                text = job.jobTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp))
                    Text(text = "${job.city}, ${job.state}", fontSize = 11.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Group, contentDescription = null, modifier = Modifier.size(14.dp))
                    Text(text = "${job.workersNeeded} ${if (isHindi) "मिस्त्री" else "workers"}", fontSize = 11.sp)
                }
            }
        }
    }
}
