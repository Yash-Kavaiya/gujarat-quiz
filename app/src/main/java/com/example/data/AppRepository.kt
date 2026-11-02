package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {

    val userProfile: Flow<UserProfileEntity?> = appDao.getUserProfile()
    val transactions: Flow<List<TransactionEntity>> = appDao.getAllTransactions()
    val categoryProgressList: Flow<List<CategoryProgressEntity>> = appDao.getAllProgress()
    val bookmarkedCapsules: Flow<List<BookmarkedCapsuleEntity>> = appDao.getBookmarkedCapsules()
    val leaderboardEntries: Flow<List<LeaderboardEntity>> = appDao.getLeaderboardUsers()
    val activityDays: Flow<List<ActivityDayEntity>> = appDao.getActivityDays()

    /** Records a completed quiz for [epochDay] and marks [frozenDays] as protected by a freeze. */
    suspend fun recordActivity(epochDay: Long, frozenDays: List<Long>) {
        val existing = appDao.getActivityDay(epochDay)
        val today = ActivityDayEntity(
            epochDay = epochDay,
            quizzesCompleted = (existing?.quizzesCompleted ?: 0) + 1,
            isFrozen = false
        )
        appDao.upsertActivityDays(frozenDays.map { ActivityDayEntity(epochDay = it, isFrozen = true) } + today)
    }

    suspend fun recordCategoryResult(categoryId: String, scorePercent: Int) {
        val existing = appDao.getProgress(categoryId)
        appDao.saveProgress(
            CategoryProgressEntity(
                categoryId = categoryId,
                isUnlocked = true,
                quizzesCompleted = (existing?.quizzesCompleted ?: 0) + 1,
                bestScorePercent = maxOf(existing?.bestScorePercent ?: 0, scorePercent)
            )
        )
    }

    suspend fun seedLeaderboardIfEmpty() {
        if (appDao.getLeaderboardCount() == 0) {
            val defaultUsers = listOf(
                LeaderboardEntity(id = "l1", name = "Priyank V.", initials = "PV", city = "Surat", totalScore = 4820, delta = "+1"),
                LeaderboardEntity(id = "l2", name = "Kaushal M.", initials = "KM", city = "Ahmedabad", totalScore = 4790, delta = "-1"),
                LeaderboardEntity(id = "l3", name = "Dhruvi P.", initials = "DP", city = "Rajkot", totalScore = 4715, delta = "+3"),
                LeaderboardEntity(id = "l4", name = "Harsh S.", initials = "HS", city = "Vadodara", totalScore = 4602, delta = "+2"),
                LeaderboardEntity(id = "l5", name = "Mira J.", initials = "MJ", city = "Bhavnagar", totalScore = 4588, delta = "0"),
                LeaderboardEntity(id = "l6", name = "Yash T.", initials = "YT", city = "Gandhinagar", totalScore = 4570, delta = "+4"),
                LeaderboardEntity(id = "l7", name = "Siddharth R.", initials = "SR", city = "Jamnagar", totalScore = 4410, delta = "+1"),
                LeaderboardEntity(id = "l8", name = "Pooja K.", initials = "PK", city = "Junagadh", totalScore = 4320, delta = "-2"),
                LeaderboardEntity(id = "l9", name = "Anand B.", initials = "AB", city = "Anand", totalScore = 4250, delta = "0"),
                LeaderboardEntity(id = "l10", name = "Dhaval M.", initials = "DM", city = "Mehsana", totalScore = 4180, delta = "+5"),
                LeaderboardEntity(id = "l_user", name = "You", initials = "AS", city = "Ahmedabad", totalScore = 0, delta = "0", isCurrentUser = true)
            )
            appDao.insertLeaderboardUsers(defaultUsers)
        }
    }

    suspend fun updateUserQuizScore(pointsEarned: Int, userName: String, userCity: String, userInitials: String) {
        val existing = appDao.getCurrentUserLeaderboardEntity()
        val currentScore = existing?.totalScore ?: 0
        val newScore = currentScore + pointsEarned
        val updatedEntity = LeaderboardEntity(
            id = "l_user",
            name = displayName(userName),
            initials = userInitials.ifEmpty { "AS" },
            city = userCity.ifEmpty { "Ahmedabad" },
            totalScore = newScore,
            delta = if (pointsEarned > 0) "+$pointsEarned" else existing?.delta ?: "0",
            isCurrentUser = true
        )
        appDao.insertOrUpdateLeaderboardUser(updatedEntity)
    }

    /** Keeps the leaderboard row in sync after the user edits their name or city. */
    suspend fun syncLeaderboardIdentity(userName: String, userCity: String, userInitials: String) {
        updateUserQuizScore(0, userName, userCity, userInitials)
    }

    private fun displayName(userName: String): String =
        if (userName.contains("(You)")) userName else "$userName (You)"

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        appDao.insertOrUpdateProfile(profile)
    }

    suspend fun updateCoins(coins: Int) {
        appDao.updateCoins(coins)
    }

    suspend fun updateStreak(streak: Int) {
        appDao.updateStreak(streak)
    }

    suspend fun addTransaction(transaction: TransactionEntity) {
        appDao.insertTransaction(transaction)
    }

    suspend fun updateCategoryProgress(progress: CategoryProgressEntity) {
        appDao.saveProgress(progress)
    }

    suspend fun toggleBookmark(capsuleId: String, isBookmarked: Boolean) {
        if (isBookmarked) {
            appDao.bookmarkCapsule(BookmarkedCapsuleEntity(capsuleId = capsuleId))
        } else {
            appDao.removeBookmark(capsuleId)
        }
    }
}
