package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.Asteroid
import com.udacity.asteroidradar.api.PlanetaryApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Repo(private val database: AsteroidDao) {
    val asteroidList: LiveData<List<Asteroid>> =
        Transformations.map(database.getAsteroids(getNextSevenDaysFormattedDates().first())) {
            it.asDomainModel()
        }

    suspend fun syncAllData() {
        withContext(Dispatchers.IO) {
            val response = PlanetaryApi.retrofitService.getAllPlanetary().await()
            val asteroidList = parseAsteroidsJsonResult(JSONObject(response))
            database.insertAll(*asteroidList.asDatabaseModel())
        }
    }
}