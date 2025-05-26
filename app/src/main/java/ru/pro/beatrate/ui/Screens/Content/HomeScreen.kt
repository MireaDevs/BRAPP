package ru.pro.beatrate.ui.Screens.Content

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.pro.beatrate.R
import ru.pro.beatrate.data.data_store.UserPreferences
import ru.pro.beatrate.data.objects.bottomMenu.BottomNavItems
import ru.pro.beatrate.data.models.Session
import ru.pro.beatrate.data.models.SessionResponse
import ru.pro.beatrate.domain.beatrate_backend.instance.RetrofitInstance
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import ru.pro.beatrate.domain.beatrate_backend.models.NewsResponse
import ru.pro.beatrate.domain.devices_api.data.EquipmentItem


@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    var selectedSession by remember { mutableStateOf<Session?>(null) }
    var newsList by remember { mutableStateOf<List<NewsResponse>>(emptyList()) }

    var selectedItemIndex by remember { mutableStateOf(0) }
    val bottomNavItems = BottomNavItems.BottomNavItems

    var upcomingSessions by remember { mutableStateOf<List<SessionResponse>>(emptyList()) }
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
    LaunchedEffect("load_news") {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getAllNews()
                newsList = response
            } catch (e: Exception) {
                println("Ошибка загрузки новостей: ${e.localizedMessage}")
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val role = prefs.roleFlow.firstOrNull()
            if (role == "ADMIN") {
                navController.navigate("AdminScreen")
                return@launch
            }
            // иначе продолжаем загрузку сессий
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
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        },
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
                }
                if (isLoading) {
                    item { Text("Загрузка...") }
                } else if (upcomingSessions.isEmpty()) {
                    item { Text("Нет активных записей") }
                } else {
                    items(upcomingSessions) { session ->
                        SessionCard(
                            session = session,
                            onDetailsClick = {
                                val sessionJson = Json.encodeToString(session)
                                navController.navigate("SessionDetail/${sessionJson}")
                            },
                            onDelete = {
                                coroutineScope.launch {
                                    try {
                                        val token = prefs.tokenFlow.firstOrNull()?.let { "Bearer $it" } ?: return@launch
                                        val response = RetrofitInstance.api.deleteBooking(token, session.id)
                                        if (response.isSuccessful) {
                                            upcomingSessions = upcomingSessions.filter { it.id != session.id }
                                        } else {
                                            println("Ошибка при удалении: ${response.code()}")
                                        }
                                    } catch (e: Exception) {
                                        println("Ошибка удаления: ${e.localizedMessage}")
                                    }
                                }
                            }
                        )
                    }

                }
                item {
                    SectionTitle(title = "Оборудование студии")
                    RowEquipment(navController)
                }
                item {
                    SectionTitle(title = "Сайт студии")
                    WebsiteLink()
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

@Composable
fun SessionCard(
    session: SessionResponse,
    onDetailsClick: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${session.date}, ${session.time}")
            Text(text = "Статус: ${session.status}")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onDelete) {
                    Text("Отменить")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDetailsClick) {
                    Text("Подробнее")
                }
            }
        }
    }
}




// Карточка новости/акции
@Composable
fun NewsCard(newsItem: NewsResponse) {
    val title = newsItem.title ?: "(без заголовка)"
    val description = newsItem.content ?: "(без описания)"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Composable
fun WebsiteLink() {
    val context = LocalContext.current
    val url = "https://beatrate.pro"

    Text(
        text = "Перейти на сайт BeatRate →",
        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .padding(top = 16.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
    )
}
@Composable
fun MapPlaceholder() {
    val context = LocalContext.current
    val mapUrl = "https://yandex.ru/maps/-/CHFdnZ4g"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.map),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
                        context.startActivity(intent) }
                    ,
                contentScale = ContentScale.Crop
            )
        }
    }
}
@Composable
fun RowEquipment(
    nav: NavController,
    vm: EquipmentViewModel = viewModel()
){
    val uiState by vm.ui.collectAsState()
    Column(
        modifier = Modifier

            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        /* ---------- Горизонтальный список ---------- */
        when (uiState) {
            EquipmentUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
            }
            is EquipmentUiState.Error -> {
                Text(
                    text = "Не удалось загрузить оборудование",
                    modifier = Modifier.padding(32.dp)
                )
            }
            is EquipmentUiState.Success -> {
                val list = (uiState as EquipmentUiState.Success).list
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(list, key = EquipmentItem::id) { item ->
                        MiniEquipmentCard(item) {
                            nav.navigate("equipment")      // переход на экран поиска
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun MiniEquipmentCard(
    item: EquipmentItem,
    onClick: () -> Unit
) {
    Image(
        painter = rememberAsyncImagePainter(item.image),
        contentDescription = item.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    )
}