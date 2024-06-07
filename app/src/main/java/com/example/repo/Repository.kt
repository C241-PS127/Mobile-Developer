package com.example.repo

import com.example.api.ApiService
import com.example.storyapp.data.pref.UserPreference

class Repository private constructor(
    private var apiService: ApiService, private val userPreference: UserPreference
){



    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }
    }
}
