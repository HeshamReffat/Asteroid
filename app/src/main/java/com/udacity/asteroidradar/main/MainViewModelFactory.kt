package com.udacity.asteroidradar.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDao

class MainViewModelFactory(val dataSource:AsteroidDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel( dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}