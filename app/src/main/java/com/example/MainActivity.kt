package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppTab
import com.example.ui.FitAmmarViewModel
import com.example.ui.components.BreathingExerciseDialog
import com.example.ui.components.MealScannerSheet
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FitnessScreen
import com.example.ui.screens.HealthVitalsScreen
import com.example.ui.screens.NutritionScreen
import com.example.ui.screens.PostureCoachScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.FitCyan
import com.example.ui.theme.FitDarkBackground
import com.example.ui.theme.FitDarkSurface
import com.example.ui.theme.FitDarkSurfaceVariant
import com.example.ui.theme.FitEmerald
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: FitAmmarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userSettings by viewModel.repository.userSettings.collectAsStateWithLifecycle()
            MyApplicationTheme(themeChoice = userSettings.theme) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    FitAmmarApp(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitAmmarApp(viewModel: FitAmmarViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val isCameraCoachActive by viewModel.isCameraCoachActive.collectAsStateWithLifecycle()
    val isSettingsOpen by viewModel.isSettingsOpen.collectAsStateWithLifecycle()
    val isMealScannerOpen by viewModel.isMealScannerOpen.collectAsStateWithLifecycle()
    val isBreathingActive by viewModel.isBreathingActive.collectAsStateWithLifecycle()
    val isAiLoading by viewModel.isAiLoading.collectAsStateWithLifecycle()
    val statusNotice by viewModel.statusNotice.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    if (isCameraCoachActive) {
        PostureCoachScreen(
            viewModel = viewModel,
            onBack = { viewModel.closeCameraCoach() }
        )
    } else if (isSettingsOpen) {
        SettingsScreen(
            viewModel = viewModel,
            onBack = { viewModel.closeSettings() }
        )
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(FitDarkBackground),
            containerColor = FitDarkBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(FitCyan),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = Color(0xFF00363D),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "FitAmmar",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "AI",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = FitCyan
                                    )
                                }
                                Text(
                                    text = "مدربك الذكي ومتابعك الصحي",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = FitEmerald
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.openCameraCoach() },
                            modifier = Modifier.testTag("appbar_camera_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "رصد الكاميرا",
                                tint = FitCyan
                            )
                        }
                        IconButton(
                            onClick = { viewModel.openMealScanner() },
                            modifier = Modifier.testTag("appbar_meal_scanner_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "ماسح الوجبات",
                                tint = FitEmerald
                            )
                        }
                        IconButton(
                            onClick = { viewModel.openSettings() },
                            modifier = Modifier.testTag("appbar_settings_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "الإعدادات والمظهر",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = FitDarkBackground,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                )
            },
            bottomBar = {
                FitBottomNavigation(
                    currentTab = currentTab,
                    onSelectTab = { viewModel.setTab(it) }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    AppTab.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                    AppTab.FITNESS -> FitnessScreen(viewModel = viewModel)
                    AppTab.NUTRITION -> NutritionScreen(viewModel = viewModel)
                    AppTab.HEALTH -> HealthVitalsScreen(viewModel = viewModel)
                    AppTab.PREMIUM -> PremiumScreen(viewModel = viewModel)
                }

                // Status Notification Banner
                AnimatedVisibility(
                    visible = statusNotice != null,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(FitDarkSurface)
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(FitEmerald))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = statusNotice ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    // Meal Scanner Bottom Sheet
    if (isMealScannerOpen) {
        MealScannerSheet(
            isLoading = isAiLoading,
            onDismiss = { viewModel.closeMealScanner() },
            onAnalyzeMeal = { dish, base64 -> viewModel.processMealScan(dish, base64) }
        )
    }

    // Guided Breathing Dialog
    if (isBreathingActive) {
        BreathingExerciseDialog(
            onDismiss = { viewModel.setBreathingActive(false) }
        )
    }
}

@Composable
fun FitBottomNavigation(
    currentTab: AppTab,
    onSelectTab: (AppTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("fit_bottom_nav"),
        containerColor = FitDarkSurface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(AppTab.DASHBOARD, Icons.Default.Home, "الرئيسية"),
            Triple(AppTab.FITNESS, Icons.Default.FitnessCenter, "التمارين"),
            Triple(AppTab.NUTRITION, Icons.Default.Restaurant, "التغذية"),
            Triple(AppTab.HEALTH, Icons.Default.Favorite, "المؤشرات"),
            Triple(AppTab.PREMIUM, Icons.Default.WorkspacePremium, "الباقات")
        )

        items.forEach { (tab, icon, label) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF00363D),
                    selectedTextColor = FitCyan,
                    indicatorColor = FitCyan,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
            )
        }
    }
}
