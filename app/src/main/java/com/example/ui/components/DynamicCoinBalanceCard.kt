package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors
import com.example.ui.theme.NumericFontFamily

@Composable
fun DynamicCoinBalanceCard(
    coinBalance: Int,
    accuracyPercentage: Int,
    streakDays: Int,
    totalQuizzes: Int,
    onEarnMoreClick: () -> Unit,
    onOpenShopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    // Smooth animated number counter for coin balance
    val animatedCoins by animateIntAsState(
        targetValue = coinBalance,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "coin_counter_anim"
    )

    // Glowing icon pulse effect
    val infiniteTransition = rememberInfiniteTransition(label = "coin_glow_pulse")
    val coinScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "coin_scale"
    )

    var showDetails by remember { mutableStateOf(false) }

    // Dynamic performance multiplier calculation based on metrics
    val multiplier = when {
        accuracyPercentage >= 85 -> 2.0f
        accuracyPercentage >= 70 -> 1.5f
        accuracyPercentage >= 50 -> 1.2f
        else -> 1.0f
    }

    val multiplierTierLabel = when {
        accuracyPercentage >= 85 -> "Legend Tier (2.0x)"
        accuracyPercentage >= 70 -> "Master Tier (1.5x)"
        accuracyPercentage >= 50 -> "Scholar Tier (1.2x)"
        else -> "Starter Rate (1.0x)"
    }

    val estimatedYieldPerQuiz = ((10 * 25 * multiplier) + (streakDays * 5)).toInt()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dynamic_coin_balance_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.ink),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row: Label + Multiplier Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .scale(coinScale)
                            .testTag("glowing_coin_icon")
                    ) {
                        GoldCoinIcon(sizeDp = 28)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "CURRENT COIN BALANCE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accentGold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Dynamic Quiz Rewards Active",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                // Dynamic Multiplier Badge
                Surface(
                    color = colors.primary,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "%.1fx Multiplier".format(multiplier),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Large Animated Coin Counter
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = String.format("%,d", animatedCoins),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = NumericFontFamily,
                        color = Color.White,
                        modifier = Modifier.testTag("coin_balance_counter")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Coins",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.accentGold
                    )
                }

                Surface(
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.clickable { showDetails = !showDetails }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (showDetails) "Hide Formula" else "Performance Formula",
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = if (showDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metrics Bar Overview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetricPill(
                    icon = Icons.Outlined.Star,
                    label = "Accuracy",
                    value = "$accuracyPercentage%",
                    tint = colors.primary
                )
                Divider(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp),
                    color = Color.White.copy(alpha = 0.15f)
                )
                MetricPill(
                    icon = Icons.Outlined.FlashOn,
                    label = "Streak Yield",
                    value = "+${streakDays * 5} pts",
                    tint = colors.accentGold
                )
                Divider(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp),
                    color = Color.White.copy(alpha = 0.15f)
                )
                MetricPill(
                    icon = Icons.Outlined.Speed,
                    label = "Avg Yield",
                    value = "~$estimatedYieldPerQuiz/quiz",
                    tint = colors.correctGreen
                )
            }

            // Expandable Performance Formula Breakdown
            AnimatedVisibility(
                visible = showDetails,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .testTag("performance_metrics_breakdown")
                ) {
                    Divider(color = Color.White.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "DYNAMIC QUIZ REWARD FORMULA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.accentGold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FormulaRow(
                        title = "Base Answer Reward",
                        detail = "25 Coins per correct answer",
                        value = "+25/Q"
                    )
                    FormulaRow(
                        title = "Accuracy Multiplier",
                        detail = "$multiplierTierLabel ($accuracyPercentage% Accuracy)",
                        value = "${multiplier}x"
                    )
                    FormulaRow(
                        title = "Daily Streak Multiplier",
                        detail = "$streakDays Day Streak streak bonus",
                        value = "+${streakDays * 5} Coins"
                    )
                    FormulaRow(
                        title = "Perfect Score Bonus",
                        detail = "100% Accuracy instant reward",
                        value = "+200 Coins"
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = colors.primary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = colors.accentGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Higher accuracy directly boosts coin multipliers for all upcoming quizzes and daily arenas!",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Earn Coins & Coin Shop
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onEarnMoreClick,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("earn_coins_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Earn Coins",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }

                OutlinedButton(
                    onClick = onOpenShopClick,
                    shape = RoundedCornerShape(14.dp),
                    border = borderColor(Color.White.copy(alpha = 0.4f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("spend_coins_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GoldCoinIcon(sizeDp = 16)
                        Text(
                            text = "Coin Vault",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    tint: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun FormulaRow(
    title: String,
    detail: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            Text(text = detail, fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
        }
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD54F),
            fontFamily = NumericFontFamily
        )
    }
}

private fun borderColor(color: Color) = androidx.compose.foundation.BorderStroke(1.dp, color)
