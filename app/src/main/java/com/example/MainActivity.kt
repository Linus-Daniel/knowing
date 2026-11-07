package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.FloatingMiniPlayer
import com.example.ui.components.HarmonixBottomNav
import com.example.ui.components.HarmonixTopBar
import com.example.ui.screens.AudioReaderScreen
import com.example.ui.screens.DocumentImportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.HarmonixTheme
import com.example.ui.theme.LocalHarmonixColors
import com.example.viewmodel.HarmonixViewModel
import com.example.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {

    private val viewModel: HarmonixViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            LaunchedEffect(uiState.userBookmarkToast) {
                uiState.userBookmarkToast?.let { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    viewModel.clearBookmarkToast()
                }
            }

            HarmonixTheme(themeMode = uiState.themeMode) {
                val harmonixColors = LocalHarmonixColors.current

                Scaffold(
                    topBar = {
                        val (title, subtitle) = when (uiState.currentScreen) {
                            ScreenDestination.HOME -> Pair("Harmonix", "Home")
                            ScreenDestination.STUDIO -> Pair("Harmonix", "Document Import")
                            ScreenDestination.READER -> Pair("Harmonix", "Audio Reading View")
                            ScreenDestination.LIBRARY -> Pair("Harmonix", "Library")
                            ScreenDestination.PROFILE -> Pair("Harmonix", "Profile")
                        }

                        HarmonixTopBar(
                            title = title,
                            subtitle = subtitle,
                            showBackButton = uiState.currentScreen == ScreenDestination.READER ||
                                    uiState.currentScreen == ScreenDestination.PROFILE,
                            onBackClick = { viewModel.navigateBack() },
                            onProfileClick = { viewModel.navigateTo(ScreenDestination.PROFILE) }
                        )
                    },
                    bottomBar = {
                        if (uiState.currentScreen != ScreenDestination.READER) {
                            HarmonixBottomNav(
                                currentScreen = uiState.currentScreen,
                                onNavigate = { destination ->
                                    viewModel.navigateTo(destination)
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(harmonixColors.surfaceBase)
                            .padding(innerPadding)
                    ) {
                        // Main Screen Routing
                        when (uiState.currentScreen) {
                            ScreenDestination.HOME -> HomeScreen(viewModel = viewModel)
                            ScreenDestination.STUDIO -> DocumentImportScreen(viewModel = viewModel)
                            ScreenDestination.READER -> AudioReaderScreen(viewModel = viewModel)
                            ScreenDestination.LIBRARY -> LibraryScreen(viewModel = viewModel)
                            ScreenDestination.PROFILE -> ProfileScreen(viewModel = viewModel)
                        }

                        // Floating Level 2 Mini-Player Dock above Bottom Nav
                        if (uiState.currentScreen != ScreenDestination.READER) {
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically { it } + fadeIn(),
                                exit = slideOutVertically { it } + fadeOut(),
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                            ) {
                                FloatingMiniPlayer(
                                    book = uiState.activeBook,
                                    isPlaying = uiState.isPlaying,
                                    progressFraction = uiState.activeBook.progressPercent / 100f,
                                    onTogglePlay = { viewModel.togglePlayback() },
                                    onRewind15 = { viewModel.skip15Seconds(false) },
                                    onExpand = { viewModel.navigateTo(ScreenDestination.READER) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
