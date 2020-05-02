package cz.cvut.fel.tlappka

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import cz.cvut.fel.tlappka.databinding.ActivityMainBinding
import cz.cvut.fel.tlappka.home.HomeFragment
import cz.cvut.fel.tlappka.login.SignInActivity
import cz.cvut.fel.tlappka.options.AboutActivity
import cz.cvut.fel.tlappka.options.HelpActivity
import cz.cvut.fel.tlappka.options.SettingsActivity
import cz.cvut.fel.tlappka.profile.ProfileFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item->

        when(item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment())
                supportActionBar?.setTitle("Novinky")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                replaceFragment(ProfileFragment())
                supportActionBar?.setTitle("Profil")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_events -> {
                replaceFragment(EventFragment())
                supportActionBar?.setTitle("Události")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_friends -> {
                replaceFragment(FriendsFragment())
                supportActionBar?.setTitle("Přátelé")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notification -> {
                replaceFragment(NotificationsFragment())
                supportActionBar?.setTitle("Oznámení")
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val toolbar : Toolbar = binding.homeToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Novinky")
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        replaceFragment(HomeFragment())
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_item -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.help_item -> {
                val intent = Intent(this, HelpActivity::class.java)
                startActivity(intent)
            }
            R.id.sign_out_item -> {
                FirebaseAuth.getInstance().signOut();
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            // about
            android.R.id.home -> {
                Toast.makeText(applicationContext, "back", Toast.LENGTH_SHORT).show()
                this.onBackPressed()
                return true
            }
            else -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
//        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }



}
