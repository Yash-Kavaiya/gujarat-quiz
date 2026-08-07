package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SeedData
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors

@Composable
fun CapsulesScreen(onBack: () -> Unit) {
    val colors = LocalAppColors.current

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
                text = "Revision Capsules (Micro Notes)",
                fontSize = 18.sp,
                fontFamily = DisplayFontFamily,
                fontWeight = FontWeight.Bold,
                color = colors.ink
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            items(SeedData.sampleCapsules) { capsule ->
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
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = capsule.contentEn, fontSize = 14.sp, color = colors.ink, lineHeight = 20.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = capsule.contentGu, fontSize = 13.sp, color = colors.inkSecondary, lineHeight = 19.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.Bookmark, contentDescription = "Bookmark", tint = colors.muted)
                            }
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.Share, contentDescription = "Share", tint = colors.muted)
                            }
                        }
                    }
                }
            }
        }
    }
}
