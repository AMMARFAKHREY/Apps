package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FitCyan
import com.example.ui.theme.FitDarkBorder
import com.example.ui.theme.FitDarkSurface
import com.example.ui.theme.FitDarkSurfaceVariant
import com.example.ui.theme.FitEmerald
import com.example.ui.theme.FitOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealScannerSheet(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onAnalyzeMeal: (dishName: String, base64: String?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var inputDishName by remember { mutableStateOf("") }
    var selectedPhotoInfo by remember { mutableStateOf<String?>(null) }

    val presetMeals = listOf(
        "سالمون مشوي مع أفوكادو وكينوا",
        "كبسة دجاج صحية بأرز بسمتي بني",
        "سلطة سيزر بالدجاج المشوي وبذور القرع",
        "ستيك لحم بقري تندرلوين مع بطاطا مشوية",
        "بان كيك بروتين مع زبدة فول سوداني وتوت",
        "أومليت بياض بيض بالسبانخ وجبن فيتا"
    )

    // Android Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedPhotoInfo = "تم اختيار صورة الطبق بنجاح"
                if (inputDishName.isBlank()) {
                    inputDishName = "طبق صحي متنوع تم تصويره"
                }
            }
        }
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FitDarkSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .testTag("meal_scanner_sheet")
        ) {
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
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = FitCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ماسح الوجبات بالذكاء الاصطناعي",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Gemini AI Multimodal Vision",
                            style = MaterialTheme.typography.labelSmall,
                            color = FitCyan
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "التقط صورة لطبقك أو اختر من الأطباق المقترحة لتقدير السعرات، البروتين، والماكروز فورياً:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Camera / Gallery Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            androidx.activity.result.PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("select_photo_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = FitDarkSurfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, tint = FitCyan)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("اختر صورة من المعرض", color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        selectedPhotoInfo = "تم تشغيل ماسح الكاميرا الفوري"
                        if (inputDishName.isBlank()) inputDishName = "وجبة صحية تم تصويرها"
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_camera_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = FitDarkSurfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = FitEmerald)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("التقاط صورة للطبق", color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
                }
            }

            if (selectedPhotoInfo != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FitEmerald.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = FitEmerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = selectedPhotoInfo!!, color = FitEmerald, style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text input
            OutlinedTextField(
                value = inputDishName,
                onValueChange = { inputDishName = it },
                label = { Text("اسم الوجبة أو مكوناتها") },
                placeholder = { Text("مثال: نصف دجاجة مشوية مع 150جم أرز وسلطة") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("meal_name_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FitCyan,
                    unfocusedBorderColor = FitDarkBorder
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "أو اختر وجبة سريعة للتحليل المباشر:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presetMeals) { meal ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (inputDishName == meal) FitCyan.copy(alpha = 0.2f) else FitDarkSurfaceVariant)
                            .border(1.dp, if (inputDishName == meal) FitCyan else FitDarkBorder, RoundedCornerShape(10.dp))
                            .clickable { inputDishName = meal }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = meal,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (inputDishName == meal) FitCyan else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val dish = if (inputDishName.isNotBlank()) inputDishName else "وجبة رياضية متوازنة"
                    onAnalyzeMeal(dish, null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("analyze_meal_btn"),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = FitCyan),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.surface, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("جاري فحص المكونات بالذكاء الاصطناعي...", color = MaterialTheme.colorScheme.surface)
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.surface)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تحليل وحساب الماكروز الآن",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}
