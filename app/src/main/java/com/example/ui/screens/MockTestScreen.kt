package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.LocalAppColors
import com.example.viewmodel.MockTestState

@Composable
fun MockTestScreen(
    mockState: MockTestState,
    onSelectOption: (Int, Int) -> Unit,
    onToggleMarkForReview: (Int) -> Unit,
    onGoToQuestion: (Int) -> Unit,
    onSetPaletteOpen: (Boolean) -> Unit,
    onSetSubmitModalOpen: (Boolean) -> Unit,
    onSubmitMockTest: () -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalAppColors.current
    val currentQuestion = mockState.questions.getOrNull(mockState.currentIndex) ?: return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                color = colors.card,
                shadowElevation = 2.dp
            ) {
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
                        Column {
                            Text(text = "GPSC Model Paper I", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.ink)
                            Text(text = "Q. ${mockState.currentIndex + 1} of ${mockState.questions.size}", fontSize = 12.sp, color = colors.muted)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⏱ 1h 58m", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colors.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { onSetPaletteOpen(true) }) {
                            Icon(Icons.Default.GridView, contentDescription = "Palette", tint = colors.ink)
                        }
                    }
                }
            }

            // Question Container
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = colors.primaryLight,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = currentQuestion.section,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    TextButton(onClick = { onToggleMarkForReview(mockState.currentIndex) }) {
                        val isMarked = mockState.markedForReview.contains(mockState.currentIndex)
                        Icon(
                            if (isMarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isMarked) colors.primary else colors.muted
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isMarked) "Marked" else "Mark for review",
                            fontSize = 12.sp,
                            color = if (isMarked) colors.primary else colors.muted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.card),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = currentQuestion.textEn, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = colors.ink, lineHeight = 24.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = currentQuestion.textGu, fontSize = 14.sp, color = colors.inkSecondary, lineHeight = 22.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options
                val selectedOption = mockState.userAnswers[mockState.currentIndex]
                val optionTexts = listOf(currentQuestion.optionA, currentQuestion.optionB, currentQuestion.optionC, currentQuestion.optionD)

                optionTexts.forEachIndexed { optIdx, optText ->
                    val isSelected = selectedOption == optIdx
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectOption(mockState.currentIndex, optIdx) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) colors.primaryLight else colors.card
                        ),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, colors.primary) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSelectOption(mockState.currentIndex, optIdx) },
                                colors = RadioButtonDefaults.colors(selectedColor = colors.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = optText, fontSize = 14.sp, color = colors.ink, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Bottom Nav Bar
            Surface(
                color = colors.card,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { if (mockState.currentIndex > 0) onGoToQuestion(mockState.currentIndex - 1) },
                        enabled = mockState.currentIndex > 0,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("← Previous")
                    }

                    Button(
                        onClick = { onSetSubmitModalOpen(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.wrongRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Submit Test", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { if (mockState.currentIndex < mockState.questions.size - 1) onGoToQuestion(mockState.currentIndex + 1) },
                        enabled = mockState.currentIndex < mockState.questions.size - 1,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Next →", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Question Palette Sheet / Dialog
        if (mockState.isPaletteOpen) {
            AlertDialog(
                onDismissRequest = { onSetPaletteOpen(false) },
                title = { Text("Question Palette (100 Qs)", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        itemsIndexed(mockState.questions) { index, _ ->
                            val isAnswered = mockState.userAnswers.containsKey(index)
                            val isMarked = mockState.markedForReview.contains(index)
                            val isCurrent = index == mockState.currentIndex

                            val bg = when {
                                isCurrent -> colors.primary
                                isMarked -> colors.accentGold
                                isAnswered -> colors.correctGreen
                                else -> colors.paper
                            }

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bg)
                                    .clickable { onGoToQuestion(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent || isMarked || isAnswered) Color.White else colors.ink
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { onSetPaletteOpen(false) }) {
                        Text("Close")
                    }
                }
            )
        }

        // Submit Confirm Modal
        if (mockState.isSubmitModalOpen) {
            AlertDialog(
                onDismissRequest = { onSetSubmitModalOpen(false) },
                title = { Text("Submit Test?", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("You have answered ${mockState.userAnswers.size} of 100 questions.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Unattempted questions: ${100 - mockState.userAnswers.size}")
                        Text("Marked for review: ${mockState.markedForReview.size}")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = onSubmitMockTest,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                    ) {
                        Text("Confirm & Submit", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onSetSubmitModalOpen(false) }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
