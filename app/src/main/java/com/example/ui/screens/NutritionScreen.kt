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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.data.model.DietType
import com.example.data.model.GroceryItem
import com.example.data.model.MealItem
import com.example.data.model.MealType
import com.example.ui.FitAmmarViewModel
import com.example.ui.components.ActivityAdjustmentBanner
import com.example.ui.components.MacroProgressCard
import com.example.ui.theme.FitCoral
import com.example.ui.theme.FitCyan
import com.example.ui.theme.FitDarkBorder
import com.example.ui.theme.FitDarkSurface
import com.example.ui.theme.FitDarkSurfaceVariant
import com.example.ui.theme.FitEmerald
import com.example.ui.theme.FitOrange
import com.example.ui.theme.FitPurple

@Composable
fun NutritionScreen(
    viewModel: FitAmmarViewModel,
    modifier: Modifier = Modifier
) {
    val meals by viewModel.repository.loggedMeals.collectAsStateWithLifecycle()
    val diet by viewModel.repository.currentDiet.collectAsStateWithLifecycle()
    val groceries by viewModel.repository.groceryList.collectAsStateWithLifecycle()
    val adjustment by viewModel.repository.activityAdjustment.collectAsStateWithLifecycle()
    val condition by viewModel.repository.userCondition.collectAsStateWithLifecycle()
    val macros = viewModel.repository.calculateMacros()

    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Food Log, 1: Smart Grocery
    var extraKmInput by remember { mutableFloatStateOf(condition.extraRunningKmToday) }
    var showAddGroceryDialog by remember { mutableStateOf(false) }
    var newGroceryName by remember { mutableStateOf("") }
    var newGroceryQty by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("nutrition_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // AI Meal Scanner Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_meal_scanner_banner"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FitDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FitEmerald, FitCyan)))
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
                                    .background(FitEmerald.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = FitEmerald,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ماسح الوجبات الذكي بالذكاء الاصطناعي",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "صوّر طبقك ليحسب السعرات والماكروز تلقائياً",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FitCyan
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.openMealScanner() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("open_meal_scanner_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = FitEmerald),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF00391A))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "مسح طبق جديد وحساب القيمة الغذائية",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00391A)
                        )
                    }
                }
            }
        }

        // Daily Macros
        item {
            MacroProgressCard(macros = macros)
        }

        // Dynamic Calorie Adjustment Section (تعديل الوجبات بناءً على النشاط)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("activity_compensator_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FitDarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(FitOrange.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.DirectionsRun, contentDescription = null, tint = FitOrange, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "تعديل الوجبات التلقائي حسب المجهود والنشاط",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "إذا ركضت مسافة إضافية، يضيف التطبيق سعرات وبروتين للتعافي",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0f, 3f, 5f, 8f).forEach { km ->
                            val isSelected = extraKmInput == km
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) FitOrange.copy(alpha = 0.2f) else FitDarkSurface)
                                    .border(1.dp, if (isSelected) FitOrange else FitDarkBorder, RoundedCornerShape(10.dp))
                                    .clickable {
                                        extraKmInput = km
                                        viewModel.logRunningDistance(km)
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (km == 0f) "لا ركض" else "+${km.toInt()} كم",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) FitOrange else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    if (adjustment != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        ActivityAdjustmentBanner(adjustment = adjustment!!)
                    }
                }
            }
        }

        // Sub Tabs: Logged Meals vs Smart Grocery
        item {
            TabRow(
                selectedTabIndex = selectedSubTab,
                containerColor = FitDarkSurface,
                contentColor = FitCyan,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                        color = FitCyan
                    )
                }
            ) {
                Tab(
                    selected = selectedSubTab == 0,
                    onClick = { selectedSubTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("سجل الوجبات اليومي (${meals.size})", fontWeight = FontWeight.Bold)
                        }
                    }
                )
                Tab(
                    selected = selectedSubTab == 1,
                    onClick = { selectedSubTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("قائمة التسوق الذكية", fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        }

        if (selectedSubTab == 0) {
            // Food Log List
            items(meals) { meal ->
                MealCard(
                    meal = meal,
                    onDelete = { viewModel.deleteMeal(meal.id) }
                )
            }
        } else {
            // Smart Grocery Tab
            item {
                Column {
                    Text(
                        text = "النظام الغذائي المعتمد:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(DietType.values()) { d ->
                            val isSelected = d == diet
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) FitEmerald.copy(alpha = 0.2f) else FitDarkSurface)
                                    .border(1.dp, if (isSelected) FitEmerald else FitDarkBorder, RoundedCornerShape(10.dp))
                                    .clickable { viewModel.changeDiet(d) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = d.titleAr,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) FitEmerald else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "المشتريات المقترحة لنظام (${diet.titleAr}):",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            items(groceries) { item ->
                GroceryRowItem(
                    item = item,
                    onToggle = { viewModel.toggleGrocery(item.id) }
                )
            }

            item {
                // Add custom item
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newGroceryName,
                        onValueChange = { newGroceryName = it },
                        placeholder = { Text("إضافة غرض (مثال: حليب لوز)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitEmerald,
                            unfocusedBorderColor = FitDarkBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Button(
                        onClick = {
                            if (newGroceryName.isNotBlank()) {
                                viewModel.addGrocery(newGroceryName, "مشتريات إضافية", "1 عبوة")
                                newGroceryName = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FitEmerald),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color(0xFF00391A))
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
fun MealCard(
    meal: MealItem,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("meal_card_${meal.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FitDarkSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(FitEmerald.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = meal.healthGrade,
                            color = FitEmerald,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = meal.nameAr,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${meal.calories} Kcal",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = FitOrange
                    )
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = FitDarkBorder, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "بروتين: ${meal.proteinGrams.toInt()}غ", style = MaterialTheme.typography.labelSmall, color = FitEmerald)
                Text(text = "كارب: ${meal.carbsGrams.toInt()}غ", style = MaterialTheme.typography.labelSmall, color = FitOrange)
                Text(text = "دهون: ${meal.fatGrams.toInt()}غ", style = MaterialTheme.typography.labelSmall, color = FitPurple)
                Text(text = meal.timeLogged, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "💬 " + meal.aiAdviceAr,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun GroceryRowItem(
    item: GroceryItem,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(containerColor = FitDarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = FitEmerald,
                        uncheckedColor = FitDarkBorder
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = item.nameAr,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (item.isChecked) FontWeight.Normal else FontWeight.Medium,
                        color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = item.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
            }

            Text(
                text = item.quantity,
                style = MaterialTheme.typography.labelSmall,
                color = FitCyan
            )
        }
    }
}
