package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GoldCoinIcon
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    step: Int,
    selectedGoal: String,
    onSelectGoal: (String) -> Unit,
    onNextStep: () -> Unit,
    onClaimGift: () -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        when (step) {
            1 -> {
                // Step 1: Welcome
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "GUJARAT · EST. 1960",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = colors.muted
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Every aspirant deserves a fair shot.",
                            fontSize = 36.sp,
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.ink,
                            lineHeight = 44.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "17,000+ questions sourced from GPSC, GSSSB & GPSSB papers. Bilingual. Offline. Built for Gujarat.",
                            fontSize = 15.sp,
                            color = colors.inkSecondary,
                            lineHeight = 22.sp
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onNextStep,
                            colors = ButtonDefaults.buttonColors(containerColor = colors.ink),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Get started →",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        OutlinedButton(
                            onClick = onNextStep,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                        ) {
                            Text(
                                text = "I already have an account",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.ink
                            )
                        }
                    }
                }
            }

            2 -> {
                // Step 2: Goal selection
                val goals = listOf(
                    "GPSC Class 1 & 2 / Prelims 2026",
                    "GSSSB Clerk / CCE pattern",
                    "Talati cum Mantri / GPSSB",
                    "Police Sub Inspector / Physical + Written",
                    "Just exploring"
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "STEP 2 OF 3",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = colors.muted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "What are you preparing for?",
                            fontSize = 28.sp,
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.ink
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        goals.forEach { goalOption ->
                            val isSelected = selectedGoal == goalOption
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { onSelectGoal(goalOption) },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) colors.primaryLight else colors.card
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, colors.primary) else null
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = goalOption,
                                        fontSize = 15.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = colors.ink,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = colors.primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onNextStep,
                        enabled = selectedGoal.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            3 -> {
                // Step 3: Welcome Gift
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(40.dp))
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color(0xFFFFD54F), colors.primary)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            GoldCoinIcon(sizeDp = 64)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "WELCOME GIFT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = colors.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "+500 coins",
                            fontSize = 40.sp,
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.ink
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Use your coins to unlock practice quizzes, buy hints, or enter daily challenges and duels.",
                            fontSize = 15.sp,
                            color = colors.muted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Button(
                        onClick = onClaimGift,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Claim & continue",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
