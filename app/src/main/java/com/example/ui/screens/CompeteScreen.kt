package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DuelFriend
import com.example.data.LeaderboardEntry
import com.example.data.LeaderboardUser
import com.example.data.SeedData
import com.example.ui.components.CoinBalanceBadge
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Storage
import androidx.compose.ui.platform.testTag

@Composable
fun CompeteScreen(
    userCoins: Int,
    leaderboardUsers: List<LeaderboardUser> = emptyList(),
    onStartDailyChallenge: () -> Unit,
    onStartDuel: (DuelFriend) -> Unit,
    onOpenMockTest: () -> Unit
) {
    val colors = LocalAppColors.current
    var selectedTab by remember { mutableStateOf(0) } // 0: Leaderboard, 1: 1v1 Duels, 2: Mock Tests

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "COMPETE & ARENA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = colors.muted
                )
                Text(
                    text = "Rankings & Duels",
                    fontSize = 24.sp,
                    fontFamily = DisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink
                )
            }
            CoinBalanceBadge(coins = userCoins)
        }

        // Sub Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = colors.paper,
            contentColor = colors.primary,
            divider = {}
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Leaderboard", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("1v1 Duels", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Mock Tests", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> LeaderboardTab(
                leaderboardUsers = leaderboardUsers,
                onStartDailyChallenge = onStartDailyChallenge
            )
            1 -> DuelsTab(onStartDuel = onStartDuel)
            2 -> MockTestsTab(onOpenMockTest = onOpenMockTest)
        }
    }
}

@Composable
fun LeaderboardTab(
    leaderboardUsers: List<LeaderboardUser>,
    onStartDailyChallenge: () -> Unit
) {
    val colors = LocalAppColors.current
    val entries = if (leaderboardUsers.isNotEmpty()) leaderboardUsers else SeedData.sampleLeaderboard

    var selectedFilter by remember { mutableStateOf("All Cities") }
    val cities = listOf("All Cities", "Ahmedabad", "Surat", "Rajkot", "Vadodara", "Gandhinagar")

    val filteredEntries = remember(entries, selectedFilter) {
        if (selectedFilter == "All Cities") entries
        else entries.filter { it.district.equals(selectedFilter, ignoreCase = true) || it.isCurrentUser }
    }

    val currentUser = entries.find { it.isCurrentUser }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .testTag("global_leaderboard_list"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Room DB Sync Badge & Hero Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.ink),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("leaderboard_banner_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = colors.primary.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = colors.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ROOM DATABASE SYNC",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primary
                                )
                            }
                        }
                        if (currentUser != null) {
                            Text(
                                text = "Your Rank: #${currentUser.rank}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.accentGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FlashOn, contentDescription = null, tint = colors.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "DAILY ARENA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.primary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Grand Prize: 5,000 Coins", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Earn points in quizzes to climb rankings!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                        }
                        Button(
                            onClick = onStartDailyChallenge,
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Enter", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Filter Chips
        item {
            Column {
                Text(
                    text = "Global & City Leaderboards",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(cities) { city ->
                        FilterChip(
                            selected = selectedFilter == city,
                            onClick = { selectedFilter = city },
                            label = { Text(city, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colors.primary,
                                selectedLabelColor = Color.White,
                                containerColor = colors.card
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        // Podium Top 3
        item {
            val top1 = filteredEntries.getOrNull(0)
            val top2 = filteredEntries.getOrNull(1)
            val top3 = filteredEntries.getOrNull(2)

            if (top1 != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (top2 != null) PodiumCard(entry = top2, height = 110.dp, color = Color(0xFFC0C0C0))
                    PodiumCard(entry = top1, height = 140.dp, color = colors.primary)
                    if (top3 != null) PodiumCard(entry = top3, height = 90.dp, color = Color(0xFFCD7F32))
                }
            }
        }

        // Current User Highlight Card if outside top 3
        if (currentUser != null && currentUser.rank > 3) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.primaryLight),
                    border = androidx.compose.foundation.BorderStroke(2.dp, colors.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("user_current_rank_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = colors.primary,
                                shape = CircleShape,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "#${currentUser.rank}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = currentUser.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(color = colors.primary, shape = RoundedCornerShape(4.dp)) {
                                        Text("YOU", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                                Text(text = "${currentUser.district} · Room DB Live", fontSize = 11.sp, color = colors.muted)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${currentUser.score} pts",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.primary
                            )
                            Text(
                                text = currentUser.delta,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }
        }

        // Remaining Rankings List
        val remainingEntries = filteredEntries.drop(3)
        itemsIndexed(remainingEntries) { _, item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (item.isCurrentUser) colors.primaryLight else colors.card
                ),
                border = if (item.isCurrentUser) androidx.compose.foundation.BorderStroke(1.5.dp, colors.primary) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("leaderboard_item_${item.rank}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "#${item.rank}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.muted,
                            modifier = Modifier.width(36.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(colors.primary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.avatar, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.primary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = item.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                            Text(text = item.district, fontSize = 12.sp, color = colors.muted)
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${item.score} pts",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                        Text(
                            text = item.delta,
                            fontSize = 11.sp,
                            color = colors.muted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumCard(entry: LeaderboardEntry, height: Dp, color: Color) {
    val colors = LocalAppColors.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
                .border(2.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = entry.avatar, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = entry.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colors.ink)
        Text(text = "${entry.score} pts", fontSize = 11.sp, color = colors.muted)
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#${entry.rank}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun DuelsTab(onStartDuel: (DuelFriend) -> Unit) {
    val colors = LocalAppColors.current
    val friends = SeedData.sampleDuelFriends

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = colors.accentGold, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "1v1 Realtime Quiz Duel", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Challenge friends, wager 100 coins, winner takes 200!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }

        item {
            Text(
                text = "Challenge Online Friends",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colors.ink,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        itemsIndexed(friends) { _, friend ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.card),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(colors.primaryLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = friend.avatar, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = friend.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (friend.isOnline) Color(0xFF4CAF50) else colors.muted)
                                )
                            }
                            Text(text = "Win Rate: ${friend.winRate}% · ${friend.district}", fontSize = 12.sp, color = colors.muted)
                        }
                    }
                    Button(
                        onClick = { onStartDuel(friend) },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Challenge", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MockTestsTab(onOpenMockTest: () -> Unit) {
    val colors = LocalAppColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
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
                            text = "FULL MOCK PAPER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(text = "Entry: 100 Coins", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colors.accentGold)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "GPSC Class 1-2 Prelims Model Paper I", fontSize = 20.sp, fontFamily = DisplayFontFamily, fontWeight = FontWeight.Bold, color = colors.ink)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "100 Questions · 2 Hours · CCE Pattern Negative Marking (0.25)", fontSize = 13.sp, color = colors.muted)

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onOpenMockTest,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.ink),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Start Mock Test", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
