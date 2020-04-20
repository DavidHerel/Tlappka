package cz.cvut.fel.tlappka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import cz.cvut.fel.tlappka.databinding.ActivityMainBinding
import cz.cvut.fel.tlappka.home.HomeFragment
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

//        utils for navigation -- which is currently not working o.O
//        val navController = this.findNavController(R.id.myNavHostFragment)
//        NavigationUI.setupActionBarWithNavController(this, navController)

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

    ////  TODO: Connect navigation with options menu
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item, this.findNavController(R.id.myNavHostFragment))  ||
//                super.onOptionsItemSelected(item)
//    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
//        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }



}
