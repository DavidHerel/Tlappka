package cz.cvut.fel.tlappka.home

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.internal.Objects
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.home.model.Post
import cz.cvut.fel.tlappka.home.model.PostContentHandler
import kotlinx.android.synthetic.main.activity_content_home.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        super.onResume()
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
        recyclerView.adapter = PostsAdapter(posts, requireActivity().applicationContext)
    }

}
