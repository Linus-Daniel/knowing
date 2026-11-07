package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class AppThemeMode {
    LIGHT, SEPIA, DARK, OLED
}

data class HarmonixThemeColors(
    val surfaceBase: Color,
    val surfaceRaised: Color,
    val surfaceSunken: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val borderHairline: Color,
    val activeKaraokeBg: Color,
    val activeKaraokeBorder: Color
)

val LocalHarmonixColors = staticCompositionLocalOf {
    HarmonixThemeColors(
        surfaceBase = LightSurfaceBase,
        surfaceRaised = LightSurfaceRaised,
        surfaceSunken = LightSurfaceSunken,
        textPrimary = LightOnSurface,
        textSecondary = LightOnSurfaceVariant,
        borderHairline = LightOutlineVariant.copy(alpha = 0.5f),
        activeKaraokeBg = ActiveSentenceHighlightLight,
        activeKaraokeBorder = ActiveSentenceBorder
    )
}

val LightHarmonixThemeColors = HarmonixThemeColors(
    surfaceBase = LightSurfaceBase,
    surfaceRaised = LightSurfaceRaised,
    surfaceSunken = LightSurfaceSunken,
    textPrimary = LightOnSurface,
    textSecondary = LightOnSurfaceVariant,
    borderHairline = Color(0xFFE2E8F0),
    activeKaraokeBg = ActiveSentenceHighlightLight,
    activeKaraokeBorder = ActiveSentenceBorder
)

val SepiaHarmonixThemeColors = HarmonixThemeColors(
    surfaceBase = SepiaSurfaceBase,
    surfaceRaised = SepiaSurfaceRaised,
    surfaceSunken = SepiaSurfaceSunken,
    textPrimary = SepiaOnSurface,
    textSecondary = SepiaOnSurfaceVariant,
    borderHairline = SepiaOutline.copy(alpha = 0.4f),
    activeKaraokeBg = Color(0x3DFEB447),
    activeKaraokeBorder = Color(0xFFD97706)
)

val DarkHarmonixThemeColors = HarmonixThemeColors(
    surfaceBase = DarkSurfaceBase,
    surfaceRaised = DarkSurfaceRaised,
    surfaceSunken = DarkSurfaceSunken,
    textPrimary = DarkOnSurface,
    textSecondary = DarkOnSurfaceVariant,
    borderHairline = Color(0x1AFFFFFF),
    activeKaraokeBg = ActiveSentenceHighlightDark,
    activeKaraokeBorder = ActiveSentenceBorder
)

val OledHarmonixThemeColors = HarmonixThemeColors(
    surfaceBase = OledSurfaceBase,
    surfaceRaised = OledSurfaceRaised,
    surfaceSunken = OledSurfaceSunken,
    textPrimary = OledOnSurface,
    textSecondary = OledOnSurfaceVariant,
    borderHairline = Color(0x22FFFFFF),
    activeKaraokeBg = Color(0x40D97706),
    activeKaraokeBorder = ActiveSentenceBorder
)

private val LightScheme = lightColorScheme(
    primary = PrimaryAmber,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = Color(0xFFFFFBFB),
    secondary = SecondaryAmber,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    background = LightSurface,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceContainerHighest,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFFFB77D),
    onPrimary = Color(0xFF4D2600),
    primaryContainer = PrimaryAmber,
    onPrimaryContainer = Color(0xFFFFDCC3),
    secondary = SecondaryContainer,
    onSecondary = OnSecondaryContainer,
    secondaryContainer = Color(0xFF4C3000),
    onSecondaryContainer = Color(0xFFFFDDB8),
    background = DarkSurfaceBase,
    onBackground = DarkOnSurface,
    surface = DarkSurfaceRaised,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceContainer,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = Color(0x22FFFFFF)
)

@Composable
fun HarmonixTheme(
    themeMode: AppThemeMode = AppThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val (colorScheme, harmonixColors) = when (themeMode) {
        AppThemeMode.LIGHT -> Pair(LightScheme, LightHarmonixThemeColors)
        AppThemeMode.SEPIA -> Pair(
            LightScheme.copy(
                background = SepiaSurfaceBase,
                surface = SepiaSurfaceRaised,
                onSurface = SepiaOnSurface,
                onBackground = SepiaOnSurface
            ),
            SepiaHarmonixThemeColors
        )
        AppThemeMode.DARK -> Pair(DarkScheme, DarkHarmonixThemeColors)
        AppThemeMode.OLED -> Pair(
            DarkScheme.copy(
                background = OledSurfaceBase,
                surface = OledSurfaceRaised,
                onSurface = OledOnSurface,
                onBackground = OledOnSurface
            ),
            OledHarmonixThemeColors
        )
    }

    CompositionLocalProvider(LocalHarmonixColors provides harmonixColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
