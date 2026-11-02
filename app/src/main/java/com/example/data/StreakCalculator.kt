package com.example.data

import java.util.Calendar
import java.util.TimeZone

/** Day-based helpers. Epoch days are counted in the device's local time zone. */
object DayClock {
    private const val MILLIS_PER_DAY = 24L * 60 * 60 * 1000

    fun epochDay(millis: Long = System.currentTimeMillis(), tz: TimeZone = TimeZone.getDefault()): Long =
        Math.floorDiv(millis + tz.getOffset(millis), MILLIS_PER_DAY)

    /** Calendar positioned at local midnight of [epochDay]. */
    fun calendarFor(epochDay: Long, tz: TimeZone = TimeZone.getDefault()): Calendar {
        val cal = Calendar.getInstance(tz)
        cal.timeInMillis = epochDay * MILLIS_PER_DAY
        // Shift from UTC midnight to local midnight of the same date.
        cal.add(Calendar.MILLISECOND, -tz.getOffset(cal.timeInMillis))
        return cal
    }

    /** Human readable "x ago" label for a past timestamp. */
    fun timeAgo(timestamp: Long, now: Long = System.currentTimeMillis()): String {
        val seconds = ((now - timestamp) / 1000).coerceAtLeast(0)
        return when {
            seconds < 60 -> "Just now"
            seconds < 3600 -> "${seconds / 60}m ago"
            seconds < 86_400 -> "${seconds / 3600}h ago"
            seconds < 7 * 86_400 -> "${seconds / 86_400}d ago"
            else -> "${seconds / (7 * 86_400)}w ago"
        }
    }
}

data class StreakUpdate(
    val streak: Int,
    val freezesRemaining: Int,
    /** Missed days covered by a streak freeze. */
    val frozenDays: List<Long>,
    val isFirstActivityToday: Boolean,
    val streakWasReset: Boolean
)

/**
 * Daily streak rules:
 * - The first completed activity of a day extends the streak by one.
 * - Missed days are covered by streak freezes (one freeze per missed day) if enough are owned.
 * - Otherwise the streak restarts at 1.
 */
object StreakCalculator {

    fun onActivity(today: Long, lastActiveDay: Long, currentStreak: Int, freezesOwned: Int): StreakUpdate {
        if (lastActiveDay == today) {
            return StreakUpdate(currentStreak.coerceAtLeast(1), freezesOwned, emptyList(), false, false)
        }
        if (lastActiveDay < 0 || today - lastActiveDay == 1L) {
            return StreakUpdate(currentStreak + 1, freezesOwned, emptyList(), true, false)
        }
        if (lastActiveDay > today) {
            // Clock moved backwards; don't punish the user.
            return StreakUpdate(currentStreak.coerceAtLeast(1), freezesOwned, emptyList(), true, false)
        }
        val missed = (today - lastActiveDay - 1).toInt()
        return if (currentStreak > 0 && missed <= freezesOwned) {
            StreakUpdate(
                streak = currentStreak + 1,
                freezesRemaining = freezesOwned - missed,
                frozenDays = ((lastActiveDay + 1) until today).toList(),
                isFirstActivityToday = true,
                streakWasReset = false
            )
        } else {
            StreakUpdate(1, freezesOwned, emptyList(), true, currentStreak > 0)
        }
    }

    /** Streak value to display right now (0 if it has lapsed beyond the available freezes). */
    fun effectiveStreak(today: Long, lastActiveDay: Long, currentStreak: Int, freezesOwned: Int): Int {
        if (lastActiveDay < 0 || lastActiveDay >= today - 1) return currentStreak
        val missed = today - lastActiveDay - 1
        return if (missed <= freezesOwned) currentStreak else 0
    }
}

data class StreakMilestone(val days: Int, val rewardCoins: Int)

object Milestones {
    val all = listOf(
        StreakMilestone(3, 50),
        StreakMilestone(7, 150),
        StreakMilestone(14, 300),
        StreakMilestone(30, 750),
        StreakMilestone(100, 3000)
    )

    fun parseClaimed(raw: String): Set<Int> =
        raw.split(',').mapNotNull { it.trim().toIntOrNull() }.toSet()

    fun encodeClaimed(days: Set<Int>): String = days.sorted().joinToString(",")
}

/** Mock-test scoring, kept pure so it can be unit tested. */
object MockScoring {
    data class Result(val score: Float, val correct: Int, val wrong: Int, val unattempted: Int, val maxScore: Float)

    fun score(questions: List<MockQuestion>, answers: Map<Int, Int>): Result {
        var correct = 0
        var wrong = 0
        var score = 0f
        questions.forEachIndexed { index, q ->
            val ans = answers[index] ?: return@forEachIndexed
            if (ans == q.correctIndex) {
                correct++
                score += q.positiveMarks
            } else {
                wrong++
                score -= q.negativeMarks
            }
        }
        return Result(
            score = score,
            correct = correct,
            wrong = wrong,
            unattempted = questions.size - correct - wrong,
            maxScore = questions.sumOf { it.positiveMarks.toDouble() }.toFloat()
        )
    }
}
