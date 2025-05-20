package ru.pro.beatrate.ui.Screens.StartScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.pro.beatrate.R

@Composable
fun WelcomeScreen(
    navController:NavController
) {
    // Используем BoxWithConstraints, чтобы динамически вычислить размер экрана для градиента
    BoxWithConstraints {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    // Линейный градиент: слева-сверху (#4682B4) -> справа-снизу (#0D0D0D)
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF4682B4),
                            Color(0xFF0D0D0D)
                        ),
                        start = Offset(-2000f, -2000f),
                        end = Offset(width, height)
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Картинка без фона (PNG)
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        // заставляем картинку занять доступное место по высоте
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )

                // Нижняя плашка (#404040) с закруглёнными верхними углами
                Surface(
                    color = Color(0xFF404040),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome Gobars",
                            style = MaterialTheme.typography.titleLarge, // M3 стиль
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Find the best grooming experience in your city\n" +
                                    "with just one tap! Don't miss out on the haircut\n" +
                                    "or treatment of your dreams. Let's start now!",
                            style = MaterialTheme.typography.bodyMedium, // M3 стиль
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Кнопка "Get Started" (#4682B4) - используем M3-параметр containerColor
                        Button(
                            onClick = { navController.navigate("LoginScreen") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4682B4)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Get Started", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
