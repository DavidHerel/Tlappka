package cz.cvut.fel.tlappka.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityNoficationsSettingsBinding

class NoficationsSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoficationsSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityNoficationsSettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_nofications_settings)
        val toolbar : Toolbar = binding.notificationSettingsToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Nastavení oznámení")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        NavUtils.navigateUpTo(this, intent)
        return true
    }
}
