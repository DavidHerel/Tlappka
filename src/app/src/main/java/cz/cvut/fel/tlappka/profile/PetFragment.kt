package cz.cvut.fel.tlappka.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.home.PostsAdapter
import cz.cvut.fel.tlappka.home.model.Post
import cz.cvut.fel.tlappka.profile.adapter.IkonkaModelClass
import cz.cvut.fel.tlappka.profile.adapter.PetAdapter
import kotlinx.android.synthetic.main.activity_content_pet.*


/**
 * A simple [Fragment] subclass.
 */
class PetFragment : Fragment() {
    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()
    private var UID : String? = "";
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        UID = arguments?.getString("UID");
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

        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val profileFragment = ProfileFragment()
            val fragmentTransaction =
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, profileFragment)
//        fragmentTransaction.addToBackStack(null)
            //        fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun initChangeProfileButton() {
        //listener on edit
        changeProfilePetButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, EditPetActivity::class.java)
            intent.putExtra("UID", UID);
            startActivity(intent)
        })
    }

    override fun onResume() {
        super.onResume()
        fillTexts();
        fillProfilePhoto();
        updateOwners()
    }

    //everytime the user profile data are updated -> UI refreshed
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillTexts();
        fillProfilePhoto();
        updateOwners()
    }


    private fun fillTexts(){
        if(UID.isNullOrEmpty()){
            return;
        }
        profileFragmentViewModel.getPet(UID!!).observe(viewLifecycleOwner) { pet ->
            // update UI
            namePetTextView.text = pet?.name;
            profilePetPlace.text = pet?.place;
            profilePetHobbies.text = pet?.hobbies;
           // profilePetAge.text = pet?.birthDate.toString();
            profilePetAbout.text = pet?.about;
        }
    }

    private fun fillProfilePhoto(){
        profileFragmentViewModel.getUriProfilePet(UID!!)?.observe(viewLifecycleOwner) { uri ->
            // update UI
            Picasso.with(activity).load(uri).into(profilePetIcon)
        }
    }

    private fun updateOwners(){
        //TODO - když uživatel nemá žádnou fotku tak to spadne :D
        var items: java.util.ArrayList<IkonkaModelClass> = java.util.ArrayList<IkonkaModelClass>()
        val adapter =
            PetAdapter(context, items)
        recycler_view_owners_icons.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recycler_view_owners_icons.adapter = adapter

        //getting user
        profileFragmentViewModel.getPet(UID!!).observe(viewLifecycleOwner) {
            items.clear();
            // update UI
            for(item in it.owners){
                if(!item.isNullOrBlank()) {
                    profileFragmentViewModel.getUriUser(item)?.observe(viewLifecycleOwner){uri->
                        items.add(IkonkaModelClass(uri, "Popisek", item, false))
                        adapter.notifyDataSetChanged()
                    }

                }
            }
        }
    }
}
