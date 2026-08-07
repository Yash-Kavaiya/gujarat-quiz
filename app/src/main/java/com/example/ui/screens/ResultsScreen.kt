package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GoldCoinIcon
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors
import com.example.viewmodel.QuizState

@Composable
fun ResultsScreen(
    quizState: QuizState,
    onHome: () -> Unit,
    onRetry: () -> Unit
) {
    val colors = LocalAppColors.current

    if (quizState.isWinState) {
        // Full screen Big Win Gold Celebration
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.accentGold)
                .statusBarsPadding()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = colors.ink,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "BIG WIN!",
                    fontSize = 42.sp,
                    fontFamily = DisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Outstanding performance!",
                    fontSize = 16.sp,
                    color = colors.ink.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "SCORE ACCURACY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.muted)
                        Text(text = "${quizState.scorePercentage}%", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = colors.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GoldCoinIcon(sizeDp = 28)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "+${quizState.coinsEarnedInQuiz + 200} Coins Won", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colors.accentGold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.ink)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Retry", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onHome,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.ink)
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Home", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    } else {
        // Standard Results Screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.paper)
                .statusBarsPadding()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "QUIZ COMPLETED", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = colors.muted)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Good Effort!", fontSize = 32.sp, fontFamily = DisplayFontFamily, fontWeight = FontWeight.Bold, color = colors.ink)

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(colors.primaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${quizState.scorePercentage}%", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = colors.primary)
                            Text(text = "${quizState.correctCount} / ${quizState.totalCount} Correct", fontSize = 13.sp, color = colors.muted)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.card),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Correct", fontSize = 12.sp, color = colors.muted)
                                Text(text = "${quizState.correctCount}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colors.correctGreen)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Wrong", fontSize = 12.sp, color = colors.muted)
                                Text(text = "${quizState.totalCount - quizState.correctCount}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colors.wrongRed)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Earned", fontSize = 12.sp, color = colors.muted)
                                Text(text = "+${quizState.coinsEarnedInQuiz} coins", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colors.accentGold)
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Try Again", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onHome,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Home", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
