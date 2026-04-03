package com.apachi.auraskin

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
import com.apachi.auraskin.core.designsystem.theme.AuraSkinTheme
import com.apachi.auraskin.core.domain.repository.SettingsRepository
import com.apachi.auraskin.data.ads.AdManager
import com.apachi.auraskin.domain.use_case.GetOnboardingStatusUseCase
import com.apachi.auraskin.feature.onboarding.OnboardingScreen
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

            AuraSkinTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val onboardingCompleted by getOnboardingStatusUseCase().collectAsState(initial = null)

                    if (onboardingCompleted != null) {
                        val startDestination =
                            if (onboardingCompleted == true) com.apachi.auraskin.domain.navigation.Screen.MainScreen.route
                            else com.apachi.auraskin.domain.navigation.Screen.OnboardingScreen.route

                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable(com.apachi.auraskin.domain.navigation.Screen.OnboardingScreen.route) {
                                val onboardingViewModel: com.apachi.auraskin.feature.onboarding.OnboardingViewModel =
                                    androidx.hilt.navigation.compose.hiltViewModel()
                                OnboardingScreen(
                                    navController = navController,
                                    viewModel = onboardingViewModel
                                )
                            }
                            composable(com.apachi.auraskin.domain.navigation.Screen.MainScreen.route) {
                                com.apachi.auraskin.feature.main.MainScreen(
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