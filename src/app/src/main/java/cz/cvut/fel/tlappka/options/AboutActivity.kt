package cz.cvut.fel.tlappka.options

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
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
}
