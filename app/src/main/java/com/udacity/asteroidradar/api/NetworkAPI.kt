package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.Constants.apikey
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ServerSide {


    @GET("neo/rest/v1/feed")
    suspend fun getAllPlanetary(
        @Query("api_key") type: String = apikey,
        @Query("start_date") startDate: String = getNextSevenDaysFormattedDates().first(),
        @Query("end_date") endDate: String = getNextSevenDaysFormattedDates().last()
    ): String

    @GET("planetary/apod")
    suspend fun getTodayImage(@Query("api_key") type: String = apikey): PictureOfDay
}

object PlanetaryApi {
    val retrofitService: ServerSide by lazy { retrofit.create(ServerSide::class.java) }
}