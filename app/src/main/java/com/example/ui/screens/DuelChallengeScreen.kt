package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
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
import com.example.viewmodel.DuelPhase
import com.example.viewmodel.DuelState

@Composable
fun DuelChallengeScreen(
    duelState: DuelState,
    onAdvanceToWaiting: () -> Unit,
    onSelectOption: (Int) -> Unit,
    onRevealQuestion: () -> Unit,
    onNextQuestion: () -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        when (duelState.phase) {
            DuelPhase.INVITE -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.ink)
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = colors.accentGold, modifier = Modifier.size(72.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "1v1 REALTIME DUEL", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = colors.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Challenge ${duelState.friend?.name ?: "Friend"}", fontSize = 28.sp, fontFamily = DisplayFontFamily, fontWeight = FontWeight.Bold, color = colors.ink)

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.card),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Wager: 100 Coins", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                                Text(text = "Winner receives 200 Coins prize pool", fontSize = 13.sp, color = colors.muted)
                            }
                        }
                    }

                    Button(
                        onClick = onAdvanceToWaiting,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(text = "Send Challenge Invite", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            DuelPhase.WAITING -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = colors.primary, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "Waiting for ${duelState.friend?.name ?: "Opponent"} to join...", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Match starts automatically when accepted", fontSize = 13.sp, color = colors.muted)
                    }
                }
            }

            DuelPhase.LIVE_MATCH -> {
                val q = duelState.questions.getOrNull(duelState.currentQuestionIndex) ?: return

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Match Scoreboard Header
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.ink),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "YOU", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.primary)
                                Text(text = "${duelState.userScore}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Text(text = "VS", fontSize = 18.sp, fontFamily = DisplayFontFamily, fontWeight = FontWeight.Bold, color = colors.accentGold)

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = duelState.friend?.name?.uppercase() ?: "OPPONENT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.wrongRed)
                                Text(text = "${duelState.opponentScore}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Question Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.card),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = q.textEn, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = colors.ink)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = q.textGu, fontSize = 14.sp, color = colors.inkSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Options
                    val optionTexts = listOf(q.optionA, q.optionB, q.optionC, q.optionD)
                    optionTexts.forEachIndexed { index, optionText ->
                        val isUserPick = duelState.userChoice == index
                        val isOppPick = duelState.opponentChoice == index
                        val isCorrect = index == q.correctIndex

                        val cardBg = when {
                            isUserPick && isOppPick -> colors.accentGold.copy(alpha = 0.2f)
                            isUserPick -> colors.primaryLight
                            else -> colors.card
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = duelState.userChoice == null) {
                                    onSelectOption(index)
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            border = if (isUserPick) androidx.compose.foundation.BorderStroke(2.dp, colors.primary) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = optionText, fontSize = 14.sp, color = colors.ink, fontWeight = FontWeight.Medium)
                                Row {
                                    if (isUserPick) {
                                        Surface(color = colors.primary, shape = RoundedCornerShape(6.dp)) {
                                            Text("You", fontSize = 10.sp, color = Color.White, modifier = Modifier.padding(4.dp))
                                        }
                                    }
                                    if (isOppPick) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Surface(color = colors.wrongRed, shape = RoundedCornerShape(6.dp)) {
                                            Text(duelState.friend?.name ?: "Opponent", fontSize = 10.sp, color = Color.White, modifier = Modifier.padding(4.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (duelState.userChoice != null) {
                        Button(
                            onClick = onNextQuestion,
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = if (duelState.currentQuestionIndex < duelState.questions.size - 1) "Next Duel Round →" else "Finish Match",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
