package ru.pro.beatrate.ui.Navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.pro.beatrate.ui.Screens.AuthScreens.LoginScreen
import ru.pro.beatrate.ui.Screens.AuthScreens.RegisterScreen
import ru.pro.beatrate.ui.Screens.Content.BookingScreen
import ru.pro.beatrate.ui.Screens.Content.HomeScreen
import ru.pro.beatrate.ui.Screens.Content.SessionDetailScreen
import ru.pro.beatrate.ui.Screens.StartScreens.SplashScreen
import ru.pro.beatrate.ui.Screens.StartScreens.WelcomeScreen
import kotlinx.serialization.json.Json
import ru.pro.beatrate.data.data_store.UserPreferences
import ru.pro.beatrate.data.models.SessionResponse
import ru.pro.beatrate.ui.Screens.Content.Admin.AdminScreen
import ru.pro.beatrate.ui.Screens.Content.EquipmentScreen
import ru.pro.beatrate.ui.Screens.Content.ScreenSettings

@Composable
fun Navigation(navController: NavHostController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()

    val isDarkTheme by prefs.isDarkThemeFlow.collectAsState(initial = false)

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        NavHost(navController = navController, startDestination = "SplashScreen") {
            composable("SplashScreen") {
                SplashScreen(navController)
            }
            composable("LoginScreen") {
                LoginScreen(navController)
            }
            composable("WelcomeScreen") {
                WelcomeScreen(navController)
            }
            composable("HomeScreen") {
                HomeScreen(navController)
            }
            composable("BookingScreen") {
                BookingScreen(navController)
            }
            composable("RegisterScreen") {
                RegisterScreen(navController)
            }
            composable("SessionDetail/{sessionJson}") { backStackEntry ->
                val json = backStackEntry.arguments?.getString("sessionJson") ?: return@composable
                val session = Json.decodeFromString<SessionResponse>(json)
                SessionDetailScreen(session)
            }
            composable("AdminScreen") {
                AdminScreen(navController)
            }
            composable("ScreenSettings") {
                ScreenSettings(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = {
                        coroutineScope.launch {
                            prefs.saveTheme(!isDarkTheme)
                        }
                    }
                )
            }
            composable("equipment") {
                EquipmentScreen(navController)
            }


        }
    }
}
