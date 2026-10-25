package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.data.model.AppThemeChoice

private val CyberCyanScheme = darkColorScheme(
    primary = FitCyan,
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = Color(0xFF99F0FF),
    secondary = FitEmerald,
    onSecondary = Color(0xFF00391A),
    secondaryContainer = Color(0xFF005228),
    onSecondaryContainer = Color(0xFF6BFF9E),
    tertiary = FitOrange,
    onTertiary = Color(0xFF4B2800),
    background = FitDarkBackground,
    onBackground = FitTextPrimary,
    surface = FitDarkSurface,
    onSurface = FitTextPrimary,
    surfaceVariant = FitDarkSurfaceVariant,
    onSurfaceVariant = FitTextSecondary,
    outline = FitDarkBorder,
    error = FitCoral
)

private val EmeraldPowerScheme = darkColorScheme(
    primary = EmeraldNeon,
    onPrimary = Color(0xFF003915),
    primaryContainer = Color(0xFF005221),
    onPrimaryContainer = Color(0xFF6BFF90),
    secondary = FitCyan,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004F58),
    onSecondaryContainer = Color(0xFF99F0FF),
    tertiary = EmeraldMint,
    onTertiary = Color(0xFF003820),
    background = EmeraldDarkBg,
    onBackground = FitTextPrimary,
    surface = EmeraldDarkSurface,
    onSurface = FitTextPrimary,
    surfaceVariant = EmeraldDarkVariant,
    onSurfaceVariant = Color(0xFFA5D6A7),
    outline = Color(0xFF1E4D3A),
    error = FitCoral
)

private val SolarAmberScheme = darkColorScheme(
    primary = SolarAmber,
    onPrimary = Color(0xFF432C00),
    primaryContainer = Color(0xFF604100),
    onPrimaryContainer = Color(0xFFFFDF9E),
    secondary = SolarFlame,
    onSecondary = Color(0xFF551400),
    secondaryContainer = Color(0xFF7A2000),
    onSecondaryContainer = Color(0xFFFFDBCF),
    tertiary = FitCyan,
    onTertiary = Color(0xFF00363D),
    background = SolarDarkBg,
    onBackground = Color(0xFFFFEDE6),
    surface = SolarDarkSurface,
    onSurface = Color(0xFFFFEDE6),
    surfaceVariant = SolarDarkVariant,
    onSurfaceVariant = Color(0xFFD7C2B9),
    outline = Color(0xFF4E372E),
    error = FitCoral
)

private val RoyalVioletScheme = darkColorScheme(
    primary = RoyalViolet,
    onPrimary = Color(0xFF2E1163),
    primaryContainer = Color(0xFF442483),
    onPrimaryContainer = Color(0xFFEADBFF),
    secondary = RoyalMagenta,
    onSecondary = Color(0xFF5A0027),
    secondaryContainer = Color(0xFF7F003A),
    onSecondaryContainer = Color(0xFFFFD9E2),
    tertiary = FitCyan,
    onTertiary = Color(0xFF00363D),
    background = RoyalDarkBg,
    onBackground = Color(0xFFF3EDFA),
    surface = RoyalDarkSurface,
    onSurface = Color(0xFFF3EDFA),
    surfaceVariant = RoyalDarkVariant,
    onSurfaceVariant = Color(0xFFD0C3E3),
    outline = Color(0xFF4A3866),
    error = FitCoral
)

private val MidnightCarbonScheme = darkColorScheme(
    primary = CarbonWhite,
    onPrimary = Color(0xFF16181C),
    primaryContainer = Color(0xFF2C3038),
    onPrimaryContainer = Color(0xFFECEFF1),
    secondary = FitCyan,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004F58),
    onSecondaryContainer = Color(0xFF99F0FF),
    tertiary = CarbonSilver,
    onTertiary = Color(0xFF16181C),
    background = CarbonDarkBg,
    onBackground = Color(0xFFF1F5F9),
    surface = CarbonDarkSurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = CarbonDarkVariant,
    onSurfaceVariant = CarbonSilver,
    outline = Color(0xFF373E47),
    error = FitCoral
)

private val CleanLightScheme = lightColorScheme(
    primary = FitCyanDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBAF4FF),
    onPrimaryContainer = Color(0xFF001F24),
    secondary = FitEmeraldDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF8FFFA8),
    onSecondaryContainer = Color(0xFF00210C),
    tertiary = FitOrange,
    onTertiary = Color.White,
    background = FitLightBackground,
    onBackground = FitLightTextPrimary,
    surface = FitLightSurface,
    onSurface = FitLightTextPrimary,
    surfaceVariant = FitLightSurfaceVariant,
    onSurfaceVariant = FitLightTextSecondary,
    outline = FitLightBorder,
    error = FitCoral
)

@Composable
fun MyApplicationTheme(
    themeChoice: AppThemeChoice = AppThemeChoice.CYBER_CYAN,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (themeChoice.isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> when (themeChoice) {
            AppThemeChoice.CYBER_CYAN -> CyberCyanScheme
            AppThemeChoice.EMERALD_POWER -> EmeraldPowerScheme
            AppThemeChoice.SOLAR_AMBER -> SolarAmberScheme
            AppThemeChoice.ROYAL_VIOLET -> RoyalVioletScheme
            AppThemeChoice.MIDNIGHT_CARBON -> MidnightCarbonScheme
            AppThemeChoice.CLEAN_LIGHT -> CleanLightScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
