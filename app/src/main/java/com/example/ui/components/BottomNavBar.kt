package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlueLightContainer
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.OrangePrimary

sealed class NavItem(val route: String, val hindiTitle: String, val englishTitle: String, val icon: ImageVector) {
    object Home : NavItem("home", "होम", "Home", Icons.Default.Home)
    object Search : NavItem("search", "खोजें", "Search", Icons.Default.Search)
    object PostJob : NavItem("post_job", "काम पोस्ट", "Post Job", Icons.Default.AddCircle)
    object Dashboard : NavItem("dashboard", "डैशबोर्ड", "Dashboard", Icons.Default.Dashboard)
    object Admin : NavItem("admin", "एडमिन", "Admin", Icons.Default.AdminPanelSettings)
}

@Composable
fun MistriBottomNavBar(
    currentRoute: String,
    isHindi: Boolean,
    onNavigate: (String) -> Unit
) {
    val navItems = listOf(
        NavItem.Home,
        NavItem.Search,
        NavItem.PostJob,
        NavItem.Dashboard,
        NavItem.Admin
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = if (isHindi) item.hindiTitle else item.englishTitle
                    )
                },
                label = {
                    Text(
                        text = if (isHindi) item.hindiTitle else item.englishTitle,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = if (item == NavItem.PostJob) OrangePrimary else BluePrimary,
                    selectedTextColor = if (item == NavItem.PostJob) OrangePrimary else BluePrimary,
                    indicatorColor = BlueLightContainer
                ),
                modifier = Modifier.testTag("nav_${item.route}")
            )
        }
    }
}

