package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.SampleData
import com.example.model.AudiobookItem
import com.example.model.BookCategory
import com.example.ui.theme.LiterataFontFamily
import com.example.ui.theme.LocalHarmonixColors
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.viewmodel.HarmonixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: HarmonixViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val harmonixColors = LocalHarmonixColors.current
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    BackHandler {
        viewModel.navigateBack()
    }

    val allItems = if (uiState.librivoxBooks.isNotEmpty()) {
        uiState.librivoxBooks + SampleData.LibraryShelf
    } else {
        SampleData.LibraryShelf
    }

    val segmentTabs = listOf(
        "All" to "${allItems.size}",
        "Audiobooks" to "${allItems.count { it.category != BookCategory.MY_DOCUMENTS }}",
        "My Documents" to "${allItems.count { it.category == BookCategory.MY_DOCUMENTS }}",
        "Downloaded" to "${allItems.count { it.isDownloaded }}"
    )
    val filterChips = listOf("Currently Reading", "Want to Read", "Finished", "Notes & Quotes")

    val shelfItems = when (uiState.librarySegmentIndex) {
        1 -> allItems.filter { it.category != BookCategory.MY_DOCUMENTS }
        2 -> allItems.filter { it.category == BookCategory.MY_DOCUMENTS }
        3 -> allItems.filter { it.isDownloaded }
        else -> allItems
    }.filter {
        uiState.librarySearchQuery.isEmpty() ||
                it.title.contains(uiState.librarySearchQuery, ignoreCase = true) ||
                it.author.contains(uiState.librarySearchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search & Quick Action Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.librarySearchQuery,
                onValueChange = { viewModel.setLibrarySearchQuery(it) },
                placeholder = {
                    Text(
                        text = "Search 18 titles, documents & notes...",
                        fontFamily = ManropeFontFamily,
                        fontSize = 13.sp,
                        color = harmonixColors.textSecondary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = harmonixColors.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (uiState.librarySearchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setLibrarySearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = harmonixColors.surfaceSunken,
                    unfocusedContainerColor = harmonixColors.surfaceSunken,
                    focusedBorderColor = PrimaryAmber,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .testTag("library_search_input")
            )

            IconButton(
                onClick = {
                    Toast.makeText(context, "Sort: Recently played • Filter: Synchronized", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(harmonixColors.surfaceSunken)
                    .testTag("library_filter_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Sort and filter options",
                    tint = harmonixColors.textSecondary
                )
            }
        }

        // Primary Segmented Control (Swipeable Tab Rail)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(harmonixColors.surfaceSunken)
                .padding(4.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            segmentTabs.forEachIndexed { index, (label, count) ->
                val isSelected = uiState.librarySegmentIndex == index
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) harmonixColors.surfaceRaised else Color.Transparent)
                        .clickable { viewModel.setLibrarySegment(index) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("library_tab_$index")
                ) {
                    if (label == "Downloaded") {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = null,
                            tint = PrimaryAmber,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = label,
                        fontFamily = ManropeFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        color = if (isSelected) PrimaryAmber else harmonixColors.textSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) PrimaryFixed else harmonixColors.surfaceSunken)
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = count,
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            color = if (isSelected) PrimaryAmber else harmonixColors.textSecondary
                        )
                    }
                }
            }
        }

        // Secondary Filter Chips Rail
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterChips.forEachIndexed { index, chip ->
                val isSelected = uiState.libraryFilterChipIndex == index
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) PrimaryAmber else harmonixColors.surfaceSunken)
                        .clickable { viewModel.setLibraryFilterChip(index) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("filter_chip_$index")
                ) {
                    if (index == 0) {
                        Icon(
                            imageVector = Icons.Default.Timelapse,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else harmonixColors.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    } else if (index == 3) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else PrimaryAmber,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = chip,
                        fontFamily = ManropeFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp,
                        color = if (isSelected) Color.White else harmonixColors.textSecondary
                    )
                }
            }
        }

        // Offline Storage & Health Banner Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceSunken),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                                .clip(RoundedCornerShape(8.dp))
                                .background(harmonixColors.surfaceRaised),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.OfflinePin,
                                contentDescription = null,
                                tint = PrimaryAmber,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Offline Hub & Storage",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = harmonixColors.textPrimary
                            )
                            Text(
                                text = "1.32 GB utilized • 42 GB free on device",
                                fontFamily = ManropeFontFamily,
                                fontSize = 11.sp,
                                color = harmonixColors.textSecondary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(harmonixColors.surfaceRaised)
                            .clickable {
                                Toast.makeText(context, "Device storage optimized: 1.32 GB cached", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("manage_files_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = harmonixColors.textSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Manage Files",
                                fontFamily = ManropeFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = harmonixColors.textPrimary
                            )
                        }
                    }
                }

                // Storage Telemetry Multi-Segment Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(harmonixColors.surfaceRaised)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.76f)
                            .height(10.dp)
                            .background(SecondaryContainer)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.38f) // 9% of remaining
                            .height(10.dp)
                            .background(PrimaryAmber)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(harmonixColors.borderHairline)
                    )
                }

                // Legend items
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LegendItem("Audio (1.2 GB)", SecondaryContainer)
                    LegendItem("Docs (45 MB)", PrimaryAmber)
                    LegendItem("Cache (80 MB)", harmonixColors.borderHairline)
                }
            }
        }

        // Active Shelf Header & List/Grid toggles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Active Shelf",
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = harmonixColors.textPrimary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.ViewModule, contentDescription = "Grid", tint = PrimaryAmber, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.ViewList, contentDescription = "List", tint = harmonixColors.textSecondary, modifier = Modifier.size(20.dp))
                }
            }
        }

        // Shelf Items
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            shelfItems.forEach { book ->
                ShelfItemCard(
                    book = book,
                    onPlay = { viewModel.selectBookToPlay(book) },
                    onMore = {
                        Toast.makeText(context, "${book.title} options", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // Recent Highlights & Quotes Widget
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = SecondaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Highlights & Quotes",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = harmonixColors.textPrimary
                    )
                }

                Text(
                    text = "View All (43)",
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = PrimaryAmber,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "43 highlights synchronized with cloud", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Interactive Highlight Showcase Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceRaised),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(SecondaryContainer)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${uiState.activeHighlight.bookTitle} • ${uiState.activeHighlight.chapter}",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp,
                                color = harmonixColors.textSecondary
                            )
                        }

                        Text(
                            text = uiState.activeHighlight.dateText,
                            fontFamily = ManropeFontFamily,
                            fontSize = 11.sp,
                            color = harmonixColors.textSecondary
                        )
                    }

                    // Quote Body
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(46.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(SecondaryContainer)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "“${uiState.activeHighlight.quote}”",
                            fontFamily = LiterataFontFamily,
                            fontStyle = FontStyle.Italic,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = harmonixColors.textPrimary
                        )
                    }

                    // Marginalia Note
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = harmonixColors.surfaceSunken,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    tint = PrimaryAmber,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "My Marginalia Note",
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = PrimaryAmber
                                )
                            }
                            Text(
                                text = uiState.activeHighlight.marginaliaNote,
                                fontFamily = ManropeFontFamily,
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                color = harmonixColors.textSecondary
                            )
                        }
                    }

                    // Action Footer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(uiState.activeHighlight.quote))
                                    Toast.makeText(context, "Quote copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(harmonixColors.surfaceSunken)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy quote",
                                    tint = harmonixColors.textSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            IconButton(
                                onClick = { viewModel.togglePlayback() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(harmonixColors.surfaceSunken)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = "Replay audio snippet",
                                    tint = PrimaryAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.openQuoteModal(uiState.activeHighlight) },
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryContainer),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("create_quote_card_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Style,
                                contentDescription = null,
                                tint = Color(0xFF684000),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Create Quote Card",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF684000)
                            )
                        }
                    }
                }
            }
        }

        // Quote Card Creator Modal Sheet
        if (uiState.isQuoteModalOpen) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.closeQuoteModal() },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = harmonixColors.surfaceRaised
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 36.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Export Quote Card",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = harmonixColors.textPrimary
                        )

                        IconButton(onClick = { viewModel.closeQuoteModal() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = harmonixColors.textSecondary)
                        }
                    }

                    // Card Preview
                    val cardBgColors = listOf(
                        Color(0xFFFEF3C7) to Color(0xFF78350F), // Amber
                        Color(0xFFE0E7FF) to Color(0xFF312E81), // Indigo
                        Color(0xFFFEE2E2) to Color(0xFF881337), // Rose
                        Color(0xFF1E293B) to Color(0xFFF8FAFC)  // Slate Dark
                    )
                    val (previewBg, previewText) = cardBgColors[uiState.quoteModalPaletteIndex]

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = previewBg,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = PrimaryAmber,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "“${uiState.activeHighlight.quote}”",
                                fontFamily = LiterataFontFamily,
                                fontStyle = FontStyle.Italic,
                                fontSize = 17.sp,
                                lineHeight = 26.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                color = previewText
                            )
                            Text(
                                text = "Kazuo Ishiguro • Klara and the Sun",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = previewText.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Color Palette Picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        cardBgColors.forEachIndexed { idx, (bg, _) ->
                            val isSelected = uiState.quoteModalPaletteIndex == idx
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(bg)
                                    .border(
                                        width = if (isSelected) 2.5.dp else 1.dp,
                                        color = if (isSelected) PrimaryAmber else Color.Gray.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.setQuoteModalPalette(idx) }
                            )
                        }
                    }

                    // Share to Story Action
                    Button(
                        onClick = { viewModel.shareQuoteCard() },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryAmber),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("share_quote_card_action")
                    ) {
                        if (uiState.userQuoteExportedToast) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Exported!",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        } else {
                            Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Share to Story",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShelfItemCard(
    book: AudiobookItem,
    onPlay: () -> Unit,
    onMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val harmonixColors = LocalHarmonixColors.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = harmonixColors.surfaceRaised),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("shelf_card_${book.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(115.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(harmonixColors.surfaceSunken)
            ) {
                if (book.coverUrl.isNotEmpty()) {
                    AsyncImage(
                        model = book.coverUrl,
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = PrimaryAmber,
                            modifier = Modifier.size(34.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "PDF • 14 pgs",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = harmonixColors.textSecondary
                        )
                    }
                }

                if (book.isDownloaded) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Downloaded",
                            tint = PrimaryAmber,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(115.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(PrimaryFixed)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = book.formatTag,
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = PrimaryAmber
                            )
                        }

                        if (book.isSynchronized) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(harmonixColors.surfaceSunken)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Sync 2.0",
                                    fontFamily = ManropeFontFamily,
                                    fontSize = 10.sp,
                                    color = harmonixColors.textSecondary
                                )
                            }
                        }
                    }

                    Text(
                        text = book.title,
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = harmonixColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${book.author} • ${book.narrator}",
                        fontFamily = ManropeFontFamily,
                        fontSize = 11.sp,
                        color = harmonixColors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${book.currentChapter} • ${book.remainingText}",
                            fontFamily = ManropeFontFamily,
                            fontSize = 10.sp,
                            color = harmonixColors.textSecondary
                        )
                        Text(
                            text = "${book.progressPercent}%",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = PrimaryAmber
                        )
                    }

                    LinearProgressIndicator(
                        progress = { book.progressPercent / 100f },
                        color = SecondaryContainer,
                        trackColor = harmonixColors.surfaceSunken,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onPlay,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryAmber),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                    ) {
                        Icon(
                            imageVector = if (book.category == BookCategory.MY_DOCUMENTS) Icons.Default.VolumeUp else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (book.category == BookCategory.MY_DOCUMENTS) "Read Aloud" else if (book.progressPercent > 0) "Resume" else "Listen",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                    IconButton(
                        onClick = onMore,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(harmonixColors.surfaceSunken)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Options",
                            tint = harmonixColors.textSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(text: String, dotColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontFamily = ManropeFontFamily,
            fontSize = 10.sp,
            color = LocalHarmonixColors.current.textSecondary
        )
    }
}
