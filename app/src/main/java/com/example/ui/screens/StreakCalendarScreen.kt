package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActivityDayEntity
import com.example.data.DayClock
import com.example.data.Milestones
import com.example.data.UserProfileEntity
import com.example.ui.components.GoldCoinIcon
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private data class CalendarCell(val dayNumber: Int, val epochDay: Long)

@Composable
fun StreakCalendarScreen(
    profile: UserProfileEntity,
    onBuyFreeze: () -> Unit,
    onClaimMilestone: (Int, Int) -> Unit,
    onBack: () -> Unit,
    displayStreak: Int = profile.currentStreak,
    activityDays: List<ActivityDayEntity> = emptyList()
) {
    val colors = LocalAppColors.current
    val today = remember { DayClock.epochDay() }
    val activityByDay = remember(activityDays) { activityDays.associateBy { it.epochDay } }
    val claimed = remember(profile.claimedMilestones) { Milestones.parseClaimed(profile.claimedMilestones) }

    // Build the current month's grid (Sunday-first), with nulls as leading blanks.
    val (monthTitle, cells) = remember(today) {
        val cal = DayClock.calendarFor(today)
        val title = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(cal.time).uppercase()
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val firstDayEpoch = today - (dayOfMonth - 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val leadingBlanks = cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val list = List<CalendarCell?>(leadingBlanks) { null } +
            (1..daysInMonth).map { CalendarCell(it, firstDayEpoch + it - 1) }
        title to list
    }
    val activeThisMonth = cells.count { cell -> cell != null && activityByDay[cell.epochDay]?.let { !it.isFrozen } == true }

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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Streak Hero Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.primaryLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = colors.primary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "$displayStreak DAY STREAK", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.primary, letterSpacing = 1.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        val playedToday = activityByDay[today]?.let { !it.isFrozen } == true
                        Text(
                            text = when {
                                playedToday -> "Today's done. Keep it up!"
                                displayStreak > 0 -> "Play a quiz today to extend it"
                                else -> "Start a streak today!"
                            },
                            fontSize = 18.sp,
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.ink
                        )
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = monthTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.muted, letterSpacing = 1.sp)
                Text(text = "$activeThisMonth active days", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.primary)
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Weekday header
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { label ->
                    Text(
                        text = label,
                        modifier = Modifier.weight(1f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.muted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Month grid
            cells.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (0 until 7).forEach { i ->
                        val cell = week.getOrNull(i)
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f), contentAlignment = Alignment.Center) {
                            if (cell != null) {
                                val activity = activityByDay[cell.epochDay]
                                val isCompleted = activity != null && !activity.isFrozen
                                val isFrozen = activity?.isFrozen == true
                                val isToday = cell.epochDay == today
                                val isFuture = cell.epochDay > today
                                val bg = when {
                                    isCompleted -> colors.primary
                                    isFrozen -> Color(0xFF64B5F6)
                                    isToday -> colors.primaryLight
                                    else -> colors.card
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(bg)
                                        .border(if (isToday) 2.dp else 0.dp, if (isToday) colors.primary else Color.Transparent, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${cell.dayNumber}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isCompleted || isFrozen -> Color.White
                                            isFuture -> colors.muted
                                            else -> colors.ink
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                LegendDot(colors.primary, "Played")
                LegendDot(Color(0xFF64B5F6), "Freeze used")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "STREAK MILESTONES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.muted, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Milestones.all.forEach { milestone ->
                val isClaimed = milestone.days in claimed
                val isUnlocked = profile.longestStreak >= milestone.days
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.card),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when {
                                isClaimed -> Icons.Default.CheckCircle
                                isUnlocked -> Icons.Default.LocalFireDepartment
                                else -> Icons.Default.Lock
                            },
                            contentDescription = null,
                            tint = if (isUnlocked) colors.primary else colors.muted
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "${milestone.days}-Day Streak", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                GoldCoinIcon(sizeDp = 14)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "+${milestone.rewardCoins} coins", fontSize = 12.sp, color = colors.muted)
                            }
                        }
                        when {
                            isClaimed -> Text("Claimed", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.correctGreen)
                            isUnlocked -> Button(
                                onClick = { onClaimMilestone(milestone.days, milestone.rewardCoins) },
                                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                                shape = RoundedCornerShape(12.dp)
                            ) { Text("Claim", color = Color.White, fontWeight = FontWeight.Bold) }
                            else -> Text(
                                "${(milestone.days - displayStreak).coerceAtLeast(1)} days to go",
                                fontSize = 12.sp,
                                color = colors.muted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onBuyFreeze,
                colors = ButtonDefaults.buttonColors(containerColor = colors.ink),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.AcUnit, contentDescription = null, tint = colors.accentGold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Buy Streak Freeze (200 Coins)", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A freeze automatically protects your streak for one missed day.",
                fontSize = 12.sp,
                color = colors.muted
            )
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    val colors = LocalAppColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, color = colors.muted)
    }
}
