package com.example.parcial.di

import com.example.parcial.data.remote.ApiService
import com.example.parcial.data.remote.RetrofitClient

class AppContainer {
    val apiService: ApiService by lazy { RetrofitClient.apiService }
}
