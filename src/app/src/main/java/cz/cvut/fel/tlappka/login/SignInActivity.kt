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
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R
import kotlinx.android.synthetic.main.activity_sign_in.*

/*
Activity that handles login of a user
 */
class SignInActivity : AppCompatActivity() {

    private var TAG : String = "SignInActivity";
    private var REQUEST_SIGNUP : Int = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

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


        _googleButton.setOnClickListener{
            Intent(applicationContext, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
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
        _loginButton.isEnabled = false

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
        // TODO: There we will send email to password to Database/server where we will check if it matches
        Handler().postDelayed(
            {
                //If datas match some data in databse then call onLoginSucces
                onLoginSuccess()

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


}
