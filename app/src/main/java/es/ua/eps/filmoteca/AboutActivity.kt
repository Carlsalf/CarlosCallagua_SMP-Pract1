package es.ua.eps.filmoteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button as M3Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn

enum class Mode { Layouts, Compose }

class AboutActivity : AppCompatActivity() {

    private val mode = Mode.Compose
    private val personalUrl = "https://github.com/Carlsalf"
    private val soporteEmail = "mailto:carls.alfred9@gmail.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Acerca de"
        initUI()
    }

    private fun getGoogleUser(): Pair<String, String> {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val name = account?.displayName ?: "Usuario no autenticado"
        val email = account?.email ?: "Email no disponible"
        return Pair(name, email)
    }

    private fun initUI() {
        when (mode) {
            Mode.Layouts -> initLayouts()
            Mode.Compose -> initCompose()
        }
    }

    private fun initLayouts() {
        setContentView(R.layout.activity_about)

        val (name, email) = getGoogleUser()

        findViewById<TextView>(R.id.tvAutor).text =
            "${getString(R.string.autor_texto)}\n\nUsuario Google:\n$name\n$email"

        findViewById<Button>(R.id.btnWeb).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(personalUrl)))
        }

        findViewById<Button>(R.id.btnSoporte).setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(soporteEmail)))
        }

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }

    private fun initCompose() {
        val (name, email) = getGoogleUser()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AboutScreenCompose(
                        googleName = name,
                        googleEmail = email,
                        onOpenWeb = {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(personalUrl)))
                        },
                        onSoporte = {
                            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(soporteEmail)))
                        },
                        onVolver = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun AboutScreenCompose(
    googleName: String,
    googleEmail: String,
    onOpenWeb: () -> Unit,
    onSoporte: () -> Unit,
    onVolver: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        contentPadding = PaddingValues(
            top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding() + 20.dp,
            bottom = 28.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(id = R.string.autor_texto),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Usuario Google",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = googleName,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = googleEmail,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(26.dp))

            Image(
                painter = painterResource(id = R.drawable.carlosalfredo),
                contentDescription = stringResource(id = R.string.autor_texto),
                modifier = Modifier.size(160.dp)
            )

            Spacer(Modifier.height(36.dp))

            M3Button(onClick = onOpenWeb, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.btn_web))
            }

            Spacer(Modifier.height(14.dp))

            M3Button(onClick = onSoporte, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.btn_soporte))
            }

            Spacer(Modifier.height(14.dp))

            M3Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.btn_volver))
            }
        }
    }
}