package cz.cvut.fel.tlappka.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R
import kotlinx.android.synthetic.main.activity_sign_in.*


/*
Activity that handles login of a user
 */
class SignInActivity : AppCompatActivity() {

    private var TAG : String = "SignInActivity";
    private var REQUEST_SIGNUP : Int = 0;
    private lateinit var auth: FirebaseAuth

    private val RC_SIGN_IN = 1

    var googleSignInClient: GoogleSignInClient? = null
    lateinit var gso: GoogleSignInOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //firebase acc
        auth = FirebaseAuth.getInstance()

        //gooogle
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //find components
        val _loginButton : Button = findViewById(R.id.btn_login);
        val _signupLink : TextView = findViewById(R.id.link_signup);
        val _googleButton = sign_in_google_button;

        //set up listeners

        _loginButton.setOnClickListener{
            login();
        };

        _signupLink.setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish()
        }

        link_forgot_password.setOnClickListener{
            val intent = Intent(applicationContext, ForgotPasswordActivity::class.java);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }


        _googleButton.setOnClickListener{
            signInToGoogle();

        };


    }

/*
Function handles login
 */
    fun login() {
        val _emailText : EditText = findViewById(R.id.input_email);
        val _passwordText : EditText = findViewById(R.id.input_password);
        val _loginButton : Button = findViewById(R.id.btn_login);

    //if user data are ok
        if (!validate()) {
            //throw error to user
            onLoginFailed()
            return
        }

    //create fancy process dialog
        val progressDialog = ProgressDialog(
            this@SignInActivity,
            R.style.ThemeOverlay_MaterialComponents_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Ověřování...")
        progressDialog.show()
        val email = _emailText.text.toString()
        val password = _passwordText.text.toString()
        Handler().postDelayed(
            {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, "Úspěšně přihlášen", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, "Přihlášení se nezdařilo", Toast.LENGTH_LONG).show()
                    }
                })

                //If datas match some data in databse then call onLoginSucces
                //onLoginSuccess()

                // If not clal onLoginFailed
                // onLoginFailed();
                progressDialog.dismiss()
            }, 3000
        )
    }

/*
If user is logged keep him logged
 */
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) { // TODO: Need to check if user didnt signed out
// By default we just want to  finish the Activity and log them in automatically
                finish()
            }
        }

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult (task)
        }else {
            Toast.makeText(this, "Problem in execution order :(", Toast.LENGTH_LONG).show()
        }
    }



    override fun onBackPressed() { // disable going back to the MainActivity
        moveTaskToBack(true)
    }

    /**
     * If success start Main activity
     */
    fun onLoginSuccess() {
        val _loginButton : Button = findViewById(R.id.btn_login);
        _loginButton.isEnabled = true
        intent  = Intent(this, MainActivity::class.java);
        startActivity(intent);
        finish()
    }

    /**
     * On error show it to user
     */
    fun onLoginFailed() {
        val _loginButton : Button = findViewById(R.id.btn_login);
        Toast.makeText(baseContext, "Přihlášení selhalo", Toast.LENGTH_LONG).show()
        _loginButton.isEnabled = true
    }

    /**
     * Validate all data from user
     */
    fun validate(): Boolean {
        val _emailText : EditText = findViewById(R.id.input_email);
        val _passwordText : EditText = findViewById(R.id.input_password);
        var valid = true
        val email = _emailText.text.toString()
        val password = _passwordText.text.toString()
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.error = resources.getString(R.string.activity_email_text_error);
            valid = false
        } else {
            _emailText.error = null
        }
        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            _passwordText.error = resources.getString(R.string.activity_password_text_error);
            valid = false
        } else {
            _passwordText.error = null
        }
        return valid
    }


    override fun onStart() {
        super.onStart()
        if(auth.currentUser == null){
        }else{
            Toast.makeText(this, "Úspěšně přihlášen jako " + auth.currentUser!!.email, Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Úspěšně přihlášen přes Google jako " + alreadyloggedAccount.email, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
        }
    }

    private fun configureGoogleClient() { // Configure Google Sign In
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // for the requestIdToken, this is in the values.xml file that
// is generated from your google-services.json
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    fun signInToGoogle() {
        val signInIntent: Intent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleResult (completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            Toast.makeText(this, "Přihlášen přes google jako " + account!!.email, Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }


}
