package com.example.jetpackcomposetest

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposetest.model.Interval
import com.example.jetpackcomposetest.model.Task
import com.example.jetpackcomposetest.ui.theme.JetpackComposeTestTheme
import kotlinx.coroutines.launch
import org.apache.commons.lang3.time.DurationFormatUtils
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val tasks =
                remember { mutableStateListOf<Task>() }
            JetpackComposeTestTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Text(
                                "JetpackComposeText",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(16.dp),
                            )
                            Divider()
                            NavigationDrawerItem(
                                label = { Text("Settings") },
                                selected = false,
                                onClick = { /*TODO*/ },
                                icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings") },
                            )
                            NavigationDrawerItem(
                                label = { Text("About") },
                                selected = false,
                                onClick = { /*TODO*/ },
                                icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "About") },
                            )
                        }
                    },
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors =
                                    topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                    ),
                                title = { Text("JetpackComposeTest") },
                                navigationIcon =
                                    {
                                        IconButton(onClick = {
                                            scope.launch {
                                                drawerState.apply {
                                                    if (isOpen) {
                                                        close()
                                                    } else {
                                                        open()
                                                    }
                                                }
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Menu,
                                                contentDescription = "Navigation Menu",
                                            )
                                        }
                                    },
                            )
                        },
                        floatingActionButton = {
                            FloatingActionButton(onClick = {
                                tasks.add(
                                    Task(
                                        tasks.count(),
                                        "My Task ${tasks.count()}",
                                        "Unscheduled",
                                        Interval(
                                            Instant.parse("2024-02-24T14:00:00.00Z"),
                                            Instant.parse("2024-02-24T18:00:00.00Z"),
                                        ),
                                        Duration.ofHours(2),
                                        1000.0,
                                    ),
                                )
                            }) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Task")
                            }
                        },
                    ) { innerPadding ->
                        LazyColumn(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .padding(all = 8.dp),
                        ) {
                            items(
                                count = tasks.count(),
                                key = { tasks[it].id },
                                itemContent = {
                                    TaskCard(tasks[it], Modifier.fillMaxWidth())
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposeTestTheme {
        Greeting("Android")
    }
}

@Composable
fun TaskCard(
    task: Task,
    modifier: Modifier = Modifier,
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val startTime =
        LocalDateTime.ofInstant(task.interval.start, ZoneOffset.UTC)
            .format(timeFormatter)

    val endTime =
        LocalDateTime.ofInstant(task.interval.end, ZoneOffset.UTC)
            .format(timeFormatter)

    val duration = DurationFormatUtils.formatDuration(task.duration.toMillis(), "HH:mm", true)

    Card(
        modifier =
            modifier
                .animateContentSize()
                .padding(top = 4.dp, bottom = 4.dp),
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        Row(
            modifier =
                Modifier
                    .padding(all = 8.dp)
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
        ) {
            Column {
                val painter = painterResource(R.drawable.baseline_task_24)
                Image(
                    painter = painter,
                    contentDescription = "A task",
                    modifier =
                        Modifier
                            .weight(1f, fill = false)
                            .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                            .padding(all = 4.dp)
                            .fillMaxSize(),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier =
                    Modifier
                        .weight(0.1f)
                        .fillMaxWidth(),
            ) {
                if (!isExpanded) {
                    Text(task.name, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(task.status, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("$startTime - $endTime ($duration)", style = MaterialTheme.typography.bodySmall)
                } else {
                    Text(task.name, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                }
            }

            Column {
                val painter =
                    painterResource(
                        if (isExpanded) {
                            R.drawable.baseline_expand_less_24
                        } else {
                            R.drawable.baseline_expand_more_24
                        },
                    )

                Image(
                    painter = painter,
                    contentDescription = (
                        if (isExpanded) {
                            "Compact"
                        } else {
                            "Expand"
                        }
                    ),
                    modifier =
                        Modifier
                            .weight(1f, fill = false)
                            .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                            .padding(all = 4.dp)
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable { isExpanded = !isExpanded },
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                )
            }
        }

        if (isExpanded) {
            Row(modifier = Modifier.padding(all = 8.dp)) {
                Column {
                    Text("Status: ${task.status}")
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Interval: $startTime - $endTime")
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Duration: $duration")
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Effect: ${task.effect}W")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    JetpackComposeTestTheme {
        TaskCard(
            Task(
                0,
                "Test Task",
                "Unscheduled",
                Interval(Instant.parse("2024-02-24T14:00:00.00Z"), Instant.parse("2024-02-24T18:00:00.00Z")),
                Duration.ofHours(2),
                1000.0,
            ),
        )
    }
}
