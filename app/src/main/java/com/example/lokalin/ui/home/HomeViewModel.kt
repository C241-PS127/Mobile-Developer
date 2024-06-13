package com.example.lokalin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.Brand
import com.example.response.ProductsItem
import com.example.response.SliderModel
import com.example.storyapp.data.pref.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>()
    val banners: LiveData<List<SliderModel>> = _banner

    private val _products = MutableLiveData<List<ProductsItem>>()
    val products: LiveData<List<ProductsItem>> get() = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _brands = MutableLiveData<List<Brand>>()
    val brands: LiveData<List<Brand>> get() = _brands

    init {
        allProducts()
        allBrands()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun allProducts() {
        _isLoading.postValue(true) // Set loading to true
        viewModelScope.launch {
            try {
                val stories = repository.getProducts()
                _products.postValue(stories)
                _isLoading.postValue(false) // Set loading to true

            } catch (_: Exception) {
                _isLoading.postValue(true) // Set loading to true

            }
        }
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

    fun allBrands() {
        _isLoading.postValue(true) // Set loading to true
        viewModelScope.launch {
            try {
                val stories = repository.getBrands()
                _brands.postValue(stories)
                _isLoading.postValue(false) // Set loading to true

            } catch (_: Exception) {
                _isLoading.postValue(true) // Set loading to true

            }
        }
    }
}