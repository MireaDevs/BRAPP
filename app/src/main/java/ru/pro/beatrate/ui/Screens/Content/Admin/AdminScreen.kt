package ru.pro.beatrate.ui.Screens.Content.Admin

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.pro.beatrate.data.data_store.UserPreferences
import android.widget.Toast

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.pro.beatrate.domain.beatrate_backend.models.News
import ru.pro.beatrate.data.models.SessionResponse
import ru.pro.beatrate.domain.beatrate_backend.instance.RetrofitInstance
import ru.pro.beatrate.domain.beatrate_backend.models.NewsResponse
import ru.pro.beatrate.ui.Screens.Content.SessionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    var bookings by remember { mutableStateOf<List<SessionResponse>>(emptyList()) }
    var newsTitle by remember { mutableStateOf("") }
    var newsContent by remember { mutableStateOf("") }
    var newsList by remember { mutableStateOf<List<NewsResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = prefs.tokenFlow.firstOrNull()?.let { "Bearer $it" } ?: return@launch
            try {
                bookings = RetrofitInstance.api.getAllBookings(token)
                newsList = RetrofitInstance.api.getAllNews()
            } catch (e: Exception) {
                println("Ошибка при загрузке: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Админ панель") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Все записи", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else if (bookings.isEmpty()) {
                Text("Нет записей.")
            } else {
                bookings.forEach { session ->
                    SessionCard(
                        session = session,
                        onDetailsClick = {},
                        onDelete = {
                            coroutineScope.launch {
                                val token = prefs.tokenFlow.firstOrNull()?.let { "Bearer $it" } ?: return@launch
                                try {
                                    val response = RetrofitInstance.api.deleteBooking(token, session.id)
                                    if (response.isSuccessful) {
                                        bookings = bookings.filterNot { it.id == session.id }
                                    }
                                } catch (e: Exception) {
                                    println("Ошибка при удалении: ${e.localizedMessage}")
                                }
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

            Text("Добавить новость", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = newsTitle,
                onValueChange = { newsTitle = it },
                label = { Text("Заголовок") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = newsContent,
                onValueChange = { newsContent = it },
                label = { Text("Описание") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        val token = prefs.tokenFlow.firstOrNull()?.let { "Bearer $it" } ?: return@launch
                        try {
                            val news = News(title = newsTitle, content = newsContent)
                            val response = RetrofitInstance.api.createNews(token, news)
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Новость добавлена", Toast.LENGTH_SHORT).show()
                                newsTitle = ""
                                newsContent = ""
                                newsList = RetrofitInstance.api.getAllNews() // обновление списка
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Ошибка: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Добавить новость")
            }

            Spacer(Modifier.height(32.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

            Text("Новости", style = MaterialTheme.typography.headlineSmall)
            if (newsList.isEmpty()) {
                Text("Нет новостей")
            } else {
                newsList.forEach { news ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = news.title, style = MaterialTheme.typography.titleMedium)
                            Text(text = news.content, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                coroutineScope.launch {
                                    val token = prefs.tokenFlow.firstOrNull()?.let { "Bearer $it" } ?: return@launch
                                    try {
                                        val response = RetrofitInstance.api.deleteNews(token, news.id)
                                        if (response.isSuccessful) {
                                            newsList = newsList.filterNot { it.id == news.id }
                                            Toast.makeText(context, "Новость удалена", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Ошибка: ${response.code()}", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Ошибка удаления: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }) {
                                Text("Удалить")
                            }
                        }
                    }
                }
            }
        }
    }
}
