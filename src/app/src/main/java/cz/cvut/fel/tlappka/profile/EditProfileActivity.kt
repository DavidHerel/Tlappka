package cz.cvut.fel.tlappka.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.model.User
import kotlinx.android.synthetic.main.activity_content_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.ByteArrayOutputStream
import java.util.*


class EditProfileActivity : AppCompatActivity() {
    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()

    //just to track the call
    private val REQUEST_IMAGE_CAPTURE = 100;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        setSupportActionBar(findViewById(R.id.profileEditToolBar));

        fillTexts();
        fillProfilePhoto();
    }

    fun fillTexts(){
        profileFragmentViewModel.getUser().observe(this) { user ->
            // update UI
            usernameProfileEdit.setText(user?.name);
            placeProfileEdit.setText(user?.place);
            hobbiesProfileEdit.setText(user?.hobbies);
            jobProfileEdit.setText(user?.job);
            aboutProfileEdit.setText(user?.about);
        }
    }

    fun fillProfilePhoto(){
        profileFragmentViewModel.getUri().observe(this) { uri ->
            // update UI
            Picasso.with(this).load(uri).into(profile_photo_edit)
        }
    }

    fun cancelEditProfileButton(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_click));
        finish();
    }

    override fun onResume() {
        super.onResume()
        fillTexts();
        fillProfilePhoto();
    }

    //update data
    fun doneEditProfileButton(view: View) {
        var user = User();
        user.name = usernameProfileEdit.text.toString();
        user.about = aboutProfileEdit.text.toString();
        user.hobbies = hobbiesProfileEdit.text.toString();
        user.job = jobProfileEdit.text.toString();
        user.place = placeProfileEdit.text.toString();
        profileFragmentViewModel.updateUser(user);

        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_click));
        finish();
    }

    fun changeProfilePicButton(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_click));

        takePictureIntent();
    }

    //open camera intent
    private fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ pictureIntent->
            pictureIntent.resolveActivity(this?.packageManager!!)?.also{
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }

        }
    }

    //get pic from camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){

            //pic from camera
            val imageBitmap = data?.extras?.get("data") as Bitmap

            profileFragmentViewModel.saveImage(imageBitmap);
        }
    }
}
