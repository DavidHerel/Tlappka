package cz.cvut.fel.tlappka.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cz.cvut.fel.tlappka.R

/*
Splash activity which shows Tlappka intro to user
Nowadays it last under 1 sec (1 sec was too long IMHO)
 */
class SplashActivity : AppCompatActivity() {
    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT:Long = 1000 // 3000 = 1 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash);

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this,OnboardingActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)

    }
}
