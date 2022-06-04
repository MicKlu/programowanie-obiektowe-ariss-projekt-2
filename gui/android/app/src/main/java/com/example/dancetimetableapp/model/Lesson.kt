package com.example.dancetimetableapp.model

import org.json.JSONObject
import java.io.Serializable

class Lesson: Serializable {

    var day: String
    var time: String
    var course: String
    var level: String
    var notes = ArrayList<String>()
    var instructor: String
    var enrollment: String

    val timeStart: String
        get() = time.split(" - ")[0]

    val timeEnd: String
        get() = time.split(" - ")[1]

    constructor(json: JSONObject) {
        day = json.getString("dzien")
        time = json.getString("godziny")
        course = json.getString("kurs")
        level = json.getString("poziom")

        val notesJSONArray = json.getJSONArray("uwagi")
        repeat(notesJSONArray.length()) { i ->
            notes.add(notesJSONArray.getString(i))
        }

        instructor = json.getString("instruktor")
        enrollment = json.getString("zapisy")
    }

    constructor(csv: String) {
        val list = csv.substring(1, csv.length - 1).split("\",\"").map {
            it.replace("\"\"", "\"")
        }
        day = list[0]
        time = list[1]
        course = list[2]
        level = list[3]
        notes.addAll(list[4].substring(1, list[4].length - 1).split(", ").map {
            it.substring(1, it.length - 1)
        })
        instructor = list[5]
        enrollment = list[6]
    }

    override fun toString(): String {
        val list = arrayListOf(
            day,
            time,
            course,
            level,
            notes.map { "'${it}'" }.toString(),
            instructor,
            enrollment
        )
        return list.joinToString(",") {
            "\"${it.replace("\"", "\"\"")}\""
        }
    }

    fun hash(): Int {
        return toString().hashCode()
    }
}
