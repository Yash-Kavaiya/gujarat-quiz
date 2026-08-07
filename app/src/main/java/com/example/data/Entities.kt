package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Aakash P.",
    val initials: String = "AP",
    val city: String = "Ahmedabad",
    val goal: String = "GPSC Class 1 & 2",
    val coins: Int = 7900,
    val streakDays: Int = 20,
    val longestStreak: Int = 28,
    val freezesOwned: Int = 2,
    val theme: String = "Saffron", // Saffron, Indigo, Midnight
    val onboardingCompleted: Boolean = true,
    val referralsCount: Int = 4,
    val totalQuizzes: Int = 28,
    val accuracyPercentage: Int = 72,
    val globalRank: Int = 142,
    val referralCode: String = "AP2026",
    val avatarUrl: String? = null
) {
    val district: String get() = city
    val currentStreak: Int get() = streakDays
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val timeAgo: String,
    val amount: Int, // positive for gain, negative for spend
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "category_progress")
data class CategoryProgressEntity(
    @PrimaryKey val categoryId: String,
    val isUnlocked: Boolean = true,
    val quizzesCompleted: Int = 0,
    val bestScorePercent: Int = 0
)

@Entity(tableName = "unlocked_capsules")
data class BookmarkedCapsuleEntity(
    @PrimaryKey val capsuleId: String,
    val isBookmarked: Boolean = true
)
