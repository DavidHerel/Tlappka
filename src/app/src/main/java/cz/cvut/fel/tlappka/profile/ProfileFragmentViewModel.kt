package cz.cvut.fel.tlappka.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fel.tlappka.model.User

class ProfileFragmentViewModel : ViewModel() {
    private val profileFragmentRepository : ProfileFragmentRepository = ProfileFragmentRepository();
    private var mUser = MutableLiveData<User>();
    private var mUri = MutableLiveData<Uri>();
    private var isDoneSaving = MutableLiveData<Boolean>();

    fun getUser() : LiveData<User>{
        mUser = profileFragmentRepository.getUser();
        return mUser;
    }

    fun getUri() : LiveData<Uri>{
        mUri = profileFragmentRepository.getUri();
        return mUri;
    }

    fun saveImage(bitmap : Bitmap) : LiveData<Boolean>{
        isDoneSaving = profileFragmentRepository.saveImage(bitmap);
        return isDoneSaving;

    }

    fun updateUser(user : User){
        profileFragmentRepository.updateUser(user);
        mUser = profileFragmentRepository.getUser();
    }

}