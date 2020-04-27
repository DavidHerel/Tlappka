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
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.model.User
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.ByteArrayOutputStream


class EditProfileActivity : AppCompatActivity() {
    //just to track the call
    private val REQUEST_IMAGE_CAPTURE = 100;

    //upload into firebase storage
    private lateinit var imageUri : Uri;

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

        //get storage ref
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}/profilePic");


        //fill photo
        storageRef.downloadUrl.addOnCompleteListener { urlTask ->
            if(urlTask.isSuccessful) {
                urlTask.result?.let {
                    //display it
                    imageUri = it;
                    Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show()
                    Picasso.with(this).load(imageUri).into(profile_photo_edit)
                }
            }else{
                Toast.makeText(this, "Chyba při načítání profilového obrázku", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun cancelEditProfileButton(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_click));
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

            uploadImageAndSaveUri(imageBitmap);
        }
    }

    //saving camera pic
    private fun uploadImageAndSaveUri(bitmap : Bitmap){
        val baos = ByteArrayOutputStream();

        //get storage ref
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}/profilePic");

        //compress (100 means best image quality, 0 means worst)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        val image = baos.toByteArray();

        //put it in storage database
        val upload = storageRef.putBytes(image);

        //when uploaded
        upload.addOnCompleteListener{ uploadTask ->
            if (uploadTask.isSuccessful){

                //download profile pic
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    if (urlTask.isSuccessful) {
                        urlTask.result?.let {
                            //display it
                            imageUri = it;
                            Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show()
                            profile_photo_edit.setImageBitmap(bitmap);
                        }
                    }else{
                        Toast.makeText(this, "Chyba při načítání profilového obrázku", Toast.LENGTH_LONG).show()
                    }

                }
            }else{
                uploadTask.exception?.let {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
