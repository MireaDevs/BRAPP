package ru.pro.beatrate.ui.Screens.AuthScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AuthScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text("Authentication", style = MaterialTheme.typography.titleLarge, color = Color(0xFF2D2362))
        Spacer(modifier = Modifier.height(8.dp))
        Text("Please enter the authentication code that we have sent to your email", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(4) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .width(64.dp)
                        .height(64.dp),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate("WelcomeScreen") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D2362)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Have not receive code?", color = Color.Gray)
    }
}