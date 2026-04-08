package cz.cvut.bekalim.app.data.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MoviesScreen(
    moviesVM: MoviesViewModel,
    onSelect: (Long) -> Unit
) {
    val ui by moviesVM.ui.collectAsState()
    if (ui.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (ui.error != null) {
        Text(ui.error!!, color = Color.Red, modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn(Modifier.fillMaxSize()) {
            items(ui.movies) { movie ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(movie.id!!) }
                        .padding(8.dp)
                ) {
                    AsyncImage(
                        model = movie.photoPath ?: "https://alimmovie.s3.us-east-005.backblazeb2.com/nomovie.jpg",
                        contentDescription = movie.title,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(movie.title, style = MaterialTheme.typography.titleMedium)
                        Text(movie.year.toString(), style = MaterialTheme.typography.bodySmall)
                    }
                }
                Divider()
            }
        }
    }
}
