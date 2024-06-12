package com.example.lokalin.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.Product
import com.example.response.SliderModel
import com.example.response.SliderModel2
import com.example.response.UserProfileResponseItem
import com.example.storyapp.data.pref.UserModel
import com.example.utils.ResultState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel2>>()
    val banners: LiveData<List<SliderModel2>> = _banner

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getProfile(token: String): LiveData<ResultState<List<UserProfileResponseItem>>> {
        return repository.getProfile(token)
    }


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()


    }

    init {
        allProducts()
    }

    fun allProducts() {
        _isLoading.postValue(true) // Set loading to true
        viewModelScope.launch {
            try {
                val stories = repository.getProducts()
                _products.postValue(stories)
            } catch (_: Exception) {
                // Handle error
            } finally {
                _isLoading.postValue(false) // Set loading to false after request completes
            }
        }
    }


    fun loadBanners(){
        val Ref=firebaseDatabase.getReference("banners")
        Ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel2>()
                for(childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SliderModel2::class.java)
                    if(list!=null){
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}