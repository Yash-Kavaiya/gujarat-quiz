package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.ui.screens.OnboardingScreen
import com.example.ui.theme.GujaratQuizTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class OnboardingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun onboarding_welcome_screenshot() {
    composeTestRule.setContent {
      GujaratQuizTheme {
        OnboardingScreen(
          step = 1,
          selectedGoal = "GPSC Class 1 & 2 / Prelims 2026",
          onSelectGoal = {},
          onNextStep = {},
          onClaimGift = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/onboarding_welcome.png")
  }
}
