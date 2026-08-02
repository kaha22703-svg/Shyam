package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.OrangePrimaryVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MistriTopBar(
    isHindi: Boolean,
    isDarkMode: Boolean,
    currentUser: UserEntity?,
    onLanguageToggle: () -> Unit,
    onDarkModeToggle: () -> Unit,
    onAdminClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Bold Brand Logo Block
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "M",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                }
                Column {
                    Text(
                        text = if (isHindi) "मिस्त्री कनेक्ट" else "MistriConnect",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        text = if (isHindi) "भारत का भरोसा" else "BHARAT'S TRUSTED NETWORK",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OrangePrimaryVariant,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        },
        actions = {
            // Language Switcher Chip (Pill Style)
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onLanguageToggle() }
                    .testTag("language_toggle_button"),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (isHindi) "हिन्दी | EN" else "EN | हिन्दी",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.width(2.dp))

            // Dark Mode Toggle Button
            IconButton(
                onClick = onDarkModeToggle,
                modifier = Modifier.testTag("dark_mode_toggle_button")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Dark Mode"
                )
            }

            // Admin Shortcut Icon
            IconButton(
                onClick = onAdminClick,
                modifier = Modifier.testTag("admin_panel_top_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = "Admin Panel",
                    tint = OrangePrimary
                )
            }

            // User Profile / Login Action
            if (currentUser != null) {
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier.testTag("user_profile_avatar_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.fullName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                TextButton(
                    onClick = onLoginClick,
                    modifier = Modifier.testTag("top_login_button")
                ) {
                    Text(
                        text = if (isHindi) "लॉगिन" else "Login",
                        fontWeight = FontWeight.ExtraBold,
                        color = BluePrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

