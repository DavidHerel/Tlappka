package cz.cvut.fel.tlappka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import cz.cvut.fel.tlappka.databinding.ActivityMainBinding
import cz.cvut.fel.tlappka.home.HomeFragment

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
        binding = setContentView(this, R.layout.activity_main)

        // TODO: Replace findViewById() with binding object handle - error not Toolbar object
        /*val toolbar: Toolbar = binding.homeToolbar
        setSupportActionBar(binding.homeToolbar)
        supportActionBar?.setTitle("Novinky")*/
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.home_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Novinky")

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        replaceFragment(HomeFragment())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

}
