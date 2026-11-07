package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalHarmonixColors
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.PrimaryAmber
import com.example.viewmodel.ScreenDestination

data class NavItem(
    val destination: ScreenDestination,
    val label: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun HarmonixBottomNav(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val harmonixColors = LocalHarmonixColors.current
    val items = listOf(
        NavItem(ScreenDestination.HOME, "Home", Icons.Default.AutoStories, "nav_home"),
        NavItem(ScreenDestination.STUDIO, "Studio", Icons.Default.GraphicEq, "nav_studio"),
        NavItem(ScreenDestination.LIBRARY, "Library", Icons.Default.LocalLibrary, "nav_library"),
        NavItem(ScreenDestination.PROFILE, "Profile", Icons.Default.AccountCircle, "nav_profile")
    )

    Surface(
        color = harmonixColors.surfaceBase.copy(alpha = 0.95f),
        shadowElevation = 8.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentScreen == item.destination
                val color = if (selected) PrimaryAmber else harmonixColors.textSecondary

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onNavigate(item.destination) }
                        .testTag(item.tag)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = item.label,
                        fontFamily = ManropeFontFamily,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp,
                        color = color
                    )
                }
            }
        }
    }
}
