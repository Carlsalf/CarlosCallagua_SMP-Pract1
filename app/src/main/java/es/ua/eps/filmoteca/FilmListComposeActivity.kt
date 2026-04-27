package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class FilmListComposeActivity : ComponentActivity() {

    private var refreshKey by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    override fun onResume() {
        super.onResume()
        refreshKey++
    }

    private fun render() {
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    FilmList(
                        refreshKey = refreshKey,
                        films = FilmDataSource.films,
                        onClick = { index ->
                            val i = Intent(this, FilmDataActivity::class.java)
                            i.putExtra("film_index", index)
                            startActivity(i)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilmList(
    refreshKey: Int,
    films: List<Film>,
    onClick: (Int) -> Unit
) {
    val visibleFilms = remember(refreshKey, films.size) {
        films.toList()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(visibleFilms) { index, film ->
            FilmRow(
                film = film,
                onClick = { onClick(index) }
            )
        }
    }
}

@Composable
private fun FilmRow(
    film: Film,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val poster = if (film.imageResId != 0) {
            film.imageResId
        } else {
            R.drawable.ic_launcher_foreground
        }

        Image(
            painter = painterResource(poster),
            contentDescription = film.title ?: "",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = film.title ?: "<Sin título>",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = film.director ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = yearText(film.year),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

private fun yearText(year: Int): String =
    if (year != 0) "$year" else "—"