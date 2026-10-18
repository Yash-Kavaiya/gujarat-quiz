package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun GoldCoinIcon(sizeDp: Int = 22) {
    Box(
        modifier = Modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFFFE57F), Color(0xFFFFB300), Color(0xFFFF8F00))
                )
            )
            .border(1.dp, Color(0xFFFFF8E1), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "G",
            color = Color(0xFF5D4037),
            fontSize = (sizeDp * 0.58).sp,
            fontFamily = DisplayFontFamily,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CoinBalanceBadge(
    coins: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val colors = LocalAppColors.current
    val animatedCoins by animateIntAsState(
        targetValue = coins,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "coin_badge_anim"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.card,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp,
        modifier = modifier
            .testTag("coin_balance_badge")
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            GoldCoinIcon(sizeDp = 20)
            Text(
                text = String.format("%,d", animatedCoins),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colors.ink,
                fontFamily = NumericFontFamily
            )
        }
    }
}


@Composable
fun TopBar(
    userName: String = "Aakash P.",
    userInitials: String = "AP",
    streakDays: Int = 20,
    coinBalance: Int = 7900,
    onAvatarClick: () -> Unit,
    onStreakClick: () -> Unit,
    onCoinsClick: () -> Unit
) {
    val colors = LocalAppColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Avatar + Greeting
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { onAvatarClick() }
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.primary)
                    .border(1.5.dp, colors.accent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userInitials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = DisplayFontFamily
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "NAMASTE",
                    fontSize = 10.sp,
                    color = colors.muted,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = userName,
                    fontSize = 15.sp,
                    color = colors.ink,
                    fontWeight = FontWeight.Bold,
                    fontFamily = DisplayFontFamily
                )
            }
        }

        // Right: Streak Pill & Coin Balance Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Streak Pill
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = colors.card,
                tonalElevation = 2.dp,
                shadowElevation = 1.dp,
                modifier = Modifier.clickable { onStreakClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "🔥", fontSize = 14.sp)
                    Text(
                        text = "$streakDays",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.ink,
                        fontFamily = NumericFontFamily
                    )
                    Text(
                        text = "day",
                        fontSize = 11.sp,
                        color = colors.muted
                    )
                }
            }

            // Coin Pill
            CoinBalanceBadge(
                coins = coinBalance,
                onClick = onCoinsClick
            )
        }
    }
}

@Composable
fun FloatingBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val colors = LocalAppColors.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .testTag("floating_bottom_nav"),
        shape = RoundedCornerShape(32.dp),
        color = colors.card,
        shadowElevation = 12.dp,
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf(
                Triple(0, "Home", Icons.Outlined.Home to Icons.Filled.Home),
                Triple(1, "Categories", Icons.Outlined.GridView to Icons.Filled.GridView),
                Triple(2, "Compete", Icons.Outlined.EmojiEvents to Icons.Filled.EmojiEvents),
                Triple(3, "Profile", Icons.Outlined.Person to Icons.Filled.Person)
            )

            tabs.forEach { (index, label, icons) ->
                val isSelected = selectedTab == index
                val activeIcon = if (isSelected) icons.second else icons.first
                val textColor = if (isSelected) colors.primary else colors.muted

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .testTag("tab_$label")
                ) {
                    Icon(
                        imageVector = activeIcon,
                        contentDescription = label,
                        tint = textColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferralBottomSheet(
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current
    var isCopied by remember { mutableStateOf(false) }
    val referralCode = "GUJ2026AP"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.card,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(colors.muted.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bring a friend. Both win 500.",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = DisplayFontFamily,
                color = colors.ink,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Share your personal invite code with fellow Gujarat exam aspirants. When they take their first quiz, you both instantly get +500 coins!",
                fontSize = 13.sp,
                color = colors.muted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Dashed-border referral code box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.paper)
                    .border(1.5.dp, colors.primary, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "YOUR REFERRAL CODE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.muted,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = referralCode,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = NumericFontFamily,
                            color = colors.primary
                        )
                    }

                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Referral Code", referralCode))
                            isCopied = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isCopied) "Copied ✓" else "Copy",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* WhatsApp share logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Share via WhatsApp",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = colors.paper,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "4 / 10 referrals this month · Resets on the 1st",
                    fontSize = 12.sp,
                    color = colors.muted,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun CircularTimer(
    totalSeconds: Int = 30,
    currentSeconds: Int = 20,
    modifier: Modifier = Modifier.size(54.dp)
) {
    val colors = LocalAppColors.current
    val progress = (currentSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)

    val strokeColor = when {
        currentSeconds <= 5 -> colors.error
        currentSeconds <= 12 -> colors.accent
        else -> colors.success
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background ring
            drawCircle(
                color = strokeColor.copy(alpha = 0.15f),
                style = Stroke(width = 4.dp.toPx())
            )
            // Progress arc
            drawArc(
                color = strokeColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = "$currentSeconds",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = strokeColor,
            fontFamily = NumericFontFamily
        )
    }
}
