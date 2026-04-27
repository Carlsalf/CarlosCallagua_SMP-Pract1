package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 1001
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout programático simple (válido para la práctica)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(48, 48, 48, 48)
        }

        val signInButton = SignInButton(this).apply {
            setSize(SignInButton.SIZE_STANDARD)
            setOnClickListener { signIn() }
        }

        root.addView(signInButton)
        setContentView(root)

        //  CONFIGURACIÓN CORRECTA PARA LA PRÁCTICA
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Si ya hay sesión previa, entra directo
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            goToMain()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

                // Logs útiles (para la práctica)
                Log.d(TAG, "id: ${account.id}")
                Log.d(TAG, "name: ${account.displayName}")
                Log.d(TAG, "email: ${account.email}")

                Toast.makeText(
                    this,
                    "Bienvenido ${account.displayName}",
                    Toast.LENGTH_SHORT
                ).show()

                goToMain()

            } catch (e: ApiException) {
                Log.e(TAG, "Google Sign-In failed. Code: ${e.statusCode}", e)

                Toast.makeText(
                    this,
                    "Error en Google Sign-In: ${e.statusCode}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}