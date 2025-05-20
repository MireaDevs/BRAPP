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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.pro.beatrate.data.objects.bottomMenu.BottomNavItems
import ru.pro.beatrate.data.models.Session


// Модель данных для новости/акции
data class NewsItem(val title: String, val description: String)


// Главная функция-компоновка экрана
@Composable
fun HomeScreen(navController: NavController) {
    // Состояние для выбранного пункта нижней навигации (по умолчанию первый)
    var selectedItemIndex by remember { mutableStateOf(0) }
    val bottomNavItems = BottomNavItems.BottomNavItems

    // Мок-данные для списков
    val upcomingSessions = listOf(
        Session( "21 мая", "16:00", true,true,true,true,true, "Подтверждено",7),
        Session( "25 мая", "14:00", true,true,true,true,true, "В ожидании",3)
    )
    val studioServices = listOf("Запись вокала", "Живые инструменты", "Сведение и мастеринг", "Аренда зала", "Видеосъёмка")
    val projects = listOf("Demo_2025_May", "LiveSession_June2025")
    val newsList = listOf(
        NewsItem("Мастер-класс по гитаре", "Приходите 30 мая — скидка 20% для участников"),
        NewsItem("Скидка на аренду зала", "До конца месяца аренда зала со скидкой 15%")
    )

    // Scaffold с нижней панелью навигации и основным содержимым
    Scaffold(
        bottomBar = {
            // Нижняя панель навигации Material3
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
            // Основной контент экрана: прокручиваемый столбец (LazyColumn) со всеми секциями
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp,  // учитываем отступ под навигацией
                    start = 16.dp, end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)  // расстояние между секциями
            ) {
                // 1. Блок "Ваши ближайшие сессии"
                item {
                    Spacer(modifier = Modifier.padding(top = 30.dp))
                    SectionTitle(title = "Запись")
                    Button(
                        onClick = { navController.navigate("BookingScreen") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Создать запись!")
                    }
                }
                items(upcomingSessions) { session ->
                    SessionCard(session = session)
                }

                // 2. Блок "Услуги студии"
                item {
                    SectionTitle(title = "Услуги студии")
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(studioServices) { service ->
                            ServiceCard(serviceName = service)
                        }
                    }
                }

                // 3. Блок "Ваши проекты"
                item {
                    SectionTitle(title = "Ваши проекты")
                }
                items(projects) { projectName ->
                    ProjectItemRow(projectName = projectName)
                }
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
                items(newsList) { news ->
                    NewsCard(newsItem = news)
                }

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
