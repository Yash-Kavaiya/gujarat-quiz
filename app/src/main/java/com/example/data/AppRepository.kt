package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {

    val userProfile: Flow<UserProfileEntity?> = appDao.getUserProfile()
    val transactions: Flow<List<TransactionEntity>> = appDao.getAllTransactions()
    val categoryProgressList: Flow<List<CategoryProgressEntity>> = appDao.getAllProgress()
    val bookmarkedCapsules: Flow<List<BookmarkedCapsuleEntity>> = appDao.getBookmarkedCapsules()

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        appDao.insertOrUpdateProfile(profile)
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
