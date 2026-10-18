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

    @Query("UPDATE user_profile SET coins = :coins WHERE id = 1")
    suspend fun updateCoins(coins: Int)

    @Query("UPDATE user_profile SET streakDays = :streak WHERE id = 1")
    suspend fun updateStreak(streak: Int)

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

    @Query("SELECT * FROM leaderboard_users ORDER BY totalScore DESC")
    fun getLeaderboardUsers(): Flow<List<LeaderboardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardUsers(users: List<LeaderboardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLeaderboardUser(user: LeaderboardEntity)

    @Query("SELECT COUNT(*) FROM leaderboard_users")
    suspend fun getLeaderboardCount(): Int

    @Query("SELECT * FROM leaderboard_users WHERE isCurrentUser = 1 LIMIT 1")
    suspend fun getCurrentUserLeaderboardEntity(): LeaderboardEntity?
}
