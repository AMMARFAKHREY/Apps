package com.example

import com.example.data.model.DietType
import com.example.data.model.FitnessGoal
import com.example.data.repository.FitAmmarRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FitAmmarUnitTest {

    @Test
    fun testDynamicWorkoutAdaptationOnHighFatigue() {
        val repository = FitAmmarRepository()

        // When fatigue is high (8/10) and sleep is short (5 hours)
        repository.updateCondition(fatigue = 8, sleep = 5.0f, goal = FitnessGoal.MUSCLE_GAIN)

        val plan = repository.currentWorkoutPlan.value
        assertTrue(plan.adaptedReason.contains("خفض شدة التمرين"))
        assertEquals("استشفاء نشط وحركية", plan.difficulty)
    }

    @Test
    fun testDynamicWorkoutAdaptationOnOptimalReadiness() {
        val repository = FitAmmarRepository()

        // When condition is optimal (fatigue 2/10, sleep 8.0 hours)
        repository.updateCondition(fatigue = 2, sleep = 8.0f, goal = FitnessGoal.MUSCLE_GAIN)

        val plan = repository.currentWorkoutPlan.value
        assertTrue(plan.adaptedReason.contains("جاهزية عضلية ممتازة"))
        assertEquals("شدة عالية وبناء مكثف", plan.difficulty)
    }

    @Test
    fun testActivityBasedCalorieCompensation() {
        val repository = FitAmmarRepository()

        // When user logs running 5 km
        repository.logExtraRunning(5.0f)

        val adjustment = repository.activityAdjustment.value
        assertNotNull(adjustment)
        assertEquals(5.0f, adjustment!!.extraKmRun, 0.01f)
        assertTrue(adjustment.addedCalories > 300)
        assertTrue(adjustment.addedProteinG > 20f)
    }

    @Test
    fun testDietPlanGrocerySwitching() {
        val repository = FitAmmarRepository()

        // Switch to Keto
        repository.setDiet(DietType.KETO)
        val groceries = repository.groceryList.value
        assertTrue(groceries.any { it.nameAr.contains("أفوكادو") || it.nameAr.contains("لحم") })
    }

    @Test
    fun testAppThemeSelection() {
        val repository = FitAmmarRepository()

        // Switch theme to Emerald Power
        repository.updateTheme(com.example.data.model.AppThemeChoice.EMERALD_POWER)
        assertEquals(com.example.data.model.AppThemeChoice.EMERALD_POWER, repository.userSettings.value.theme)

        // Switch theme to Solar Amber
        repository.updateTheme(com.example.data.model.AppThemeChoice.SOLAR_AMBER)
        assertEquals(com.example.data.model.AppThemeChoice.SOLAR_AMBER, repository.userSettings.value.theme)
    }

    @Test
    fun testUserSettingsUpdates() {
        val repository = FitAmmarRepository()

        val newSettings = repository.userSettings.value.copy(
            voiceCoachEnabled = false,
            weightUnit = "رطل (Lb)"
        )
        repository.updateSettings(newSettings)
        assertEquals(false, repository.userSettings.value.voiceCoachEnabled)
        assertEquals("رطل (Lb)", repository.userSettings.value.weightUnit)
    }
}
