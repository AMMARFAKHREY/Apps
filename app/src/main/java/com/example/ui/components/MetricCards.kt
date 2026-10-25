package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityAdjustment
import com.example.data.model.Macronutrients
import com.example.data.model.VitalsData
import com.example.ui.theme.FitCoral
import com.example.ui.theme.FitCyan
import com.example.ui.theme.FitDarkBorder
import com.example.ui.theme.FitDarkSurface
import com.example.ui.theme.FitDarkSurfaceVariant
import com.example.ui.theme.FitEmerald
import com.example.ui.theme.FitOrange
import com.example.ui.theme.FitPurple

@Composable
fun SmartWatchTelemetryCard(
    vitals: VitalsData,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("smartwatch_telemetry_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FitDarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FitCyan.copy(alpha = 0.5f), FitEmerald.copy(alpha = 0.3f))))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Watch,
                        contentDescription = "الساعة الذكية",
                        tint = FitCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = vitals.connectedDeviceName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(FitEmerald.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "مباشر • مزامنة",
                        color = FitEmerald,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Heart Rate
                TelemetryPill(
                    icon = Icons.Default.Favorite,
                    iconColor = FitCoral,
                    value = "${vitals.heartRateBpm}",
                    unit = "BPM",
                    label = "نبض القلب",
                    iconModifier = Modifier.scale(pulseScale)
                )

                // Calories
                TelemetryPill(
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = FitOrange,
                    value = "${vitals.caloriesBurned}",
                    unit = "Kcal",
                    label = "حرق السعرات"
                )

                // SpO2
                TelemetryPill(
                    icon = Icons.Default.WaterDrop,
                    iconColor = FitCyan,
                    value = "${vitals.bloodOxygenSpO2}%",
                    unit = "SpO2",
                    label = "أكسجين الدم"
                )

                // Steps
                TelemetryPill(
                    icon = Icons.AutoMirrored.Filled.DirectionsRun,
                    iconColor = FitEmerald,
                    value = "${vitals.steps}",
                    unit = "خطوة",
                    label = "النشاط اليومي"
                )
            }
        }
    }
}

@Composable
fun TelemetryPill(
    icon: ImageVector,
    iconColor: Color,
    value: String,
    unit: String,
    label: String,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(FitDarkSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = iconModifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 9.sp
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable
fun ActivityAdjustmentBanner(
    adjustment: ActivityAdjustment,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("activity_adjustment_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FitDarkSurfaceVariant),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FitOrange, FitCyan)))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(FitOrange.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "تعويض نشاط",
                            tint = FitOrange,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تعديل ذكي: تعويض مجهود الركض (+${adjustment.extraKmRun} كم)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FitOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = adjustment.suggestionAr,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MacroTag(label = "سعرات مضافة", value = "+${adjustment.addedCalories} Kcal", color = FitOrange)
                MacroTag(label = "بروتين استشفاء", value = "+${adjustment.addedProteinG.toInt()} غ", color = FitEmerald)
                MacroTag(label = "كاربوهيدرات طاقة", value = "+${adjustment.addedCarbsG.toInt()} غ", color = FitCyan)
            }
        }
    }
}

@Composable
fun MacroTag(label: String, value: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = color)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = color.copy(alpha = 0.8f), fontSize = 10.sp)
        }
    }
}

@Composable
fun MacroProgressCard(
    macros: Macronutrients,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("macro_progress_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FitDarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "توازن الطاقة والماكروز اليومي",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${macros.currentCalories} / ${macros.targetCalories} Kcal",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = FitCyan
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            val calProgress = (macros.currentCalories.toFloat() / macros.targetCalories.coerceAtLeast(1)).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { calProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = FitCyan,
                trackColor = FitDarkSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroColumnItem(
                    label = "بروتين",
                    current = "${macros.currentProtein.toInt()}غ",
                    target = "${macros.targetProtein.toInt()}غ",
                    progress = (macros.currentProtein / macros.targetProtein.coerceAtLeast(1f)).coerceIn(0f, 1f),
                    color = FitEmerald
                )
                MacroColumnItem(
                    label = "كاربوهيدرات",
                    current = "${macros.currentCarbs.toInt()}غ",
                    target = "${macros.targetCarbs.toInt()}غ",
                    progress = (macros.currentCarbs / macros.targetCarbs.coerceAtLeast(1f)).coerceIn(0f, 1f),
                    color = FitOrange
                )
                MacroColumnItem(
                    label = "دهون صحية",
                    current = "${macros.currentFat.toInt()}غ",
                    target = "${macros.targetFat.toInt()}غ",
                    progress = (macros.currentFat / macros.targetFat.coerceAtLeast(1f)).coerceIn(0f, 1f),
                    color = FitPurple
                )
            }
        }
    }
}

@Composable
fun MacroColumnItem(
    label: String,
    current: String,
    target: String,
    progress: Float,
    color: Color
) {
    Column(
        modifier = Modifier.width(96.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = FitDarkSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$current / $target",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 11.sp
        )
    }
}
