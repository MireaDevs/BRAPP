package ru.pro.beatrate.ui.Screens.AuthScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
fun ForgotPasswordScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text("Forgot password?", style = MaterialTheme.typography.titleLarge, color = Color(0xFF2D2362))
        Spacer(modifier = Modifier.height(8.dp))
        Text("Please enter your email for the password reset process", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate("AuthScreen") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D2362)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send", color = Color.White)
        }
    }
}