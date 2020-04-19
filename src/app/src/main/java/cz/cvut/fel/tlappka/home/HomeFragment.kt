package cz.cvut.fel.tlappka.home

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.home.model.Post
import kotlinx.android.synthetic.main.activity_content_home.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val posts: ArrayList<Post> = ArrayList()
        for (i in 1..100) {
            posts.add(Post("Lorem ipsum" + i, "Můj popisek", "https://picsum.photos/600/300?random&" + i))
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = PostsAdapter(posts, activity!!.applicationContext)

    }
}
