package com.example.dancetimetableapp.datasources

import android.content.Context
import android.util.Log
import com.example.dancetimetableapp.R
import com.example.dancetimetableapp.model.FilterParams
import com.example.dancetimetableapp.model.Lesson
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class CachedDataSource(private val fileName: String) : DataSource {

    override fun getCourses(context: Context): List<String>? {
        val list = arrayListOf(context.resources.getString(R.string.any))
        readListData(context)?.let {
            list.addAll(it)
        } ?: return null
        return list
    }

    override fun getLessons(context: Context, filterParams: FilterParams): List<Lesson>? {
        val list = ArrayList<Lesson>()
        readListData(context)?.let { rows ->
            rows.forEach {
                val lesson = Lesson(it)
                if(filterParams.matchLesson(lesson))
                    list.add(lesson)
            }
        } ?: return null
        return list
    }

    fun isUpToDate(context: Context, time: Long = 3600): Boolean {
        val now = Date().time
        val mtime = getCacheFile(context).lastModified()
        val delta: Long = (now - mtime) / 1000

        if(delta >= time) // 1 hour by default
            return false

        return true
    }

    fun cacheListData(context: Context, list: List<*>) {
        getCacheFile(context).parentFile?.mkdirs()
        getCacheFile(context).bufferedWriter().use {
            for(row in list)
                it.appendLine(row.toString())
        }
    }

    fun exists(context: Context): Boolean {
        return getCacheFile(context).exists()
    }

    private fun readListData(context: Context): List<String>? {
        val file = getCacheFile(context)
        if(!file.exists())
            return null

        val list = ArrayList<String>()
        file.bufferedReader().use { reader ->
            reader.forEachLine {
                list.add(it)
            }
        }
        return list
    }

    private fun getCacheFile(context: Context): File {
        return context.cacheDir.resolve("$fileName.csv")
    }
}