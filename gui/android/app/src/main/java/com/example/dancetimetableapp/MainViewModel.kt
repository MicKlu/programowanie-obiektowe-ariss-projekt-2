package com.example.dancetimetableapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dancetimetableapp.model.FilterParams
import com.example.dancetimetableapp.model.Lesson

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var dialogFilterParams = FilterParams()
    val filterParams: FilterParams = FilterParams()

    fun getCourses(): LiveData<List<String>> {
        return Repository.getCourses(getApplication())
    }

    private val lessonsLiveData = MutableLiveData<List<Lesson>>()

    fun getLessons(): LiveData<List<Lesson>> {
        loadLessons()
        return lessonsLiveData
    }

    fun loadLessons() {
        Repository.loadLessons(getApplication(), lessonsLiveData, filterParams)
    }
}