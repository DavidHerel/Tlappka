package cz.cvut.fel.tlappka.events

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import cz.cvut.fel.tlappka.R

/**
 * A simple [Fragment] subclass.
 */
class EventFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        showTabs(view)
        setFABclickListeners(view)
        return view
    }


    private fun setFABclickListeners(view: View) {

        val fab_new = view.findViewById(R.id.fab_new) as com.getbase.floatingactionbutton.FloatingActionButton
//        val fab_find = view.findViewById(R.id.fab_find) as FloatingActionButton

        fab_new.setOnClickListener {
            val intent = Intent(activity, CreateEventActivity::class.java)
            startActivity(intent)
        }
//        fab_find.setOnClickListener {
//            val intent = Intent(activity, FindEventActivity::class.java)
//        }
    }

    private fun showTabs(view: View) {
        val tabPager =
            MyTabPagerAdapter(
                childFragmentManager
            )
        val viewPager = view.findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = tabPager

        val tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(viewPager)
    }


    internal class MyTabPagerAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> EventHistoryFragment()
                else -> EventPlannedFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Historie"
                else -> "Plánované"
            }
        }
    }

}
