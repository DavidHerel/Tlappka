package cz.cvut.fel.tlappka.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import cz.cvut.fel.tlappka.R

/*
Activity where user can choose between registration and login
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //register buttons
        val buttonLogin = findViewById<Button>(R.id.signUpButton);
        val buttonRegister = findViewById<Button>(R.id.registerButton);

        //setup listeners and new activities

        buttonLogin.setOnClickListener {
            Intent(applicationContext, SignInActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        buttonRegister.setOnClickListener {
            Intent(applicationContext, RegisterActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}
