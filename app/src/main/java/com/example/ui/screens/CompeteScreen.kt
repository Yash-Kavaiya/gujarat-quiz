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
import com.example.data.SeedData
import com.example.ui.components.CoinBalanceBadge
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors

@Composable
fun CompeteScreen(
    userCoins: Int,
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
            0 -> LeaderboardTab(onStartDailyChallenge = onStartDailyChallenge)
            1 -> DuelsTab(onStartDuel = onStartDuel)
            2 -> MockTestsTab(onOpenMockTest = onOpenMockTest)
        }
    }
}

@Composable
fun LeaderboardTab(onStartDailyChallenge: () -> Unit) {
    val colors = LocalAppColors.current
    val entries = SeedData.sampleLeaderboard

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            // Daily Challenge Banner
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.ink),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
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
                        Text(text = "Entry: 50 coins · Top 10 win rewards", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
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

        item {
            Text(
                text = "State Rankings (This Week)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colors.ink,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Podium Top 3
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {

                // Rank 2
                val rank2 = entries.find { it.rank == 2 }
                if (rank2 != null) PodiumCard(entry = rank2, height = 110.dp, color = Color(0xFFC0C0C0))

                // Rank 1
                val rank1 = entries.find { it.rank == 1 }
                if (rank1 != null) PodiumCard(entry = rank1, height = 140.dp, color = colors.primary)

                // Rank 3
                val rank3 = entries.find { it.rank == 3 }
                if (rank3 != null) PodiumCard(entry = rank3, height = 90.dp, color = Color(0xFFCD7F32))
            }
        }

        itemsIndexed(entries.filter { it.rank > 3 }) { _, item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (item.isUser) colors.primaryLight else colors.card
                ),
                border = if (item.isUser) androidx.compose.foundation.BorderStroke(1.5.dp, colors.primary) else null,
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
                            Text(text = item.avatar, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = item.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                            Text(text = item.district, fontSize = 12.sp, color = colors.muted)
                        }
                    }
                    Text(
                        text = "${item.score} pts",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
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
