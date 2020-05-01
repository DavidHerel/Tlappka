package cz.cvut.fel.tlappka.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityAccountSettingsBinding

class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityAccountSettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_account_settings)
        val toolbar : Toolbar = binding.accountSettingsToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Nastavení účtu")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.passwordChange.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        NavUtils.navigateUpTo(this, intent)
        return true
    }
}
