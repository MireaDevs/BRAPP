package ru.pro.beatrate.ui.Screens.Content

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.pro.beatrate.data.data_store.UserPreferences
import ru.pro.beatrate.data.objects.bottomMenu.BottomNavItems
import ru.pro.beatrate.data.models.Session
import ru.pro.beatrate.domain.beatrate_backend.instance.RetrofitInstance


// Модель данных для новости/акции
data class NewsItem(val title: String, val description: String)


// Главная функция-компоновка экрана
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()

    var selectedItemIndex by remember { mutableStateOf(0) }
    val bottomNavItems = BottomNavItems.BottomNavItems

    var upcomingSessions by remember { mutableStateOf<List<Session>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = prefs.tokenFlow.firstOrNull()?.let { "Bearer $it" } ?: return@launch
            val username = prefs.usernameFlow.firstOrNull() ?: return@launch
            try {
                val response = RetrofitInstance.api.getBookingsByUser(token, username)
                upcomingSessions = response
            } catch (e: Exception) {
                println("Ошибка загрузки сессий: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = { selectedItemIndex = index },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp,
                    start = 16.dp, end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.padding(top = 30.dp))
                    SectionTitle(title = "Запись")
                    Button(
                        onClick = { navController.navigate("BookingScreen") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Text("Создать запись!")
                    }
                }
                if (isLoading) {
                    item { Text("Загрузка...") }
                } else if (upcomingSessions.isEmpty()) {
                    item { Text("Нет активных записей") }
                } else {
                    items(upcomingSessions) { session ->
                        SessionCard(session = session)
                    }
                }

                // 2. Блок "Услуги студии"
                item {
                    SectionTitle(title = "Услуги студии")
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                        items(studioServices) { service ->
//                            ServiceCard(serviceName = service)
//                        }
                    }
                }

                // 3. Блок "Ваши проекты"
                item {
                    SectionTitle(title = "Ваши проекты")
                }
//                items(projects) { projectName ->
//                    ProjectItemRow(projectName = projectName)
//                }
                item {
                    // Кнопка "Загрузить новый трек"
                    Button(
                        onClick = { /* TODO: Загрузить новый трек */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Загрузить новый трек")
                    }
                }

                // 4. Блок "Новое в студии"
                item {
                    SectionTitle(title = "Новое в студии")
                }
//                items(newsList) { news ->
//                    NewsCard(newsItem = news)
//                }

                // 5. Блок с геолокацией студии
                item {
                    SectionTitle(title = "Геолокация студии")
                }
                item {
                    MapPlaceholder()
                }
            }
        }
    )
}

// Заголовок раздела с общим стилем
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(bottom = 8.dp)  // нижний отступ после заголовка
    )
}

// Карточка для сессии (название, дата/время, студия, статус, кнопки)
@Composable
fun SessionCard(session: Session) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)  // отступ после каждой карточки
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${session.date}, ${session.time}")
            Text(text = "Статус: ${session.status}")
            Spacer(modifier = Modifier.height(8.dp))
            // Кнопки "Подробнее" и "Отменить" в строке
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Кнопка "Отменить" (OutlinedButton как пример второстепенной кнопки)
                OutlinedButton(
                    onClick = { /* TODO: Отменить сессию */ },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Отменить")
                }
                // Кнопка "Подробнее" (основная кнопка)
                Button(onClick = { /* TODO: Подробнее о сессии */ }) {
                    Text("Подробнее")
                }
            }
        }
    }
}

// Карточка услуги студии (горизонтальный список)
@Composable
fun ServiceCard(serviceName: String) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .width(120.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = serviceName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Строка с проектом и иконкой "поделиться"
@Composable
fun ProjectItemRow(projectName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),  // небольшой вертикальный отступ между проектами
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = projectName, modifier = Modifier.weight(1f))
        IconButton(onClick = { /* TODO: Поделиться проектом */ }) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Поделиться")
        }
    }
}

// Карточка новости/акции
@Composable
fun NewsCard(newsItem: NewsItem) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = newsItem.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = newsItem.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Заглушка для карты с геолокацией
@Composable
fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // В реальном приложении здесь можно использовать Composable от карт (например, Google Maps)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Карта",
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
        }
    }
}
