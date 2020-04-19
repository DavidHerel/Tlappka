package cz.cvut.fel.tlappka.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityAboutBinding
import cz.cvut.fel.tlappka.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityHelpBinding = DataBindingUtil.setContentView(this, R.layout.activity_help)
        val toolbar : Toolbar = binding.helpToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.help)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent : Intent = NavUtils.getParentActivityIntent(this) as Intent
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        NavUtils.navigateUpTo(this, intent)
        return true
    }
}
