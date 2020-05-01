package cz.cvut.fel.tlappka.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.faq_item.view.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivitySettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        val toolbar : Toolbar = binding.settingsToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.notificationsSettingsButton.setOnClickListener {
            val intent = Intent(this, NoficationsSettingsActivity::class.java)
            startActivity(intent)
        }
        binding.accountSettingsButton.setOnClickListener {
            val intent = Intent(this, AccountSettingsActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent : Intent = NavUtils.getParentActivityIntent(this) as Intent
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        NavUtils.navigateUpTo(this, intent)
        return true
    }
}
