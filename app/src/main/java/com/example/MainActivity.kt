package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.ReferralBottomSheet
import com.example.ui.screens.*
import com.example.ui.theme.GujaratQuizTheme
import com.example.ui.theme.LocalAppColors
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val COMPETE = "compete"
    const val PROFILE = "profile"
    const val QUIZ_PLAY = "quiz_play"
    const val RESULTS = "results"
    const val MOCK_TEST = "mock_test"
    const val COIN_SHOP = "coin_shop"
    const val STREAK_CALENDAR = "streak_calendar"
    const val CAPSULES = "capsules"
    const val DUEL_CHALLENGE = "duel_challenge"
}

fun Screen.toRoute(): String = when (this) {
    Screen.ONBOARDING -> Routes.ONBOARDING
    Screen.HOME -> Routes.HOME
    Screen.CATEGORIES -> Routes.CATEGORIES
    Screen.COMPETE -> Routes.COMPETE
    Screen.PROFILE -> Routes.PROFILE
    Screen.QUIZ_PLAY -> Routes.QUIZ_PLAY
    Screen.RESULTS -> Routes.RESULTS
    Screen.MOCK_TEST -> Routes.MOCK_TEST
    Screen.COIN_SHOP -> Routes.COIN_SHOP
    Screen.STREAK_CALENDAR -> Routes.STREAK_CALENDAR
    Screen.CAPSULES -> Routes.CAPSULES
    Screen.DUEL_CHALLENGE -> Routes.DUEL_CHALLENGE
}

