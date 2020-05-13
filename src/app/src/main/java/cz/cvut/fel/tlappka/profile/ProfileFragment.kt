package cz.cvut.fel.tlappka.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.home.PostsAdapter
import cz.cvut.fel.tlappka.home.model.Post
import cz.cvut.fel.tlappka.model.User
import kotlinx.android.synthetic.main.activity_content_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.top_profile_edit_toolbar.*
import java.lang.Thread.sleep


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()

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
        //TODO connect to PostContentHandler & DB
        for (i in 1..100) {
            posts.add(Post("14.3. 15:00", "Username" + i, ArrayList<String>(),
                0, ArrayList<String>(),"1",
                "Lorem ipsum" + i, "https://picsum.photos/600/300?random&" + (i*5),
                "Popisek aktivity bude zde :)",
                "https://picsum.photos/600/300?random&" + i,
                "Username" + i + " Lorem ipsum" + i))
        }

        recyclerViewProfile.layoutManager = LinearLayoutManager(activity)
        recyclerViewProfile.adapter = PostsAdapter(posts, requireActivity().applicationContext)
        initChangeProfileButton();
    }

    private fun initChangeProfileButton() {
        //listener on edit
        changeProfileButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onResume() {
        super.onResume()
        fillTexts();
        fillProfilePhoto();
    }

    //everytime the user profile data are updated -> UI refreshed
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillTexts();
        fillProfilePhoto();
    }


    private fun fillTexts(){
        profileFragmentViewModel.getUser().observe(viewLifecycleOwner) { user ->
            // update UI
            nameTextView.text = user?.name;
            profilePlace.text = user?.place;
            profileHobbies.text = user?.hobbies;
            profileJob.text = user?.job;
            profileAbout.text = user?.about;
        }
    }

    private fun fillProfilePhoto(){
        profileFragmentViewModel.getUri().observe(viewLifecycleOwner) { uri ->
            // update UI
            Picasso.with(activity).load(uri).into(profileIcon)
        }
    }

    private fun hideProgressBar() {
        profileProgressBar.visibility = View.VISIBLE;
    }

    private fun showProgressBar() {
        profileProgressBar.visibility = View.GONE;
    }


}
