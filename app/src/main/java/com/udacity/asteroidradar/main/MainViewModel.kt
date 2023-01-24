package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.PlanetaryApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {
    init {
        getAllPlanetary()
        getImageToday()
    }
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _planetaryList = MutableLiveData<List<Asteroid>>()
    val planetaryList: LiveData<List<Asteroid>>
        get() = _planetaryList
    private val _imageOfDay = MutableLiveData<String>()
    val imageOfDay: LiveData<String>
        get() = _imageOfDay

    private fun getAllPlanetary() {
        viewModelScope.launch {
            try {
                val ob = PlanetaryApi.retrofitService.getAllPlanetary()
                val json = JSONObject(ob)
                _planetaryList.value = parseAsteroidsJsonResult(json)
                Log.i("cccccc", parseAsteroidsJsonResult(json).toString())
            } catch (e: Exception) {
                Log.i("cccccc", e.toString())
            }
        }
    }

    private fun getImageToday() {

        viewModelScope.launch {
            try {
                val image = PlanetaryApi.retrofitService.getTodayImage()
                if (image.mediaType == "image") {
                    _imageOfDay.value = image.url
                } else {
                    _imageOfDay.value = ""
                }
            } catch (e: Exception) {
                Log.i("cccccc", e.toString())
            }
        }
    }

    fun navigateToAsteroidDetails(asteroid: Asteroid){
        _navigateToSelectedAsteroid.value = asteroid
    }
    fun navigateToAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
}