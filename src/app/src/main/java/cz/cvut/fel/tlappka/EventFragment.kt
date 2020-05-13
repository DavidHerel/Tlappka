package cz.cvut.fel.tlappka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import cz.cvut.fel.tlappka.events.EventHistoryFragment
import cz.cvut.fel.tlappka.events.EventPlannedFragment

/**
 * A simple [Fragment] subclass.
 */
class EventFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        showTabs(view)
        return view
    }

    private fun showTabs(view: View) {
        val tabPager = MyTabPagerAdapter(childFragmentManager)
        val viewPager = view.findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = tabPager
//
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
