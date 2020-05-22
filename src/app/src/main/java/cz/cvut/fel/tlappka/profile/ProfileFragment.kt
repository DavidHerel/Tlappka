package cz.cvut.fel.tlappka.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.home.PostsAdapter
import cz.cvut.fel.tlappka.home.model.Post
import cz.cvut.fel.tlappka.profile.adapter.PetAdapter
import cz.cvut.fel.tlappka.profile.adapter.IkonkaModelClass
import kotlinx.android.synthetic.main.activity_content_profile.*


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
        initAddPetButton();

    }

    private fun updatePets(){

        var items: java.util.ArrayList<IkonkaModelClass> = java.util.ArrayList<IkonkaModelClass>()
        val adapter =
            PetAdapter(context, items)
        recycler_view_pets_icons.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recycler_view_pets_icons.adapter = adapter

        var i = 0;
        //getting user
        profileFragmentViewModel.getUser().observe(viewLifecycleOwner) {
            items.clear();
            // update UI
            for(item in it.pets){
                if(!item.isNullOrBlank()) {
                    //Toast.makeText(context, item + " petNumber "+  i, Toast.LENGTH_LONG).show()
                    i++;

                    items.add(IkonkaModelClass(Uri.parse("https://picsum.photos/600/300?random&"), "t", item))
                    adapter.notifyDataSetChanged()

                    //TODO - Tady je chyba, padá do, když chci použít fotky z DB
                    /*
                    profileFragmentViewModel.getUriProfilePet(item)?.observe(viewLifecycleOwner){uri->
                            items.add(IkonkaModelClass(uri, "Popisek", item))
                            adapter.notifyDataSetChanged()
                    }
                    */

                }
            }
        }
    }

    private fun initChangeProfileButton() {
        //listener on edit
        changeProfileButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        })
    }

    private fun initAddPetButton() {
        //listener on edit
        addPetIcon.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, AddPetActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onResume() {
        super.onResume()
        fillTexts();
        fillProfilePhoto();
        updatePets();
    }

    //everytime the user profile data are updated -> UI refreshed
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillTexts();
        fillProfilePhoto();
        updatePets();
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
