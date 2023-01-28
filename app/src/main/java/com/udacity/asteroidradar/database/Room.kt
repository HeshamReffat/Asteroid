package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.api.Asteroid

@Database(entities = [AsteroidEntity::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object {
        private lateinit var INSTANCE: AsteroidDatabase
        fun initDatabase(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroids"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

}

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: AsteroidEntity)

    @Query("select * from asteroids_data WHERE closeApproachDate >= :key ORDER BY closeApproachDate ASC")
    fun getAsteroids(key: String): LiveData<List<AsteroidEntity>>
    @Query("select * from asteroids_data WHERE closeApproachDate == :key ORDER BY closeApproachDate ASC")
    fun getTodayAsteroids(key: String): LiveData<List<AsteroidEntity>>
    @Query("select * from asteroids_data ORDER BY closeApproachDate ASC")
    fun getSavedAsteroids(): LiveData<List<AsteroidEntity>>

}

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun ArrayList<Asteroid>.asDatabaseModel(): Array<AsteroidEntity> {
    return map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
}