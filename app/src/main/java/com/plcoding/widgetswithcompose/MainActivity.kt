package com.plcoding.widgetswithcompose

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plcoding.widgetswithcompose.ui.theme.WidgetsWithComposeTheme
import com.plcoding.widgetswithcompose.ui.theme.setEdgeToEdgeConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var getOnboardingStatusUseCase: com.plcoding.widgetswithcompose.domain.use_case.GetOnboardingStatusUseCase

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
            WidgetsWithComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    val onboardingCompleted by getOnboardingStatusUseCase().collectAsState(initial = null)

                    if (onboardingCompleted != null) {
                        val startDestination = if (onboardingCompleted == true) com.plcoding.widgetswithcompose.presentation.navigation.Screen.HomeScreen.route else com.plcoding.widgetswithcompose.presentation.navigation.Screen.OnboardingScreen.route
                        
                        androidx.navigation.compose.NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable(com.plcoding.widgetswithcompose.presentation.navigation.Screen.OnboardingScreen.route) {
                                com.plcoding.widgetswithcompose.presentation.onboarding.OnboardingScreen(navController = navController)
                            }
                            composable(com.plcoding.widgetswithcompose.presentation.navigation.Screen.HomeScreen.route) {
                                com.plcoding.widgetswithcompose.presentation.home.HomeScreen()
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