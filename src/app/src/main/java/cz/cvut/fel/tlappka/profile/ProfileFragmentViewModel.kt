package cz.cvut.fel.tlappka.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fel.tlappka.model.User

class ProfileFragmentViewModel : ViewModel() {
    val profileFragmentRepository : ProfileFragmentRepository = ProfileFragmentRepository();
    private lateinit var mUser : MutableLiveData<User>;
    private lateinit var mUri : MutableLiveData<Uri>;

    fun getUser() : LiveData<User>{
        mUser = profileFragmentRepository.getUser();
        return mUser;
    }

    fun getUri() : LiveData<Uri>{
        mUri = profileFragmentRepository.getUri();
        return mUri;
    }

    fun saveImage(bitmap : Bitmap){
        profileFragmentRepository.saveImage(bitmap);
    }

    fun updateUser(user : User){
        profileFragmentRepository.updateUser(user);
    }

}