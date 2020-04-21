package cz.cvut.fel.tlappka.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityHelpBinding
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityHelpBinding = DataBindingUtil.setContentView(this, R.layout.activity_help)
        val toolbar : Toolbar = binding.helpToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.help)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val FAQList = createFAQList()
        faq_recyclerView.adapter = FAQAdapter(FAQList)
        faq_recyclerView.layoutManager = LinearLayoutManager(this)
        faq_recyclerView.setHasFixedSize(true)
    }

    private fun createFAQList() : List<FAQItem> {
        val list = ArrayList<FAQItem>()
        val q1 = "Question1"
        val q2 = "Question2"
        val q3 = "Question3"
        val a1 = "Answer1"
        val a2 = "Answer2"
        val a3 = "Answer3"
        val q_list = listOf<String>(q1, q2, q3)
        val a_list = listOf<String>(a1, a2, a3)
        for (i in q_list.indices) {
            val item = FAQItem(q_list[i], a_list[i])
            list += item
        }
        return list
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent : Intent = NavUtils.getParentActivityIntent(this) as Intent
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        NavUtils.navigateUpTo(this, intent)
        return true
    }
}
