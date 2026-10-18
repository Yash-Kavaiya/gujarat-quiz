package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val COIN_BALANCE = intPreferencesKey("coin_balance")
        val STREAK_COUNT = intPreferencesKey("streak_count")
    }

    val coinBalanceFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.COIN_BALANCE] ?: 7900
    }

    val streakCountFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.STREAK_COUNT] ?: 20
    }

    suspend fun saveCoinBalance(coins: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.COIN_BALANCE] = coins
        }
    }

    suspend fun saveStreakCount(streak: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.STREAK_COUNT] = streak
        }
    }

    suspend fun saveCoinsAndStreak(coins: Int, streak: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.COIN_BALANCE] = coins
            preferences[PreferencesKeys.STREAK_COUNT] = streak
        }
    }
}
