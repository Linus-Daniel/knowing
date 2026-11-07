package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.VoiceModel
import com.example.ui.theme.LiterataFontFamily
import com.example.ui.theme.LocalHarmonixColors
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.viewmodel.HarmonixViewModel

@Composable
fun DocumentImportScreen(
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

    var isDialectMenuOpen by remember { mutableStateOf(false) }
    val dialects = listOf(
        "English - US (Neural v4 Ultra)",
        "English - UK (Royal Narrative Model)",
        "English - AU (Natural Fluidity)",
        "Japanese - Tokyo (Bilingual Polyglot)"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Restart
        ),
        label = "spinAngle"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Top Step Pill Bar
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = harmonixColors.surfaceSunken,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(harmonixColors.surfaceRaised)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(PrimaryAmber)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "FLOW 5: AUDIO SYNTHESIS",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = PrimaryAmber
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = PrimaryAmber,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Neural Studio 4.2",
                        fontFamily = ManropeFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = harmonixColors.textSecondary
                    )
                }
            }
        }

        // Step 1: Document Importer
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Document Importer",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = harmonixColors.textPrimary
                    )
                    Text(
                        text = "Transform your manuscripts & ebooks into fluid audio",
                        fontFamily = ManropeFontFamily,
                        fontSize = 12.sp,
                        color = harmonixColors.textSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(harmonixColors.surfaceSunken)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Step 1 of 3",
                        fontFamily = ManropeFontFamily,
                        fontSize = 11.sp,
                        color = harmonixColors.textSecondary
                    )
                }
            }

            // Drop Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceSunken),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("document_drop_target_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(harmonixColors.surfaceRaised),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.UploadFile,
                            contentDescription = null,
                            tint = PrimaryAmber,
                            modifier = Modifier.size(32.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(PrimaryAmber),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Tap or Drop Document Here",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = harmonixColors.textPrimary
                        )
                        Text(
                            text = "Supports manuscripts with complex table-of-contents & footnote hierarchies",
                            fontFamily = ManropeFontFamily,
                            fontSize = 12.sp,
                            color = harmonixColors.textSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    // Format Badges
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(".EPUB", ".PDF", ".DOCX", ".TXT").forEach { ext ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(harmonixColors.surfaceRaised)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = ext,
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = harmonixColors.textSecondary
                                )
                            }
                        }
                    }

                    // Browse CTA
                    Button(
                        onClick = {
                            Toast.makeText(context, "Loaded Neuromancer_Manuscript_Ch1-3.epub", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryAmber),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("browse_local_files_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Browse Local Files",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = null,
                            tint = harmonixColors.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "iCloud • Google Drive • Files",
                            fontFamily = ManropeFontFamily,
                            fontSize = 11.sp,
                            color = harmonixColors.textSecondary
                        )
                    }
                }
            }

            // Architecture notice
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = harmonixColors.surfaceSunken,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = PrimaryAmber,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Text-based documents are parsed into synchronized audiobooks using neural AI speech with sentence-level timestamps and bidirectional karaoke tracking.",
                        fontFamily = ManropeFontFamily,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = harmonixColors.textPrimary
                    )
                }
            }
        }

        // Step 2: Live Parsing Telemetry
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = PrimaryAmber,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Live Parsing Telemetry",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = harmonixColors.textPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(SecondaryFixed)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (uiState.isSynthesizing) "Synthesizing" else "Ready",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color(0xFF653E00)
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceSunken),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // File Metadata Row
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = harmonixColors.surfaceRaised,
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
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PrimaryFixed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoStories,
                                        contentDescription = null,
                                        tint = PrimaryAmber,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = uiState.documentImportState.fileName,
                                        fontFamily = ManropeFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = harmonixColors.textPrimary
                                    )
                                    Text(
                                        text = "${uiState.documentImportState.fileSize} • ${uiState.documentImportState.chaptersCount} Chapters • ${uiState.documentImportState.wordCount} Words",
                                        fontFamily = ManropeFontFamily,
                                        fontSize = 11.sp,
                                        color = harmonixColors.textSecondary
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = PrimaryAmber,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // 3-Stage Progress
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Stage 1
                        StageRow("1. Uploading file to local sandbox", isComplete = true)
                        // Stage 2
                        StageRow("2. Extracting text & chapter hierarchy", isComplete = true)
                        // Stage 3
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryFixed),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Autorenew,
                                            contentDescription = null,
                                            tint = PrimaryAmber,
                                            modifier = Modifier
                                                .size(14.dp)
                                                .rotate(spinAngle)
                                        )
                                    }
                                    Text(
                                        text = "3. Generating sentence-alignment index",
                                        fontFamily = ManropeFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = harmonixColors.textPrimary
                                    )
                                }

                                Text(
                                    text = "${(uiState.documentImportState.stage3Progress * 100).toInt()}%",
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = PrimaryAmber
                                )
                            }

                            LinearProgressIndicator(
                                progress = { uiState.documentImportState.stage3Progress },
                                color = PrimaryAmber,
                                trackColor = harmonixColors.surfaceRaised,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Aligning sentence ${uiState.documentImportState.alignedSentences} of ${uiState.documentImportState.totalSentences}",
                                    fontFamily = ManropeFontFamily,
                                    fontSize = 11.sp,
                                    color = harmonixColors.textSecondary
                                )
                                Text(
                                    text = "~12s remaining",
                                    fontFamily = ManropeFontFamily,
                                    fontSize = 11.sp,
                                    color = harmonixColors.textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Step 3: Neural TTS Voice & Pipeline
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceRaised),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "DRAWER ENGINE 4.2",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            color = PrimaryAmber
                        )
                        Text(
                            text = "Neural TTS Voice & Pipeline",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = harmonixColors.textPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(harmonixColors.surfaceSunken),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = PrimaryAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Engine Status Badge & Secrets Guidance
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = harmonixColors.surfaceSunken,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (viewModel.isGoogleCloudTtsConfigured) SecondaryContainer else PrimaryFixed)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (viewModel.isGoogleCloudTtsConfigured) "GOOGLE CLOUD TTS ACTIVE" else "ANDROID NATIVE TTS (FALLBACK)",
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = if (viewModel.isGoogleCloudTtsConfigured) Color(0xFF684000) else PrimaryAmber
                                )
                            }
                            Text(
                                text = if (viewModel.isGoogleCloudTtsConfigured) "Neural2 High-Fidelity" else "Zero-latency on-device",
                                fontFamily = ManropeFontFamily,
                                fontSize = 11.sp,
                                color = harmonixColors.textSecondary
                            )
                        }
                        if (!viewModel.isGoogleCloudTtsConfigured) {
                            Text(
                                text = "Pro-tip: Add GOOGLE_CLOUD_TTS_API_KEY to AI Studio Secrets to unlock cloud Neural2 wave synthesis. Android on-device speech is running as automatic fallback.",
                                fontFamily = ManropeFontFamily,
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                color = harmonixColors.textSecondary
                            )
                        }
                    }
                }

                // Dialect dropdown
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Target Acoustic Model & Dialect",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = harmonixColors.textPrimary
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(harmonixColors.surfaceSunken)
                            .clickable { isDialectMenuOpen = true }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = PrimaryAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uiState.documentImportState.targetDialect,
                                    fontFamily = ManropeFontFamily,
                                    fontSize = 13.sp,
                                    color = harmonixColors.textPrimary
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = harmonixColors.textSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = isDialectMenuOpen,
                            onDismissRequest = { isDialectMenuOpen = false }
                        ) {
                            dialects.forEach { dialect ->
                                DropdownMenuItem(
                                    text = { Text(dialect, fontFamily = ManropeFontFamily, fontSize = 13.sp) },
                                    onClick = {
                                        viewModel.updateTargetDialect(dialect)
                                        isDialectMenuOpen = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Voice Cards Stack
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Available Voices",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = harmonixColors.textPrimary
                    )

                    SampleData.VoiceOptions.forEach { voice ->
                        val isSelected = uiState.documentImportState.selectedVoiceId == voice.id
                        val isPlayingSample = uiState.playingVoiceSampleId == voice.id

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) harmonixColors.surfaceSunken else harmonixColors.surfaceSunken.copy(alpha = 0.5f),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryAmber) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectVoice(voice.id) }
                                .testTag("voice_card_${voice.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) PrimaryAmber else harmonixColors.surfaceRaised)
                                            .clickable { viewModel.playVoiceSample(voice) }
                                            .testTag("voice_sample_button_${voice.id}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isPlayingSample) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = "Preview voice sample",
                                            tint = if (isSelected) Color.White else harmonixColors.textPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = voice.name,
                                                fontFamily = ManropeFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = harmonixColors.textPrimary
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (isSelected) PrimaryFixed else harmonixColors.surfaceRaised)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = voice.badge,
                                                    fontFamily = ManropeFontFamily,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (isSelected) PrimaryAmber else harmonixColors.textSecondary
                                                )
                                            }
                                        }
                                        Text(
                                            text = voice.description,
                                            fontFamily = ManropeFontFamily,
                                            fontSize = 11.sp,
                                            color = harmonixColors.textSecondary
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryAmber),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Fine-Tuning Controls Bento
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceSunken),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Speed Slider
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Playback Speed",
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = harmonixColors.textPrimary
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(PrimaryFixed)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "%.2fx".format(uiState.documentImportState.speed),
                                        fontFamily = ManropeFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = PrimaryAmber
                                    )
                                }
                            }
                            Slider(
                                value = uiState.documentImportState.speed,
                                onValueChange = { viewModel.updateImportSpeed(it) },
                                valueRange = 1.0f..2.5f,
                                colors = SliderDefaults.colors(
                                    thumbColor = PrimaryAmber,
                                    activeTrackColor = PrimaryAmber
                                ),
                                modifier = Modifier.fillMaxWidth().height(24.dp)
                            )
                        }

                        // Pitch & Pause Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = harmonixColors.surfaceRaised,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Pitch Shift",
                                            fontFamily = ManropeFontFamily,
                                            fontSize = 11.sp,
                                            color = harmonixColors.textSecondary
                                        )
                                        Text(
                                            text = "+0.0 st",
                                            fontFamily = ManropeFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = PrimaryAmber
                                        )
                                    }
                                    Slider(
                                        value = uiState.documentImportState.pitchShift,
                                        onValueChange = { viewModel.updatePitchShift(it) },
                                        valueRange = -4f..4f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = PrimaryAmber,
                                            activeTrackColor = PrimaryAmber
                                        ),
                                        modifier = Modifier.fillMaxWidth().height(20.dp)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = harmonixColors.surfaceRaised,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Paragraph Pause",
                                            fontFamily = ManropeFontFamily,
                                            fontSize = 11.sp,
                                            color = harmonixColors.textSecondary
                                        )
                                        Text(
                                            text = "0.5s",
                                            fontFamily = ManropeFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = PrimaryAmber
                                        )
                                    }
                                    Slider(
                                        value = uiState.documentImportState.paragraphPauseSeconds,
                                        onValueChange = { viewModel.updateParagraphPause(it) },
                                        valueRange = 0.1f..1.5f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = PrimaryAmber,
                                            activeTrackColor = PrimaryAmber
                                        ),
                                        modifier = Modifier.fillMaxWidth().height(20.dp)
                                    )
                                }
                            }
                        }

                        // Pronunciation Lexicon Button
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = harmonixColors.surfaceRaised,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    Toast.makeText(context, "Pronunciation Lexicon: 2 rules active (Proust, Ishiguro)", Toast.LENGTH_SHORT).show()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Spellcheck,
                                        contentDescription = null,
                                        tint = PrimaryAmber,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Pronunciation Lexicon",
                                        fontFamily = ManropeFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = harmonixColors.textPrimary
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SecondaryFixed)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "2 custom rules active",
                                            fontFamily = ManropeFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = Color(0xFF653E00)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = harmonixColors.textSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Synthesize Action CTA
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(
                        onClick = { viewModel.triggerSynthesize() },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryAmber),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("synthesize_immersion_button"),
                        enabled = !uiState.isSynthesizing
                    ) {
                        if (uiState.isSynthesizing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Synthesizing Sentences...",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Synthesize • Begin Immersion",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "Est. generation time 42s • Cache stored locally on device",
                        fontFamily = ManropeFontFamily,
                        fontSize = 11.sp,
                        color = harmonixColors.textSecondary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun StageRow(
    title: String,
    isComplete: Boolean,
    modifier: Modifier = Modifier
) {
    val harmonixColors = LocalHarmonixColors.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(PrimaryAmber),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
            Text(
                text = title,
                fontFamily = ManropeFontFamily,
                fontSize = 12.sp,
                color = harmonixColors.textPrimary
            )
        }
        Text(
            text = "Completed",
            fontFamily = ManropeFontFamily,
            fontSize = 11.sp,
            color = harmonixColors.textSecondary
        )
    }
}
