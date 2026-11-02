package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Aspirant",
    val initials: String = "AS",
    val city: String = "Ahmedabad",
    val goal: String = "GPSC Class 1 & 2 / Prelims 2026",
    val coins: Int = 500,
    val streakDays: Int = 0,
    val longestStreak: Int = 0,
    val freezesOwned: Int = 1,
    val theme: String = "Saffron", // Saffron, Indigo, Midnight
    val onboardingCompleted: Boolean = false,
    val referralsCount: Int = 0,
    val totalQuizzes: Int = 0,
    val accuracyPercentage: Int = 0,
    val globalRank: Int = 0,
    val referralCode: String = "AS2026",
    val avatarUrl: String? = null,
    /** Local epoch day of the last completed quiz; -1 when the user has never played. */
    val lastActiveEpochDay: Long = -1,
    /** Comma separated streak milestone day counts that were already claimed. */
    val claimedMilestones: String = "",
    /** 50-50 hints bought from the shop. */
    val hintsOwned: Int = 0,
    /** Local epoch day of the last daily challenge attempt; -1 when never played. */
    val lastDailyChallengeEpochDay: Long = -1
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

@Entity(tableName = "leaderboard_users")
data class LeaderboardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val initials: String,
    val city: String,
    val totalScore: Int,
    val delta: String = "0",
    val isCurrentUser: Boolean = false
)

@Entity(tableName = "activity_days")
data class ActivityDayEntity(
    @PrimaryKey val epochDay: Long,
    val quizzesCompleted: Int = 0,
    val isFrozen: Boolean = false
)
