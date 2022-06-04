package com.example.dancetimetableapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.dancetimetableapp.model.FilterParams
import com.example.dancetimetableapp.model.Lesson

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var dialogFilterParams = FilterParams()
    val filterParams: FilterParams = FilterParams()
    val lessons = ArrayList<Lesson>()

    fun getCourses(): LiveData<List<String>> {
        return Repository.getCourses(getApplication())
    }
}