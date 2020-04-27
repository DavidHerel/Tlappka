package cz.cvut.fel.tlappka.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
            posts.add(Post("14.3. 15:00", "Username" + i, ArrayList<String>(),
                0, ArrayList<String>(),"1",
                "Lorem ipsum" + i, "https://picsum.photos/600/300?random&" + (i*5),
                "Popisek aktivity bude zde :)",
                "https://picsum.photos/600/300?random&" + i))
        }

        recyclerViewProfile.layoutManager = LinearLayoutManager(activity)
        recyclerViewProfile.adapter = PostsAdapter(posts, activity!!.applicationContext)

        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java);
                    val test = user?.name;
                    nameTextView.setText(test);
                }
            })

        //listener on edit
        changeProfileButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        })

        fillTexts();
        fillProfilePhoto();
    }

    fun fillTexts(){
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java);
                    val test = user?.name;
                    nameTextView.setText(test);
                    profilePlace.setText(user?.place);
                    profileHobbies.setText(user?.hobbies);
                    profileJob.setText(user?.job);
                    profileAbout.setText(user?.about);

                }
            })
    }

    fun fillProfilePhoto(){

        //get storage ref
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}/profilePic");


        //fill photo
        storageRef?.downloadUrl.addOnCompleteListener { urlTask ->
            if(urlTask.isSuccessful) {
                urlTask.result?.let {
                    //display it
                    val imageUri: Uri = it;
                    Toast.makeText(activity, imageUri?.toString(), Toast.LENGTH_LONG).show()
                    Picasso.with(activity).load(imageUri).into(profileIcon)
                }
            }else{
                Toast.makeText(activity, "Chyba při načítání profilového obrázku", Toast.LENGTH_LONG).show()
            }
        }
    }



}
