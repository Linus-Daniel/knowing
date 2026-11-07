package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.SampleData
import com.example.model.SentenceItem
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.LiterataFontFamily
import com.example.ui.theme.LocalHarmonixColors
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.viewmodel.HarmonixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioReaderScreen(
    viewModel: HarmonixViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val harmonixColors = LocalHarmonixColors.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    BackHandler {
        viewModel.navigateBack()
    }

    val selectedFontFamily = when (uiState.typefaceChoice) {
        "Manrope UI" -> ManropeFontFamily
        "Dyslexic" -> FontFamily.SansSerif
        else -> LiterataFontFamily
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveScale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(harmonixColors.surfaceBase)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Interactive In-Chapter Sub-Bar
            Surface(
                color = harmonixColors.surfaceBase.copy(alpha = 0.95f),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = { viewModel.navigateBack() },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(harmonixColors.surfaceSunken)
                                .testTag("reader_minimize_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Minimize player",
                                tint = harmonixColors.textPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(SecondaryContainer)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "LIVE KARAOKE STREAM",
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.8.sp,
                                    color = PrimaryAmber
                                )
                            }
                            Text(
                                text = "Chapter 4: The Horizon — Part II",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = harmonixColors.textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Reader Quick Action Icons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleBookmark() },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("reader_bookmark_toggle")
                        ) {
                            Icon(
                                imageVector = if (uiState.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (uiState.isBookmarked) PrimaryAmber else harmonixColors.textSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Search indexed for Chapter 4 (24 occurrences)", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search chapter",
                                tint = harmonixColors.textSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Typography Aa Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(harmonixColors.surfaceSunken)
                                .clickable { viewModel.toggleTypographySheet(true) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("reader_typography_settings_button")
                        ) {
                            Text(
                                text = "A",
                                fontFamily = LiterataFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = harmonixColors.textPrimary
                            )
                            Text(
                                text = "a",
                                fontFamily = LiterataFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = harmonixColors.textPrimary
                            )
                        }
                    }
                }
            }

            // Main Reader Canvas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
                    .padding(bottom = 180.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Chapter Pre-Title Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BOOK ONE • THE SALTED THRESHOLD",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.2.sp,
                        color = PrimaryAmber
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(harmonixColors.surfaceSunken)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Section 2 of 5",
                            fontFamily = ManropeFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = harmonixColors.textSecondary
                        )
                    }
                }

                // Chapter Banner Vignette Illustration
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(harmonixColors.surfaceSunken)
                ) {
                    AsyncImage(
                        model = SampleData.CHAPTER_VIGNETTE_URL,
                        contentDescription = "Chapter illustration",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, harmonixColors.surfaceBase.copy(alpha = 0.85f))
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(harmonixColors.surfaceBase.copy(alpha = 0.9f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Illustrated Edition • Folio 88",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = PrimaryAmber
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(harmonixColors.surfaceBase.copy(alpha = 0.85f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Audio Synced (99.8%)",
                                fontFamily = ManropeFontFamily,
                                fontSize = 11.sp,
                                color = harmonixColors.textSecondary
                            )
                        }
                    }
                }

                // Sentence 0: Past paragraph (faded context)
                SentenceItemView(
                    sentence = uiState.sentences[0],
                    isActive = uiState.activeSentenceIndex == 0,
                    fontSizeSp = uiState.fontSizeSp,
                    fontFamily = selectedFontFamily,
                    onClick = { viewModel.seekToSentence(0) }
                )

                // Sentence 1: Preceding read sentence
                SentenceItemView(
                    sentence = uiState.sentences[1],
                    isActive = uiState.activeSentenceIndex == 1,
                    fontSizeSp = uiState.fontSizeSp,
                    fontFamily = selectedFontFamily,
                    onClick = { viewModel.seekToSentence(1) }
                )

                // Context Bubble Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF263143),
                        shadowElevation = 6.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        Toast.makeText(context, "Sentence highlighted in Amber", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatQuote,
                                    contentDescription = null,
                                    tint = SecondaryFixed,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Highlight",
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(14.dp)
                                    .background(Color.Gray.copy(alpha = 0.5f))
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        Toast.makeText(context, "Marginalia note added to Chapter 4", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Note",
                                    fontFamily = ManropeFontFamily,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(14.dp)
                                    .background(Color.Gray.copy(alpha = 0.5f))
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { viewModel.togglePlayback() }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Speak",
                                    fontFamily = ManropeFontFamily,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(14.dp)
                                    .background(Color.Gray.copy(alpha = 0.5f))
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { viewModel.openQuoteModal(SampleData.FeaturedHighlight) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Card",
                                    fontFamily = ManropeFontFamily,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Sentence 2: Real-time Synchronized Karaoke Active Sentence Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(harmonixColors.activeKaraokeBg)
                        .clickable { viewModel.seekToSentence(2) }
                        .padding(14.dp)
                        .testTag("karaoke_active_sentence_box")
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Structural Leading Indicator Pillar
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(56.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(harmonixColors.activeKaraokeBorder)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "When the Sun reached the break between the roofs, the light fell straight across the desk, illuminating every particle of dust in quiet suspension.",
                                fontFamily = selectedFontFamily,
                                fontSize = uiState.fontSizeSp.sp,
                                lineHeight = (uiState.fontSizeSp * 1.7f).sp,
                                fontWeight = FontWeight.Normal,
                                color = harmonixColors.textPrimary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.GraphicEq,
                                        contentDescription = null,
                                        tint = PrimaryAmber,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Narrator: Christopher Voight (Studio Master)",
                                        fontFamily = ManropeFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp,
                                        color = PrimaryAmber
                                    )
                                }

                                Text(
                                    text = viewModel.formatTimestamp(uiState.playbackSeconds),
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = harmonixColors.textSecondary
                                )
                            }
                        }
                    }
                }

                // Sentence 3 & 4: Upcoming sentences
                SentenceItemView(
                    sentence = uiState.sentences[3],
                    isActive = uiState.activeSentenceIndex == 3,
                    fontSizeSp = uiState.fontSizeSp,
                    fontFamily = selectedFontFamily,
                    onClick = { viewModel.seekToSentence(3) }
                )

                SentenceItemView(
                    sentence = uiState.sentences[4],
                    isActive = uiState.activeSentenceIndex == 4,
                    fontSizeSp = uiState.fontSizeSp,
                    fontFamily = selectedFontFamily,
                    onClick = { viewModel.seekToSentence(4) }
                )

                // Inline Bookmark Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceSunken),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = PrimaryAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "Audio Bookmark Recorded",
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = harmonixColors.textPrimary
                                )
                                Text(
                                    text = "Synchronized across 2 devices at 14:22",
                                    fontFamily = ManropeFontFamily,
                                    fontSize = 11.sp,
                                    color = harmonixColors.textSecondary
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(harmonixColors.surfaceRaised)
                                .clickable {
                                    Toast.makeText(context, "Note: Motifs of photosynthetic empathy", Toast.LENGTH_LONG).show()
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "View Note",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = PrimaryAmber
                            )
                        }
                    }
                }
            }
        }

        // Level 2 Floating Audio Player Dock (Fixed Bottom)
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = harmonixColors.surfaceRaised.copy(alpha = 0.96f),
            shadowElevation = 12.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = Color(0x33111C2D))
                .testTag("audio_reader_player_dock")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Interactive Scrubber & Timestamps
                Column(modifier = Modifier.fillMaxWidth()) {
                    Slider(
                        value = uiState.playbackSeconds,
                        onValueChange = {
                            viewModel.seekToSentence(
                                uiState.sentences.indexOfFirst { s -> s.endTimeSeconds >= it }.coerceAtLeast(0)
                            )
                        },
                        valueRange = 0f..60f,
                        colors = SliderDefaults.colors(
                            thumbColor = PrimaryAmber,
                            activeTrackColor = PrimaryAmber,
                            inactiveTrackColor = harmonixColors.surfaceSunken
                        ),
                        modifier = Modifier.fillMaxWidth().height(20.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = viewModel.formatTimestamp(uiState.playbackSeconds),
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = PrimaryAmber
                        )
                        Text(
                            text = "-22:15",
                            fontFamily = ManropeFontFamily,
                            fontSize = 11.sp,
                            color = harmonixColors.textSecondary
                        )
                    }
                }

                // Main Transport Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.seekToSentence(0) },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous chapter",
                            tint = harmonixColors.textPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.skip15Seconds(false) },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastRewind,
                            contentDescription = "Replay 15 seconds",
                            tint = harmonixColors.textPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Big Radiant Amber Play / Pause Action Button
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(PrimaryAmber)
                            .clickable { viewModel.togglePlayback() }
                            .testTag("reader_master_play_toggle"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (uiState.isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.skip15Seconds(true) },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Forward 15 seconds",
                            tint = harmonixColors.textPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.seekToSentence(uiState.sentences.lastIndex) },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next chapter",
                            tint = harmonixColors.textPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Metadata & Micro-Controls Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Speed Toggle Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(harmonixColors.surfaceSunken)
                            .clickable { viewModel.cycleSpeed() }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .testTag("reader_speed_toggle_badge")
                    ) {
                        Text(
                            text = "${uiState.playbackSpeed}x",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = PrimaryAmber
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = harmonixColors.textSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    // Voice Source Wave Badge (Showing Google Cloud TTS or Android Native Fallback)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SecondaryFixed.copy(alpha = 0.5f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Equalizer,
                            contentDescription = null,
                            tint = PrimaryAmber,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (uiState.activeBook.audioStreamUrl != null) {
                                "LibriVox Stream"
                            } else when (uiState.ttsEngine) {
                                com.example.model.TtsEngineType.GOOGLE_CLOUD_NEURAL -> "Cloud TTS (Neural2)"
                                com.example.model.TtsEngineType.ANDROID_NATIVE_FALLBACK -> "Android TTS (Fallback)"
                            },
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = Color(0xFF653E00)
                        )
                    }

                    // Sleep Timer Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(harmonixColors.surfaceSunken)
                            .clickable {
                                Toast.makeText(context, "Sleep timer set to 30 minutes", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bedtime,
                            contentDescription = null,
                            tint = PrimaryAmber,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "30m",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = harmonixColors.textPrimary
                        )
                    }
                }
            }
        }

        // Typography & Reading Theme Quick-Drawer Sheet
        if (uiState.isTypographySheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.toggleTypographySheet(false) },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = harmonixColors.surfaceRaised
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reading Appearance",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = harmonixColors.textPrimary
                        )
                        Text(
                            text = "DYNAMIC LAYOUT",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            color = PrimaryAmber
                        )
                    }

                    // Canvas Tint
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "CANVAS TINT",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = harmonixColors.textSecondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ThemeTintOption("Light", Color(0xFFFAFAF9), uiState.themeMode == AppThemeMode.LIGHT) {
                                viewModel.setThemeMode(AppThemeMode.LIGHT)
                            }
                            ThemeTintOption("Sepia", Color(0xFFFBF7EE), uiState.themeMode == AppThemeMode.SEPIA) {
                                viewModel.setThemeMode(AppThemeMode.SEPIA)
                            }
                            ThemeTintOption("Dark", Color(0xFF090D16), uiState.themeMode == AppThemeMode.DARK) {
                                viewModel.setThemeMode(AppThemeMode.DARK)
                            }
                            ThemeTintOption("OLED", Color.Black, uiState.themeMode == AppThemeMode.OLED) {
                                viewModel.setThemeMode(AppThemeMode.OLED)
                            }
                        }
                    }

                    // Editorial Typeface
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "EDITORIAL TYPEFACE",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = harmonixColors.textSecondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf("Literata", "Manrope UI", "Dyslexic").forEach { tf ->
                                val selected = uiState.typefaceChoice == tf
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (selected) PrimaryAmber else harmonixColors.surfaceSunken)
                                        .clickable { viewModel.setTypefaceChoice(tf) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = tf,
                                        fontFamily = ManropeFontFamily,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = if (selected) Color.White else harmonixColors.textPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Type Scale Stepper
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TYPE SCALE",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                letterSpacing = 0.8.sp,
                                color = harmonixColors.textSecondary
                            )
                            Text(
                                text = "${uiState.fontSizeSp}px / 34px",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = PrimaryAmber
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(harmonixColors.surfaceSunken)
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp, 36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(harmonixColors.surfaceRaised)
                                    .clickable { viewModel.adjustFontSize(false) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "A -",
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = harmonixColors.textPrimary
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                (16..24 step 2).forEach { size ->
                                    val isCurrent = uiState.fontSizeSp == size
                                    Box(
                                        modifier = Modifier
                                            .size(if (isCurrent) 10.dp else 6.dp)
                                            .clip(CircleShape)
                                            .background(if (isCurrent) PrimaryAmber else harmonixColors.borderHairline)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(44.dp, 36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(harmonixColors.surfaceRaised)
                                    .clickable { viewModel.adjustFontSize(true) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "A +",
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = harmonixColors.textPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SentenceItemView(
    sentence: SentenceItem,
    isActive: Boolean,
    fontSizeSp: Int,
    fontFamily: FontFamily,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val harmonixColors = LocalHarmonixColors.current

    Text(
        text = sentence.text,
        fontFamily = fontFamily,
        fontSize = fontSizeSp.sp,
        lineHeight = (fontSizeSp * 1.7f).sp,
        color = if (sentence.isRead) harmonixColors.textSecondary.copy(alpha = 0.85f) else harmonixColors.textPrimary,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    )
}

@Composable
fun androidx.compose.foundation.layout.RowScope.ThemeTintOption(
    name: String,
    tintColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val harmonixColors = LocalHarmonixColors.current
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) SecondaryFixed else harmonixColors.surfaceSunken)
            .border(
                width = if (isSelected) 1.5.dp else 0.dp,
                color = if (isSelected) PrimaryAmber else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(tintColor)
                .border(1.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
        )
        Text(
            text = name,
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = if (isSelected) Color(0xFF2A1700) else harmonixColors.textPrimary
        )
    }
}
