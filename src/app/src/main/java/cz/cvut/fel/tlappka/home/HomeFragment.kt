package cz.cvut.fel.tlappka.home

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityMainBinding
import cz.cvut.fel.tlappka.home.model.Post
import cz.cvut.fel.tlappka.home.model.PostContentHandler
import kotlinx.android.synthetic.main.activity_content_home.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt


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

        val postContentHandler = PostContentHandler()

        val posts: ArrayList<Post> = ArrayList()
        for (i in 1..100) {
            posts.add(postContentHandler.generateContent(i)) //generate random data for posts
            /*
            posts.add(Post("14.3. 15:00", "Username", "1",
                "Lorem ipsum" + i, "https://picsum.photos/600/300?random&" + i,
                "Popisek aktivity bude zde",
                "https://picsum.photos/600/300?random&" + i)) */
        }

        //handle to the Layout Manager and Adapter of the Recycler View
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = PostsAdapter(posts, activity!!.applicationContext)

    }

}
