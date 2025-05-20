package ru.pro.beatrate.ui.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import ru.pro.beatrate.ui.Navigation.Navigation
import ru.pro.beatrate.ui.theme.BeatRateRecAppointmentAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeatRateRecAppointmentAppTheme {
                val navController = rememberNavController()
                Navigation(navController)
            }
        }
    }
}
