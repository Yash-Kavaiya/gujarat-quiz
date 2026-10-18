package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {

    val userProfile: Flow<UserProfileEntity?> = appDao.getUserProfile()
    val transactions: Flow<List<TransactionEntity>> = appDao.getAllTransactions()
    val categoryProgressList: Flow<List<CategoryProgressEntity>> = appDao.getAllProgress()
    val bookmarkedCapsules: Flow<List<BookmarkedCapsuleEntity>> = appDao.getBookmarkedCapsules()
    val leaderboardEntries: Flow<List<LeaderboardEntity>> = appDao.getLeaderboardUsers()

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
                LeaderboardEntity(id = "l_user", name = "Aakash P. (You)", initials = "AP", city = "Ahmedabad", totalScore = 4012, delta = "↑ 38", isCurrentUser = true)
            )
            appDao.insertLeaderboardUsers(defaultUsers)
        }
    }

    suspend fun updateUserQuizScore(pointsEarned: Int, userName: String, userCity: String, userInitials: String) {
        val existing = appDao.getCurrentUserLeaderboardEntity()
        val currentScore = existing?.totalScore ?: 4012
        val newScore = currentScore + pointsEarned
        val updatedEntity = LeaderboardEntity(
            id = "l_user",
            name = if (userName.contains("(You)")) userName else "$userName (You)",
            initials = userInitials.ifEmpty { "AP" },
            city = userCity.ifEmpty { "Ahmedabad" },
            totalScore = newScore,
            delta = "+$pointsEarned",
            isCurrentUser = true
        )
        appDao.insertOrUpdateLeaderboardUser(updatedEntity)
    }

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
