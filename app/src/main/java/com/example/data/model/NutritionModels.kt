package com.example.data.model

data class MealItem(
    val id: String,
    val nameAr: String,
    val mealType: MealType,
    val calories: Int,
    val proteinGrams: Float,
    val carbsGrams: Float,
    val fatGrams: Float,
    val timeLogged: String,
    val healthGrade: String = "A", // A+, A, B, C
    val aiAdviceAr: String = "وجبة متوازنة غنية بالألياف والبروتين للاستشفاء"
)

enum class MealType(val titleAr: String) {
    BREAKFAST("الإفطار"),
    LUNCH("الغداء"),
    DINNER("العشاء"),
    SNACK("وجبة خفيفة / سناك")
}

data class Macronutrients(
    val currentCalories: Int,
    val targetCalories: Int,
    val currentProtein: Float,
    val targetProtein: Float,
    val currentCarbs: Float,
    val targetCarbs: Float,
    val currentFat: Float,
    val targetFat: Float
)

data class GroceryItem(
    val id: String,
    val nameAr: String,
    val category: String, // بروتينات, خضار وفواكه, دهون صحية, حبوب
    val quantity: String,
    val isChecked: Boolean = false
)

enum class DietType(val titleAr: String, val descAr: String) {
    HIGH_PROTEIN("عالي البروتين", "مثالي لبناء العضلات والرياضيين"),
    KETO("كيتو كلاسيك", "منخفض الكاربوهيدرات وغني بالدهون الصحية"),
    BALANCED("متوازن صحي", "نسب متوازنة للطاقة والحيوية طوال اليوم"),
    MEDITERRANEAN("حمية البحر المتوسط", "غني بزيت الزيتون والأسماك والخضار"),
    VEGETARIAN("نباتي رياضي", "مصادر بروتين نباتية مع بذور وحبوب كاملة")
}

data class ActivityAdjustment(
    val extraKmRun: Float,
    val addedCalories: Int,
    val addedProteinG: Float,
    val addedCarbsG: Float,
    val suggestionAr: String
)
