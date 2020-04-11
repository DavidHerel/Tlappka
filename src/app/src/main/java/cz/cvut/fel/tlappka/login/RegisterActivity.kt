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
import androidx.appcompat.app.AppCompatActivity
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R

/*
Activity which is hadnling registers
 */
class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //register buttons
        val _signupButton : Button = findViewById(R.id.btn_register);
        val _loginLink : TextView = findViewById(R.id.link_login);

        //set up listeners
        _signupButton.setOnClickListener {
            //signup function
            signup();
        }

        _loginLink.setOnClickListener {
            // Finish the registration screen and return to the Login activity
            intent  = Intent(this, SignInActivity::class.java);
            startActivity(intent);
            finish();
        }
    }

    /*
    Function that handles registraion button click
     */
    fun signup() {

        //find components
        val _nameText: EditText = findViewById(R.id.input_name_register);
        val _emailText : EditText= findViewById(R.id.input_email_register);
        val _passwordText : EditText = findViewById(R.id.input_password_register);
        val _signupButton : Button = findViewById(R.id.btn_register);

        //if validation fales
        if (!validate()) {
            //print error to user
            onSignupFailed()
            return
        }
        _signupButton?.setEnabled(false)

        //Fancy rogress bar
        val progressDialog = ProgressDialog(
            this@RegisterActivity,
            R.style.ThemeOverlay_MaterialComponents_Dialog
        )
        progressDialog.setIndeterminate(true)
        //All strings in Czech but later we can do both english and czech
        progressDialog.setMessage("Vytváření účtu...")
        progressDialog.show()
        val name = _nameText!!.text.toString()
        val email = _emailText!!.text.toString()
        val password = _passwordText!!.text.toString()

        // TODO: There we will implement storing these DATAS to some Database (local/online)
        Handler().postDelayed(
            {
                // On complete call either onSignupSuccess or onSignupFailed
                // depending on success
                onSignupSuccess()
                // onSignupFailed();
                progressDialog.dismiss()
            }, 3000
        )
    }

/*
If registration was succesful create main activity
 */
    fun onSignupSuccess() {

        val _signupButton : Button = findViewById(R.id.btn_register);
        _signupButton?.setEnabled(true)
        setResult(Activity.RESULT_OK, null)
        intent  = Intent(this, MainActivity::class.java);
        startActivity(intent);
        finish()
    }

    /*
    Registratio failed
     */
    fun onSignupFailed() {

        val _signupButton : Button = findViewById(R.id.btn_register);
        Toast.makeText(baseContext, "Registrace selhala", Toast.LENGTH_LONG).show()
        _signupButton?.setEnabled(true)
    }

    /**
     * Validate inputs by user
     */
    fun validate(): Boolean {

        val _nameText: EditText = findViewById(R.id.input_name_register);
        val _emailText : EditText= findViewById(R.id.input_email_register);
        val _passwordText : EditText = findViewById(R.id.input_password_register);

        var valid = true
        val name = _nameText!!.text.toString()
        val email = _emailText!!.text.toString()
        val password = _passwordText!!.text.toString()
        if (name.isEmpty() || name.length < 3) {
            _nameText!!.error = resources.getString(R.string.activity_name_text_error)
            valid = false
        } else {
            _nameText!!.error = null
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText!!.error = resources.getString(R.string.activity_email_text_error)
            valid = false
        } else {
            _emailText!!.error = null
        }
        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            _passwordText!!.error = resources.getString(R.string.activity_password_text_error);
            valid = false
        } else {
            _passwordText!!.error = null
        }
        return valid
    }
}
