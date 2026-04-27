package es.ua.eps.filmoteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class FilmDetailComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Usa un drawable existente para evitar el error de referencia
        val posterRes = R.drawable.ic_launcher_foreground
        val title = "Blade Runner"
        val director = "Ridley Scott"
        val year = 1982
        val genre = "Sci-Fi"
        val format = "Blu-ray"
        val imdbUrl = "https://www.imdb.com/title/tt0083658/"
        val notes = getString(R.string.sample_notes)

        setContent {
            MaterialTheme {
                FilmDataScreen(
                    posterRes = posterRes,
                    title = title,
                    director = director,
                    year = year,
                    genre = genre,
                    format = format,
                    imdbUrl = imdbUrl,
                    notes = notes
                )
            }
        }
    }
}

@Composable
fun FilmDataScreen(
    posterRes: Int,
    title: String,
    director: String,
    year: Int,
    genre: String,
    format: String,
    imdbUrl: String,
    notes: String
) {
    val ctx = LocalContext.current
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(posterRes),
            contentDescription = stringResource(R.string.desc_poster),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(12.dp))
        Text(title, style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.directed_by, director))
        Text(stringResource(R.string.year_genre_format, year, genre, format))
        Spacer(Modifier.height(8.dp))
        Text(imdbUrl, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.label_notes), style = MaterialTheme.typography.labelLarge)
        Text(notes)

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.btn_imdb))
        }
    }
}
