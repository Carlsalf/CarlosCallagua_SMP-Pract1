package es.ua.eps.filmoteca

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging
import es.ua.eps.filmoteca.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(this, "Notificaciones permitidas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notificaciones desactivadas", Toast.LENGTH_SHORT).show()
            }
        }

    private val googleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this, gso)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()
        printFcmToken()

        supportActionBar?.show()
        supportActionBar?.title = "Filmoteca"
        invalidateOptionsMenu()

        runCatching {
            ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
                insets
            }
        }

        fun launchOrToast(intent: Intent) {
            runCatching { startActivity(intent) }
                .onFailure {
                    Toast.makeText(
                        this,
                        "No pude abrir la pantalla. Revisa el Manifest / clase.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        binding.btnAbout.setOnClickListener {
            launchOrToast(Intent(this, AboutActivity::class.java))
        }

        binding.btnListaXml.setOnClickListener {
            launchOrToast(Intent(this, FilmListActivity::class.java))
        }

        binding.btnListaCompose.setOnClickListener {
            try {
                startActivity(Intent(this, FilmListComposeActivity::class.java))
            } catch (t: Throwable) {
                Log.e("NAV", "Fallo al abrir FilmListComposeActivity", t)
                Toast.makeText(
                    this,
                    "Error al abrir Lista (Compose): ${t::class.simpleName}: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.btnDetailXml.setOnClickListener {
            val i = Intent(this, FilmDataActivity::class.java).apply {
                putExtra("film_index", 0)
            }
            launchOrToast(i)
        }

        binding.btnEditXml.setOnClickListener {
            val i = Intent(this, FilmEditActivity::class.java).apply {
                putExtra("film_index", 0)
            }
            launchOrToast(i)
        }

        binding.btnDetailCompose.setOnClickListener {
            launchOrToast(Intent(this, FilmDetailComposeActivity::class.java))
        }

        binding.btnEditCompose.setOnClickListener {
            launchOrToast(Intent(this, FilmEditComposeActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.action_about -> {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }

        R.id.action_web -> {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Carlsalf")))
            true
        }

        R.id.action_share -> {
            val i = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Prueba la app Filmoteca")
            }
            startActivity(Intent.createChooser(i, "Compartir vía"))
            true
        }

        R.id.action_close_session -> {
            closeSession()
            true
        }

        R.id.action_disconnect_account -> {
            disconnectAccount()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS

            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(permission)
            } else {
                Log.d("FCM_PERMISSION", "POST_NOTIFICATIONS ya concedido")
            }
        }
    }

    private fun printFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM_TOKEN", "Error obteniendo token", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                Log.d("FCM_TOKEN", "Token: $token")
            }
    }

    private fun closeSession() {
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            goToLogin()
        }
    }

    private fun disconnectAccount() {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            Toast.makeText(this, "Cuenta desconectada", Toast.LENGTH_SHORT).show()
            goToLogin()
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}