package cz.cvut.fel.tlappka.profile

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.model.User
import kotlinx.android.synthetic.main.activity_content_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.ByteArrayOutputStream

class ProfileFragmentRepository {
    fun getUser(): MutableLiveData<User> {
        var data : MutableLiveData<User> = MutableLiveData<User>();
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    data.value = p0.getValue(User::class.java)!!;
                }
            })
        return data
    }

    fun getUri(): MutableLiveData<Uri> {
        var data : MutableLiveData<Uri> = MutableLiveData<Uri>();
        //get storage ref
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}/profilePic");


        //fill photo
        storageRef?.downloadUrl.addOnCompleteListener { urlTask ->
            if(urlTask.isSuccessful) {
                urlTask.result?.let {
                    //display it
                    data.value = it;
                }!!
            }else{
            }
        }
        return data
    }

    fun saveImage(bitmap : Bitmap){
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

            }else{
                uploadTask.exception?.let {
                }
            }
        }
    }

    fun updateUser(user : User){
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("about").setValue(user.about);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("hobbies").setValue(user.hobbies);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("job").setValue(user.job);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("name").setValue(user.name);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("place").setValue(user.place);
    }
}