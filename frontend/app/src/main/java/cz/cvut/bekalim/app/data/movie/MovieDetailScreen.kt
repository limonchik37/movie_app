package cz.cvut.bekalim.app.data.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel,
    onBack: () -> Unit,
    onAddReview: () -> Unit
) {
    // 1) Подписываемся на UI-стейт
    val ui by viewModel.ui.collectAsState()

    Scaffold(
        // 2) Шапка с кнопкой «назад» и меню
        topBar = {
            SmallTopAppBar(
                title = { Text(ui.movie?.title.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* overflow menu если нужно */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },

        // 3) Плавающая кнопка «Add Review»
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Review") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                onClick = onAddReview
            )
        }
    ) { paddingValues ->
        // 4) Сам контент внутри Scaffold
        Box(Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            when {
                ui.isLoading -> {
                    // прелоадер по центру
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                ui.error != null -> {
                    // текст ошибки
                    Text(
                        text = ui.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                ui.movie != null -> {
                    // 5) Если фильм удачно подгрузился — показываем детали + отзывы
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            // Постер
                            AsyncImage(
                                model = ui.movie!!.photoPath,
                                contentDescription = ui.movie!!.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(Modifier.height(8.dp))

                            // Название + год + жанры
                            Text(
                                text = ui.movie!!.title,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = "Year: ${ui.movie!!.year}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Genres: ${ui.movie!!.genre.joinToString()}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(Modifier.height(16.dp))
                            Divider()
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Reviews:",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        if (ui.reviews.isEmpty()) {
                            item {
                                Text("No reviews yet", style = MaterialTheme.typography.bodySmall)
                            }
                        } else {
                            items(ui.reviews) { review ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "User #${review.userId}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = review.text,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    review.photoPath?.let { url ->
                                        Spacer(Modifier.height(8.dp))
                                        AsyncImage(
                                            model = url,
                                            contentDescription = "Review photo",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(180.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                        )
                                    }
                                }
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}