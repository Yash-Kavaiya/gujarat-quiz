package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TransactionEntity
import com.example.data.UserProfileEntity
import com.example.ui.components.GoldCoinIcon
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors

@Composable
fun ProfileScreen(
    profile: UserProfileEntity,
    transactions: List<TransactionEntity>,
    activeTheme: String,
    onOpenCoinShop: () -> Unit,
    onOpenReferralSheet: () -> Unit,
    onSwitchTheme: (String) -> Unit
) {
    val colors = LocalAppColors.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(colors.primaryLight)
                        .border(3.dp, colors.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = profile.avatarUrl?.ifEmpty { "🦁" } ?: "🦁", fontSize = 40.sp)

                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = profile.name,
                    fontSize = 24.sp,
                    fontFamily = DisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink
                )
                Text(
                    text = "${profile.goal} · ${profile.district}",
                    fontSize = 13.sp,
                    color = colors.muted
                )
            }
        }

        // Coin Vault Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.ink)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "COIN VAULT BALANCE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GoldCoinIcon(sizeDp = 36)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "%,d".format(profile.coins),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.accentGold
                            )
                        }
                        Button(
                            onClick = onOpenCoinShop,
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Get Coins", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Theme Switcher Section
        item {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "APP THEME",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.muted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val themes = listOf("Saffron", "Indigo", "Midnight")
                    themes.forEach { themeName ->
                        val isSelected = activeTheme == themeName
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSwitchTheme(themeName) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) colors.primaryLight else colors.card
                            ),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, colors.primary) else null
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = themeName,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = colors.ink
                                )
                            }
                        }
                    }
                }
            }
        }

        // Referral Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { onOpenReferralSheet() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.primaryLight)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = colors.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Invite Friends, Earn +500 Coins", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                            Text(text = "Your code: GUJ-${profile.referralCode}", fontSize = 12.sp, color = colors.primary)
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = colors.primary)
                }
            }
        }

        // Transaction History Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.History, contentDescription = null, tint = colors.muted, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "TRANSACTION HISTORY",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.muted,
                    letterSpacing = 1.sp
                )
            }
        }

        items(transactions) { tx ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.card)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = tx.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colors.ink)
                        Text(text = tx.timeAgo, fontSize = 11.sp, color = colors.muted)
                    }
                    Text(
                        text = if (tx.amount >= 0) "+${tx.amount}" else "${tx.amount}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (tx.amount >= 0) colors.correctGreen else colors.wrongRed
                    )
                }
            }
        }
    }
}
