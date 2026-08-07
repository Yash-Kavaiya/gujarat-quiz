package com.example.data

enum class AppThemeName(val displayName: String) {
    SAFFRON("Saffron"),
    INDIGO("Indigo"),
    MIDNIGHT("Midnight")
}

data class QuizCategory(
    val id: String,
    val titleEn: String,
    val titleGuj: String,
    val questionCount: Int,
    val difficulty: String, // Easy, Medium, Hard
    val coinCost: Int,
    val initials: String,
    val tag: String? = null, // HOT, FLAGSHIP, TRENDING, NEW
    val isRecommended: Boolean = false,
    val categoryGroup: String = "Exam"
)

data class Question(
    val id: String,
    val categoryId: String,
    val topicTag: String,
    val questionEn: String,
    val questionGuj: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
) {
    val textEn: String get() = questionEn
    val textGu: String get() = questionGuj
    val optionA: String get() = options.getOrElse(0) { "" }
    val optionB: String get() = options.getOrElse(1) { "" }
    val optionC: String get() = options.getOrElse(2) { "" }
    val optionD: String get() = options.getOrElse(3) { "" }
    val explanationEn: String get() = explanation
    val explanationGu: String get() = explanation
}

data class Capsule(
    val id: String,
    val topic: String,
    val readTime: String,
    val titleEn: String,
    val titleGuj: String,
    val tldr: String,
    val keyFacts: List<String>,
    val relatedCategoryId: String,
    val questionCount: Int = 15
) {
    val categoryTitleEn: String get() = topic
    val contentEn: String get() = tldr
    val contentGu: String get() = tldr
}

typealias LeaderboardEntry = LeaderboardUser

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val initials: String,
    val city: String,
    val score: Int,
    val delta: String, // e.g., "+2", "-1", "↑ 38"
    val isCurrentUser: Boolean = false
) {
    val avatar: String get() = initials
    val district: String get() = city
    val isUser: Boolean get() = isCurrentUser
}

data class CoinPack(
    val id: String,
    val coins: Int,
    val bonusTag: String? = null,
    val priceInr: Int,
    val approxUsd: String,
    val isPopular: Boolean = false,
    val isBestValue: Boolean = false
) {
    val badge: String? get() = bonusTag
}

data class PowerUp(
    val id: String,
    val title: String,
    val description: String,
    val coinPrice: Int,
    val iconName: String
)

data class DuelFriend(
    val id: String,
    val name: String,
    val initials: String,
    val city: String,
    val status: String, // "Online now", "Last seen 2h ago"
    val isOnline: Boolean
) {
    val avatar: String get() = initials
    val district: String get() = city
    val winRate: String get() = "78%"
}

data class MockQuestion(
    val id: Int,
    val section: String, // General Studies, Aptitude, Gujarati
    val topic: String,
    val questionEn: String,
    val questionGuj: String,
    val options: List<String>,
    val correctIndex: Int,
    val positiveMarks: Float = 2.0f,
    val negativeMarks: Float = 0.66f
) {
    val textEn: String get() = questionEn
    val textGu: String get() = questionGuj
    val optionA: String get() = options.getOrElse(0) { "" }
    val optionB: String get() = options.getOrElse(1) { "" }
    val optionC: String get() = options.getOrElse(2) { "" }
    val optionD: String get() = options.getOrElse(3) { "" }
}

data class StreakCalendarDay(
    val dayNumber: Int,
    val isCompleted: Boolean,
    val isFrozen: Boolean
)


enum class QuestionStatus {
    NOT_VISITED,
    UNANSWERED,
    ANSWERED,
    MARKED,
    MARKED_AND_ANSWERED
}
