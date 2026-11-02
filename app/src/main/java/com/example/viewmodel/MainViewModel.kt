package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random

enum class Screen {
    ONBOARDING,
    HOME,
    CATEGORIES,
    COMPETE,
    PROFILE,
    QUIZ_PLAY,
    RESULTS,
    MOCK_TEST,
    COIN_SHOP,
    STREAK_CALENDAR,
    CAPSULES,
    DUEL_CHALLENGE
}

enum class DuelPhase {
    INVITE,
    WAITING,
    LIVE_MATCH
}

/** Which flow produced the current [QuizState]; drives the results screen and "retry". */
enum class QuizMode {
    PRACTICE,
    DAILY_CHALLENGE,
    MOCK_TEST,
    DUEL
}

const val QUIZ_SECONDS_PER_QUESTION = 30
const val DUEL_SECONDS_PER_QUESTION = 20
const val MOCK_TEST_SECONDS = 7200
const val HINT_COIN_COST = 50
const val COINS_PER_CORRECT = 30
const val HIGH_SCORE_BONUS = 200
const val DAILY_CHALLENGE_ENTRY = 50
const val DAILY_CHALLENGE_TOP_BONUS = 500
const val MOCK_TEST_ENTRY = 100
const val MOCK_TEST_HIGH_SCORE_REWARD = 1000
const val DUEL_ENTRY = 100
const val DUEL_PRIZE = 200

data class QuizState(
    val mode: QuizMode = QuizMode.PRACTICE,
    val category: QuizCategory? = null,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val userAnswers: Map<Int, Int> = emptyMap(),
    val isRevealed: Boolean = false,
    val selectedOption: Int? = null,
    val timerSeconds: Int = QUIZ_SECONDS_PER_QUESTION,
    val timerTotalSeconds: Int = QUIZ_SECONDS_PER_QUESTION,
    val isTimerActive: Boolean = false,
    val timedOut: Boolean = false,
    val eliminatedOptions: Set<Int> = emptySet(),
    /** Coins earned from correct answers during the quiz (before bonuses). */
    val coinsEarnedInQuiz: Int = 0,
    /** Total coins credited to the wallet when the session finished (incl. bonuses / prizes). */
    val coinsAwarded: Int = 0,
    val showCoinPopAnimation: Boolean = false,
    val quizCompleted: Boolean = false,
    val isWinState: Boolean = false,
    val isTie: Boolean = false,
    val scorePercentage: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val unattemptedCount: Int = 0,
    val totalCount: Int = 0,
    /** e.g. "142.6 / 200" for mock tests or "50 – 30" for duels. */
    val scoreLabel: String? = null,
    val pointsGained: Int = 0
)

data class MockTestState(
    val questions: List<MockQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val activeSection: String = "General Studies",
    val userAnswers: Map<Int, Int> = emptyMap(),
    val markedForReview: Set<Int> = emptySet(),
    val visitedQuestions: Set<Int> = emptySet(),
    val timeRemainingSeconds: Int = MOCK_TEST_SECONDS,
    val isPaletteOpen: Boolean = false,
    val isSubmitModalOpen: Boolean = false,
    val isSubmitted: Boolean = false,
    val totalScore: Float = 0f,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val unattemptedCount: Int = 0
)

