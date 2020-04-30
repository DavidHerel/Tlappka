package cz.cvut.fel.tlappka.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.model.User
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.ByteArrayOutputStream


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

    private fun fillProfilePhoto(){
        profileFragmentViewModel.getUri().observe(this) { uri ->
            // update UI
            Picasso.with(this).load(uri).into(profile_photo_edit)
        }
    }

    fun cancelEditProfileButton(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_click));
        finish();
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

        //TODO - now it passes to next activity only when it is succesfuly uploaded (so without internet connections it does nothing, just
        // stores in db after device gets online) (also all text fields when offline come blank so blank text fields are stored)
        profileFragmentViewModel.saveImage((profile_photo_edit.getDrawable() as BitmapDrawable).bitmap).observe(this){
            if (it){
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_click));
                finish();
            }
        }

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
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, ByteArrayOutputStream());
            profile_photo_edit.setImageBitmap(imageBitmap);


        }
    }
}
