package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.SampleData
import com.example.ui.theme.LiterataFontFamily
import com.example.ui.theme.LocalHarmonixColors
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.SecondaryContainer
import com.example.viewmodel.HarmonixViewModel

@Composable
fun ProfileScreen(
    viewModel: HarmonixViewModel,
    modifier: Modifier = Modifier
) {
    val harmonixColors = LocalHarmonixColors.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var isWhispersyncEnabled by remember { mutableStateOf(true) }
    var isHdAudioEnabled by remember { mutableStateOf(true) }
    var isWifiOnlyEnabled by remember { mutableStateOf(true) }

    BackHandler {
        viewModel.navigateBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceRaised),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, PrimaryAmber, CircleShape)
                ) {
                    AsyncImage(
                        model = SampleData.AVATAR_URL,
                        contentDescription = "Elena Rostova",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Elena Rostova",
                            fontFamily = LiterataFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = harmonixColors.textPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Member",
                            tint = PrimaryAmber,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "elena.rostova@literary.studio • Member since 2024",
                        fontFamily = ManropeFontFamily,
                        fontSize = 12.sp,
                        color = harmonixColors.textSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryFixed)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "STUDIO PRO SUITE",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = PrimaryAmber
                    )
                }
            }
        }

        // Telemetry Stats Bento
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                label = "Hours Listened",
                value = "148.5h",
                icon = Icons.Default.Headphones,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Finished Titles",
                value = "32",
                icon = Icons.Default.MenuBook,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Streak",
                value = "9 Days",
                icon = Icons.Default.LocalFireDepartment,
                modifier = Modifier.weight(1f)
            )
        }

        // Settings Sections
        Text(
            text = "Playback & Sync Preferences",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = harmonixColors.textPrimary
        )

        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceRaised),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Whispersync
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null, tint = PrimaryAmber)
                        Column {
                            Text(
                                text = "Whispersync Cloud Stream",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = harmonixColors.textPrimary
                            )
                            Text(
                                text = "Continuous sentence-level sync across all devices",
                                fontFamily = ManropeFontFamily,
                                fontSize = 11.sp,
                                color = harmonixColors.textSecondary
                            )
                        }
                    }
                    Switch(
                        checked = isWhispersyncEnabled,
                        onCheckedChange = { isWhispersyncEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryAmber)
                    )
                }

                // HD Audio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Equalizer, contentDescription = null, tint = PrimaryAmber)
                        Column {
                            Text(
                                text = "Master Audio Fidelity (320 kbps)",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = harmonixColors.textPrimary
                            )
                            Text(
                                text = "Studio vocal spatial separation & dynamic range",
                                fontFamily = ManropeFontFamily,
                                fontSize = 11.sp,
                                color = harmonixColors.textSecondary
                            )
                        }
                    }
                    Switch(
                        checked = isHdAudioEnabled,
                        onCheckedChange = { isHdAudioEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryAmber)
                    )
                }

                // Offline auto-download
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.OfflinePin, contentDescription = null, tint = PrimaryAmber)
                        Column {
                            Text(
                                text = "Wi-Fi Only Sync & Cache",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = harmonixColors.textPrimary
                            )
                            Text(
                                text = "Prevent cellular data consumption for voice synthesis",
                                fontFamily = ManropeFontFamily,
                                fontSize = 11.sp,
                                color = harmonixColors.textSecondary
                            )
                        }
                    }
                    Switch(
                        checked = isWifiOnlyEnabled,
                        onCheckedChange = { isWifiOnlyEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryAmber)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    val harmonixColors = LocalHarmonixColors.current
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceRaised),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryAmber,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = harmonixColors.textPrimary
            )
            Text(
                text = label,
                fontFamily = ManropeFontFamily,
                fontSize = 10.sp,
                color = harmonixColors.textSecondary
            )
        }
    }
}
