package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.FitAmmarViewModel
import com.example.ui.theme.FitCoral
import com.example.ui.theme.FitCyan
import com.example.ui.theme.FitDarkBorder
import com.example.ui.theme.FitDarkSurface
import com.example.ui.theme.FitDarkSurfaceVariant
import com.example.ui.theme.FitEmerald
import com.example.ui.theme.FitOrange
import com.example.ui.theme.FitPurple

@Composable
fun HealthVitalsScreen(
    viewModel: FitAmmarViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val vitals by viewModel.repository.vitals.collectAsStateWithLifecycle()
    val sleep by viewModel.repository.sleepAnalysis.collectAsStateWithLifecycle()
    val report = viewModel.repository.generateHealthReport()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("health_vitals_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Sleep Tracking Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sleep_tracking_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FitDarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(FitPurple.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Nightlight, contentDescription = null, tint = FitPurple, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "متابع جودة النوم والاستشفاء العصبي",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${sleep.durationHours} ساعات • كفاءة ${sleep.sleepEfficiency}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(FitEmerald.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "تقييم ${sleep.qualityScore}%",
                                color = FitEmerald,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sleep Stages breakdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SleepStagePill(label = "نوم عميق (Deep)", hours = "${sleep.deepSleepHours} س", color = FitPurple)
                        SleepStagePill(label = "حركة العين (REM)", hours = "${sleep.remSleepHours} س", color = FitCyan)
                        SleepStagePill(label = "نوم خفيف (Light)", hours = "${sleep.lightSleepHours} س", color = FitEmerald)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "💡 نصيحة الاستشفاء: ${sleep.sleepAdviceAr}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Stress & RHR Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stress_tracker_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FitDarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(FitCyan.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = FitCyan, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "مؤشر التوتر والجهد العصبي (Stress Index)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "نبض وقت الراحة (RHR): ${vitals.restingHeartRate} bpm • تباين النبض: ${vitals.hrvMs}ms",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FitCyan
                                )
                            }
                        }

                        Text(
                            text = "${vitals.stressLevel} / 100",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FitEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val stressProgress = (vitals.stressLevel / 100f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { stressProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = FitEmerald,
                        trackColor = FitDarkSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.setBreathingActive(true) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_breathing_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = FitDarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FitCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.SelfImprovement, contentDescription = null, tint = FitCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بدء تمرين التنفس والتأمل لخفض التوتر (4-7-8)",
                            color = FitCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Shareable Monthly Clinical Health Dossier Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("clinical_report_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FitDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FitEmerald, FitCyan)))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(FitEmerald.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.LocalHospital, contentDescription = null, tint = FitEmerald, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "ملف صحي شامل للمشاركة مع الطبيب",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${report.period} • ${report.generatedDate}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4 Metric Grid
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            ReportMetricCell(label = "معدل ضغط الدم", value = report.avgBloodPressure, color = FitCyan)
                            ReportMetricCell(label = "نبض الراحة (RHR)", value = "${report.avgRestingHeartRate} bpm", color = FitCoral)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            ReportMetricCell(label = "الوزن ومؤشر الكتلة", value = "${report.currentWeightKg} كجم (BMI ${report.bmi})", color = FitEmerald)
                            ReportMetricCell(label = "إجمالي حرق السعرات", value = "${report.totalCaloriesBurned} Kcal", color = FitOrange)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(FitDarkSurfaceVariant)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "📋 التوصية: " + report.clinicalRecommendationAr,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.shareHealthReport(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("export_report_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = FitEmerald),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color(0xFF00391A))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "مشاركة التقرير الصحي مع الطبيب أو المدرب",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00391A)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SleepStagePill(label: String, hours: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = hours, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = color)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = color.copy(alpha = 0.8f), fontSize = 10.sp)
        }
    }
}

@Composable
fun ReportMetricCell(label: String, value: String, color: Color) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(FitDarkSurfaceVariant)
            .padding(10.dp)
    ) {
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
