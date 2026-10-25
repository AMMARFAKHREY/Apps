package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.DietType
import com.example.data.model.FitnessGoal
import com.example.data.model.MealItem
import com.example.data.model.MealType
import com.example.data.model.PostureFeedback
import com.example.data.repository.FitAmmarRepository
import com.example.network.GeminiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppTab(val titleAr: String) {
    DASHBOARD("الرئيسية"),
    FITNESS("التمارين والذكاء"),
    NUTRITION("التغذية الذكية"),
    HEALTH("المؤشرات والصحة"),
    PREMIUM("الباقات والمدرب")
}

class FitAmmarViewModel(application: Application) : AndroidViewModel(application) {

    val repository = FitAmmarRepository()

    private val _currentTab = MutableStateFlow(AppTab.DASHBOARD)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _isCameraCoachActive = MutableStateFlow(false)
    val isCameraCoachActive: StateFlow<Boolean> = _isCameraCoachActive.asStateFlow()

    private val _isMealScannerOpen = MutableStateFlow(false)
    val isMealScannerOpen: StateFlow<Boolean> = _isMealScannerOpen.asStateFlow()

    private val _isBreathingActive = MutableStateFlow(false)
    val isBreathingActive: StateFlow<Boolean> = _isBreathingActive.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _postureFeedback = MutableStateFlow<PostureFeedback?>(null)
    val postureFeedback: StateFlow<PostureFeedback?> = _postureFeedback.asStateFlow()

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

    private val _repCount = MutableStateFlow(0)
    val repCount: StateFlow<Int> = _repCount.asStateFlow()

    private val _isSettingsOpen = MutableStateFlow(false)
    val isSettingsOpen: StateFlow<Boolean> = _isSettingsOpen.asStateFlow()

    private val _statusNotice = MutableStateFlow<String?>(null)
    val statusNotice: StateFlow<String?> = _statusNotice.asStateFlow()

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun openSettings() {
        _isSettingsOpen.value = true
    }

    fun closeSettings() {
        _isSettingsOpen.value = false
    }

    fun changeTheme(theme: com.example.data.model.AppThemeChoice) {
        repository.updateTheme(theme)
        showNotice("تم تفعيل ثيم '${theme.titleAr}' بنجاح!")
    }

    fun updateUserSettings(settings: com.example.data.model.UserSettings) {
        repository.updateSettings(settings)
        showNotice("تم حفظ الإعدادات بنجاح!")
    }

    fun resetAllData() {
        repository.resetData()
        showNotice("تمت إعادة تعيين البيانات وسجل الوجبات إلى الحالة الافتراضية.")
    }

    fun openCameraCoach() {
        _repCount.value = 0
        _postureFeedback.value = PostureFeedback(
            exerciseName = "تمرين القرفصاء الذكي (Smart Squat)",
            formScore = 96,
            isSafe = true,
            statusMessage = "وجّه الكاميرا لكامل جسمك. استعد لبدء التكرار الأول.",
            angleDegrees = 175f,
            recommendations = listOf("ضع الهاتف على بعد مترين بزاوية جانبية", "تأكد من إضاءة كافية في الغرفة")
        )
        _isCameraCoachActive.value = true
    }

    fun closeCameraCoach() {
        _isCameraCoachActive.value = false
    }

    fun openMealScanner() {
        _isMealScannerOpen.value = true
    }

    fun closeMealScanner() {
        _isMealScannerOpen.value = false
    }

    fun setBreathingActive(active: Boolean) {
        _isBreathingActive.value = active
    }

    fun updateFatigueAndSleep(fatigue: Int, sleepHours: Float, goal: FitnessGoal) {
        repository.updateCondition(fatigue, sleepHours, goal)
        showNotice("تم تحديث خطتك التدريبية والغذائية فورياً بالذكاء الاصطناعي!")
    }

    fun logRunningDistance(km: Float) {
        repository.logExtraRunning(km)
        showNotice("تم تحديث نشاطك (${km} كم) وتعديل السعرات لتعويض الطاقة!")
    }

