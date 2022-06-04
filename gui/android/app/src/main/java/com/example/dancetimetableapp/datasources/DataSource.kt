package com.example.dancetimetableapp.datasources

import android.content.Context
import androidx.lifecycle.LiveData

interface DataSource {
    fun getCourses(context: Context): List<String>?
}