package com.example.lokalin.ui.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.data.Repository
import com.example.data.response.ProductsItem
import com.example.data.response.SliderModel
import com.example.data.response.UserProfileResponseItem
import com.example.storyapp.data.pref.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>()
    val banners: LiveData<List<SliderModel>> = _banner

    private val _products = MutableLiveData<List<ProductsItem>>()
    val products: LiveData<List<ProductsItem>> get() = _products

    private val _profile = MutableLiveData<List<UserProfileResponseItem>>()
    val profile: LiveData<List<UserProfileResponseItem>> get() = _profile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getProfile(token: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val stories = repository.getProfile(token)
                _profile.postValue(stories)
                _isLoading.postValue(false)

            } catch (_: Exception) {
                _isLoading.postValue(true)

            }
        }
    }


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


    val productss = try {
        _isLoading.postValue(true)
        val pager = Pager(config = PagingConfig(
            pageSize = 4, enablePlaceholders = false
        ), pagingSourceFactory = { repository.getProductsPagingSource() })
        _isLoading.postValue(false)
        pager.flow.cachedIn(viewModelScope)
    } catch (e: Exception) {
        // Handle the exception, if needed
        _isLoading.postValue(true)
        Log.e(ContentValues.TAG, "Error creating Pager", e)
        null
    }

    fun loadBanners() {
        val Ref = firebaseDatabase.getReference("banners")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null) {
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