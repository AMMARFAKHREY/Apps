package com.example.data.model

data class VitalsData(
    val heartRateBpm: Int = 72,
    val restingHeartRate: Int = 58,
    val caloriesBurned: Int = 1840,
    val steps: Int = 8940,
    val bloodOxygenSpO2: Int = 98,
    val stressLevel: Int = 34, // 0 to 100
    val hrvMs: Int = 64, // Heart Rate Variability
    val connectedDeviceName: String = "Apple Watch Ultra 2 / Wear OS"
)

data class SleepAnalysis(
    val durationHours: Float = 7.6f,
    val qualityScore: Int = 88, // out of 100
    val deepSleepHours: Float = 2.1f,
    val remSleepHours: Float = 1.9f,
    val lightSleepHours: Float = 3.6f,
    val sleepEfficiency: String = "ممتازة",
    val sleepAdviceAr: String = "معدل استشفاء عصبي رائع. ينصح بالبدء بتمارين قوة خفيفة ثم متوسطة."
)

data class HealthReportSummary(
    val reportId: String,
    val userName: String = "عمار المنصوري",
    val generatedDate: String,
    val period: String = "تقرير أكتوبر الصحي الشامل",
    val avgBloodPressure: String = "118 / 76 mmHg",
    val avgRestingHeartRate: Int = 58,
    val currentWeightKg: Float = 77.4f,
    val weightDeltaKg: Float = -2.1f,
    val bmi: Float = 22.8f,
    val totalWorkoutsCompleted: Int = 22,
    val totalCaloriesBurned: Int = 41200,
    val avgSleepScore: Int = 86,
    val avgDailyStress: Int = 32,
    val clinicalRecommendationAr: String = "المؤشرات الحيوية ممتازة ومثالية لممارسة التمارين عالية الشدة. ينصح بمواصلة شرب 3.2 لتر ماء يومياً."
)
