package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM category_progress")
    fun getAllProgress(): Flow<List<CategoryProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: CategoryProgressEntity)

    @Query("SELECT * FROM unlocked_capsules")
    fun getBookmarkedCapsules(): Flow<List<BookmarkedCapsuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkCapsule(bookmark: BookmarkedCapsuleEntity)

    @Query("DELETE FROM unlocked_capsules WHERE capsuleId = :capsuleId")
    suspend fun removeBookmark(capsuleId: String)
}
