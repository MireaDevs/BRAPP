package ru.pro.beatrate.ui.Screens.Content

import android.app.TimePickerDialog
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.pro.beatrate.data.data_store.UserPreferences
import ru.pro.beatrate.data.models.Device
import ru.pro.beatrate.data.models.Service
import ru.pro.beatrate.data.models.Session
import ru.pro.beatrate.data.objects.booking.ConstantsOfDevices
import ru.pro.beatrate.data.objects.booking.ConstantsOfServices
import ru.pro.beatrate.domain.beatrate_backend.instance.RetrofitInstance
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun BookingScreen(navController: NavController) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf("") }
    var selectedHours by remember { mutableStateOf(3) }

    var vocalRecording by remember { mutableStateOf(false) }
    var arrangements by remember { mutableStateOf(false) }
    var mixingAndMastering by remember { mutableStateOf(false) }

    var neumann by remember { mutableStateOf(false) }
    var shureSM by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text("Выберите дату записи", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        DatePickerFieldToModal(selectedDate = selectedDate,
            onDateSelected = { selectedDate = it })
        Spacer(modifier = Modifier.height(16.dp))
        Text("Выберите время записи", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        TimePickerContent(
            selectedTime = selectedTime,
            onTimeSelected = { selectedTime = it })

        Spacer(modifier = Modifier.height(16.dp))
        Text("Выберите услугу", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            ServiceCheckBox(ConstantsOfServices.ServiceItems[0], vocalRecording) { vocalRecording = it }
            ServiceCheckBox(ConstantsOfServices.ServiceItems[1], arrangements) { arrangements = it }
            ServiceCheckBox(ConstantsOfServices.ServiceItems[2], mixingAndMastering) { mixingAndMastering = it }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Выберите оборудование", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            DeviceCheckBox(ConstantsOfDevices.DeviceItems[0], neumann) { neumann = it }
            DeviceCheckBox(ConstantsOfDevices.DeviceItems[1], shureSM) { shureSM = it }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Выберите длительность сессии", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        DurationSelector(selectedHours) { selectedHours = it }

        Spacer(modifier = Modifier.height(16.dp))
        val context = LocalContext.current
        val prefs = remember { UserPreferences(context) }
        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = {
                coroutineScope.launch {
                    val username = prefs.usernameFlow.firstOrNull() ?: ""
                    val token = prefs.tokenFlow.firstOrNull()?.let { "Bearer $it" } ?: ""

                    val session = Session(
                        username = username,
                        date = selectedDate?.let { convertMillisToDate(it) } ?: "",
                        time = selectedTime,
                        arrangements = arrangements,
                        mixing_and_mastering = mixingAndMastering,
                        vocal_recording = vocalRecording,
                        neumann = neumann,
                        shuresm = shureSM,
                        status = "В ожидании",
                        duration = selectedHours
                    )

                    try {
                        val response = RetrofitInstance.api.createBooking(token, session)
                        if (response.isSuccessful) {
                            println("Бронь успешно создана: $session")
                            // Можно показать Toast или навигацию на другой экран
                        } else {
                            println("Ошибка при создании брони: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        println("Ошибка сети: ${e.localizedMessage}")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать бронь!")
        }

    }
}

@Composable
fun DatePickerFieldToModal(
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("Дата записи") },
        placeholder = { Text("ДД/ММ/ГГГГ") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (up != null) showModal = true
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                onDateSelected(it)
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun TimePickerContent(
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    var showPicker by remember { mutableStateOf(false) }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            val hour12 = if (selectedHour % 12 == 0) 12 else selectedHour % 12
            val amPm = if (selectedHour < 12) "AM" else "PM"
            val formattedTime = String.format("%d:%02d %s", hour12, selectedMinute, amPm)
            onTimeSelected(formattedTime)
        },
        hour,
        minute,
        false
    )

    if (showPicker) {
        timePickerDialog.show()
        showPicker = false
    }

    OutlinedTextField(
        value = selectedTime,
        onValueChange = { },
        label = { Text("Время записи") },
        placeholder = { Text("ЧЧ:ММ") },
        trailingIcon = {
            Icon(Icons.Default.AccessTime, contentDescription = "Выбрать время")
        },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (up != null) showPicker = true
                }
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberDatePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                state.selectedDateMillis?.let(onDateSelected)
            }) {
                Text("Выбрать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        title = {
            Text("Выбор даты")
        },
        text = {
            DatePicker(state = state)
        }
    )
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun DurationSelector(selectedHours: Int, onSelect: (Int) -> Unit) {
    val options = listOf(3, 5, 7)
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { hours ->
            val isSelected = hours == selectedHours
            Button(
                onClick = { onSelect(hours) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("$hours ч")
            }
        }
    }
}

@Composable
fun ServiceCheckBox(service: Service, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Column(
        modifier = Modifier.width(screenWidth),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(service.photo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(maxHeight = 300.dp),
            contentScale = ContentScale.Fit
        )
        Text(service.name, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Выбрать эту услугу ->")
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
fun DeviceCheckBox(device: Device, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Column(
        modifier = Modifier.width(screenWidth),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(device.photo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(maxHeight = 300.dp),
            contentScale = ContentScale.Fit
        )
        Text(device.name, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Выбрать это оборудование ->")
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
