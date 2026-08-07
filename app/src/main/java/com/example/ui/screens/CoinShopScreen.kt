package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CoinPack
import com.example.data.PowerUp
import com.example.data.SeedData
import com.example.ui.components.CoinBalanceBadge
import com.example.ui.components.GoldCoinIcon
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors

@Composable
fun CoinShopScreen(
    userCoins: Int,
    onBuyCoinPack: (CoinPack) -> Unit,
    onBuyPowerUp: (PowerUp) -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalAppColors.current

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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.ink)
                }
                Text(
                    text = "Coin Shop & Vault",
                    fontSize = 20.sp,
                    fontFamily = DisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink
                )
            }
            CoinBalanceBadge(coins = userCoins)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            item {
                Text(
                    text = "COIN PACKS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.muted,
                    letterSpacing = 1.sp
                )
            }

            items(SeedData.coinPacks) { pack ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.card),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GoldCoinIcon(sizeDp = 40)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "%,d Coins".format(pack.coins), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                                    val badge = pack.badge
                                    if (badge != null) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(color = colors.accentGold, shape = RoundedCornerShape(6.dp)) {
                                            Text(text = badge, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colors.ink, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                }
                                Text(text = "Instant credit", fontSize = 12.sp, color = colors.muted)
                            }
                        }

                        Button(
                            onClick = { onBuyCoinPack(pack) },
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "₹${pack.priceInr}", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "POWER-UPS & BOOSTERS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.muted,
                    letterSpacing = 1.sp
                )
            }

            items(SeedData.powerUps) { powerUp ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.card),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(colors.primaryLight),
                                contentAlignment = Alignment.Center
                            ) {
                                val powerUpIcon = when (powerUp.iconName) {
                                    "lightbulb" -> "💡"
                                    "lightbulb_bulk" -> "💡"
                                    "snowflake" -> "❄️"
                                    else -> "⚡"
                                }
                                Text(text = powerUpIcon, fontSize = 22.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = powerUp.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                                Text(text = powerUp.description, fontSize = 12.sp, color = colors.muted)
                            }
                        }

                        Button(
                            onClick = { onBuyPowerUp(powerUp) },
                            colors = ButtonDefaults.buttonColors(containerColor = colors.ink),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "${powerUp.coinPrice} C", fontWeight = FontWeight.Bold, color = colors.accentGold)
                        }
                    }
                }
            }
        }
    }
}
