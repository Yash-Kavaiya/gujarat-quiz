package com.example

import com.example.data.QuestionBank
import com.example.data.SeedData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionBankTest {

  @Test
  fun `every question is well formed`() {
    val ids = QuestionBank.all.map { it.id }
    assertEquals("question ids must be unique", ids.size, ids.toSet().size)
    QuestionBank.all.forEach { q ->
      assertEquals("${q.id} must have 4 options", 4, q.options.size)
      assertEquals("${q.id} options must be distinct", 4, q.options.toSet().size)
      assertTrue("${q.id} correct index out of range", q.correctIndex in q.options.indices)
      assertTrue("${q.id} needs Gujarati text", q.questionGuj.isNotBlank())
      assertTrue("${q.id} needs an explanation", q.explanation.isNotBlank())
    }
  }

  @Test
  fun `every category has a playable pool`() {
    SeedData.categories.forEach { category ->
      assertTrue("${category.id} has too few questions", QuestionBank.poolFor(category.id).size >= 5)
      assertTrue(QuestionBank.quizFor(category.id).size <= 10)
    }
  }

  @Test
  fun `capsules link to real categories`() {
    val ids = SeedData.categories.map { it.id }.toSet()
    SeedData.sampleCapsules.forEach { assertTrue(it.relatedCategoryId in ids) }
  }

  @Test
  fun `daily challenge is stable within a day and changes across days`() {
    val today = QuestionBank.dailyChallenge(20_000).map { it.id }
    assertEquals(today, QuestionBank.dailyChallenge(20_000).map { it.id })
    assertTrue(today != QuestionBank.dailyChallenge(20_001).map { it.id })
  }

  @Test
  fun `mock paper uses the whole bank grouped by section`() {
    val paper = QuestionBank.mockPaper()
    assertEquals(QuestionBank.all.size, paper.size)
    val sectionOrder = paper.map { it.section }.distinct()
    assertEquals(QuestionBank.mockSections.filter { it in sectionOrder }, sectionOrder)
  }
}
