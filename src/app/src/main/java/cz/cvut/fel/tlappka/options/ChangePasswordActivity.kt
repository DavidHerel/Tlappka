package cz.cvut.fel.tlappka.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityChangePasswordBinding
import cz.cvut.fel.tlappka.databinding.ActivitySettingsBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityChangePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        val toolbar : Toolbar = binding.changePasswordToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Nastavení účtu")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, AccountSettingsActivity::class.java)
        NavUtils.navigateUpTo(this, intent)
        return true
    }
}
