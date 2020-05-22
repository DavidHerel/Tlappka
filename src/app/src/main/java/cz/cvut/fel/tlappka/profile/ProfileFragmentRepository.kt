package cz.cvut.fel.tlappka.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import cz.cvut.fel.tlappka.model.Pet
import cz.cvut.fel.tlappka.model.User
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

    fun getPet(uid : String): MutableLiveData<Pet> {
        var data : MutableLiveData<Pet> = MutableLiveData<Pet>();
        FirebaseDatabase.getInstance().getReference("Pets")
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    data.value = p0.getValue(Pet::class.java)!!;
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

    fun getUriProfilePet(uid : String): MutableLiveData<Uri>? {
        var data : MutableLiveData<Uri>? = MutableLiveData<Uri>();
        //get storage ref
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${uid}/profilePic");

        //fill photo
        /*
        storageRef?.downloadUrl.addOnCompleteListener { urlTask ->
            if(urlTask.isSuccessful) {
                urlTask.addOnCompleteListener {
                    if(it.isSuccessful){
                        data?.value = it.result;
                    }
                }
            }

        }
        */
        storageRef?.downloadUrl
            .addOnSuccessListener {
                // Got the download URL for 'users/me/profile.png'
                data?.value = it;
            }.addOnFailureListener {
                // Handle any errors
            }

        return data
    }

    fun saveImage(bitmap : Bitmap) : MutableLiveData<Boolean>{
        var data : MutableLiveData<Boolean> = MutableLiveData<Boolean>();
        val baos = ByteArrayOutputStream();
        data.value = false;
        //get storage ref
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}/profilePic");

        //compress (100 means best image quality, 0 means worst)
        var bitmap = bitmap;
        bitmap = getResizedBitmap(bitmap, 270);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        val image = baos.toByteArray();

        //put it in storage database
        val upload = storageRef.putBytes(image);

        //when uploaded
        upload.addOnCompleteListener{ uploadTask ->
            if (uploadTask.isSuccessful){
                data.value = true;
            }else{
                uploadTask.exception?.let {
                    data.value = false;
                }
            }
        }
        return data;
    }

    fun saveImageProfilePet(bitmap : Bitmap, pet : Pet) : MutableLiveData<Boolean>{
        var data : MutableLiveData<Boolean> = MutableLiveData<Boolean>();
        var dataPet : MutableLiveData<Pet> = MutableLiveData<Pet>();
        val baos = ByteArrayOutputStream();
        data.value = false;

        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${pet.uid}/profilePic");
        var bitmap = bitmap;
        //compress (100 means best image quality, 0 means worst)
        bitmap = getResizedBitmap(bitmap, 270);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        val image = baos.toByteArray();

        //put it in storage database
        val upload = storageRef.putBytes(image);

        //when uploaded
        upload.addOnCompleteListener{ uploadTask ->
            if (uploadTask.isSuccessful){
                data.value = true;
            }else{
                uploadTask.exception?.let {
                    data.value = false;
                }
            }
        }

        return data;
    }

    fun updateUser(user : User){
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user).addOnCompleteListener {
            if (it.isSuccessful){
            }else{
            }
        };

    }

    fun updatePet(pet : Pet){
        FirebaseDatabase.getInstance().getReference("Pets").child(pet.uid)
            .setValue(pet).addOnCompleteListener {
                if (it.isSuccessful){
                }else{
                }
            };

    }

    fun createPet(pet : Pet) : MutableLiveData<String>{
        var dataID : MutableLiveData<String> = MutableLiveData<String>();
       val key = FirebaseDatabase.getInstance().getReference("Pets").push().key
        dataID.value = key;
        if (key != null) {
            pet.uid = key;
            FirebaseDatabase.getInstance().getReference("Pets").child(key)
                .setValue(pet).addOnCompleteListener {
                    if (it.isSuccessful){
                    }else{
                    }
                };
            var data : MutableLiveData<User> = MutableLiveData<User>();
            FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        data.value = p0.getValue(User::class.java)!!;
                        data.value?.pets?.add(key)
                        if(data.value != null) {
                            updateUser(data.value!!);
                        }
                    }
                })
        }
        return dataID;


    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}