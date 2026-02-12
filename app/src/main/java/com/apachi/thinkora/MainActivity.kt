package com.apachi.thinkora

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.apachi.thinkora.core.designsystem.theme.ThinkoraTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.compose.runtime.LaunchedEffect
import androidx.core.os.LocaleListCompat
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var getOnboardingStatusUseCase: com.apachi.thinkora.domain.use_case.GetOnboardingStatusUseCase

    @Inject
    lateinit var settingsRepository: com.apachi.thinkora.core.domain.repository.SettingsRepository

    @Inject
    lateinit var adManager: com.apachi.thinkora.data.ads.AdManager

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

            // Apply language dynamically
            LaunchedEffect(languageCode) {
                val locale = Locale(languageCode)
                val localeList = LocaleListCompat.create(locale)
                AppCompatDelegate.setApplicationLocales(localeList)
            }

            ThinkoraTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    val onboardingCompleted by getOnboardingStatusUseCase().collectAsState(initial = null)

                    if (onboardingCompleted != null) {
                        val startDestination = if (onboardingCompleted == true) com.apachi.thinkora.domain.navigation.Screen.MainScreen.route else com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route
                        
                        androidx.navigation.compose.NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            navigation(
                                startDestination = com.apachi.thinkora.domain.navigation.Screen.OnboardingIntro.route,
                                route = com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route
                            ) {
                                composable(com.apachi.thinkora.domain.navigation.Screen.OnboardingIntro.route) { backStackEntry ->
                                    // Scope ViewModel to the navigation graph to share state
                                    val parentEntry = remember(backStackEntry) {
                                        navController.getBackStackEntry(com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route)
                                    }
                                    val onboardingViewModel: com.apachi.thinkora.feature.onboarding.OnboardingViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                    com.apachi.thinkora.feature.onboarding.OnboardingIntroScreen(navController = navController)
                                }
                                composable(com.apachi.thinkora.domain.navigation.Screen.OnboardingName.route) { backStackEntry ->
                                    val parentEntry = remember(backStackEntry) {
                                        navController.getBackStackEntry(com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route)
                                    }
                                    val onboardingViewModel: com.apachi.thinkora.feature.onboarding.OnboardingViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                    com.apachi.thinkora.feature.onboarding.OnboardingNameScreen(navController = navController, viewModel = onboardingViewModel)
                                }
                                composable(com.apachi.thinkora.domain.navigation.Screen.OnboardingInterests.route) { backStackEntry ->
                                    val parentEntry = remember(backStackEntry) {
                                        navController.getBackStackEntry(com.apachi.thinkora.domain.navigation.Screen.OnboardingScreen.route)
                                    }
                                    val onboardingViewModel: com.apachi.thinkora.feature.onboarding.OnboardingViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                    com.apachi.thinkora.feature.onboarding.OnboardingInterestsScreen(navController = navController, viewModel = onboardingViewModel)
                                }
                            }
                            composable(com.apachi.thinkora.domain.navigation.Screen.MainScreen.route) {
                                com.apachi.thinkora.feature.main.MainScreen(rootNavController = navController, adManager = adManager)
                            }
                        }
                    } else {
                         // Loading state
                         Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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