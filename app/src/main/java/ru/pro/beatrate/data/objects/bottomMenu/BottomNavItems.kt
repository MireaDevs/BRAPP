package ru.pro.beatrate.data.objects.bottomMenu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import ru.pro.beatrate.data.models.BottomNavItem

object BottomNavItems {
    val BottomNavItems = listOf(
        // Home screen
        BottomNavItem(
            label = "Главная",
            icon = Icons.Filled.Home,
            route = "HomeScreen"//поменять
        ),

        BottomNavItem(
            label = "Бронирование",
            icon = Icons.Filled.DateRange,
            route = "BookingScreen"//поменять
        ),
        BottomNavItem(
            label = "Насторойки",
            icon = Icons.Filled.Settings,
            route = "ScreenSettings"//поменять
        )

    )
}
//+чат но я его хуй сделаю за такое время