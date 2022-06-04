package com.example.dancetimetableapp.datasources

import android.content.Context
import android.util.Log
import com.example.dancetimetableapp.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RemoteDataSource : DataSource {

    private fun doRequest(spec: String): String {
        val url = URL(spec)
        val conn = url.openConnection() as HttpURLConnection
        val reader = BufferedReader(InputStreamReader(conn.inputStream))
        val result = StringBuilder()

        do {
            val line = reader.readLine()
            result.append(line)
        } while(line != null)

        reader.close()

        return result.toString()
    }

    override fun getCourses(context: Context): List<String>? {
        val endpoint = context.resources.getString(R.string.endpoint, "kursy")

        val coursesList = arrayListOf(context.resources.getString(R.string.any))

        try {
            val result = doRequest(endpoint)
            val json = JSONObject(result)
            val courses = json.getJSONArray("kursy")
            repeat(courses.length()) { i ->
                coursesList.add(courses.getString(i))
            }

            val cachedDataSource = CachedDataSource("courses")
            cachedDataSource.cacheListData(
                context, coursesList.subList(1, coursesList.size - 1))

        } catch (e: IOException) {
            Log.w("dta", e.stackTraceToString())
            return null
        }

        return coursesList
    }
}