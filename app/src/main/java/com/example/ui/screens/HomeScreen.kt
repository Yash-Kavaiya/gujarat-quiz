package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuizCategory
import com.example.data.SeedData
import com.example.ui.components.DynamicCoinBalanceCard
import com.example.ui.components.GoldCoinIcon
import com.example.ui.components.TopBar
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    userName: String,
    userInitials: String,
    streakDays: Int,
    coinBalance: Int,
    accuracy: Int,
    rank: Int,
    quizzesCount: Int,
    onStartDailyChallenge: () -> Unit,
    onCategoryClick: (QuizCategory) -> Unit,
    onSeeAllCategories: () -> Unit,
    onInviteClick: () -> Unit,
    onLeaderboardTeaserClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onStreakClick: () -> Unit,
    onCoinsClick: () -> Unit,
    onCapsulesClick: () -> Unit,
    onDuelClick: () -> Unit,
    onMockTestClick: () -> Unit
) {
    val colors = LocalAppColors.current

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Daily Challenge Hero Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("daily_challenge_hero"),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF2C224E), Color(0xFFC86D12))
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = "TODAY · 18 APRIL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                color = Color(0xFFFFD54F)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Daily Challenge:\nConstitution & Polity",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = DisplayFontFamily,
                                color = Color.White,
                                lineHeight = 28.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Timer, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("10 min", fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                                }
                                Text("·", color = Color.White.copy(alpha = 0.6f))
                                Text("15 Qs", fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                                Text("·", color = Color.White.copy(alpha = 0.6f))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    GoldCoinIcon(sizeDp = 16)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("5,000 prize pool", fontSize = 13.sp, color = Color(0xFFFFD54F), fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onStartDailyChallenge,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.testTag("enter_daily_challenge")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("Enter · 50 coins", color = Color(0xFF2C224E), fontWeight = FontWeight.Bold)
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF2C224E), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Dynamic Coin Balance Component
            item {
                DynamicCoinBalanceCard(
                    coinBalance = coinBalance,
                    accuracyPercentage = accuracy,
                    streakDays = streakDays,
                    totalQuizzes = quizzesCount,
                    onEarnMoreClick = onStartDailyChallenge,
                    onOpenShopClick = onCoinsClick
                )
            }

            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tile 1: Accuracy
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.card)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("$accuracy%", fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = NumericFontFamily, color = colors.ink)
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Accuracy", fontSize = 11.sp, color = colors.muted)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+4", fontSize = 11.sp, color = colors.success, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Tile 2: Rank
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.card)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("#$rank", fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = NumericFontFamily, color = colors.ink)
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Rank", fontSize = 11.sp, color = colors.muted)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("↑ 38", fontSize = 11.sp, color = colors.success, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Tile 3: Quizzes
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.card)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("$quizzesCount", fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = NumericFontFamily, color = colors.ink)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Quizzes", fontSize = 11.sp, color = colors.muted)
                        }
                    }
                }
            }

            // Quick Actions: Capsules, 1v1 Duel, Mock Test
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onCapsulesClick() },
                        shape = RoundedCornerShape(16.dp),
                        color = colors.primaryLight
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("📖", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Study Capsules", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.primary)
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onDuelClick() },
                        shape = RoundedCornerShape(16.dp),
                        color = colors.card
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("⚔️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("1v1 Duel", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onMockTestClick() },
                        shape = RoundedCornerShape(16.dp),
                        color = colors.card
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("📝", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mock Test", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                        }
                    }
                }
            }

            // Recommended for you Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recommended for you",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = DisplayFontFamily,
                        color = colors.ink
                    )
                    Text(
                        text = "See all 25 →",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary,
                        modifier = Modifier.clickable { onSeeAllCategories() }
                    )
                }
            }

            // Horizontal Recommended Cards
            item {
                val recs = SeedData.categories.filter { it.isRecommended }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recs) { cat ->
                        Card(
                            modifier = Modifier
                                .width(170.dp)
                                .clickable { onCategoryClick(cat) },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.card)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(colors.cardSecondary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(cat.initials, fontWeight = FontWeight.Bold, color = colors.primary)
                                    }
                                    if (cat.tag != null) {
                                        Surface(
                                            color = colors.primary,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                cat.tag,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = cat.titleEn,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.ink,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${cat.questionCount} Qs · ${cat.difficulty}",
                                    fontSize = 11.sp,
                                    color = colors.muted
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    GoldCoinIcon(sizeDp = 16)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("${cat.coinCost}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                                }
                            }
                        }
                    }
                }
            }

            // Invite a friend banner
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onInviteClick() }
                        .testTag("invite_friend_banner"),
                    shape = RoundedCornerShape(20.dp),
                    color = colors.card,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, colors.primary)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Invite a friend",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = DisplayFontFamily,
                                color = colors.ink
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "You both get +500 coins on their first quiz",
                                fontSize = 12.sp,
                                color = colors.muted
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(colors.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }

            // Continue preparing vertical list
            item {
                Text(
                    text = "Continue preparing",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = DisplayFontFamily,
                    color = colors.ink
                )
            }

            items(SeedData.categories.take(4)) { cat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoryClick(cat) },
                    shape = RoundedCornerShape(16.dp),
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
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colors.cardSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(cat.initials, fontWeight = FontWeight.Bold, color = colors.primary, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(cat.titleEn, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                                Text(cat.titleGuj, fontSize = 12.sp, color = colors.muted)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = colors.paper
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GoldCoinIcon(sizeDp = 14)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${cat.coinCost}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                            }
                        }
                    }
                }
            }

            // Leaderboard Teaser
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLeaderboardTeaserClick() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.ink)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("WEEKLY COMPETITION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colors.accent, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Leaderboard closes in 2d 14h", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Top 3 aspirants win 5,000 coins each", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = colors.accent)
                    }
                }
            }
        }
    }
}
