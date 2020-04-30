package cz.cvut.fel.tlappka.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.model.User
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.ByteArrayOutputStream
import java.io.File


class EditProfileActivity : AppCompatActivity() {
    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()

    //just to track the call
    private val REQUEST_IMAGE_CAPTURE = 100;
    private val REQUEST_GALLERY = 200;

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

        //takePictureIntent();
        selectImage();
    }

    //open camera intent
    private fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    //open camera intent
    private fun getGalleryIntent(){
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    //get pic from camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                //pic from camera
                val imageBitmap = data?.extras?.get("data") as Bitmap
                profile_photo_edit.setImageBitmap(imageBitmap)
            }else  if (requestCode == REQUEST_GALLERY){
                val uri = data?.data;
                //photo from gallery
                profile_photo_edit.setImageURI(data?.data)
            }
        }
    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Vyfotit", "Vybrat z galerie", "Zrušit")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@EditProfileActivity)
        builder.setTitle("Přidat fotku")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Vyfotit") {
                takePictureIntent();
            } else if (options[item] == "Vybrat z galerie") {
                getGalleryIntent();
            } else if (options[item] == "Zrušit") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

}
