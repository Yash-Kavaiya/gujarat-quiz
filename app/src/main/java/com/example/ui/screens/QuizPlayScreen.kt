package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import com.example.ui.components.CircularTimer
import com.example.ui.components.CoinBalanceBadge
import com.example.ui.components.GoldCoinIcon
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors
import com.example.viewmodel.QuizState

@Composable
fun QuizPlayScreen(
    quizState: QuizState,
    userCoins: Int,
    hintsOwned: Int = 0,
    onSelectOption: (Int) -> Unit,
    onUseHint: () -> Unit,
    onNextQuestion: () -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalAppColors.current
    val currentQuestion = quizState.questions.getOrNull(quizState.currentIndex) ?: return
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler { showExitDialog = true }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Leave quiz?", fontWeight = FontWeight.Bold) },
            text = { Text("Your progress in this quiz will be lost and the entry fee is not refunded.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onBack()
                }) { Text("Leave", color = colors.wrongRed) }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) { Text("Keep playing") }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showExitDialog = true }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.ink)
                }
                Text(
                    text = "${quizState.currentIndex + 1} of ${quizState.questions.size}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink
                )
                CoinBalanceBadge(coins = userCoins)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Timer & Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularTimer(
                        totalSeconds = quizState.timerTotalSeconds,
                        currentSeconds = quizState.timerSeconds,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = quizState.category?.titleEn ?: "Quiz",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.muted,
                        maxLines = 1
                    )
                }

                TextButton(
                    onClick = onUseHint,
                    enabled = !quizState.isRevealed && quizState.eliminatedOptions.isEmpty()
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = colors.accentGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (hintsOwned > 0) "50-50 Hint ($hintsOwned left)" else "50-50 Hint (50 coins)",
                        fontSize = 12.sp,
                        color = colors.accentGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question Box (Bilingual English + Gujarati)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.card),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = currentQuestion.textEn,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.ink,
                        lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = currentQuestion.textGu,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.inkSecondary,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Options List
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                val optionLabels = listOf("A", "B", "C", "D")
                val optionTexts = currentQuestion.options

                optionTexts.forEachIndexed { index, optionText ->
                    val isEliminated = quizState.eliminatedOptions.contains(index)
                    val isSelected = quizState.selectedOption == index
                    val isCorrect = index == currentQuestion.correctIndex

                    val cardBg = when {
                        !quizState.isRevealed && isSelected -> colors.primaryLight
                        quizState.isRevealed && isCorrect -> colors.correctGreen.copy(alpha = 0.15f)
                        quizState.isRevealed && isSelected && !isCorrect -> colors.wrongRed.copy(alpha = 0.15f)
                        else -> colors.card
                    }

                    val borderColor = when {
                        !quizState.isRevealed && isSelected -> colors.primary
                        quizState.isRevealed && isCorrect -> colors.correctGreen
                        quizState.isRevealed && isSelected && !isCorrect -> colors.wrongRed
                        else -> Color.Transparent
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !quizState.isRevealed && !isEliminated) {
                                onSelectOption(index)
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
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
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(colors.paper),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = optionLabels.getOrElse(index) { "${index + 1}" },
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isEliminated) colors.muted else colors.ink
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (isEliminated) "Option eliminated" else optionText,
                                    fontSize = 15.sp,
                                    color = if (isEliminated) colors.muted else colors.ink,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            if (quizState.isRevealed) {
                                if (isCorrect) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = colors.correctGreen)
                                } else if (isSelected) {
                                    Icon(Icons.Default.Close, contentDescription = null, tint = colors.wrongRed)
                                }
                            }
                        }
                    }
                }

                // Explanation card on reveal
                if (quizState.isRevealed) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.primaryLight),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (quizState.timedOut) {
                                Text(text = "⏱ TIME'S UP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.wrongRed)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Text(text = "EXPLANATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = currentQuestion.explanationEn, fontSize = 13.sp, color = colors.ink)
                        }
                    }
                }
            }

            // Bottom Continue / Next Button
            if (quizState.isRevealed) {
                val isLastQuestion = quizState.currentIndex == quizState.questions.size - 1
                Button(
                    onClick = onNextQuestion,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = if (isLastQuestion) "Finish →" else "Next Question →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Coin Pop Overlay Animation
        AnimatedVisibility(
            visible = quizState.showCoinPopAnimation,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.card)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GoldCoinIcon(sizeDp = 64)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "+30 Coins!", fontSize = 28.sp, fontFamily = DisplayFontFamily, fontWeight = FontWeight.Bold, color = colors.accentGold)
                        Text(text = "Correct Answer!", fontSize = 14.sp, color = colors.ink)
                    }
                }
            }
        }
    }
}
