package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Capsule
import com.example.data.SeedData
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors

@Composable
fun CapsulesScreen(
    onBack: () -> Unit,
    bookmarkedIds: Set<String> = emptySet(),
    onToggleBookmark: (String) -> Unit = {},
    onPractice: (String) -> Unit = {}
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current
    var showSavedOnly by remember { mutableStateOf(false) }
    val capsules = if (showSavedOnly) SeedData.sampleCapsules.filter { it.id in bookmarkedIds } else SeedData.sampleCapsules

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.ink)
            }
            Text(
                text = "Revision Capsules",
                fontSize = 18.sp,
                fontFamily = DisplayFontFamily,
                fontWeight = FontWeight.Bold,
                color = colors.ink
            )
        }

        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = !showSavedOnly,
                onClick = { showSavedOnly = false },
                label = { Text("All (${SeedData.sampleCapsules.size})") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.primary, selectedLabelColor = Color.White)
            )
            FilterChip(
                selected = showSavedOnly,
                onClick = { showSavedOnly = true },
                label = { Text("Saved (${bookmarkedIds.size})") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.primary, selectedLabelColor = Color.White)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (capsules.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No saved capsules yet. Tap the bookmark icon on any capsule to save it for quick revision.",
                    fontSize = 14.sp,
                    color = colors.muted
                )
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 40.dp)
        ) {
            items(capsules, key = { it.id }) { capsule ->
                val isBookmarked = capsule.id in bookmarkedIds
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.card),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = colors.primaryLight,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = capsule.categoryTitleEn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Text(text = capsule.readTime, fontSize = 12.sp, color = colors.muted)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = capsule.titleEn, fontSize = 18.sp, fontFamily = DisplayFontFamily, fontWeight = FontWeight.Bold, color = colors.ink)
                        Text(text = capsule.titleGuj, fontSize = 14.sp, color = colors.inkSecondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = capsule.tldr, fontSize = 14.sp, color = colors.ink, lineHeight = 20.sp)

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "KEY FACTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.primary, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        capsule.keyFacts.forEach { fact ->
                            Row(modifier = Modifier.padding(vertical = 3.dp)) {
                                Text(text = "•", fontSize = 14.sp, color = colors.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = fact, fontSize = 13.sp, color = colors.ink, lineHeight = 19.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { onPractice(capsule.relatedCategoryId) },
                                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Practice", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { onToggleBookmark(capsule.id) }) {
                                Icon(
                                    if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark",
                                    tint = if (isBookmarked) colors.primary else colors.muted
                                )
                            }
                            IconButton(onClick = { shareCapsule(context, capsule) }) {
                                Icon(Icons.Default.Share, contentDescription = "Share", tint = colors.muted)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun shareCapsule(context: Context, capsule: Capsule) {
    val text = buildString {
        appendLine("📘 ${capsule.titleEn} (${capsule.titleGuj})")
        appendLine(capsule.tldr)
        appendLine()
        capsule.keyFacts.forEach { appendLine("• $it") }
        appendLine()
        append("Revise more with Gujarat Quiz!")
    }
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, capsule.titleEn)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share capsule"))
}
