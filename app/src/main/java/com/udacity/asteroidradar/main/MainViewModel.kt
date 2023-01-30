package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.Asteroid
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.api.PlanetaryApi
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.repository.Repo
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }
enum class FilterType { ALL, WEEK, TODAY }
class MainViewModel(val repo: Repo) : ViewModel() {


    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val filterType = MutableLiveData(FilterType.WEEK)
    var planetaryList = Transformations.switchMap(filterType) {
        when (it) {
            FilterType.ALL -> repo.getSavedAsteroids()
            FilterType.WEEK -> repo.getWeekAsteroids()
            FilterType.TODAY -> repo.getTodayAsteroids()
        }
    }

    private val _imageOfDay = MutableLiveData<PictureOfDay>()
    val imageOfDay: LiveData<PictureOfDay>
        get() = _imageOfDay
    private val _loading = MutableLiveData<ApiStatus?>()
    val loading: LiveData<ApiStatus?>
        get() = _loading

    private fun getAllPlanetary() {
        viewModelScope.launch {
            _loading.value = ApiStatus.LOADING
            try {
                repo.syncAllData()
                _loading.value = ApiStatus.DONE
            } catch (e: Exception) {
                Log.i("cccccc", e.toString())
                _loading.value = ApiStatus.ERROR
            }
        }
    }

    fun getWeekAsteroids() {
        filterType.value = FilterType.WEEK

    }

    fun getTodayAsteroids() {
        filterType.value = FilterType.TODAY
    }

    fun getSavedAsteroids() {
        filterType.value = FilterType.ALL
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

    fun navigateToAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun navigateToAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
}