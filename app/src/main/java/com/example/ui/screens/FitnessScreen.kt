package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Exercise
import com.example.data.model.FitnessGoal
import com.example.ui.FitAmmarViewModel
import com.example.ui.theme.FitCoral
import com.example.ui.theme.FitCyan
import com.example.ui.theme.FitDarkBorder
import com.example.ui.theme.FitDarkSurface
import com.example.ui.theme.FitDarkSurfaceVariant
import com.example.ui.theme.FitEmerald
import com.example.ui.theme.FitOrange

@Composable
fun FitnessScreen(
    viewModel: FitAmmarViewModel,
    modifier: Modifier = Modifier
) {
    val workoutPlan by viewModel.repository.currentWorkoutPlan.collectAsStateWithLifecycle()
    val condition by viewModel.repository.userCondition.collectAsStateWithLifecycle()

    var showConditionEditor by remember { mutableStateOf(false) }
    var tempFatigue by remember { mutableIntStateOf(condition.fatigueLevel) }
    var tempSleep by remember { mutableFloatStateOf(condition.sleepHours) }
    var tempGoal by remember { mutableStateOf(condition.goal) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("fitness_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Launch Computer Vision Coach Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("camera_cv_banner"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FitDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FitCyan, FitEmerald)))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(FitCyan.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Computer Vision",
                                    tint = FitCyan,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "رصد الأداء بالكاميرا (Computer Vision)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "تحليل حركات الجسم وتنبيهات فورية ضد الإصابات",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FitEmerald
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.openCameraCoach() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("launch_camera_coach_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = FitCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF00363D))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بدء جلسة التدريب ورصد الوضعية",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00363D)
                        )
                    }
                }
            }
        }

        // Dynamic AI Adaptation Controller Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("adaptation_controller_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FitDarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showConditionEditor = !showConditionEditor },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = FitCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "محرك التكيّف الديناميكي (إجهاد + نوم)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = if (showConditionEditor) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = workoutPlan.adaptedReason,
                        style = MaterialTheme.typography.bodySmall,
                        color = FitCyan,
                        lineHeight = 18.sp
                    )

                    AnimatedVisibility(visible = showConditionEditor) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            // Fatigue slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("مستوى الإجهاد العصبي والبدني:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$tempFatigue / 10", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = if (tempFatigue >= 7) FitCoral else FitCyan)
                            }
                            Slider(
                                value = tempFatigue.toFloat(),
                                onValueChange = { tempFatigue = it.toInt() },
                                valueRange = 1f..10f,
                                steps = 8,
                                colors = SliderDefaults.colors(thumbColor = FitCyan, activeTrackColor = FitCyan),
                                modifier = Modifier.testTag("fatigue_slider")
                            )

                            // Sleep slider
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("ساعات النوم الليلة الماضية:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${String.format("%.1f", tempSleep)} ساعة", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = FitEmerald)
                            }
                            Slider(
                                value = tempSleep,
                                onValueChange = { tempSleep = it },
                                valueRange = 4f..10f,
                                steps = 11,
                                colors = SliderDefaults.colors(thumbColor = FitEmerald, activeTrackColor = FitEmerald),
                                modifier = Modifier.testTag("sleep_slider")
                            )

                            // Fitness Goal selection
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("الهدف الرياضي الحالي:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FitnessGoal.values().forEach { goal ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (tempGoal == goal) FitCyan.copy(alpha = 0.2f) else FitDarkSurface)
                                            .border(1.dp, if (tempGoal == goal) FitCyan else FitDarkBorder, RoundedCornerShape(8.dp))
                                            .clickable { tempGoal = goal }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = goal.titleAr.split(" ").first(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (tempGoal == goal) FontWeight.Bold else FontWeight.Normal,
                                            color = if (tempGoal == goal) FitCyan else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    viewModel.updateFatigueAndSleep(tempFatigue, tempSleep, tempGoal)
                                    showConditionEditor = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("apply_adaptation_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = FitEmerald),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("تحديث الخطة تلقائياً بالذكاء الاصطناعي", color = Color(0xFF00391A), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "جدول تمارين الجلسة (${workoutPlan.exercises.size} تمارين)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${workoutPlan.estimatedMinutes} دقيقة",
                    style = MaterialTheme.typography.labelMedium,
                    color = FitCyan
                )
            }
        }

        items(workoutPlan.exercises) { exercise ->
            ExerciseDetailCard(
                exercise = exercise,
                onToggleCompleted = { viewModel.repository.toggleExerciseCompleted(exercise.id) },
                onLaunchCamera = { viewModel.openCameraCoach() }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ExerciseDetailCard(
    exercise: Exercise,
    onToggleCompleted: () -> Unit,
    onLaunchCamera: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("exercise_card_${exercise.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = FitDarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = if (exercise.isCompleted) {
                androidx.compose.ui.graphics.SolidColor(FitEmerald)
            } else {
                androidx.compose.ui.graphics.SolidColor(FitDarkBorder)
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (exercise.isCompleted) FitEmerald else FitDarkSurfaceVariant)
                            .clickable { onToggleCompleted() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (exercise.isCompleted) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                        } else {
                            Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = null, tint = FitCyan, modifier = Modifier.size(18.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = exercise.nameAr,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = exercise.targetMuscle,
                            style = MaterialTheme.typography.labelSmall,
                            color = FitCyan
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(FitDarkSurfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${exercise.sets} جولات × ${exercise.reps}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = FitOrange
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "راحة بين الجولات: ${exercise.restSeconds} ثانية",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = if (isExpanded) "إخفاء التفاصيل" else "تفاصيل الحركة والسلامة",
                    style = MaterialTheme.typography.labelSmall,
                    color = FitCyan,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = exercise.instructionsAr,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "نصائح الكاميرا وتفادي الإصابة:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = FitEmerald
                    )
                    exercise.formTipsAr.forEach { tip ->
                        Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(FitEmerald))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = tip, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onLaunchCamera,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitDarkSurfaceVariant),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = FitCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("رصد وضعية هذا التمرين بالكاميرا", color = FitCyan, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
