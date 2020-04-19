package cz.cvut.fel.tlappka.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityAboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about)
        val toolbar : Toolbar = binding.aboutToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent : Intent = NavUtils.getParentActivityIntent(this) as Intent
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        NavUtils.navigateUpTo(this, intent)
        return true
    }
}
