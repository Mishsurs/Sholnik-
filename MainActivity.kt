package ru.shkolnikplus.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF172B4D)
private val Blue = Color(0xFF356AE6)
private val Bg = Color(0xFFF4F6FA)
private val Muted = Color(0xFF78849A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SchoolApp() }
    }
}

private enum class Tab(val title: String, val icon: ImageVector) {
    Home("Главная", Icons.Default.Home),
    Grades("Оценки", Icons.Default.Grade),
    Schedule("Расписание", Icons.Default.CalendarMonth),
    Homework("Задания", Icons.Default.MenuBook)
}

@Composable
fun SchoolApp() {
    var selected by remember { mutableStateOf(Tab.Home) }
    MaterialTheme(
        colorScheme = lightColorScheme(primary = Blue, background = Bg, surface = Color.White)
    ) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    Tab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = selected == tab,
                            onClick = { selected = tab },
                            icon = { Icon(tab.icon, contentDescription = tab.title) },
                            label = { Text(tab.title, fontSize = 11.sp) }
                        )
                    }
                }
            }
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding)) {
                Header()
                when (selected) {
                    Tab.Home -> HomeScreen(
                        onGrades = { selected = Tab.Grades },
                        onSchedule = { selected = Tab.Schedule },
                        onHomework = { selected = Tab.Homework }
                    )
                    Tab.Grades -> GradesScreen()
                    Tab.Schedule -> ScheduleScreen()
                    Tab.Homework -> HomeworkScreen()
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        Modifier.fillMaxWidth().background(Navy).padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Школьник+", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text("Твой школьный помощник", color = Color(0xFFCBD7EA), fontSize = 12.sp)
        }
        Icon(Icons.Default.NotificationsNone, null, tint = Color.White)
    }
}

@Composable
private fun HomeScreen(onGrades: () -> Unit, onSchedule: () -> Unit, onHomework: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Text("СРЕДНИЙ БАЛЛ", color = Color(0xFFCBD7EA), fontSize = 12.sp)
                    Text("4,65", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
                    Text("Демонстрационные данные", color = Color(0xFFCBD7EA), fontSize = 11.sp)
                }
            }
        }
        item { Text("Мои разделы", color = Navy, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureCard("Оценки", Icons.Default.Grade, Modifier.weight(1f), onGrades)
                FeatureCard("Расписание", Icons.Default.CalendarMonth, Modifier.weight(1f), onSchedule)
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureCard("Домашние задания", Icons.Default.MenuBook, Modifier.weight(1f), onHomework)
                FeatureCard("Успеваемость", Icons.Default.ShowChart, Modifier.weight(1f), onGrades)
            }
        }
        item {
            Text("Сегодня", color = Navy, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Card(shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LessonRow("08:30", "Алгебра", "Кабинет 204")
                    HorizontalDivider(color = Bg)
                    LessonRow("09:25", "Русский язык", "Кабинет 118")
                    HorizontalDivider(color = Bg)
                    LessonRow("10:30", "История", "Кабинет 305")
                }
            }
        }
        item {
            Text("Подключение к дневнику", color = Navy, fontWeight = FontWeight.Bold)
            Text(
                "Пока это макет с примерами. Подключение реальных оценок появится только через официальный разрешённый способ.",
                color = Muted, fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun FeatureCard(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(112.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, null, tint = Blue, modifier = Modifier.size(26.dp))
            Text(title, color = Navy, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
    }
}

@Composable
private fun LessonRow(time: String, subject: String, room: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(time, color = Blue, fontWeight = FontWeight.Bold, modifier = Modifier.width(62.dp))
        Column {
            Text(subject, color = Navy, fontWeight = FontWeight.SemiBold)
            Text(room, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun GradesScreen() {
    val rows = listOf("Математика" to listOf("5", "4", "5"), "Русский язык" to listOf("4", "5"), "История" to listOf("5", "5", "4"), "Английский" to listOf("4", "5"))
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Оценки", color = Navy, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        item { Text("Средний балл · 4,65", color = Blue, fontWeight = FontWeight.Bold) }
        items(rows) { (subject, grades) ->
            Card(shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(subject, color = Navy, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        grades.forEach { grade ->
                            Surface(color = if (grade == "5") Color(0xFFE3F5EA) else Color(0xFFFFF2D9), shape = RoundedCornerShape(8.dp)) {
                                Text(grade, modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp), color = Navy, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
        item { DemoNote() }
    }
}

@Composable
private fun ScheduleScreen() {
    val lessons = listOf("08:30" to "Алгебра", "09:25" to "Русский язык", "10:30" to "История", "11:25" to "Физика", "12:20" to "Английский язык")
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Расписание", color = Navy, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        item { Text("Понедельник · пример", color = Muted) }
        items(lessons) { (time, subject) ->
            Card(shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(time, color = Blue, fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp))
                    Text(subject, color = Navy, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        item { DemoNote() }
    }
}

@Composable
private fun HomeworkScreen() {
    val tasks = listOf("Алгебра" to "№ 245, решить уравнения", "Русский язык" to "Упражнение 86", "История" to "Прочитать § 12")
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Домашние задания", color = Navy, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        items(tasks) { (subject, task) ->
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(subject, color = Blue, fontWeight = FontWeight.Bold)
                    Text(task, color = Navy)
                }
            }
        }
        item { DemoNote() }
    }
}

@Composable
private fun DemoNote() {
    Text("Это демонстрационный макет. Оценки, расписание и задания здесь вымышленные, не из школьного аккаунта.", color = Muted, fontSize = 12.sp)
}
