package cz.cvut.fel.tlappka.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.model.User
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.*


/*
Activity that handles login of a user
 */
class SignInActivity : AppCompatActivity() {
    private var REQUEST_SIGNUP : Int = 0;
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 1

    var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initFireBase()
        initLoginButton()
        initsignupLinkButton();
        initForgotPasswordButton();
        initSignInButton();
        initGoogleSignInClient();

    }

    private fun initFireBase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun initsignupLinkButton() {
        val _signupLink : TextView = findViewById(R.id.link_signup);
        _signupLink.setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish()
        }
    }

    private fun initLoginButton() {
        val _loginButton : Button = findViewById(R.id.btn_login);
        _loginButton.setOnClickListener{
            login();
        };
    }

    private fun initForgotPasswordButton() {
        val forgotPass : TextView= findViewById(R.id.link_forgot_password);
        forgotPass.setOnClickListener{
            val intent = Intent(applicationContext, ForgotPasswordActivity::class.java);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }
    }

    private fun initSignInButton() {
        val googleSignInButton: SignInButton = findViewById(R.id.sign_in_google_button)
        googleSignInButton.setOnClickListener { v: View? -> signIn() }
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    fun signIn() {
        val signInIntent: Intent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    fun login() {
        val _emailText : EditText = findViewById(R.id.input_email);
        val _passwordText : EditText = findViewById(R.id.input_password);

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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) { // TODO: Need to check if user didnt signed out
                finish()
            }
        }

        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount = task.getResult(
                    ApiException::class.java
                )
                googleSignInAccount?.let { getGoogleAuthCredential(it) }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential =
            GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        signInWithGoogle(googleAuthCredential);
    }

    private fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(this) { authTask ->
            if(authTask.isSuccessful){
                val user =
                    auth.currentUser?.displayName?.let { auth.currentUser?.email?.let { it1 ->
                        User(it, Date(), "",
                            it1, "", "", "")
                    } };
                FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(user).addOnCompleteListener {task2 ->
                        if (task2.isSuccessful){
                            Toast.makeText(this, "Zapsano pres google do db", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this, "Nezapsano pres google do db", Toast.LENGTH_LONG).show()
                        }
                    };
            }else{

            }

        }
    }

    private fun createNewUser(authenticatedUser: User?) {
        

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() { // disable going back to the MainActivity
        moveTaskToBack(true)
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



}
