package com.example.dancetimetableapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dancetimetableapp.datasources.CachedDataSource
import com.example.dancetimetableapp.datasources.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Repository {

      private val scope = CoroutineScope(Dispatchers.IO)
      private val remoteDataSource by lazy {
            RemoteDataSource()
      }

      fun getCourses(context: Context): LiveData<List<String>> {
            val coursesLiveData = MutableLiveData<List<String>>()

            val cachedDataSource = CachedDataSource("courses")
            cachedDataSource.getCourses(context)?.let {
                  coursesLiveData.postValue(it)
            }

            if(!cachedDataSource.isUpToDate(context)) {
                  Log.d("dta", "Need fetch")
                  scope.launch {
                        remoteDataSource.getCourses(context)?.let {
                              coursesLiveData.postValue(it)
                        }
                  }
            }

            return coursesLiveData
      }


}