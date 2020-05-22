package cz.cvut.fel.tlappka.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.model.Pet
import cz.cvut.fel.tlappka.model.User
import kotlinx.android.synthetic.main.activity_add_pet.*
import kotlinx.android.synthetic.main.top_profile_edit_toolbar.*
import java.io.ByteArrayOutputStream
import java.io.File


class AddPetActivity : AppCompatActivity() {
    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()

    //just to track the call
    private val REQUEST_IMAGE_CAPTURE = 100;
    private val REQUEST_GALLERY = 200;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        profile_pet_photo_edit.setImageBitmap(drawableToBitmap(ContextCompat.getDrawable(applicationContext ,R.drawable.account_circle_24px__1_)!!))

        profileName.setText("Přidat mazlíčka")
        setSupportActionBar(findViewById(R.id.profileEditToolBar));

        addPetProfilePicButton();
        cancelAddPetButton();
        doneAddPetButton()
    }

    fun cancelAddPetButton() {
        backArrow.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_click));
            finish();
        }

    }


    //update data
    fun doneAddPetButton() {
        saveChanges.setOnClickListener {
            var pet = Pet();
            pet.name = usernameAddPet.text.toString();
            pet.about = aboutAddPet.text.toString();
            pet.hobbies = hobbiesAddPet.text.toString();
            pet.place = placeAddPet.text.toString();
            pet.owners.add(FirebaseAuth.getInstance().currentUser!!.uid)
            profileFragmentViewModel.updatePet(pet).observe(this){ uid->
                pet.uid = uid;
                //TODO - now it passes to next activity only when it is succesfuly uploaded (so without internet connections it does nothing, just
                // stores in db after device gets online) (also all text fields when offline come blank so blank text fields are stored)
                    profileFragmentViewModel.saveImageProfilePet(
                        (profile_pet_photo_edit.getDrawable() as BitmapDrawable).bitmap,
                        pet
                    ).observe(this) { bool ->
                        if (bool) {
                            it.startAnimation(
                                AnimationUtils.loadAnimation(
                                    this,
                                    R.anim.layout_click
                                )
                            );
                            finish();
                        }
                    }
            };
        }

    }

    fun addPetProfilePicButton() {
        changeProfilePhotoPet.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_click));

            //takePictureIntent();
            selectImage();
        }
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
                profile_pet_photo_edit.setImageBitmap(imageBitmap)
            }else  if (requestCode == REQUEST_GALLERY){
                val uri = data?.data;
                //photo from gallery
                profile_pet_photo_edit.setImageURI(data?.data)
            }
        }
    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Vyfotit", "Vybrat z galerie", "Zrušit")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@AddPetActivity)
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

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }
}
