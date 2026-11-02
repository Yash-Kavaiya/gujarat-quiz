package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        UserProfileEntity::class,
        TransactionEntity::class,
        CategoryProgressEntity::class,
        BookmarkedCapsuleEntity::class,
        LeaderboardEntity::class,
        ActivityDayEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /** v2 -> v3: streak tracking, milestones, hint inventory and activity calendar. */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE user_profile ADD COLUMN lastActiveEpochDay INTEGER NOT NULL DEFAULT -1")
                db.execSQL("ALTER TABLE user_profile ADD COLUMN claimedMilestones TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE user_profile ADD COLUMN hintsOwned INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE user_profile ADD COLUMN lastDailyChallengeEpochDay INTEGER NOT NULL DEFAULT -1")
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `activity_days` (" +
                        "`epochDay` INTEGER NOT NULL, " +
                        "`quizzesCompleted` INTEGER NOT NULL, " +
                        "`isFrozen` INTEGER NOT NULL, " +
                        "PRIMARY KEY(`epochDay`))"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gujarat_quiz_database"
                )
                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
