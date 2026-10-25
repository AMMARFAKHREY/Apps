package com.example.data.repository

import com.example.data.model.ActivityAdjustment
import com.example.data.model.DietType
import com.example.data.model.AppThemeChoice
import com.example.data.model.Exercise
import com.example.data.model.FitnessGoal
import com.example.data.model.GroceryItem
import com.example.data.model.HealthReportSummary
import com.example.data.model.Macronutrients
import com.example.data.model.MealItem
import com.example.data.model.MealType
import com.example.data.model.SleepAnalysis
import com.example.data.model.SubscriptionTier
import com.example.data.model.TrainerConsultant
import com.example.data.model.UserDailyCondition
import com.example.data.model.UserSettings
import com.example.data.model.VitalsData
import com.example.data.model.WorkoutPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class FitAmmarRepository {

    private val _userCondition = MutableStateFlow(
        UserDailyCondition(
            fatigueLevel = 3,
            sleepHours = 7.5f,
            goal = FitnessGoal.MUSCLE_GAIN,
            previousAdherenceRate = 88f,
            extraRunningKmToday = 0f
        )
    )
    val userCondition: StateFlow<UserDailyCondition> = _userCondition.asStateFlow()

    private val _currentWorkoutPlan = MutableStateFlow(generateAdaptedPlan(_userCondition.value))
    val currentWorkoutPlan: StateFlow<WorkoutPlan> = _currentWorkoutPlan.asStateFlow()

    private val _vitals = MutableStateFlow(VitalsData())
    val vitals: StateFlow<VitalsData> = _vitals.asStateFlow()

    private val _sleepAnalysis = MutableStateFlow(SleepAnalysis())
    val sleepAnalysis: StateFlow<SleepAnalysis> = _sleepAnalysis.asStateFlow()

    private val _loggedMeals = MutableStateFlow(getInitialMeals())
    val loggedMeals: StateFlow<List<MealItem>> = _loggedMeals.asStateFlow()

    private val _currentDiet = MutableStateFlow(DietType.HIGH_PROTEIN)
    val currentDiet: StateFlow<DietType> = _currentDiet.asStateFlow()

    private val _groceryList = MutableStateFlow(generateGroceries(DietType.HIGH_PROTEIN))
    val groceryList: StateFlow<List<GroceryItem>> = _groceryList.asStateFlow()

    private val _activityAdjustment = MutableStateFlow<ActivityAdjustment?>(null)
    val activityAdjustment: StateFlow<ActivityAdjustment?> = _activityAdjustment.asStateFlow()

    private val _subscriptionTiers = MutableStateFlow(getTiers())
    val subscriptionTiers: StateFlow<List<SubscriptionTier>> = _subscriptionTiers.asStateFlow()

    private val _trainerConsultants = MutableStateFlow(getTrainers())
    val trainerConsultants: StateFlow<List<TrainerConsultant>> = _trainerConsultants.asStateFlow()

    private val _userSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _userSettings.asStateFlow()

    fun updateTheme(newTheme: AppThemeChoice) {
        _userSettings.value = _userSettings.value.copy(theme = newTheme)
    }

    fun updateSettings(updated: UserSettings) {
        _userSettings.value = updated
    }

    fun resetData() {
        _loggedMeals.value = getInitialMeals()
        _activityAdjustment.value = null
        _vitals.value = VitalsData()
        _userCondition.value = UserDailyCondition()
        _currentWorkoutPlan.value = generateAdaptedPlan(_userCondition.value)
    }

    fun updateCondition(fatigue: Int, sleep: Float, goal: FitnessGoal) {
        val updated = _userCondition.value.copy(
            fatigueLevel = fatigue,
            sleepHours = sleep,
            goal = goal
        )
        _userCondition.value = updated
        _currentWorkoutPlan.value = generateAdaptedPlan(updated)
    }

    fun logExtraRunning(km: Float) {
        val current = _userCondition.value
        val updated = current.copy(extraRunningKmToday = km)
        _userCondition.value = updated

        if (km > 0.5f) {
            val calories = (km * 68).toInt()
            val extraProtein = (km * 4.5f)
            val extraCarbs = (km * 8.2f)
            _activityAdjustment.value = ActivityAdjustment(
                extraKmRun = km,
                addedCalories = calories,
                addedProteinG = extraProtein,
                addedCarbsG = extraCarbs,
                suggestionAr = "ركضت ${km} كم اليوم! أضاف الذكاء الاصطناعي $calories سعرة و ${extraProtein.toInt()}غ بروتين و ${extraCarbs.toInt()}غ كارب لخطتك الغذائية لضمان التعافي وسرعة الاستشفاء العضلي."
            )
            // also update wearable calories burned
            _vitals.value = _vitals.value.copy(
                caloriesBurned = _vitals.value.caloriesBurned + calories,
                steps = _vitals.value.steps + (km * 1350).toInt()
            )
        } else {
            _activityAdjustment.value = null
        }
    }

    fun addMeal(meal: MealItem) {
        _loggedMeals.value = listOf(meal) + _loggedMeals.value
    }

    fun removeMeal(mealId: String) {
        _loggedMeals.value = _loggedMeals.value.filter { it.id != mealId }
    }

    fun setDiet(diet: DietType) {
        _currentDiet.value = diet
        _groceryList.value = generateGroceries(diet)
    }

    fun toggleGroceryItem(id: String) {
        _groceryList.value = _groceryList.value.map {
            if (it.id == id) it.copy(isChecked = !it.isChecked) else it
        }
    }

    fun addCustomGroceryItem(name: String, category: String, qty: String) {
        val item = GroceryItem(
            id = UUID.randomUUID().toString(),
            nameAr = name,
            category = category,
            quantity = qty,
            isChecked = false
        )
        _groceryList.value = _groceryList.value + item
    }

    fun toggleExerciseCompleted(exerciseId: String) {
        val current = _currentWorkoutPlan.value
        val updatedExercises = current.exercises.map {
            if (it.id == exerciseId) it.copy(isCompleted = !it.isCompleted) else it
        }
        _currentWorkoutPlan.value = current.copy(exercises = updatedExercises)
    }

    fun calculateMacros(): Macronutrients {
        val meals = _loggedMeals.value
        val totalCalories = meals.sumOf { it.calories }
        val totalProtein = meals.sumOf { it.proteinGrams.toDouble() }.toFloat()
        val totalCarbs = meals.sumOf { it.carbsGrams.toDouble() }.toFloat()
        val totalFat = meals.sumOf { it.fatGrams.toDouble() }.toFloat()

        val extra = _activityAdjustment.value
        val targetCals = 2300 + (extra?.addedCalories ?: 0)
        val targetProt = 160f + (extra?.addedProteinG ?: 0f)
        val targetCarb = 210f + (extra?.addedCarbsG ?: 0f)
        val targetFat = 65f

        return Macronutrients(
            currentCalories = totalCalories,
            targetCalories = targetCals,
            currentProtein = totalProtein,
            targetProtein = targetProt,
            currentCarbs = totalCarbs,
            targetCarbs = targetCarb,
            currentFat = totalFat,
            targetFat = targetFat
        )
    }

    fun generateHealthReport(): HealthReportSummary {
        val dateStr = SimpleDateFormat("dd MMMM yyyy", Locale("ar")).format(Date())
        return HealthReportSummary(
            reportId = "FA-" + (1000..9999).random(),
            userName = "عمار المنصوري",
            generatedDate = dateStr,
            period = "تقرير اللياقة والمؤشرات الحيوية الشهري",
            avgBloodPressure = "118 / 76 mmHg",
            avgRestingHeartRate = _vitals.value.restingHeartRate,
            currentWeightKg = 77.4f,
            weightDeltaKg = -2.1f,
            bmi = 22.8f,
            totalWorkoutsCompleted = 24,
            totalCaloriesBurned = _vitals.value.caloriesBurned * 22,
            avgSleepScore = _sleepAnalysis.value.qualityScore,
            avgDailyStress = _vitals.value.stressLevel,
            clinicalRecommendationAr = "مؤشرات القلب والجهاز العضلي والتعافي في النطاق الذهبي. ينصح بالاستمرار في البروتين المرتفع وتمارين المقاومة مع الحفاظ على معدل شرب الماء."
        )
    }

    private fun generateAdaptedPlan(cond: UserDailyCondition): WorkoutPlan {
        val isFatigued = cond.fatigueLevel >= 7 || cond.sleepHours < 6.0f
        val isMildFatigue = cond.fatigueLevel in 5..6 || cond.sleepHours in 6.0f..6.9f

        val (adaptedReason, adjustedSets, difficulty, minutes) = when {
            isFatigued -> Tuple4(
                "تم خفض شدة التمرين تلقائياً بنسبة 35% لارتفاع الإجهاد (${cond.fatigueLevel}/10) وقلة النوم (${cond.sleepHours}h) لمنع الإصابة والإرهاق العصبي.",
                2,
                "استشفاء نشط وحركية",
                30
            )
            isMildFatigue -> Tuple4(
                "تمت موازنة الأحمال وتقليل المجموعات بمقدار مجموعة واحدة مع زيادة فترات الراحة بين الجولات لضمان استمرار البناء العضلي.",
                3,
                "متوسط متكيف",
                45
            )
            else -> Tuple4(
                "جاهزية عضلية ممتازة! نومك ${cond.sleepHours} ساعات ومستوى الإجهاد منخفض (${cond.fatigueLevel}/10). الخطة اليومية تعمل بأقصى كفاءة تحفيزية.",
                4,
                "شدة عالية وبناء مكثف",
                55
            )
        }

        val baseExercises = when (cond.goal) {
            FitnessGoal.MUSCLE_GAIN -> listOf(
                Exercise(
                    id = "ex_1",
                    nameAr = "قرفصاء البار الخلفي (Barbell Squat)",
                    nameEn = "Barbell Back Squat",
                    targetMuscle = "الأفخاذ الأمامية، الألوية وعضلات الجذع",
                    sets = adjustedSets,
                    reps = if (isFatigued) 8 else 12,
                    restSeconds = if (isFatigued) 90 else 60,
                    instructionsAr = "باعد بين قدميك بعرض الكتفين، انزل ببطء بحوضك للخلف حتى يوازي فخذك الأرض، ثم ادفع بكعبيك بقوة.",
                    formTipsAr = listOf("حافظ على استقامة الظهر", "لا تدع ركبتيك تتجاوزان أطراف أصابعك", "تنفس بعمق قبل النزول"),
                    postureKeyPoints = listOf("زاوية الركبة 90 درجة", "استقامة الجذع 45-60 درجة", "ثبات الكعبين")
                ),
                Exercise(
                    id = "ex_2",
                    nameAr = "تمرين الضغط الصدري الذكي (Push-ups)",
                    nameEn = "Standard Push-ups",
                    targetMuscle = "عضلات الصدر، الترايسبس والكتف الأمامي",
                    sets = adjustedSets,
                    reps = if (isFatigued) 10 else 15,
                    restSeconds = 60,
                    instructionsAr = "ثبت كفيك بمستوى أوسع قليلاً من الصدر، حافظ على خط مستقيم من الرأس حتى الكعبين.",
                    formTipsAr = listOf("لا تدع الحوض يهبط لأسفل", "المرفق بزاوية 45 درجة مع الجسم"),
                    postureKeyPoints = listOf("استقامة العمود الفقري", "ثبات عضلات البطن")
                ),
                Exercise(
                    id = "ex_3",
                    nameAr = "تمرين البلانك الثابت (Core Plank)",
                    nameEn = "Core Plank Hold",
                    targetMuscle = "عضلات البطن، أسفل الظهر وثبات الحوض",
                    sets = adjustedSets,
                    reps = if (isFatigued) 30 else 50,
                    restSeconds = 45,
                    instructionsAr = "استند على الساعدين وأطراف أصابع القدمين واضغط عضلات بطنك وألويتك بقوة.",
                    formTipsAr = listOf("تنفس منتظم وهادئ", "ممنوع تقوس الظهر لأسفل"),
                    postureKeyPoints = listOf("خط أفقي كامل للجسم", "المرفقان تحت الكتفين مباشرة")
                ),
                Exercise(
                    id = "ex_4",
                    nameAr = "الطعن التبادلي للأفخاذ (Walking Lunges)",
                    nameEn = "Walking Lunges",
                    targetMuscle = "الأفخاذ والأوتار الخلفية والتوازن",
                    sets = adjustedSets,
                    reps = 10,
                    restSeconds = 60,
                    instructionsAr = "اخطُ خطوة واسعة للأمام واخفض ركبتك الخلفية حتى تكاد تلامس الأرض بزاوية 90 درجة.",
                    formTipsAr = listOf("الجذع قائم دون ميل مفرط", "ثبات الركبة الأمامية"),
                    postureKeyPoints = listOf("زاوية 90 في الركبتين", "توزيع الوزن على منتصف القدم")
                )
            )
            FitnessGoal.FAT_LOSS -> listOf(
                Exercise(
                    id = "ex_fl_1",
                    nameAr = "بيربي مع قفزة حارقة (HIIT Burpees)",
                    nameEn = "HIIT Burpees",
                    targetMuscle = "كامل عضلات الجسم واللياقة الهوائية",
                    sets = adjustedSets,
                    reps = 12,
                    restSeconds = 45,
                    instructionsAr = "اهبط في وضع الضغط ثم اقفز بركبتيك للأمام واختتم بقفزة عمودية مع رفع اليدين.",
                    formTipsAr = listOf("سرعة محسوبة مع حذر للمفاصل", "الهبوط بخفة على مشطي القدمين"),
                    postureKeyPoints = listOf("ثبات الظهر أثناء النزول", "قفزة انسيابية")
                ),
                Exercise(
                    id = "ex_fl_2",
                    nameAr = "تسلق الجبل التبادلي (Mountain Climbers)",
                    nameEn = "Mountain Climbers",
                    targetMuscle = "عضلات البطن وحرق السعرات السريع",
                    sets = adjustedSets,
                    reps = 24,
                    restSeconds = 30,
                    instructionsAr = "في وضع البلانك المرتفع، ادفع بركبتيك بالتبادل باتجاه الصدر بسرعة وثبات.",
                    formTipsAr = listOf("حافظ على بقاء الحوض منخفضاً", "الكفان تحت الكتفين"),
                    postureKeyPoints = listOf("استقرار عضلات الجذع", "إيقاع تنفس متواصل")
                )
            )
            else -> listOf(
                Exercise(
                    id = "ex_gen_1",
                    nameAr = "قرفصاء بوزن الجسم (Bodyweight Squats)",
                    nameEn = "Bodyweight Squats",
                    targetMuscle = "الأفخاذ وحركية المفاصل",
                    sets = adjustedSets,
                    reps = 15,
                    restSeconds = 45,
                    instructionsAr = "انزل بحركة سلسة مع مد الذراعين للأمام للتوازن.",
                    formTipsAr = listOf("الصدر مرفوع والنظر للأمام", "النزول المتزن"),
                    postureKeyPoints = listOf("عمق متوازن 90 درجة")
                )
            )
        }

        return WorkoutPlan(
            id = "plan_" + UUID.randomUUID().toString().take(6),
            titleAr = "جلسة تكيّف ذكية: ${cond.goal.titleAr}",
            subtitleAr = if (isFatigued) "برنامج وقاية وتنشيط منخفض الجهد" else "برنامج محفز للنمو والقوة الرياضية",
            difficulty = difficulty,
            estimatedMinutes = minutes,
            caloriesBurnTarget = if (isFatigued) 220 else 460,
            adaptedReason = adaptedReason,
            exercises = baseExercises
        )
    }

    private fun getInitialMeals(): List<MealItem> = listOf(
        MealItem(
            id = "meal_1",
            nameAr = "شوفان بروتين بذور الشيا مع التوت البري",
            mealType = MealType.BREAKFAST,
            calories = 420,
            proteinGrams = 32f,
            carbsGrams = 54f,
            fatGrams = 9f,
            timeLogged = "08:30 ص",
            healthGrade = "A+",
            aiAdviceAr = "بداية مثالية ليومك مع كربوهيدرات معقدة بطيئة الامتصاص ومضادات أكسدة."
        ),
        MealItem(
            id = "meal_2",
            nameAr = "صدر دجاج مشوي مع كينوا وخضار مشكلة",
            mealType = MealType.LUNCH,
            calories = 580,
            proteinGrams = 52f,
            carbsGrams = 48f,
            fatGrams = 14f,
            timeLogged = "02:15 م",
            healthGrade = "A+",
            aiAdviceAr = "محتوى عالي من اللوسين لتحفيز تخليق البروتين العضلي (MPS)."
        )
    )

    private fun generateGroceries(diet: DietType): List<GroceryItem> = when (diet) {
        DietType.HIGH_PROTEIN -> listOf(
            GroceryItem("g1", "صدور دجاج طازجة متبلة", "بروتينات نقية", "1.5 كجم"),
            GroceryItem("g2", "سمك السلمون النرويجي", "أوميغا 3 وبروتين", "800 جم"),
            GroceryItem("g3", "بيض عضوي بلدي", "بروتين وفيتامينات", "30 بيضة"),
            GroceryItem("g4", "زبادي يوناني 0% دسم", "بروتين بطيء الامتصاص", "4 علب"),
            GroceryItem("g5", "أرز بسمتي بني / كينوا", "كاربوهيدرات معقدة", "1 كجم"),
            GroceryItem("g6", "بروكلي وسبانخ طازجة", "ألياف ومغذيات دقيقة", "500 جم"),
            GroceryItem("g7", "لوز نيء وزبدة فول سوداني", "دهون صحية غير مشبعة", "350 جم")
        )
        DietType.KETO -> listOf(
            GroceryItem("gk1", "لحم عجل مفروم 80/20", "بروتينات ودهون صحية", "1 كجم"),
            GroceryItem("gk2", "أفوكادو هاس مستوي", "بوتاسيوم ودهون مفيدة", "6 حبات"),
            GroceryItem("gk3", "زيت زيتون بكر ممتاز", "دهون أحادية نقية", "1 لتر"),
            GroceryItem("gk4", "جبن شيدر طبيعي وموزاريلا", "كالسيوم ودهون", "400 جم"),
            GroceryItem("gk5", "عين جمل ومكسرات مكاديميا", "دهون كيتو ممتازة", "300 جم")
        )
        else -> listOf(
            GroceryItem("gb1", "عدس بني وحمص عضوي", "بروتين نباتي وألياف", "800 جم"),
            GroceryItem("gb2", "فواكه طازجة (موز، تفاح، توت)", "سكريات طبيعية", "2 كجم"),
            GroceryItem("gb3", "شوفان حبوب كاملة", "ألياف قابلة للذوبان", "1 كجم")
        )
    }

    private fun getTiers(): List<SubscriptionTier> = listOf(
        SubscriptionTier(
            id = "tier_free",
            nameAr = "الباقة المجانية الأساسية",
            priceMonthlyAr = "مجاناً دائماً",
            badgeAr = "الأساسية",
            isPopular = false,
            featuresAr = listOf(
                "تتبع الخطوات والسعرات اليومية",
                "سجل الوجبات وحساب الماكروز الأساسي",
                "قائمة تسوق أسبوعية مصغرة",
                "تقرير مؤشرات حيوية موجز"
            )
        ),
        SubscriptionTier(
            id = "tier_smart_pro",
            nameAr = "FitAmmar AI Pro",
            priceMonthlyAr = "19.99 $ / شهرياً",
            badgeAr = "الأكثر طلباً",
            isPopular = true,
            featuresAr = listOf(
                "جميع ميزات الباقة المجانية",
                "رصد الأداء بالكاميرا والذكاء الاصطناعي (Computer Vision بدون حدود)",
                "تحليل فوري لوضعيات التمارين وتنبيهات تفادي الإصابات",
                "ماسح الوجبات الذكي بالكاميرا عبر Gemini Multimodal",
                "تعديل تلقائي فوري للسعرات عند بذل مجهود إضافي",
                "تصدير التقرير الصحي الشامل بصيغة تقرير طبي جاهز للمشاركة"
            )
        ),
        SubscriptionTier(
            id = "tier_vip",
            nameAr = "الباقة الملكية VIP (مدرب وأخصائي حقيقي)",
            priceMonthlyAr = "49.99 $ / شهرياً",
            badgeAr = "الشاملة",
            isPopular = false,
            featuresAr = listOf(
                "جميع مزايا FitAmmar AI Pro",
                "جلسات استشارية شهرية مباشرة مع مدرب معتمد",
                "أخصائي تغذية إكلينيكي مخصص لمتابعة فحوصاتك الدورية",
                "دعم واتساب خاص على مدار 24 ساعة للإجابة على استفساراتك"
            )
        )
    )

    private fun getTrainers(): List<TrainerConsultant> = listOf(
        TrainerConsultant(
            id = "tr_1",
            nameAr = "كابتن أحمد العتيبي",
            specialtyAr = "ماجستير علوم الرياضة وبناء الأجسام الوظيفي (CSCS)",
            rating = 4.9f,
            experienceYears = 9,
            avatarInitials = "أ.ع",
            isAvailableNow = true
        ),
        TrainerConsultant(
            id = "tr_2",
            nameAr = "د. سارة المنصور",
            specialtyAr = "أخصائية تغذية علاجية ورياضية معتمدة من CISSN",
            rating = 5.0f,
            experienceYears = 8,
            avatarInitials = "س.م",
            isAvailableNow = true
        )
    )
}

data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