    fun processMealScan(dishName: String, base64Image: String? = null) {
        viewModelScope.launch {
            _isAiLoading.value = true
            val analyzedMeal = GeminiClient.analyzeMealWithAi(dishName, base64Image)
            repository.addMeal(analyzedMeal)
            _isAiLoading.value = false
            _isMealScannerOpen.value = false
            showNotice("تم تحليل وجبة '${analyzedMeal.nameAr}' بنجاح وإضافتها لسجلك!")
        }
    }

    fun addManualMeal(name: String, type: MealType, cals: Int, prot: Float, carbs: Float, fat: Float) {
        val meal = MealItem(
            id = java.util.UUID.randomUUID().toString(),
            nameAr = name,
            mealType = type,
            calories = cals,
            proteinGrams = prot,
            carbsGrams = carbs,
            fatGrams = fat,
            timeLogged = "الآن",
            healthGrade = "A",
            aiAdviceAr = "وجبة مدخلة يدوياً تم تضمينها في حسابات الماكروز اليومية."
        )
        repository.addMeal(meal)
        showNotice("تمت إضافة الوجبة بنجاح!")
    }

    fun deleteMeal(mealId: String) {
        repository.removeMeal(mealId)
    }

    fun changeDiet(diet: DietType) {
        repository.setDiet(diet)
        showNotice("تم تحويل النظام إلى ${diet.titleAr} وتحديث قائمة التسوق!")
    }

    fun toggleGrocery(id: String) {
        repository.toggleGroceryItem(id)
    }

    fun addGrocery(name: String, cat: String, qty: String) {
        repository.addCustomGroceryItem(name, cat, qty)
        showNotice("تمت إضافة الغرض لقائمة التسوق!")
    }

    fun triggerSimulatedRepAnalysis(depthAngle: Float, isKneesPastToes: Boolean) {
        viewModelScope.launch {
            val currentReps = _repCount.value + 1
            _repCount.value = currentReps
            val feedback = GeminiClient.analyzePostureWithAi(
                exerciseName = "تمرين القرفصاء (Smart Squat)",
                currentDepthAngle = depthAngle,
                isKneesSafe = !isKneesPastToes,
                repCount = currentReps
            )
            _postureFeedback.value = feedback
            vibrateDevice(!isKneesPastToes)
        }
    }

    private fun vibrateDevice(isSuccess: Boolean) {
        try {
            val vibrator = getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator != null && vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val effect = if (isSuccess) {
                        VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE)
                    } else {
                        VibrationEffect.createWaveform(longArrayOf(0, 100, 80, 150), -1)
                    }
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(100)
                }
            }
        } catch (_: Exception) {}
    }

    fun shareHealthReport(context: Context) {
        val report = repository.generateHealthReport()
        val text = """
            🏥 *تقرير FitAmmar AI الصحي الشامل*
            👤 المستخدم: ${report.userName}
            📅 التاريخ: ${report.generatedDate}
            📊 الدورة: ${report.period}
            
            🔹 معدل ضغط الدم: ${report.avgBloodPressure}
            🔹 نبض الراحة (RHR): ${report.avgRestingHeartRate} bpm
            🔹 الوزن الحالي: ${report.currentWeightKg} كجم (التغير: ${report.weightDeltaKg} كجم)
            🔹 مؤشر كتلة الجسم (BMI): ${report.bmi}
            🔹 التمارين المنجزة: ${report.totalWorkoutsCompleted} تمرين
            🔹 إجمالي حرق السعرات: ${report.totalCaloriesBurned} Kcal
            🔹 جودة النوم: ${report.avgSleepScore}%
            🔹 متوسط التوتر: ${report.avgDailyStress}/100
            
            💡 التوصية الطبية والرياضية:
            ${report.clinicalRecommendationAr}
            
            📱 صادر بواسطة تطبيق FitAmmar AI - مدربك ومتابعك الصحي الذكي.
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "مشاركة التقرير الصحي")
        context.startActivity(shareIntent)
    }

    private fun showNotice(msg: String) {
        viewModelScope.launch {
            _statusNotice.value = msg
            delay(3500)
            if (_statusNotice.value == msg) {
                _statusNotice.value = null
            }
        }
    }
}
