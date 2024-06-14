package com.example.lokalin.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.repo.Repository
import com.example.response.Brand
import com.example.response.ProductsItem
import com.example.response.SliderModel
import com.example.storyapp.data.pref.UserModel
import com.google.android.gms.analytics.ecommerce.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>()
    val banners: LiveData<List<SliderModel>> = _banner

    private val _banner2 = MutableLiveData<List<SliderModel>>()
    val banner2: LiveData<List<SliderModel>> = _banner2

    private val _products = MutableLiveData<List<ProductsItem>>()
    val products: LiveData<List<ProductsItem>> get() = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isLoading2 = MutableLiveData<Boolean>()
    val isLoading2: LiveData<Boolean> get() = _isLoading2

    private val _brands = MutableLiveData<List<Brand>>()
    val brands: LiveData<List<Brand>> get() = _brands


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }


    val productss = try {
        _isLoading.postValue(true) // Set loading to true before initializing Pager
        val pager = Pager(
            config = PagingConfig(
                pageSize = 4,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { repository.getProductsPagingSource() }
        )
        _isLoading.postValue(false) // Set loading to false after initializing Pager
        pager.flow.cachedIn(viewModelScope)
    } catch (e: Exception) {
        // Handle the exception, if needed
        _isLoading2.postValue(true) // Set loading to true when an exception occurs
        Log.e(TAG, "Error creating Pager", e)
        null // Or another appropriate action
    }

    val productsss = try {
        _isLoading.postValue(true) // Set loading to true before initializing Pager
        val pager = Pager(
            config = PagingConfig(
                pageSize = 4,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { repository.getProductsPagingSource() }
        )
        _isLoading.postValue(false) // Set loading to false after initializing Pager
        pager.flow.cachedIn(viewModelScope)
    } catch (e: Exception) {
        // Handle the exception, if needed
        _isLoading2.postValue(true) // Set loading to true when an exception occurs
        Log.e(TAG, "Error creating Pager", e)
        null // Or another appropriate action
    }

    fun loadBanners(){
        val Ref=firebaseDatabase.getReference("banner")
        Ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for(childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SliderModel::class.java)
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

    fun loadBannerss(){
        val Ref=firebaseDatabase.getReference("bannerss")
        Ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for(childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if(list!=null){
                        lists.add(list)
                    }
                }
                _banner2.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    fun allBrands() {
        _isLoading2.postValue(true) // Set loading to true
        viewModelScope.launch {
            try {
                val stories = repository.getBrands()
                _brands.postValue(stories)
                _isLoading2.postValue(false) // Set loading to true

            } catch (_: Exception) {
                _isLoading2.postValue(true) // Set loading to true

            }
        }
    }
}