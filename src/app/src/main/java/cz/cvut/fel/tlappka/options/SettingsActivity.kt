package cz.cvut.fel.tlappka.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivitySettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        val toolbar : Toolbar = binding.settingsToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent : Intent = NavUtils.getParentActivityIntent(this) as Intent
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        NavUtils.navigateUpTo(this, intent)
        return true
    }
}
