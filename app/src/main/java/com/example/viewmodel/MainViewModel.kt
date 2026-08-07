package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

data class QuizState(
    val category: QuizCategory? = null,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val userAnswers: MutableMap<Int, Int> = mutableMapOf(),
    val isRevealed: Boolean = false,
    val selectedOption: Int? = null,
    val timerSeconds: Int = 30,
    val isTimerActive: Boolean = false,
    val eliminatedOptions: Set<Int> = emptySet(),
    val coinsEarnedInQuiz: Int = 0,
    val showCoinPopAnimation: Boolean = false,
    val quizCompleted: Boolean = false,
    val isWinState: Boolean = false,
    val scorePercentage: Int = 0,
    val correctCount: Int = 0,
    val totalCount: Int = 0
)

data class MockTestState(
    val questions: List<MockQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val activeSection: String = "General Studies",
    val userAnswers: MutableMap<Int, Int> = mutableMapOf(),
    val markedForReview: MutableSet<Int> = mutableSetOf(),
    val visitedQuestions: MutableSet<Int> = mutableSetOf(),
    val timeRemainingSeconds: Int = 7200, // 2 hours
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
    val userChoice: Int? = null,
    val opponentChoice: Int? = null,
    val timerSeconds: Int = 20,
    val isRevealed: Boolean = false,
    val winnerName: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    init {
        val dao = AppDatabase.getDatabase(application).appDao()
        repository = AppRepository(dao)

        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                if (profile != null) {
                    _userProfile.value = profile
                    _activeTheme.value = profile.theme
                    if (!profile.onboardingCompleted) {
                        _currentScreen.value = Screen.ONBOARDING
                    }
                } else {
                    // Seed initial user profile
                    val initialProfile = UserProfileEntity()
                    repository.saveUserProfile(initialProfile)
                    // Seed initial transactions
                    repository.addTransaction(TransactionEntity(title = "Signup bonus", timeAgo = "2d ago", amount = 500))
                    repository.addTransaction(TransactionEntity(title = "Daily login streak · D7", timeAgo = "1d ago", amount = 100))
                    repository.addTransaction(TransactionEntity(title = "Practice · Polity", timeAgo = "1d ago", amount = -40))
                    repository.addTransaction(TransactionEntity(title = "Quiz reward (88%)", timeAgo = "1d ago", amount = 80))
                    repository.addTransaction(TransactionEntity(title = "Referral · Darshan", timeAgo = "3h ago", amount = 500))
                    repository.addTransaction(TransactionEntity(title = "GPSC Mock Test", timeAgo = "2h ago", amount = -100))
                    repository.addTransaction(TransactionEntity(title = "Daily Challenge · Top 3", timeAgo = "14m ago", amount = 5000))
                }
            }
        }
    }

    private val _userProfile = MutableStateFlow(UserProfileEntity())
    val userProfile: StateFlow<UserProfileEntity> = _userProfile.asStateFlow()

    val transactions: StateFlow<List<TransactionEntity>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    // Quiz State
    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    // Mock Test State
    private val _mockTestState = MutableStateFlow(MockTestState())
    val mockTestState: StateFlow<MockTestState> = _mockTestState.asStateFlow()

    // Duel State
    private val _duelState = MutableStateFlow(DuelState())
    val duelState: StateFlow<DuelState> = _duelState.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        if (screen == Screen.HOME) _currentBottomTab.value = 0
        if (screen == Screen.CATEGORIES) _currentBottomTab.value = 1
        if (screen == Screen.COMPETE) _currentBottomTab.value = 2
        if (screen == Screen.PROFILE) _currentBottomTab.value = 3
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

    // Onboarding Actions
    fun nextOnboardingStep() {
        if (_onboardingStep.value < 3) {
            _onboardingStep.value += 1
        }
    }

    fun setGoal(goal: String) {
        val updated = _userProfile.value.copy(goal = goal)
        _userProfile.value = updated
        viewModelScope.launch { repository.saveUserProfile(updated) }
    }

    fun claimWelcomeGift() {
        addCoins(500, "Welcome gift")
        val updated = _userProfile.value.copy(onboardingCompleted = true)
        _userProfile.value = updated
        viewModelScope.launch { repository.saveUserProfile(updated) }
        _currentScreen.value = Screen.HOME
    }

    // Theme Switcher
    fun switchTheme(themeName: String) {
        _activeTheme.value = themeName
        val updated = _userProfile.value.copy(theme = themeName)
        _userProfile.value = updated
        viewModelScope.launch { repository.saveUserProfile(updated) }
        showToast("Theme switched to $themeName")
    }

    // Coin Economy
    fun deductCoins(amount: Int, reason: String): Boolean {
        if (_userProfile.value.coins >= amount) {
            val newBalance = _userProfile.value.coins - amount
            val updated = _userProfile.value.copy(coins = newBalance)
            _userProfile.value = updated
            viewModelScope.launch {
                repository.saveUserProfile(updated)
                repository.addTransaction(TransactionEntity(title = reason, timeAgo = "Just now", amount = -amount))
            }
            return true
        } else {
            showToast("Not enough coins! You need $amount coins.")
            return false
        }
    }

    fun addCoins(amount: Int, reason: String) {
        val newBalance = _userProfile.value.coins + amount
        val updated = _userProfile.value.copy(coins = newBalance)
        _userProfile.value = updated
        viewModelScope.launch {
            repository.saveUserProfile(updated)
            repository.addTransaction(TransactionEntity(title = reason, timeAgo = "Just now", amount = amount))
        }
    }

    // Practice Quiz Flow
    fun startPracticeQuiz(category: QuizCategory) {
        if (!deductCoins(category.coinCost, "Practice · ${category.titleEn}")) {
            return
        }
        val questions = SeedData.sampleQuestions.filter { it.categoryId == category.id }
            .ifEmpty { SeedData.sampleQuestions }
        _quizState.value = QuizState(
            category = category,
            questions = questions,
            currentIndex = 0,
            timerSeconds = 30,
            isTimerActive = true
        )
        _currentScreen.value = Screen.QUIZ_PLAY
    }

    fun startDailyChallenge() {
        if (!deductCoins(50, "Daily Challenge entry")) {
            return
        }
        val category = SeedData.categories.find { it.id == "polity_constitution" } ?: SeedData.categories.first()
        _quizState.value = QuizState(
            category = category,
            questions = SeedData.sampleQuestions,
            currentIndex = 0,
            timerSeconds = 30,
            isTimerActive = true
        )
        _currentScreen.value = Screen.QUIZ_PLAY
    }

    fun selectQuizOption(optionIndex: Int) {
        val current = _quizState.value
        if (current.isRevealed) return

        val q = current.questions[current.currentIndex]
        val isCorrect = optionIndex == q.correctIndex
        val answersMap = current.userAnswers.toMutableMap()
        answersMap[current.currentIndex] = optionIndex

        var coinsEarned = current.coinsEarnedInQuiz
        var showCoinPop = false

        if (isCorrect) {
            coinsEarned += 30
            showCoinPop = true
        }

        _quizState.value = current.copy(
            userAnswers = answersMap,
            isRevealed = true,
            selectedOption = optionIndex,
            isTimerActive = false,
            coinsEarnedInQuiz = coinsEarned,
            showCoinPopAnimation = showCoinPop
        )

        if (showCoinPop) {
            viewModelScope.launch {
                delay(1500)
                _quizState.value = _quizState.value.copy(showCoinPopAnimation = false)
            }
        }
    }

    fun useQuizHint() {
        val current = _quizState.value
        if (current.isRevealed) return

        if (!deductCoins(50, "Quiz Hint")) {
            return
        }

        val q = current.questions[current.currentIndex]
        val wrongIndices = (0..3).filter { it != q.correctIndex }
        val elim = wrongIndices.shuffled().take(2).toSet()

        _quizState.value = current.copy(eliminatedOptions = elim)
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
                timerSeconds = 30,
                isTimerActive = true
            )
        } else {
            // Quiz finished
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        val current = _quizState.value
        var correct = 0
        current.questions.forEachIndexed { index, question ->
            if (current.userAnswers[index] == question.correctIndex) {
                correct++
            }
        }
        val total = current.questions.size
        val scorePercent = if (total > 0) (correct * 100) / total else 0
        var totalCoinsEarned = current.coinsEarnedInQuiz

        val isWin = scorePercent >= 80
        if (isWin) {
            totalCoinsEarned += 200 // Bonus for high score
            addCoins(totalCoinsEarned, "Quiz Reward (${scorePercent}%)")
        } else if (totalCoinsEarned > 0) {
            addCoins(totalCoinsEarned, "Quiz Reward (${scorePercent}%)")
        }

        // Update total quizzes and accuracy
        val oldQuizzes = _userProfile.value.totalQuizzes
        val oldAcc = _userProfile.value.accuracyPercentage
        val newQuizzes = oldQuizzes + 1
        val newAcc = ((oldAcc * oldQuizzes) + scorePercent) / newQuizzes

        val updated = _userProfile.value.copy(
            totalQuizzes = newQuizzes,
            accuracyPercentage = newAcc
        )
        _userProfile.value = updated
        viewModelScope.launch { repository.saveUserProfile(updated) }

        _quizState.value = current.copy(
            quizCompleted = true,
            isWinState = isWin,
            scorePercentage = scorePercent,
            correctCount = correct,
            totalCount = total
        )
        _currentScreen.value = Screen.RESULTS
    }

    // Mock Test Flow
    fun startMockTest() {
        if (!deductCoins(100, "GPSC Mock Test Paper I")) {
            return
        }
        val mockQs = SeedData.generateMock100Questions()
        _mockTestState.value = MockTestState(
            questions = mockQs,
            currentIndex = 0,
            activeSection = "General Studies",
            visitedQuestions = mutableSetOf(0),
            timeRemainingSeconds = 7200
        )
        _currentScreen.value = Screen.MOCK_TEST
    }

    fun selectMockOption(qIndex: Int, optionIndex: Int) {
        val current = _mockTestState.value
        val answers = current.userAnswers.toMutableMap()
        answers[qIndex] = optionIndex
        _mockTestState.value = current.copy(userAnswers = answers)
    }

    fun toggleMockMarkForReview(qIndex: Int) {
        val current = _mockTestState.value
        val set = current.markedForReview.toMutableSet()
        if (set.contains(qIndex)) set.remove(qIndex) else set.add(qIndex)
        _mockTestState.value = current.copy(markedForReview = set)
    }

    fun goToMockQuestion(qIndex: Int) {
        val current = _mockTestState.value
        val visited = current.visitedQuestions.toMutableSet()
        visited.add(qIndex)
        val section = current.questions[qIndex].section
        _mockTestState.value = current.copy(
            currentIndex = qIndex,
            activeSection = section,
            visitedQuestions = visited,
            isPaletteOpen = false
        )
    }

    fun setMockPaletteOpen(open: Boolean) {
        _mockTestState.value = _mockTestState.value.copy(isPaletteOpen = open)
    }

    fun setMockSubmitModalOpen(open: Boolean) {
        _mockTestState.value = _mockTestState.value.copy(isSubmitModalOpen = open)
    }

    fun submitMockTest() {
        val current = _mockTestState.value
        var correct = 0
        var wrong = 0
        var score = 0f

        current.questions.forEachIndexed { index, q ->
            val ans = current.userAnswers[index]
            if (ans != null) {
                if (ans == q.correctIndex) {
                    correct++
                    score += q.positiveMarks
                } else {
                    wrong++
                    score -= q.negativeMarks
                }
            }
        }
        val unattempted = current.questions.size - (correct + wrong)

        _mockTestState.value = current.copy(
            isSubmitModalOpen = false,
            isSubmitted = true,
            totalScore = score,
            correctCount = correct,
            wrongCount = wrong,
            unattemptedCount = unattempted
        )

        // Grant reward if score positive
        if (score > 50) {
            addCoins(1000, "Mock Test High Score")
        }

        _quizState.value = QuizState(
            quizCompleted = true,
            isWinState = score > 100,
            scorePercentage = (score / 200 * 100).toInt().coerceIn(0, 100),
            correctCount = correct,
            totalCount = current.questions.size
        )
        _currentScreen.value = Screen.RESULTS
    }

    // 1v1 Duel Flow
    fun startDuelChallenge(friend: DuelFriend) {
        if (!deductCoins(100, "1v1 Duel entry fee")) {
            return
        }
        _duelState.value = DuelState(
            friend = friend,
            phase = DuelPhase.INVITE,
            questions = SeedData.sampleQuestions,
            currentQuestionIndex = 0
        )
        _currentScreen.value = Screen.DUEL_CHALLENGE
    }

    fun advanceDuelToWaiting() {
        val current = _duelState.value
        _duelState.value = current.copy(phase = DuelPhase.WAITING)

        // Simulate opponent joining after 2.5 seconds
        viewModelScope.launch {
            delay(2500)
            _duelState.value = _duelState.value.copy(phase = DuelPhase.LIVE_MATCH)
        }
    }

    fun selectDuelOption(optionIndex: Int) {
        val current = _duelState.value
        if (current.userChoice != null) return

        // Opponent picks random option
        val q = current.questions[current.currentQuestionIndex]
        val opponentPick = if (Math.random() > 0.3) q.correctIndex else (0..3).filter { it != q.correctIndex }.random()

        val userCorrect = optionIndex == q.correctIndex
        val opponentCorrect = opponentPick == q.correctIndex

        val newUserScore = current.userScore + if (userCorrect) 10 else 0
        val newOppScore = current.opponentScore + if (opponentCorrect) 10 else 0

        _duelState.value = current.copy(
            userChoice = optionIndex,
            opponentChoice = opponentPick,
            userScore = newUserScore,
            opponentScore = newOppScore
        )
    }

    fun revealDuelQuestion() {
        val current = _duelState.value
        _duelState.value = current.copy(isRevealed = true)
    }

    fun nextDuelQuestion() {
        val current = _duelState.value
        if (current.currentQuestionIndex < current.questions.size - 1) {
            _duelState.value = current.copy(
                currentQuestionIndex = current.currentQuestionIndex + 1,
                userChoice = null,
                opponentChoice = null,
                isRevealed = false
            )
        } else {
            // Match over
            val isUserWin = current.userScore >= current.opponentScore
            if (isUserWin) {
                addCoins(200, "1v1 Duel victory prize")
            }
            _duelState.value = current.copy(
                winnerName = if (isUserWin) "You" else current.friend?.name ?: "Opponent"
            )
            showToast(if (isUserWin) "Victory! You won 200 coins!" else "Match over!")
        }
    }

    // In-App Shop
    fun buyCoinPack(pack: CoinPack) {
        addCoins(pack.coins, "Bought ${pack.coins} Coins Pack")
        showToast("Successfully purchased ${pack.coins} Coins!")
    }

    fun buyPowerUp(powerUp: PowerUp) {
        if (deductCoins(powerUp.coinPrice, "Bought ${powerUp.title}")) {
            if (powerUp.id == "pu_streak_freeze") {
                val updated = _userProfile.value.copy(freezesOwned = _userProfile.value.freezesOwned + 1)
                _userProfile.value = updated
                viewModelScope.launch { repository.saveUserProfile(updated) }
            }
            showToast("Successfully purchased ${powerUp.title}!")
        }
    }

    fun buyStreakFreeze() {
        if (deductCoins(200, "Bought Streak Freeze")) {
            val updated = _userProfile.value.copy(freezesOwned = _userProfile.value.freezesOwned + 1)
            _userProfile.value = updated
            viewModelScope.launch { repository.saveUserProfile(updated) }
            showToast("Streak Freeze acquired!")
        }
    }

    fun claimMilestone(days: Int, rewardCoins: Int) {
        addCoins(rewardCoins, "$days-Day Streak Milestone")
        showToast("Claimed +$rewardCoins coins!")
    }
}