data class DuelState(
    val friend: DuelFriend? = null,
    val phase: DuelPhase = DuelPhase.INVITE,
    val currentQuestionIndex: Int = 0,
    val questions: List<Question> = emptyList(),
    val userScore: Int = 0,
    val opponentScore: Int = 0,
    /** Selected option, or -1 if the round timed out without an answer. */
    val userChoice: Int? = null,
    val opponentChoice: Int? = null,
    val timerSeconds: Int = DUEL_SECONDS_PER_QUESTION,
    val isRevealed: Boolean = false,
    val winnerName: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository
    private val preferencesRepository: UserPreferencesRepository

    private val _userProfile = MutableStateFlow(UserProfileEntity())
    val userProfile: StateFlow<UserProfileEntity> = _userProfile.asStateFlow()

    private val _profileLoaded = MutableStateFlow(false)
    val profileLoaded: StateFlow<Boolean> = _profileLoaded.asStateFlow()

    private val _activeTheme = MutableStateFlow("Saffron")
    val activeTheme: StateFlow<String> = _activeTheme.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _currentBottomTab = MutableStateFlow(0) // 0: Home, 1: Categories, 2: Compete, 3: Profile
    val currentBottomTab: StateFlow<Int> = _currentBottomTab.asStateFlow()

    private val _onboardingStep = MutableStateFlow(1)
    val onboardingStep: StateFlow<Int> = _onboardingStep.asStateFlow()

    private val _showReferralSheet = MutableStateFlow(false)
    val showReferralSheet: StateFlow<Boolean> = _showReferralSheet.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    private val _mockTestState = MutableStateFlow(MockTestState())
    val mockTestState: StateFlow<MockTestState> = _mockTestState.asStateFlow()

    private val _duelState = MutableStateFlow(DuelState())
    val duelState: StateFlow<DuelState> = _duelState.asStateFlow()

    private var quizTimerJob: Job? = null
    private var mockTimerJob: Job? = null
    private var duelTimerJob: Job? = null
    private var duelOpponentJob: Job? = null
    private var coinPopJob: Job? = null

    private val profileWriteMutex = Mutex()
    private var pendingProfileWrites = 0

    init {
        val dao = AppDatabase.getDatabase(application).appDao()
        repository = AppRepository(dao)
        preferencesRepository = UserPreferencesRepository(application)

        viewModelScope.launch {
            repository.seedLeaderboardIfEmpty()
        }

        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                if (profile != null) {
                    // Ignore echoes of our own in-flight writes; the in-memory value is newer.
                    if (pendingProfileWrites == 0) _userProfile.value = profile
                    _activeTheme.value = _userProfile.value.theme
                    if (!_profileLoaded.value) {
                        if (!profile.onboardingCompleted) {
                            _currentScreen.value = Screen.ONBOARDING
                        }
                        _profileLoaded.value = true
                    }
                    preferencesRepository.saveCoinsAndStreak(profile.coins, profile.streakDays)
                } else {
                    // First launch: seed a fresh profile and the signup bonus.
                    val initialProfile = UserProfileEntity(referralCode = generateReferralCode("AS"))
                    repository.saveUserProfile(initialProfile)
                    repository.addTransaction(TransactionEntity(title = "Signup bonus", timeAgo = "", amount = initialProfile.coins))
                }
            }
        }
    }

    val transactions: StateFlow<List<TransactionEntity>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val leaderboardUsers: StateFlow<List<LeaderboardUser>> = repository.leaderboardEntries
        .map { list ->
            list.sortedByDescending { it.totalScore }.mapIndexed { idx, item ->
                LeaderboardUser(
                    rank = idx + 1,
                    name = item.name,
                    initials = item.initials,
                    city = item.city,
                    score = item.totalScore,
                    delta = item.delta,
                    isCurrentUser = item.isCurrentUser
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** The user's live rank on the leaderboard (0 while loading). */
    val userRank: StateFlow<Int> = leaderboardUsers
        .map { list -> list.firstOrNull { it.isCurrentUser }?.rank ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val activityDays: StateFlow<List<ActivityDayEntity>> = repository.activityDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoryProgress: StateFlow<Map<String, CategoryProgressEntity>> = repository.categoryProgressList
        .map { list -> list.associateBy { it.categoryId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val bookmarkedCapsuleIds: StateFlow<Set<String>> = repository.bookmarkedCapsules
        .map { list -> list.filter { it.isBookmarked }.map { it.capsuleId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    /** Streak to display, accounting for days missed since the last activity. */
    val displayStreak: StateFlow<Int> = _userProfile
        .map { p -> StreakCalculator.effectiveStreak(today(), p.lastActiveEpochDay, p.streakDays, p.freezesOwned) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private fun today(): Long = DayClock.epochDay()

    /**
     * Applies [transform] to the in-memory profile and persists it. Writes are serialised and
     * always store the latest in-memory value, so rapid successive updates can't be lost.
     */
    private fun updateProfile(transform: (UserProfileEntity) -> UserProfileEntity): UserProfileEntity {
        val updated = transform(_userProfile.value)
        _userProfile.value = updated
        pendingProfileWrites++
        viewModelScope.launch {
            try {
                profileWriteMutex.withLock { repository.saveUserProfile(_userProfile.value) }
            } finally {
                pendingProfileWrites--
            }
        }
        return updated
    }

    // ---------------------------------------------------------------- Navigation

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        syncBottomTab(screen)
    }

    fun setCurrentScreenFromNav(screen: Screen) {
        _currentScreen.value = screen
        syncBottomTab(screen)
    }

    private fun syncBottomTab(screen: Screen) {
        when (screen) {
            Screen.HOME -> _currentBottomTab.value = 0
            Screen.CATEGORIES -> _currentBottomTab.value = 1
            Screen.COMPETE -> _currentBottomTab.value = 2
            Screen.PROFILE -> _currentBottomTab.value = 3
            else -> Unit
        }
    }

    fun selectBottomTab(tabIndex: Int) {
        _currentBottomTab.value = tabIndex
        when (tabIndex) {
            0 -> _currentScreen.value = Screen.HOME
            1 -> _currentScreen.value = Screen.CATEGORIES
            2 -> _currentScreen.value = Screen.COMPETE
            3 -> _currentScreen.value = Screen.PROFILE
        }
    }

    fun setShowReferralSheet(show: Boolean) {
        _showReferralSheet.value = show
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    // ---------------------------------------------------------------- Onboarding & profile

    fun nextOnboardingStep() {
        if (_onboardingStep.value < 3) {
            _onboardingStep.value += 1
        }
    }

    fun setGoal(goal: String) {
        updateProfile { it.copy(goal = goal) }
    }

    fun updateNameAndCity(name: String, city: String) {
        val cleanName = name.trim().ifEmpty { return }
        val cleanCity = city.trim().ifEmpty { _userProfile.value.city }
        val initials = initialsFor(cleanName)
        val updated = updateProfile {
            it.copy(
                name = cleanName,
                city = cleanCity,
                initials = initials,
                referralCode = if (it.referralCode.startsWith(it.initials)) generateReferralCode(initials) else it.referralCode
            )
        }
        viewModelScope.launch {
            repository.syncLeaderboardIdentity(updated.name, updated.city, updated.initials)
        }
    }

    fun claimWelcomeGift() {
        addCoins(500, "Welcome gift")
        updateProfile { it.copy(onboardingCompleted = true) }
        _onboardingStep.value = 1
        navigateTo(Screen.HOME)
    }

    fun switchTheme(themeName: String) {
        _activeTheme.value = themeName
        updateProfile { it.copy(theme = themeName) }
        showToast("Theme switched to $themeName")
    }

    // ---------------------------------------------------------------- Coin economy

    fun deductCoins(amount: Int, reason: String): Boolean {
        if (_userProfile.value.coins < amount) {
            showToast("Not enough coins! You need $amount coins.")
            return false
        }
        updateProfile { it.copy(coins = it.coins - amount) }
        viewModelScope.launch {
            repository.addTransaction(TransactionEntity(title = reason, timeAgo = "", amount = -amount))
        }
        return true
    }

    fun addCoins(amount: Int, reason: String) {
        if (amount <= 0) return
        updateProfile { it.copy(coins = it.coins + amount) }
        viewModelScope.launch {
            repository.addTransaction(TransactionEntity(title = reason, timeAgo = "", amount = amount))
        }
    }

    // ---------------------------------------------------------------- Practice quiz & daily challenge

    fun startPracticeQuiz(category: QuizCategory) {
        val questions = QuestionBank.quizFor(category.id)
        if (questions.isEmpty()) {
            showToast("No questions available yet for ${category.titleEn}")
            return
        }
        if (!deductCoins(category.coinCost, "Practice · ${category.titleEn}")) return
        beginQuiz(QuizMode.PRACTICE, category, questions)
    }

    /** Starts a practice quiz for a capsule's related category. */
    fun startPracticeForCategoryId(categoryId: String) {
        val category = SeedData.categories.find { it.id == categoryId } ?: return
        startPracticeQuiz(category)
    }

    fun isDailyChallengePlayedToday(): Boolean = _userProfile.value.lastDailyChallengeEpochDay == today()

    fun startDailyChallenge() {
        if (isDailyChallengePlayedToday()) {
            showToast("You've already played today's challenge. Come back tomorrow!")
            return
        }
        if (!deductCoins(DAILY_CHALLENGE_ENTRY, "Daily Challenge entry")) return
        updateProfile { it.copy(lastDailyChallengeEpochDay = today()) }
        val category = QuizCategory(
            id = "daily_challenge",
            titleEn = "Daily Challenge",
            titleGuj = "દૈનિક ચેલેન્જ",
            questionCount = 10,
            difficulty = "Mixed",
            coinCost = DAILY_CHALLENGE_ENTRY,
            initials = "DC"
        )
        beginQuiz(QuizMode.DAILY_CHALLENGE, category, QuestionBank.dailyChallenge(today()))
    }

    private fun beginQuiz(mode: QuizMode, category: QuizCategory, questions: List<Question>) {
        _quizState.value = QuizState(
            mode = mode,
            category = category,
            questions = questions,
            currentIndex = 0,
            timerSeconds = QUIZ_SECONDS_PER_QUESTION,
            isTimerActive = true
        )
        startQuizTimer()
        navigateTo(Screen.QUIZ_PLAY)
    }

    private fun startQuizTimer() {
        quizTimerJob?.cancel()
        quizTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val s = _quizState.value
                if (!s.isTimerActive || s.isRevealed || s.quizCompleted) break
                val remaining = s.timerSeconds - 1
                if (remaining <= 0) {
                    onQuizTimeUp()
                    break
                }
                _quizState.value = s.copy(timerSeconds = remaining)
            }
        }
    }

    private fun onQuizTimeUp() {
        val s = _quizState.value
        if (s.isRevealed) return
        _quizState.value = s.copy(
            timerSeconds = 0,
            isRevealed = true,
            isTimerActive = false,
            selectedOption = null,
            timedOut = true
        )
    }

    fun selectQuizOption(optionIndex: Int) {
        val current = _quizState.value
        if (current.isRevealed || current.questions.isEmpty()) return
        quizTimerJob?.cancel()

        val q = current.questions[current.currentIndex]
        val isCorrect = optionIndex == q.correctIndex

        _quizState.value = current.copy(
            userAnswers = current.userAnswers + (current.currentIndex to optionIndex),
            isRevealed = true,
            selectedOption = optionIndex,
            isTimerActive = false,
            timedOut = false,
            coinsEarnedInQuiz = current.coinsEarnedInQuiz + if (isCorrect) COINS_PER_CORRECT else 0,
            showCoinPopAnimation = isCorrect
        )

        if (isCorrect) {
            coinPopJob?.cancel()
            coinPopJob = viewModelScope.launch {
                delay(1200)
                _quizState.value = _quizState.value.copy(showCoinPopAnimation = false)
            }
        }
    }

    fun useQuizHint() {
        val current = _quizState.value
        if (current.isRevealed || current.questions.isEmpty()) return
        if (current.eliminatedOptions.isNotEmpty()) {
            showToast("Hint already used on this question")
            return
        }

        if (_userProfile.value.hintsOwned > 0) {
            updateProfile { it.copy(hintsOwned = it.hintsOwned - 1) }
        } else if (!deductCoins(HINT_COIN_COST, "Quiz Hint")) {
            return
        }

        val q = current.questions[current.currentIndex]
        val wrongIndices = q.options.indices.filter { it != q.correctIndex }
        val elim = wrongIndices.shuffled().take(2).toSet()

        _quizState.value = _quizState.value.copy(eliminatedOptions = elim)
        showToast("Eliminated 2 wrong options!")
    }

    fun nextQuizQuestion() {
        val current = _quizState.value
        if (current.currentIndex < current.questions.size - 1) {
            _quizState.value = current.copy(
                currentIndex = current.currentIndex + 1,
                isRevealed = false,
                selectedOption = null,
                eliminatedOptions = emptySet(),
                timerSeconds = QUIZ_SECONDS_PER_QUESTION,
                isTimerActive = true,
                timedOut = false,
                showCoinPopAnimation = false
            )
            startQuizTimer()
        } else {
            finishQuiz()
        }
    }

    /** Called when the user leaves a quiz before finishing it. Entry fee is not refunded. */
    fun abandonQuiz() {
        quizTimerJob?.cancel()
        coinPopJob?.cancel()
        _quizState.value = _quizState.value.copy(isTimerActive = false, showCoinPopAnimation = false)
    }

    private fun finishQuiz() {
        quizTimerJob?.cancel()
        val current = _quizState.value
        if (current.quizCompleted) return

        val total = current.questions.size
        val correct = current.questions.indices.count { current.userAnswers[it] == current.questions[it].correctIndex }
        val answered = current.userAnswers.size
        val scorePercent = if (total > 0) (correct * 100) / total else 0
        val isWin = scorePercent >= 80

        var awarded = current.coinsEarnedInQuiz
        if (isWin) awarded += HIGH_SCORE_BONUS
        if (current.mode == QuizMode.DAILY_CHALLENGE && correct == total && total > 0) awarded += DAILY_CHALLENGE_TOP_BONUS
        val rewardLabel = if (current.mode == QuizMode.DAILY_CHALLENGE) "Daily Challenge reward" else "Quiz reward"
        addCoins(awarded, "$rewardLabel (${scorePercent}%)")

        val pointsGained = correct * 25
        recordCompletedActivity(scorePercent)
        current.category?.let { cat ->
            if (current.mode == QuizMode.PRACTICE) {
                viewModelScope.launch { repository.recordCategoryResult(cat.id, scorePercent) }
            }
        }
        awardLeaderboardPoints(pointsGained)

        _quizState.value = current.copy(
            isTimerActive = false,
            showCoinPopAnimation = false,
            quizCompleted = true,
            isWinState = isWin,
            scorePercentage = scorePercent,
            correctCount = correct,
            wrongCount = answered - correct,
            unattemptedCount = total - answered,
            totalCount = total,
            coinsAwarded = awarded,
            pointsGained = pointsGained
        )
        navigateTo(Screen.RESULTS)
    }

    /** Updates quiz count, accuracy and the daily streak after any completed session. */
    private fun recordCompletedActivity(scorePercent: Int) {
        val todayDay = today()
        val before = _userProfile.value
        val streak = StreakCalculator.onActivity(todayDay, before.lastActiveEpochDay, before.streakDays, before.freezesOwned)
        val newQuizzes = before.totalQuizzes + 1
        val newAcc = ((before.accuracyPercentage * before.totalQuizzes) + scorePercent) / newQuizzes

        updateProfile {
            it.copy(
                totalQuizzes = newQuizzes,
                accuracyPercentage = newAcc,
                streakDays = streak.streak,
                longestStreak = maxOf(it.longestStreak, streak.streak),
                freezesOwned = streak.freezesRemaining,
                lastActiveEpochDay = todayDay
            )
        }
        viewModelScope.launch { repository.recordActivity(todayDay, streak.frozenDays) }

        when {
            streak.frozenDays.isNotEmpty() ->
                showToast("Streak saved with ${streak.frozenDays.size} freeze(s)! 🔥 ${streak.streak} days")
            streak.streakWasReset ->
                showToast("Streak restarted. Play daily to build it back up!")
            streak.isFirstActivityToday ->
                showToast("🔥 ${streak.streak}-day streak!")
        }
    }

    private fun awardLeaderboardPoints(points: Int) {
        if (points <= 0) return
        val profile = _userProfile.value
        viewModelScope.launch {
            repository.updateUserQuizScore(points, profile.name, profile.city, profile.initials)
        }
    }

    // ---------------------------------------------------------------- Mock test

    fun startMockTest() {
        if (!deductCoins(MOCK_TEST_ENTRY, "GPSC Mock Test Paper I")) return
        val mockQs = QuestionBank.mockPaper()
        _mockTestState.value = MockTestState(
            questions = mockQs,
            currentIndex = 0,
            activeSection = mockQs.firstOrNull()?.section ?: "General Studies",
            visitedQuestions = setOf(0),
            timeRemainingSeconds = MOCK_TEST_SECONDS
        )
        startMockTimer()
        navigateTo(Screen.MOCK_TEST)
    }

    private fun startMockTimer() {
        mockTimerJob?.cancel()
        mockTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val s = _mockTestState.value
                if (s.isSubmitted || s.questions.isEmpty()) break
                val remaining = s.timeRemainingSeconds - 1
                if (remaining <= 0) {
                    _mockTestState.value = s.copy(timeRemainingSeconds = 0)
                    showToast("Time's up! Your test was submitted automatically.")
                    submitMockTest()
                    break
                }
                _mockTestState.value = s.copy(timeRemainingSeconds = remaining)
            }
        }
    }

    fun selectMockOption(qIndex: Int, optionIndex: Int) {
        val current = _mockTestState.value
        if (current.isSubmitted) return
        val answers = if (current.userAnswers[qIndex] == optionIndex) {
            current.userAnswers - qIndex // tap again to clear the response
        } else {
            current.userAnswers + (qIndex to optionIndex)
        }
        _mockTestState.value = current.copy(userAnswers = answers)
    }

    fun clearMockResponse(qIndex: Int) {
        val current = _mockTestState.value
        _mockTestState.value = current.copy(userAnswers = current.userAnswers - qIndex)
    }

    fun toggleMockMarkForReview(qIndex: Int) {
        val current = _mockTestState.value
        val set = if (qIndex in current.markedForReview) current.markedForReview - qIndex else current.markedForReview + qIndex
        _mockTestState.value = current.copy(markedForReview = set)
    }

    fun goToMockQuestion(qIndex: Int) {
        val current = _mockTestState.value
        val question = current.questions.getOrNull(qIndex) ?: return
        _mockTestState.value = current.copy(
            currentIndex = qIndex,
            activeSection = question.section,
            visitedQuestions = current.visitedQuestions + qIndex,
            isPaletteOpen = false
        )
    }

    fun goToMockSection(section: String) {
        val index = _mockTestState.value.questions.indexOfFirst { it.section == section }
        if (index >= 0) goToMockQuestion(index)
    }

    fun setMockPaletteOpen(open: Boolean) {
        _mockTestState.value = _mockTestState.value.copy(isPaletteOpen = open)
    }

    fun setMockSubmitModalOpen(open: Boolean) {
        _mockTestState.value = _mockTestState.value.copy(isSubmitModalOpen = open)
    }

    /** Leaves the mock test without submitting. Entry fee is not refunded. */
    fun quitMockTest() {
        mockTimerJob?.cancel()
        _mockTestState.value = MockTestState()
    }

    fun submitMockTest() {
        mockTimerJob?.cancel()
        val current = _mockTestState.value
        if (current.isSubmitted || current.questions.isEmpty()) return

        val result = MockScoring.score(current.questions, current.userAnswers)

        _mockTestState.value = current.copy(
            isSubmitModalOpen = false,
            isPaletteOpen = false,
            isSubmitted = true,
            totalScore = result.score,
            correctCount = result.correct,
            wrongCount = result.wrong,
            unattemptedCount = result.unattempted
        )

        val percent = if (result.maxScore > 0) (result.score / result.maxScore * 100).toInt().coerceIn(0, 100) else 0
        val isHighScore = percent >= 60
        if (isHighScore) addCoins(MOCK_TEST_HIGH_SCORE_REWARD, "Mock Test High Score")

        val mockPoints = (result.score * 5).toInt().coerceAtLeast(0)
        recordCompletedActivity(percent)
        awardLeaderboardPoints(mockPoints)

        _quizState.value = QuizState(
            mode = QuizMode.MOCK_TEST,
            quizCompleted = true,
            isWinState = isHighScore,
            scorePercentage = percent,
            correctCount = result.correct,
            wrongCount = result.wrong,
            unattemptedCount = result.unattempted,
            totalCount = current.questions.size,
            coinsAwarded = if (isHighScore) MOCK_TEST_HIGH_SCORE_REWARD else 0,
            scoreLabel = "%.2f / %.0f".format(result.score, result.maxScore),
            pointsGained = mockPoints
        )
        navigateTo(Screen.RESULTS)
    }

    // ---------------------------------------------------------------- 1v1 duel

    /** Opens the duel invite screen for [friend]. The entry fee is charged when the invite is sent. */
    fun startDuelChallenge(friend: DuelFriend) {
        if (_userProfile.value.coins < DUEL_ENTRY) {
            showToast("Not enough coins! You need $DUEL_ENTRY coins.")
            return
        }
        cancelDuelJobs()
        _duelState.value = DuelState(
            friend = friend,
            phase = DuelPhase.INVITE,
            questions = QuestionBank.quizFor("full_mock_test", count = 5),
            currentQuestionIndex = 0
        )
        navigateTo(Screen.DUEL_CHALLENGE)
    }

    /** Opens the duel screen against a random online friend. */
    fun startQuickDuel() {
        val friend = SeedData.duelFriends.filter { it.isOnline }.randomOrNull() ?: SeedData.duelFriends.first()
        startDuelChallenge(friend)
    }

    fun advanceDuelToWaiting() {
        val current = _duelState.value
        if (current.phase != DuelPhase.INVITE) return
        if (!deductCoins(DUEL_ENTRY, "1v1 Duel entry · ${current.friend?.name ?: "Opponent"}")) return
        _duelState.value = current.copy(phase = DuelPhase.WAITING)

        viewModelScope.launch {
            delay(2500)
            if (_duelState.value.phase == DuelPhase.WAITING) {
                _duelState.value = _duelState.value.copy(phase = DuelPhase.LIVE_MATCH)
                startDuelRound()
            }
        }
    }

    private fun startDuelRound() {
        cancelDuelJobs()
        _duelState.value = _duelState.value.copy(timerSeconds = DUEL_SECONDS_PER_QUESTION)

        // Simulated opponent answers after a random "thinking" delay.
        val q = _duelState.value.questions.getOrNull(_duelState.value.currentQuestionIndex) ?: return
        duelOpponentJob = viewModelScope.launch {
            delay(Random.nextLong(2_000, 9_000))
            val oppCorrect = Random.nextFloat() < 0.65f
            val pick = if (oppCorrect) q.correctIndex else q.options.indices.filter { it != q.correctIndex }.random()
            val s = _duelState.value
            if (s.opponentChoice == null) {
                _duelState.value = s.copy(
                    opponentChoice = pick,
                    opponentScore = s.opponentScore + if (oppCorrect) 10 else 0
                )
            }
            maybeRevealDuelRound()
        }

        duelTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val s = _duelState.value
                if (s.isRevealed || s.phase != DuelPhase.LIVE_MATCH) break
                val remaining = s.timerSeconds - 1
                if (remaining <= 0) {
                    onDuelTimeUp()
                    break
                }
                _duelState.value = s.copy(timerSeconds = remaining)
            }
        }
    }

    private fun onDuelTimeUp() {
        duelOpponentJob?.cancel()
        val s = _duelState.value
        if (s.questions.getOrNull(s.currentQuestionIndex) == null) return
        var next = s.copy(timerSeconds = 0)
        if (next.userChoice == null) next = next.copy(userChoice = -1)
        if (next.opponentChoice == null) {
            // Opponent ran out of time too.
            next = next.copy(opponentChoice = -1)
        }
        _duelState.value = next.copy(isRevealed = true)
    }

    private fun maybeRevealDuelRound() {
        val s = _duelState.value
        if (s.userChoice != null && s.opponentChoice != null && !s.isRevealed) {
            duelTimerJob?.cancel()
            _duelState.value = s.copy(isRevealed = true)
        }
    }

    fun selectDuelOption(optionIndex: Int) {
        val current = _duelState.value
        if (current.userChoice != null || current.isRevealed) return
        val q = current.questions.getOrNull(current.currentQuestionIndex) ?: return
        val userCorrect = optionIndex == q.correctIndex
        _duelState.value = current.copy(
            userChoice = optionIndex,
            userScore = current.userScore + if (userCorrect) 10 else 0
        )
        maybeRevealDuelRound()
    }

    fun revealDuelQuestion() {
        _duelState.value = _duelState.value.copy(isRevealed = true)
    }

    fun nextDuelQuestion() {
        val current = _duelState.value
        if (!current.isRevealed) return
        if (current.currentQuestionIndex < current.questions.size - 1) {
            _duelState.value = current.copy(
                currentQuestionIndex = current.currentQuestionIndex + 1,
                userChoice = null,
                opponentChoice = null,
                isRevealed = false
            )
            startDuelRound()
        } else {
            finishDuel()
        }
    }

    private fun finishDuel() {
        cancelDuelJobs()
        val current = _duelState.value
        val opponentName = current.friend?.name ?: "Opponent"
        val isTie = current.userScore == current.opponentScore
        val isUserWin = current.userScore > current.opponentScore
        val awarded = when {
            isUserWin -> DUEL_PRIZE
            isTie -> DUEL_ENTRY
            else -> 0
        }
        when {
            isUserWin -> addCoins(DUEL_PRIZE, "1v1 Duel victory vs $opponentName")
            isTie -> addCoins(DUEL_ENTRY, "1v1 Duel draw refund")
        }
        _duelState.value = current.copy(winnerName = if (isUserWin) "You" else if (isTie) null else opponentName)
        showToast(
            when {
                isUserWin -> "Victory! You won $DUEL_PRIZE coins!"
                isTie -> "It's a draw — entry fee refunded."
                else -> "$opponentName won this one. Rematch?"
            }
        )

        val total = current.questions.size
        val correct = current.userScore / 10
        val percent = if (total > 0) correct * 100 / total else 0
        recordCompletedActivity(percent)
        awardLeaderboardPoints(correct * 25)

        _quizState.value = QuizState(
            mode = QuizMode.DUEL,
            quizCompleted = true,
            isWinState = isUserWin,
            isTie = isTie,
            scorePercentage = percent,
            correctCount = correct,
            wrongCount = total - correct,
            totalCount = total,
            coinsAwarded = awarded,
            scoreLabel = "${current.userScore} – ${current.opponentScore}",
            pointsGained = correct * 25
        )
        navigateTo(Screen.RESULTS)
    }

    fun abandonDuel() {
        cancelDuelJobs()
        val s = _duelState.value
        if (s.phase == DuelPhase.WAITING) {
            // Invite was never accepted: refund.
            addCoins(DUEL_ENTRY, "1v1 Duel invite cancelled")
        }
        _duelState.value = DuelState()
    }

    private fun cancelDuelJobs() {
        duelTimerJob?.cancel()
        duelOpponentJob?.cancel()
    }

    /** Re-runs the session shown on the results screen. */
    fun retryLastSession() {
        val state = _quizState.value
        when (state.mode) {
            QuizMode.PRACTICE -> state.category?.let { startPracticeQuiz(it) } ?: navigateTo(Screen.HOME)
            QuizMode.DAILY_CHALLENGE -> navigateTo(Screen.CATEGORIES)
            QuizMode.MOCK_TEST -> startMockTest()
            QuizMode.DUEL -> _duelState.value.friend?.let { startDuelChallenge(it) } ?: navigateTo(Screen.COMPETE)
        }
    }

    // ---------------------------------------------------------------- Shop, streaks & capsules

    fun buyCoinPack(pack: CoinPack) {
        addCoins(pack.coins, "Bought ${pack.coins} Coins Pack")
        showToast("Successfully purchased ${pack.coins} Coins!")
    }

    fun buyPowerUp(powerUp: PowerUp) {
        if (!deductCoins(powerUp.coinPrice, "Bought ${powerUp.title}")) return
        when (powerUp.id) {
            "pu_streak_freeze" -> updateProfile { it.copy(freezesOwned = it.freezesOwned + 1) }
            "pu_hints_5" -> updateProfile { it.copy(hintsOwned = it.hintsOwned + 5) }
            "pu_hints_15" -> updateProfile { it.copy(hintsOwned = it.hintsOwned + 15) }
        }
        showToast("Successfully purchased ${powerUp.title}!")
    }

    fun buyStreakFreeze() {
        if (deductCoins(200, "Bought Streak Freeze")) {
            updateProfile { it.copy(freezesOwned = it.freezesOwned + 1) }
            showToast("Streak Freeze acquired!")
        }
    }

    fun claimMilestone(days: Int, rewardCoins: Int) {
        val profile = _userProfile.value
        val claimed = Milestones.parseClaimed(profile.claimedMilestones)
        when {
            days in claimed -> showToast("Milestone already claimed")
            profile.longestStreak < days -> showToast("Reach a $days-day streak to unlock this reward")
            else -> {
                updateProfile { it.copy(claimedMilestones = Milestones.encodeClaimed(claimed + days)) }
                addCoins(rewardCoins, "$days-Day Streak Milestone")
                showToast("Claimed +$rewardCoins coins!")
            }
        }
    }

    fun toggleCapsuleBookmark(capsuleId: String) {
        val bookmarked = capsuleId in bookmarkedCapsuleIds.value
        viewModelScope.launch { repository.toggleBookmark(capsuleId, !bookmarked) }
        showToast(if (bookmarked) "Removed from saved notes" else "Saved to your notes")
    }

    override fun onCleared() {
        quizTimerJob?.cancel()
        mockTimerJob?.cancel()
        cancelDuelJobs()
        super.onCleared()
    }

    companion object {
        fun initialsFor(name: String): String {
            val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
            return when {
                parts.isEmpty() -> "AS"
                parts.size == 1 -> parts[0].take(2).uppercase()
                else -> (parts[0].take(1) + parts[1].take(1)).uppercase()
            }
        }

        fun generateReferralCode(initials: String): String =
            initials.uppercase().take(2) + (1000 + Random.nextInt(9000)).toString()
    }
}
