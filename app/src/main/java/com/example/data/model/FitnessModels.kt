package com.example.data.model

data class Exercise(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val targetMuscle: String,
    val sets: Int,
    val reps: Int,
    val restSeconds: Int,
    val instructionsAr: String,
    val formTipsAr: List<String>,
    val postureKeyPoints: List<String>,
    val isCompleted: Boolean = false
)

data class WorkoutPlan(
    val id: String,
    val titleAr: String,
    val subtitleAr: String,
    val difficulty: String, // مبتدئ, متوسط, متقدم
    val estimatedMinutes: Int,
    val caloriesBurnTarget: Int,
    val adaptedReason: String, // "تم تقليل الحمل بنسبة 20% بسبب تسجيل إجهاد مرتفع (7/10)"
    val exercises: List<Exercise>
)

data class UserDailyCondition(
    val fatigueLevel: Int = 3, // 1 to 10
    val sleepHours: Float = 7.5f,
    val goal: FitnessGoal = FitnessGoal.MUSCLE_GAIN,
    val previousAdherenceRate: Float = 85f, // percentage
    val extraRunningKmToday: Float = 0f
)

enum class FitnessGoal(val titleAr: String) {
    MUSCLE_GAIN("بناء عضلات وقوة"),
    FAT_LOSS("حرق دهون وخسارة وزن"),
    ENDURANCE("تحسين اللياقة والتحمل"),
    FLEXIBILITY("مرونة واستشفاء عضلي")
}

data class PostureFeedback(
    val exerciseName: String,
    val formScore: Int, // 0 - 100
    val isSafe: Boolean,
    val statusMessage: String,
    val angleDegrees: Float,
    val recommendations: List<String>,
    val repsCount: Int = 0
)
