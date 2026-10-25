package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CarbonDarkBg
import com.example.ui.theme.CarbonWhite
import com.example.ui.theme.EmeraldDarkBg
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.FitCyan
import com.example.ui.theme.FitCyanDark
import com.example.ui.theme.FitDarkBackground
import com.example.ui.theme.FitEmerald
import com.example.ui.theme.FitLightBackground
import com.example.ui.theme.RoyalDarkBg
import com.example.ui.theme.RoyalViolet
import com.example.ui.theme.SolarAmber
import com.example.ui.theme.SolarDarkBg
import com.example.ui.theme.SolarFlame

enum class AppThemeChoice(
    val titleAr: String,
    val subtitleAr: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val previewBg: Color,
    val isDark: Boolean
) {
    CYBER_CYAN(
        titleAr = "نيون سيبراني",
        subtitleAr = "أزرق نيوني فائق مع زمردي رياضي",
        primaryColor = FitCyan,
        secondaryColor = FitEmerald,
        previewBg = FitDarkBackground,
        isDark = true
    ),
    EMERALD_POWER(
        titleAr = "طاقة الزمرد",
        subtitleAr = "أخضر نيون حيوي مستوحى من الحلبات الرياضية",
        primaryColor = EmeraldNeon,
        secondaryColor = FitCyan,
        previewBg = EmeraldDarkBg,
        isDark = true
    ),
    SOLAR_AMBER(
        titleAr = "الشعلة البركانية",
        subtitleAr = "برتقالي ناري وذهبي لتحفيز حرق السعرات",
        primaryColor = SolarAmber,
        secondaryColor = SolarFlame,
        previewBg = SolarDarkBg,
        isDark = true
    ),
    ROYAL_VIOLET(
        titleAr = "الأرجواني الملكي",
        subtitleAr = "بنفسجي مستقبلي متدرج مع لمسات ماجنتا",
        primaryColor = RoyalViolet,
        secondaryColor = FitCyan,
        previewBg = RoyalDarkBg,
        isDark = true
    ),
    MIDNIGHT_CARBON(
        titleAr = "كربون داكن",
        subtitleAr = "ألياف الكربون مع لمسات فضية مينيماليست",
        primaryColor = CarbonWhite,
        secondaryColor = FitCyan,
        previewBg = CarbonDarkBg,
        isDark = true
    ),
    CLEAN_LIGHT(
        titleAr = "النهاري النقي",
        subtitleAr = "مظهر رياضي مشرق وواضح تحت أشعة الشمس",
        primaryColor = FitCyanDark,
        secondaryColor = FitEmerald,
        previewBg = FitLightBackground,
        isDark = false
    )
}

data class UserSettings(
    val theme: AppThemeChoice = AppThemeChoice.CYBER_CYAN,
    val voiceCoachEnabled: Boolean = true,
    val hapticFeedbackEnabled: Boolean = true,
    val cameraPoseSensitivityHigh: Boolean = true,
    val weightUnit: String = "كجم (Kg)",
    val heightUnit: String = "سم (cm)",
    val autoSyncWatch: Boolean = true,
    val waterReminderEnabled: Boolean = true,
    val dailyWorkoutReminderTime: String = "06:30 م",
    val userDisplayName: String = "عمار المنصوري",
    val currentWeightKg: Float = 77.4f,
    val targetWeightKg: Float = 74.0f,
    val userHeightCm: Int = 178
)
