package ru.pro.beatrate.ui.Screens.Content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import ru.pro.beatrate.data.models.SessionResponse


@Composable
fun SessionDetailScreen(session: SessionResponse) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Дата: ${session.date}", style = MaterialTheme.typography.titleLarge)
        Text("Время: ${session.time}")
        Text("Длительность: ${session.duration} ч.")
        Text("Статус: ${session.status}")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Услуги:")
        if (session.vocal_recording) Text("- Запись вокала")
        if (session.arrangements) Text("- Аранжировки")
        if (session.mixing_and_mastering) Text("- Сведение и мастеринг")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Оборудование:")
        if (session.neumann) Text("- Neumann")
        if (session.shuresm) Text("- Shure SM")
    }
}