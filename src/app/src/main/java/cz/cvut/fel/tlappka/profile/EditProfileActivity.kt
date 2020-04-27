package cz.cvut.fel.tlappka.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.model.User
import kotlinx.android.synthetic.main.activity_content_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.top_profile_edit_toolbar.*

class EditProfileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        setSupportActionBar(findViewById(R.id.profileEditToolBar));

        fillTexts();
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
                    usernameProfileEdit.setText(test);
                    placeProfileEdit.setText(user?.place);
                    hobbiesProfileEdit.setText(user?.hobbies);
                    jobProfileEdit.setText(user?.job);
                    aboutProfileEdit.setText(user?.about);

                }
            })
    }

    fun cancelEditProfileButton(view: View) {
        finish();
    }
    //save data
    fun doneEditProfileButton(view: View) {
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("about").setValue(aboutProfileEdit.text.toString());
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("hobbies").setValue(hobbiesProfileEdit.text.toString());
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("job").setValue(jobProfileEdit.text.toString());
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("name").setValue(usernameProfileEdit.text.toString());
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("place").setValue(placeProfileEdit.text.toString());

        Toast.makeText(this, "Změny uloženy", Toast.LENGTH_LONG).show()
        finish();
    }
}
