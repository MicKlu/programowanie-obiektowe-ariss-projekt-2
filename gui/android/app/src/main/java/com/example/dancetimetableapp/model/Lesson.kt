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

//    constructor(csv: String) {
//
//    }

}
