package com.example.data.model

data class SubscriptionTier(
    val id: String,
    val nameAr: String,
    val priceMonthlyAr: String,
    val badgeAr: String,
    val isPopular: Boolean,
    val featuresAr: List<String>
)

data class TrainerConsultant(
    val id: String,
    val nameAr: String,
    val specialtyAr: String,
    val rating: Float,
    val experienceYears: Int,
    val avatarInitials: String,
    val isAvailableNow: Boolean
)
