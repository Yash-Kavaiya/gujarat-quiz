package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun GujaratQuizTheme(
    activeThemeName: String = "Saffron",
    content: @Composable () -> Unit
) {
    val appColors = when (activeThemeName) {
        "Indigo" -> IndigoColors
        "Midnight" -> MidnightColors
        else -> SaffronColors
    }

    val materialColorScheme = if (appColors.isDark) {
        darkColorScheme(
            primary = appColors.primary,
            background = appColors.paper,
            surface = appColors.card,
            onPrimary = appColors.paper,
            onBackground = appColors.ink,
            onSurface = appColors.ink
        )
    } else {
        lightColorScheme(
            primary = appColors.primary,
            background = appColors.paper,
            surface = appColors.card,
            onPrimary = appColors.card,
            onBackground = appColors.ink,
            onSurface = appColors.ink
        )
    }

    CompositionLocalProvider(
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
