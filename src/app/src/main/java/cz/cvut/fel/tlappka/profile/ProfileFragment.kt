package cz.cvut.fel.tlappka.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.home.PostsAdapter
import cz.cvut.fel.tlappka.home.model.Post
import kotlinx.android.synthetic.main.activity_content_home.*
import kotlinx.android.synthetic.main.activity_content_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val posts: ArrayList<Post> = ArrayList()
        for (i in 1..100) {
            posts.add(Post("Lorem ipsum" + i, "Můj popisek", "https://picsum.photos/600/300?random&" + i))
        }

        recyclerViewProfile.layoutManager = LinearLayoutManager(activity)
        recyclerViewProfile.adapter = PostsAdapter(posts, activity!!.applicationContext)
    }


}
