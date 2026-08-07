package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SeedData
import com.example.data.UserProfileEntity
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors

@Composable
fun StreakCalendarScreen(
    profile: UserProfileEntity,
    onBuyFreeze: () -> Unit,
    onClaimMilestone: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
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
                text = "Daily Streak & Rewards",
                fontSize = 20.sp,
                fontFamily = DisplayFontFamily,
                fontWeight = FontWeight.Bold,
                color = colors.ink
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Streak Hero Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.primaryLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = colors.primary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "${profile.currentStreak} DAY STREAK", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.primary, letterSpacing = 1.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Unstoppable Learning!", fontSize = 20.sp, fontFamily = DisplayFontFamily, fontWeight = FontWeight.Bold, color = colors.ink)
                        Text(text = "Longest streak: ${profile.longestStreak} days", fontSize = 12.sp, color = colors.muted)
                    }

                    Surface(
                        color = colors.card,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(1.dp, colors.primary, RoundedCornerShape(12.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.AcUnit, contentDescription = null, tint = colors.primary)
                            Text(text = "${profile.freezesOwned} Freezes", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "MARCH 2026 CALENDAR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.muted, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))

            // 31-Day Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val days = SeedData.marchStreakDays
                items(days) { day ->
                    val isToday = day.dayNumber == 7
                    val bg = when {
                        day.isCompleted -> colors.primary
                        day.isFrozen -> Color(0xFF64B5F6)
                        isToday -> colors.primaryLight
                        else -> colors.card
                    }

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(bg)
                            .border(if (isToday) 2.dp else 0.dp, colors.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${day.dayNumber}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (day.isCompleted || day.isFrozen) Color.White else colors.ink
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBuyFreeze,
                colors = ButtonDefaults.buttonColors(containerColor = colors.ink),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Icon(Icons.Default.AcUnit, contentDescription = null, tint = colors.accentGold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Buy Streak Freeze (200 Coins)", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
