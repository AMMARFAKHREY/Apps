package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.PostureFeedback
import com.example.ui.FitAmmarViewModel
import com.example.ui.theme.FitCoral
import com.example.ui.theme.FitCyan
import com.example.ui.theme.FitDarkBackground
import com.example.ui.theme.FitDarkSurface
import com.example.ui.theme.FitEmerald
import com.example.ui.theme.FitOrange

@Composable
fun PostureCoachScreen(
    viewModel: FitAmmarViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val feedback by viewModel.postureFeedback.collectAsStateWithLifecycle(initialValue = null)
    val repsCount by viewModel.repCount.collectAsStateWithLifecycle(initialValue = 0)

    var simulatedAngle by remember { mutableFloatStateOf(92f) }

    // Pulsing HUD scan animation
    val infiniteTransition = rememberInfiniteTransition(label = "scan")
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanLineY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FitDarkBackground)
            .testTag("posture_coach_screen")
    ) {
        // Camera View or High-Tech Pose Simulation Canvas
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }
                        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                        } catch (e: Exception) {
                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview)
                            } catch (_: Exception) {}
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // High-Tech Cyber Vision Canvas when camera preview initializes
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF070C14)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "جاري تفعيل مجسّات الرؤية الحاسوبية...",
                    color = FitCyan.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Computer Vision HUD Overlay Canvas (Skeleton tracking simulation)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Scanning horizontal line
            val yPos = height * scanLineY
            drawLine(
                color = FitCyan.copy(alpha = 0.5f),
                start = Offset(x = 0f, y = yPos),
                end = Offset(x = width, y = yPos),
                strokeWidth = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f)
            )

            // Skeletal joint tracking simulation for Squat
            val head = Offset(width * 0.5f, height * 0.32f)
            val spine = Offset(width * 0.5f, height * 0.48f)
            val leftHip = Offset(width * 0.44f, height * 0.52f)
            val rightHip = Offset(width * 0.56f, height * 0.52f)
            val leftKnee = Offset(width * 0.42f, height * 0.65f)
            val rightKnee = Offset(width * 0.58f, height * 0.65f)
            val leftAnkle = Offset(width * 0.42f, height * 0.78f)
            val rightAnkle = Offset(width * 0.58f, height * 0.78f)

            val jointColor = if (feedback?.isSafe == false) FitCoral else FitCyan
            val strokeColor = if (feedback?.isSafe == false) FitCoral.copy(alpha = 0.7f) else FitEmerald.copy(alpha = 0.7f)

            // Draw limbs
            drawLine(strokeColor, head, spine, strokeWidth = 4f)
            drawLine(strokeColor, spine, leftHip, strokeWidth = 4f)
            drawLine(strokeColor, spine, rightHip, strokeWidth = 4f)
            drawLine(strokeColor, leftHip, leftKnee, strokeWidth = 4f)
            drawLine(strokeColor, rightHip, rightKnee, strokeWidth = 4f)
            drawLine(strokeColor, leftKnee, leftAnkle, strokeWidth = 4f)
            drawLine(strokeColor, rightKnee, rightAnkle, strokeWidth = 4f)

            // Draw joints
            listOf(head, spine, leftHip, rightHip, leftKnee, rightKnee, leftAnkle, rightAnkle).forEach { joint ->
                drawCircle(color = jointColor, radius = 9f, center = joint)
                drawCircle(color = Color.White, radius = 4f, center = joint)
            }
        }

        // Top Control Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(FitDarkSurface.copy(alpha = 0.85f))
                    .testTag("back_from_coach_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "الرجوع",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(FitDarkSurface.copy(alpha = 0.85f))
                    .border(1.dp, FitCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(FitEmerald))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "رصد الحركة بالذكاء الاصطناعي (AI CV)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = FitCyan
                    )
                }
            }

            // Reps counter badge
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(FitCyan)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$repsCount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF00363D)
                    )
                    Text(
                        text = "تكرار",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        color = Color(0xFF00363D)
                    )
                }
            }
        }

        // Bottom HUD Feedback Panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FitDarkSurface.copy(alpha = 0.94f)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = if (feedback?.isSafe == false) {
                        androidx.compose.ui.graphics.SolidColor(FitCoral)
                    } else {
                        androidx.compose.ui.graphics.SolidColor(FitCyan.copy(alpha = 0.6f))
                    }
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (feedback?.isSafe == false) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (feedback?.isSafe == false) FitCoral else FitEmerald,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = feedback?.exerciseName ?: "تمرين القرفصاء الذكي",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Form score badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (feedback?.isSafe == false) FitCoral.copy(alpha = 0.2f) else FitEmerald.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "دقة الأداء: ${feedback?.formScore ?: 96}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (feedback?.isSafe == false) FitCoral else FitEmerald
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = feedback?.statusMessage ?: "الوضعية ممتازة. حافظ على استقامة الرقبة وانزل بثبات.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (feedback?.isSafe == false) FitCoral else MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "عمق النزول (زاوية الركبة): ${simulatedAngle.toInt()}°",
                            style = MaterialTheme.typography.labelSmall,
                            color = FitCyan
                        )
                        Text(
                            text = if (simulatedAngle in 85f..105f) "عمق موازٍ مثالي ✓" else "زاوية استقامة",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Real-time Action buttons for testing computer vision & rep counter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                simulatedAngle = (88..98).random().toFloat()
                                viewModel.triggerSimulatedRepAnalysis(depthAngle = simulatedAngle, isKneesPastToes = false)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("trigger_good_rep_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = FitCyan),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("تكرار صحيح (92°)", color = Color(0xFF00363D), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                simulatedAngle = 68f
                                viewModel.triggerSimulatedRepAnalysis(depthAngle = 68f, isKneesPastToes = true)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("trigger_bad_rep_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = FitCoral),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("محاكاة خطأ الركبة", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
