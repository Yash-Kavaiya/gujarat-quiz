package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuizCategory
import com.example.data.SeedData
import com.example.ui.components.GoldCoinIcon
import com.example.ui.components.TopBar
import com.example.ui.theme.*

@Composable
fun CategoriesScreen(
    userName: String,
    userInitials: String,
    streakDays: Int,
    coinBalance: Int,
    onCategoryClick: (QuizCategory) -> Unit,
    onAvatarClick: () -> Unit,
    onStreakClick: () -> Unit,
    onCoinsClick: () -> Unit
) {
    val colors = LocalAppColors.current
    var activeFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "🔥 Hot", "Easy", "Medium", "Hard")

    val filteredCategories = remember(activeFilter) {
        when (activeFilter) {
            "🔥 Hot" -> SeedData.categories.filter { it.tag == "HOT" || it.tag == "TRENDING" }
            "Easy" -> SeedData.categories.filter { it.difficulty == "Easy" }
            "Medium" -> SeedData.categories.filter { it.difficulty == "Medium" }
            "Hard" -> SeedData.categories.filter { it.difficulty == "Hard" }
            else -> SeedData.categories
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
    ) {
        TopBar(
            userName = userName,
            userInitials = userInitials,
            streakDays = streakDays,
            coinBalance = coinBalance,
            onAvatarClick = onAvatarClick,
            onStreakClick = onStreakClick,
            onCoinsClick = onCoinsClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp, start = 16.dp, end = 16.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "Categories",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = DisplayFontFamily,
                        color = colors.ink
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "25 active · 17,342 questions total",
                        fontSize = 14.sp,
                        color = colors.muted
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Filter chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filters) { filter ->
                            val isSelected = activeFilter == filter
                            FilterChip(
                                selected = isSelected,
                                onClick = { activeFilter = filter },
                                label = {
                                    Text(
                                        text = filter,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colors.ink,
                                    selectedLabelColor = Color.White,
                                    containerColor = colors.card,
                                    labelColor = colors.ink
                                )
                            )
                        }
                    }
                }
            }

            // Categories list
            items(filteredCategories) { cat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoryClick(cat) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.card)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colors.cardSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat.initials,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primary,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = cat.titleEn,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.ink
                                    )
                                    if (cat.tag != null) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = colors.primary,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = cat.tag,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${cat.titleGuj} · ${cat.questionCount} Qs",
                                    fontSize = 12.sp,
                                    color = colors.muted
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = colors.paper
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GoldCoinIcon(sizeDp = 16)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${cat.coinCost}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.ink,
                                    fontFamily = NumericFontFamily
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
