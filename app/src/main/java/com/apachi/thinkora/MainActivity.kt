package com.apachi.thinkora

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.apachi.thinkora.core.designsystem.theme.ThinkoraTheme
import com.apachi.thinkora.core.domain.repository.SettingsRepository
import com.apachi.thinkora.data.ads.AdManager
import com.apachi.thinkora.domain.use_case.GetOnboardingStatusUseCase
import com.apachi.thinkora.feature.onboarding.OnboardingInterestsScreen
import com.apachi.thinkora.feature.onboarding.OnboardingIntroScreen
import com.apachi.thinkora.feature.onboarding.OnboardingNameScreen
import com.apachi.thinkora.feature.onboarding.OnboardingNotificationsScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var getOnboardingStatusUseCase: GetOnboardingStatusUseCase

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        var isAppReady = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setKeepOnScreenCondition { !isAppReady }
            setupSplashScreenExitAnimation(splashScreen)
        }

        isAppReady = true
        // setEdgeToEdgeConfig()
        setContent {
            val isDarkTheme by settingsRepository.isDarkThemeConfig.collectAsState(initial = false)
            val languageCode by settingsRepository.languageCode.collectAsState(initial = "en")

            val context = LocalContext.current
            // Apply language and layout direction; no auto-recreate to avoid UI lockups
            LaunchedEffect(languageCode) {
                val locale = Locale(languageCode)
                // Update app-wide configuration locale so stringResource() uses the correct language
                Locale.setDefault(locale)
                val resources = context.resources
                val config = resources.configuration
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.displayMetrics)
                val activity = context as? android.app.Activity
                activity?.window?.decorView?.layoutDirection =
                    if (languageCode == "ar") View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
            }

            ThinkoraTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val onboardingCompleted by getOnboardingStatusUseCase().collectAsState(initial = null)

                    if (onboardingCompleted != null) {
                        val startDestination =
                            if (onboardingCompleted == true) com.apachi.thinkora.domain.navigation.Screen.MainScreen.route
                            else com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route

                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            navigation(
                                startDestination = com.apachi.thinkora.domain.navigation.Screen.OnboardingIntro.route,
                                route = com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route
                            ) {
                                composable(com.apachi.thinkora.domain.navigation.Screen.OnboardingIntro.route) {
                                    OnboardingIntroScreen(navController = navController)
                                }
                                composable(com.apachi.thinkora.domain.navigation.Screen.OnboardingName.route) { backStackEntry ->
                                    val parentEntry = remember(backStackEntry) {
                                        navController.getBackStackEntry(com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route)
                                    }
                                    val onboardingViewModel: com.apachi.thinkora.feature.onboarding.OnboardingViewModel =
                                        androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                    OnboardingNameScreen(
                                        navController = navController,
                                        viewModel = onboardingViewModel
                                    )
                                }
                                composable(com.apachi.thinkora.domain.navigation.Screen.OnboardingInterests.route) { backStackEntry ->
                                    val parentEntry = remember(backStackEntry) {
                                        navController.getBackStackEntry(com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route)
                                    }
                                    val onboardingViewModel: com.apachi.thinkora.feature.onboarding.OnboardingViewModel =
                                        androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                    OnboardingInterestsScreen(
                                        navController = navController,
                                        viewModel = onboardingViewModel
                                    )
                                }
                                composable(com.apachi.thinkora.domain.navigation.Screen.OnboardingNotifications.route) { backStackEntry ->
                                    val parentEntry = remember(backStackEntry) {
                                        navController.getBackStackEntry(com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route)
                                    }
                                    val onboardingViewModel: com.apachi.thinkora.feature.onboarding.OnboardingViewModel =
                                        androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                    OnboardingNotificationsScreen(
                                        navController = navController,
                                        viewModel = onboardingViewModel
                                    )
                                }
                            }
                            composable(com.apachi.thinkora.domain.navigation.Screen.MainScreen.route) {
                                com.apachi.thinkora.feature.main.MainScreen(
                                    rootNavController = navController,
                                    adManager = adManager
                                )
                            }
                        }
                    } else {
                        // Loading state
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    private fun setupSplashScreenExitAnimation(splashScreen: SplashScreen) {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.view.height.toFloat(),
            ).apply {
                interpolator = AnticipateInterpolator()
                duration = 200L
                doOnEnd { splashScreenView.remove() }
            }
            slideUp.start()
        }
    }
}