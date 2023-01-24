package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.PlanetaryApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class ApiStatus { LOADING, ERROR, DONE }
class MainViewModel : ViewModel() {
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _planetaryList = MutableLiveData<List<Asteroid>>()
    val planetaryList: LiveData<List<Asteroid>>
        get() = _planetaryList
    private val _imageOfDay = MutableLiveData<PictureOfDay>()
    val imageOfDay: LiveData<PictureOfDay>
        get() = _imageOfDay
    private val _loading = MutableLiveData<ApiStatus?>()
    val loading : LiveData<ApiStatus?>
        get() = _loading
    private fun getAllPlanetary() {
        viewModelScope.launch {
            _loading.value = ApiStatus.LOADING
            try {
                val ob = PlanetaryApi.retrofitService.getAllPlanetary()
                val json = JSONObject(ob)
                _planetaryList.value = parseAsteroidsJsonResult(json)
                Log.i("cccccc", parseAsteroidsJsonResult(json).toString())
                _loading.value = ApiStatus.DONE
            } catch (e: Exception) {
                Log.i("cccccc", e.toString())
                _loading.value = ApiStatus.ERROR
            }
        }
    }

    private fun getImageToday() {

        viewModelScope.launch {
            try {
                val image = PlanetaryApi.retrofitService.getTodayImage()
                if (image.mediaType == "image") {
                    _imageOfDay.value = image
                } else {
                    _imageOfDay.value = null
                }
            } catch (e: Exception) {
                Log.i("cccccc", e.toString())
            }
        }
    }
    init {
        getAllPlanetary()
        getImageToday()
    }
    fun navigateToAsteroidDetails(asteroid: Asteroid){
        _navigateToSelectedAsteroid.value = asteroid
    }
    fun navigateToAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
}