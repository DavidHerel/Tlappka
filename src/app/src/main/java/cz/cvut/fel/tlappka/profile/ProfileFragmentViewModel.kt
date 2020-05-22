package cz.cvut.fel.tlappka.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fel.tlappka.model.Pet
import cz.cvut.fel.tlappka.model.User

class ProfileFragmentViewModel : ViewModel() {
    private val profileFragmentRepository : ProfileFragmentRepository = ProfileFragmentRepository();
    private var mUser = MutableLiveData<User>();
    private var mPet = MutableLiveData<Pet>();
    private var mUri = MutableLiveData<Uri>();
    private var mUriProfilePet : MutableLiveData<Uri>? = MutableLiveData<Uri>();
    private var isDoneSaving = MutableLiveData<Boolean>();
    private var isDoneSavingProfilePet = MutableLiveData<Boolean>();
    private var petUID = MutableLiveData<String>();

    fun getUser() : LiveData<User>{
        mUser = profileFragmentRepository.getUser();
        return mUser;
    }

    fun getPet(uid : String) : LiveData<Pet>{
        mPet = profileFragmentRepository.getPet(uid);
        return mPet;
    }

    fun getUri() : LiveData<Uri>{
        mUri = profileFragmentRepository.getUri();
        return mUri;
    }

    fun getUriProfilePet(uid : String) : LiveData<Uri>?{
        mUriProfilePet = profileFragmentRepository.getUriProfilePet(uid);
        return mUriProfilePet;
    }

    fun saveImage(bitmap : Bitmap) : LiveData<Boolean>{
        isDoneSaving = profileFragmentRepository.saveImage(bitmap);
        return isDoneSaving;

    }

    fun saveImageProfilePet(bitmap : Bitmap, pet : Pet) : LiveData<Boolean>{
        isDoneSavingProfilePet = profileFragmentRepository.saveImageProfilePet(bitmap, pet);
        return isDoneSavingProfilePet;

    }

    fun updateUser(user : User){
        profileFragmentRepository.updateUser(user);
        mUser = profileFragmentRepository.getUser();
    }
    fun updatePet(pet : Pet){
        profileFragmentRepository.updatePet(pet);
        mPet = profileFragmentRepository.getPet(pet.uid);
    }

    fun createPet(pet : Pet) : LiveData<String>{
        petUID = profileFragmentRepository.createPet(pet);
        mUser = profileFragmentRepository.getUser();
        return petUID;
    }

}