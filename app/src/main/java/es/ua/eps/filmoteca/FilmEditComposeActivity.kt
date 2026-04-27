package es.ua.eps.filmoteca

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
class FilmEditComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FilmEditComposeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmEditComposeScreen() {
    val context = LocalContext.current

    // Cargar arrays de /res/values/arrays.xml
    val genreOptions = stringArrayResource(id = R.array.genres).toList()
    val formatOptions = stringArrayResource(id = R.array.formats).toList()

    // Estados del formulario
    var title by rememberSaveable { mutableStateOf("") }
    var director by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var imdb by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    var selectedGenre by rememberSaveable { mutableStateOf(genreOptions.firstOrNull().orEmpty()) }
    var selectedFormat by rememberSaveable { mutableStateOf(formatOptions.firstOrNull().orEmpty()) }

    var genreExpanded by remember { mutableStateOf(false) }
    var formatExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Título") },
            singleLine = true
        )

        OutlinedTextField(
            value = director,
            onValueChange = { director = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Director") },
            singleLine = true
        )

        OutlinedTextField(
            value = year,
            onValueChange = { year = it.filter(Char::isDigit).take(4) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Año de estreno") },
            singleLine = true
        )

        // --- Género ---
        ExposedDropdownMenuBox(
            expanded = genreExpanded,
            onExpandedChange = { genreExpanded = !genreExpanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedGenre,
                onValueChange = {},
                label = { Text("Selecciona género") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = genreExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            DropdownMenu(
                expanded = genreExpanded,
                onDismissRequest = { genreExpanded = false }
            ) {
                genreOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedGenre = option
                            genreExpanded = false
                        }
                    )
                }
            }
        }

        // --- Formato ---
        ExposedDropdownMenuBox(
            expanded = formatExpanded,
            onExpandedChange = { formatExpanded = !formatExpanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedFormat,
                onValueChange = {},
                label = { Text("Selecciona formato") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = formatExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            DropdownMenu(
                expanded = formatExpanded,
                onDismissRequest = { formatExpanded = false }
            ) {
                formatOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedFormat = option
                            formatExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = imdb,
            onValueChange = { imdb = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enlace a IMDB") },
            singleLine = true
        )

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            label = { Text("Notas") }
        )

        Button(
            onClick = {

                when {
                    title.isBlank() || director.isBlank() -> {
                        Toast.makeText(
                            context,
                            "Rellena al menos Título y Director",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    year.length != 4 -> {
                        Toast.makeText(
                            context,
                            "El año debe tener 4 dígitos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        Toast.makeText(
                            context,
                            "Guardado (demo): $title ($director)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
