package com.udacity.asteroidradar.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.Repo
import retrofit2.HttpException

class RefreshDataWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.initDatabase(context = applicationContext).asteroidDao
        val repo = Repo(database)
        return try {
            repo.syncAllData()
            Result.success()
        } catch (e: HttpException) {
            Log.i("Worker", e.toString())
            Result.retry()
        }
    }
}