fun String.toScreen(): Screen = when (this) {
    Routes.ONBOARDING -> Screen.ONBOARDING
    Routes.HOME -> Screen.HOME
    Routes.CATEGORIES -> Screen.CATEGORIES
    Routes.COMPETE -> Screen.COMPETE
    Routes.PROFILE -> Screen.PROFILE
    Routes.QUIZ_PLAY -> Screen.QUIZ_PLAY
    Routes.RESULTS -> Screen.RESULTS
    Routes.MOCK_TEST -> Screen.MOCK_TEST
    Routes.COIN_SHOP -> Screen.COIN_SHOP
    Routes.STREAK_CALENDAR -> Screen.STREAK_CALENDAR
    Routes.CAPSULES -> Screen.CAPSULES
    Routes.DUEL_CHALLENGE -> Screen.DUEL_CHALLENGE
    else -> Screen.HOME
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val activeTheme by viewModel.activeTheme.collectAsState()

            GujaratQuizTheme(activeThemeName = activeTheme) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentBottomTab by viewModel.currentBottomTab.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val activeTheme by viewModel.activeTheme.collectAsState()
    val onboardingStep by viewModel.onboardingStep.collectAsState()
    val quizState by viewModel.quizState.collectAsState()
    val mockTestState by viewModel.mockTestState.collectAsState()
    val duelState by viewModel.duelState.collectAsState()
    val showReferralSheet by viewModel.showReferralSheet.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val leaderboardUsers by viewModel.leaderboardUsers.collectAsState()
    val userRank by viewModel.userRank.collectAsState()
    val displayStreak by viewModel.displayStreak.collectAsState()
    val activityDays by viewModel.activityDays.collectAsState()
    val categoryProgress by viewModel.categoryProgress.collectAsState()
    val bookmarkedCapsuleIds by viewModel.bookmarkedCapsuleIds.collectAsState()

    val colors = LocalAppColors.current

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.HOME

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val route = destination.route ?: return@OnDestinationChangedListener
            val screen = route.toScreen()
            if (viewModel.currentScreen.value != screen) {
                viewModel.setCurrentScreenFromNav(screen)
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    BackHandler(enabled = currentRoute != Routes.HOME && currentRoute != Routes.ONBOARDING) {
        if (!navController.popBackStack()) {
            viewModel.navigateTo(Screen.HOME)
        }
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    val profileLoaded by viewModel.profileLoaded.collectAsState()

    LaunchedEffect(currentScreen, profileLoaded) {
        if (!profileLoaded) return@LaunchedEffect
        val targetRoute = currentScreen.toRoute()
        if (navController.currentBackStackEntry?.destination?.route != targetRoute) {
            val currentNavRoute = navController.currentBackStackEntry?.destination?.route
            navController.navigate(targetRoute) {
                when {
                    // Leaving onboarding: it should not stay on the back stack.
                    currentNavRoute == Routes.ONBOARDING -> {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                    targetRoute in listOf(Routes.HOME, Routes.CATEGORIES, Routes.COMPETE, Routes.PROFILE) -> {
                        popUpTo(Routes.HOME) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    // Results replace the finished session so "back" doesn't return into it.
                    targetRoute == Routes.RESULTS && currentNavRoute != null &&
                        currentNavRoute in listOf(Routes.QUIZ_PLAY, Routes.MOCK_TEST, Routes.DUEL_CHALLENGE) -> {
                        popUpTo(currentNavRoute) { inclusive = true }
                    }
                    // Retrying from results replaces the results screen.
                    currentNavRoute == Routes.RESULTS -> {
                        popUpTo(Routes.RESULTS) { inclusive = true }
                    }
                }
            }
        }
    }

    if (!profileLoaded) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = colors.primary)
        }
        return
    }

    // Fixed for the lifetime of the NavHost; later changes are handled by navigate().
    val startRoute = remember { currentScreen.toRoute() }

    val showBottomBar = currentRoute in listOf(
        Routes.HOME,
        Routes.CATEGORIES,
        Routes.COMPETE,
        Routes.PROFILE
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = colors.card,
                    contentColor = colors.primary,
                    tonalElevation = 8.dp
                ) {
                    val navItems = listOf(
                        BottomNavItem("Home", Icons.Default.Home, 0, Routes.HOME),
                        BottomNavItem("Categories", Icons.Default.Category, 1, Routes.CATEGORIES),
                        BottomNavItem("Compete", Icons.Default.EmojiEvents, 2, Routes.COMPETE),
                        BottomNavItem("Profile", Icons.Default.Person, 3, Routes.PROFILE)
                    )

                    navItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.selectBottomTab(item.index) },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colors.primary,
                                selectedTextColor = colors.primary,
                                indicatorColor = colors.primaryLight,
                                unselectedIconColor = colors.muted,
                                unselectedTextColor = colors.muted
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = startRoute
            ) {
                composable(Routes.ONBOARDING) {
                    OnboardingScreen(
                        step = onboardingStep,
                        selectedGoal = userProfile.goal,
                        onSelectGoal = { viewModel.setGoal(it) },
                        onNextStep = { viewModel.nextOnboardingStep() },
                        onClaimGift = { viewModel.claimWelcomeGift() },
                        initialName = userProfile.name,
                        onSubmitName = { name -> viewModel.updateNameAndCity(name, userProfile.city) }
                    )
                }

                composable(Routes.HOME) {
                    HomeScreen(
                        userName = userProfile.name,
                        userInitials = userProfile.initials,
                        streakDays = displayStreak,
                        coinBalance = userProfile.coins,
                        accuracy = userProfile.accuracyPercentage,
                        rank = userRank,
                        quizzesCount = userProfile.totalQuizzes,
                        onStartDailyChallenge = { viewModel.startDailyChallenge() },
                        onCategoryClick = { category -> viewModel.startPracticeQuiz(category) },
                        onSeeAllCategories = { viewModel.navigateTo(Screen.CATEGORIES) },
                        onInviteClick = { viewModel.setShowReferralSheet(true) },
                        onLeaderboardTeaserClick = { viewModel.selectBottomTab(2) },
                        onAvatarClick = { viewModel.selectBottomTab(3) },
                        onStreakClick = { viewModel.navigateTo(Screen.STREAK_CALENDAR) },
                        onCoinsClick = { viewModel.navigateTo(Screen.COIN_SHOP) },
                        onCapsulesClick = { viewModel.navigateTo(Screen.CAPSULES) },
                        onDuelClick = { viewModel.startQuickDuel() },
                        onMockTestClick = { viewModel.startMockTest() },
                        dailyChallengePlayed = userProfile.lastDailyChallengeEpochDay == com.example.data.DayClock.epochDay()
                    )
                }

                composable(Routes.CATEGORIES) {
                    CategoriesScreen(
                        userName = userProfile.name,
                        userInitials = userProfile.initials,
                        streakDays = displayStreak,
                        coinBalance = userProfile.coins,
                        onCategoryClick = { category -> viewModel.startPracticeQuiz(category) },
                        onAvatarClick = { viewModel.selectBottomTab(3) },
                        onStreakClick = { viewModel.navigateTo(Screen.STREAK_CALENDAR) },
                        onCoinsClick = { viewModel.navigateTo(Screen.COIN_SHOP) },
                        progress = categoryProgress
                    )
                }

                composable(Routes.COMPETE) {
                    CompeteScreen(
                        userCoins = userProfile.coins,
                        leaderboardUsers = leaderboardUsers,
                        onStartDailyChallenge = { viewModel.startDailyChallenge() },
                        onStartDuel = { friend -> viewModel.startDuelChallenge(friend) },
                        onOpenMockTest = { viewModel.startMockTest() }
                    )
                }

                composable(Routes.PROFILE) {
                    ProfileScreen(
                        profile = userProfile,
                        transactions = transactions,
                        activeTheme = activeTheme,
                        onOpenCoinShop = { viewModel.navigateTo(Screen.COIN_SHOP) },
                        onOpenReferralSheet = { viewModel.setShowReferralSheet(true) },
                        onSwitchTheme = { themeName -> viewModel.switchTheme(themeName) },
                        rank = userRank,
                        displayStreak = displayStreak,
                        onUpdateProfile = { name, city -> viewModel.updateNameAndCity(name, city) }
                    )
                }

                composable(Routes.QUIZ_PLAY) {
                    QuizPlayScreen(
                        quizState = quizState,
                        userCoins = userProfile.coins,
                        hintsOwned = userProfile.hintsOwned,
                        onSelectOption = { viewModel.selectQuizOption(it) },
                        onUseHint = { viewModel.useQuizHint() },
                        onNextQuestion = { viewModel.nextQuizQuestion() },
                        onBack = {
                            viewModel.abandonQuiz()
                            if (!navController.popBackStack()) viewModel.navigateTo(Screen.HOME)
                        }
                    )
                }

                composable(Routes.RESULTS) {
                    ResultsScreen(
                        quizState = quizState,
                        onHome = { viewModel.navigateTo(Screen.HOME) },
                        onRetry = { viewModel.retryLastSession() }
                    )
                }

                composable(Routes.MOCK_TEST) {
                    MockTestScreen(
                        mockState = mockTestState,
                        onSelectOption = { qIdx, optIdx -> viewModel.selectMockOption(qIdx, optIdx) },
                        onToggleMarkForReview = { viewModel.toggleMockMarkForReview(it) },
                        onGoToQuestion = { viewModel.goToMockQuestion(it) },
                        onSetPaletteOpen = { viewModel.setMockPaletteOpen(it) },
                        onSetSubmitModalOpen = { viewModel.setMockSubmitModalOpen(it) },
                        onSubmitMockTest = { viewModel.submitMockTest() },
                        onGoToSection = { viewModel.goToMockSection(it) },
                        onBack = {
                            viewModel.quitMockTest()
                            if (!navController.popBackStack()) viewModel.navigateTo(Screen.COMPETE)
                        }
                    )
                }

                composable(Routes.COIN_SHOP) {
                    CoinShopScreen(
                        userCoins = userProfile.coins,
                        onBuyCoinPack = { pack -> viewModel.buyCoinPack(pack) },
                        onBuyPowerUp = { powerUp -> viewModel.buyPowerUp(powerUp) },
                        onBack = { if (!navController.popBackStack()) viewModel.navigateTo(Screen.PROFILE) }
                    )
                }

                composable(Routes.STREAK_CALENDAR) {
                    StreakCalendarScreen(
                        profile = userProfile,
                        onBuyFreeze = { viewModel.buyStreakFreeze() },
                        onClaimMilestone = { days, coins -> viewModel.claimMilestone(days, coins) },
                        onBack = { if (!navController.popBackStack()) viewModel.navigateTo(Screen.HOME) },
                        displayStreak = displayStreak,
                        activityDays = activityDays
                    )
                }

                composable(Routes.CAPSULES) {
                    CapsulesScreen(
                        onBack = { if (!navController.popBackStack()) viewModel.navigateTo(Screen.HOME) },
                        bookmarkedIds = bookmarkedCapsuleIds,
                        onToggleBookmark = { viewModel.toggleCapsuleBookmark(it) },
                        onPractice = { categoryId -> viewModel.startPracticeForCategoryId(categoryId) }
                    )
                }

                composable(Routes.DUEL_CHALLENGE) {
                    DuelChallengeScreen(
                        duelState = duelState,
                        onAdvanceToWaiting = { viewModel.advanceDuelToWaiting() },
                        onSelectOption = { viewModel.selectDuelOption(it) },
                        onRevealQuestion = { viewModel.revealDuelQuestion() },
                        onNextQuestion = { viewModel.nextDuelQuestion() },
                        onBack = {
                            viewModel.abandonDuel()
                            if (!navController.popBackStack()) viewModel.navigateTo(Screen.COMPETE)
                        }
                    )
                }
            }

            // Referral Bottom Sheet Modal
            if (showReferralSheet) {
                ReferralBottomSheet(
                    onDismiss = { viewModel.setShowReferralSheet(false) },
                    referralCode = "GUJ-${userProfile.referralCode}"
                )
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val index: Int,
    val route: String
)

