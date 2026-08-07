package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val paper: Color,
    val card: Color,
    val cardSecondary: Color,
    val ink: Color,
    val inkSecondary: Color,
    val primary: Color,
    val primaryDeep: Color,
    val primaryLight: Color,
    val accent: Color,
    val gold: Color,
    val success: Color,
    val error: Color,
    val muted: Color,
    val isDark: Boolean = false
) {
    val accentGold: Color get() = gold
    val correctGreen: Color get() = success
    val wrongRed: Color get() = error
    val secondary: Color get() = primaryDeep
}


val SaffronColors = AppColors(
    paper = Color(0xFFFAF7F2),
    card = Color(0xFFFFFFFF),
    cardSecondary = Color(0xFFF3ECE1),
    ink = Color(0xFF24180E),
    inkSecondary = Color(0xFF5A493B),
    primary = Color(0xFFC86D12),
    primaryDeep = Color(0xFF8B3D00),
    primaryLight = Color(0xFFFDE8D3),
    accent = Color(0xFFD4AF37),
    gold = Color(0xFFE6A100),
    success = Color(0xFF2E7D32),
    error = Color(0xFFD32F2F),
    muted = Color(0xFF8C7D6E),
    isDark = false
)

val IndigoColors = AppColors(
    paper = Color(0xFFF4F5FA),
    card = Color(0xFFFFFFFF),
    cardSecondary = Color(0xFFE8EAF6),
    ink = Color(0xFF121629),
    inkSecondary = Color(0xFF373E68),
    primary = Color(0xFF3F51B5),
    primaryDeep = Color(0xFF1A237E),
    primaryLight = Color(0xFFE8EAF6),
    accent = Color(0xFFFFC107),
    gold = Color(0xFFE6A100),
    success = Color(0xFF2E7D32),
    error = Color(0xFFD32F2F),
    muted = Color(0xFF686D8A),
    isDark = false
)

val MidnightColors = AppColors(
    paper = Color(0xFF121216),
    card = Color(0xFF1E1E24),
    cardSecondary = Color(0xFF2A2A34),
    ink = Color(0xFFF0F0F5),
    inkSecondary = Color(0xFFC2C2D0),
    primary = Color(0xFFE6A100),
    primaryDeep = Color(0xFFC86D12),
    primaryLight = Color(0xFF3A301A),
    accent = Color(0xFFFFA726),
    gold = Color(0xFFFFD54F),
    success = Color(0xFF66BB6A),
    error = Color(0xFFEF5350),
    muted = Color(0xFF9E9EA8),
    isDark = true
)

val LocalAppColors = staticCompositionLocalOf { SaffronColors }

val AppColors.accentGold: Color get() = gold
val AppColors.correctGreen: Color get() = success
val AppColors.wrongRed: Color get() = error
val AppColors.secondary: Color get() = primaryDeep
