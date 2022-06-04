package com.example.dancetimetableapp.datasources

import android.content.Context
import com.example.dancetimetableapp.model.FilterParams
import com.example.dancetimetableapp.model.Lesson

interface DataSource {
    fun getCourses(context: Context): List<String>?
    fun getLessons(context: Context, filterParams: FilterParams): List<Lesson>?
}