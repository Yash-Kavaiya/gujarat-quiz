package com.example

import com.example.data.DayClock
import com.example.data.Milestones
import com.example.data.MockQuestion
import com.example.data.MockScoring
import com.example.data.StreakCalculator
import com.example.viewmodel.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StreakAndScoringTest {

  @Test
  fun `consecutive day extends streak`() {
    val u = StreakCalculator.onActivity(today = 10, lastActiveDay = 9, currentStreak = 5, freezesOwned = 0)
    assertEquals(6, u.streak)
    assertTrue(u.isFirstActivityToday)
  }

  @Test
  fun `second activity on the same day does not change streak`() {
    val u = StreakCalculator.onActivity(10, 10, 6, 0)
    assertEquals(6, u.streak)
    assertFalse(u.isFirstActivityToday)
  }

  @Test
  fun `missed day is covered by a freeze`() {
    val u = StreakCalculator.onActivity(10, 8, 5, 1)
    assertEquals(6, u.streak)
    assertEquals(0, u.freezesRemaining)
    assertEquals(listOf(9L), u.frozenDays)
  }

  @Test
  fun `streak resets when not enough freezes`() {
    val u = StreakCalculator.onActivity(10, 7, 5, 1)
    assertEquals(1, u.streak)
    assertTrue(u.streakWasReset)
    assertEquals(1, u.freezesRemaining)
  }

  @Test
  fun `effective streak drops to zero once lapsed`() {
    assertEquals(5, StreakCalculator.effectiveStreak(10, 9, 5, 0))
    assertEquals(5, StreakCalculator.effectiveStreak(10, 8, 5, 1))
    assertEquals(0, StreakCalculator.effectiveStreak(10, 7, 5, 1))
  }

  @Test
  fun `milestones round trip`() {
    assertEquals(setOf(3, 7), Milestones.parseClaimed(Milestones.encodeClaimed(setOf(7, 3))))
    assertEquals(emptySet<Int>(), Milestones.parseClaimed(""))
  }

  @Test
  fun `mock scoring applies negative marking`() {
    val qs = (0 until 3).map {
      MockQuestion(id = it, section = "GS", topic = "T", questionEn = "q", questionGuj = "q",
        options = listOf("a", "b", "c", "d"), correctIndex = 0)
    }
    val r = MockScoring.score(qs, mapOf(0 to 0, 1 to 2))
    assertEquals(1, r.correct)
    assertEquals(1, r.wrong)
    assertEquals(1, r.unattempted)
    assertEquals(2f - 0.66f, r.score, 0.0001f)
    assertEquals(6f, r.maxScore, 0.0001f)
  }

  @Test
  fun `time ago labels`() {
    val now = 1_000_000_000L
    assertEquals("Just now", DayClock.timeAgo(now - 10_000, now))
    assertEquals("5m ago", DayClock.timeAgo(now - 5 * 60_000, now))
    assertEquals("2h ago", DayClock.timeAgo(now - 2 * 3_600_000, now))
    assertEquals("3d ago", DayClock.timeAgo(now - 3 * 86_400_000L, now))
  }

  @Test
  fun `initials from names`() {
    assertEquals("YK", MainViewModel.initialsFor("Yash Kavaiya"))
    assertEquals("PR", MainViewModel.initialsFor("priya"))
    assertEquals("AS", MainViewModel.initialsFor("   "))
  }
}
