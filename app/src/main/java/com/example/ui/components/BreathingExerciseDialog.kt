package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.FitCyan
import com.example.ui.theme.FitDarkSurface
import com.example.ui.theme.FitDarkSurfaceVariant
import com.example.ui.theme.FitEmerald
import kotlinx.coroutines.delay

@Composable
fun BreathingExerciseDialog(
    onDismiss: () -> Unit
) {
    var phase by remember { mutableStateOf("شهيق (خذ نفساً عميقاً)") }
    var secondsLeft by remember { mutableStateOf(4) }
    val circleScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            // Inhale (4s)
            phase = "شهيق عميق من الأنف"
            secondsLeft = 4
            circleScale.animateTo(1.45f, animationSpec = tween(4000, easing = LinearEasing))

            // Hold (7s)
            phase = "احبس النفس واسترخِ"
            for (i in 7 downTo 1) {
                secondsLeft = i
                delay(1000)
            }

            // Exhale (8s)
            phase = "زفير بطيء وهادئ من الفم"
            secondsLeft = 8
            circleScale.animateTo(1f, animationSpec = tween(8000, easing = LinearEasing))
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("breathing_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = FitDarkSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "جلسة تنفس ذكية لخفض التوتر (4-7-8)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "تساعد على خفض نبضات القلب وإفراز هرمونات الاسترخاء لتحسين جودة النوم.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .scale(circleScale.value)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(FitCyan.copy(alpha = 0.4f), FitEmerald.copy(alpha = 0.15f))
                            )
                        )
                        .border(2.dp, FitCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$secondsLeft",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = FitCyan
                        )
                        Text(
                            text = "ثوانٍ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = phase,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = FitEmerald
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("close_breathing_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = FitDarkSurfaceVariant)
                ) {
                    Text("إنهاء الجلسة والعودة", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
