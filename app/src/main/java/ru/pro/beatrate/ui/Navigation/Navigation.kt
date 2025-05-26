package ru.pro.beatrate.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import ru.pro.beatrate.ui.Screens.AuthScreens.AuthScreen
import ru.pro.beatrate.ui.Screens.AuthScreens.LoginScreen
import ru.pro.beatrate.ui.Screens.AuthScreens.ForgotPasswordScreen
import ru.pro.beatrate.ui.Screens.AuthScreens.RegisterScreen
import ru.pro.beatrate.ui.Screens.Content.BookingScreen
import ru.pro.beatrate.ui.Screens.Content.HomeScreen
import ru.pro.beatrate.ui.Screens.StartScreens.SplashScreen
import ru.pro.beatrate.ui.Screens.StartScreens.WelcomeScreen


@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "SplashScreen") {
        composable("SplashScreen") { SplashScreen(
            navController = navController
        ) }
        composable("LoginScreen") { LoginScreen(
            navController = navController
        ) }
        composable("ForgotPasswordScreen") { ForgotPasswordScreen(
            navController = navController
        ) }
        composable("AuthScreen") { AuthScreen(
            navController = navController
        ) }
        composable("WelcomeScreen") { WelcomeScreen(
            navController = navController
        ) }
        composable("HomeScreen") { HomeScreen(
            navController = navController
        ) }
        composable("BookingScreen") { BookingScreen(
            navController = navController
        ) }
        composable("RegisterScreen") { RegisterScreen(
            navController = navController
        ) }


    }
}
