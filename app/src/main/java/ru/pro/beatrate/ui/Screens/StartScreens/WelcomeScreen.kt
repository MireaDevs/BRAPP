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
    navController: NavController
) {
    BoxWithConstraints {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
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
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )

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
                            text = "Добро пожаловать в BeatRate",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Запишитесь на студийную сессию всего в пару кликов!\n" +
                                    "Не упустите возможность создать трек своей мечты.\n" +
                                    "Начнём прямо сейчас!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { navController.navigate("LoginScreen") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4682B4)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Начать", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
