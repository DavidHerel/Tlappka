package cz.cvut.fel.tlappka.login

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cz.cvut.fel.tlappka.R


/*
Activity where user can choose between registration and login
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //skipnu zobrazeni activity kdyz uz jednou activity byla zobrazena
        //skipnu zobrazeni activity kdyz uz jednou activity byla zobrazena
        if (loadSavedPreferences()) { //kdyz jsem tu porpve ulozim ze jsem uz byl
            savePreferences()
        } else {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

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

    /*
Metoda ktera nacte jestli byl login rozkliknute poprve
 */
    private fun loadSavedPreferences(): Boolean {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getBoolean("FirstLaunchLogin", true)
    }

    /*
Ulozi flag,ze tato aktivita uz jednou zobrazena byla
 */
    private fun savePreferences() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putBoolean("FirstLaunchLogin", false)
        editor.commit()
    }
}
