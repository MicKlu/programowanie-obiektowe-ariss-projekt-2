package com.example.dancetimetableapp

import org.json.JSONObject
import java.io.Serializable

class Lesson: Serializable {

    var day: String
    var time: String
    var course: String
    var level: String
    var notes: ArrayList<String>
    var instructor: String
    var enrollment: String

    val timeStart: String
        get() = time.split(" - ")[0]

    val timeEnd: String
        get() = time.split(" - ")[1]

    constructor(json: JSONObject) {
        day = "Poniedziałek"
        time = "16:50 - 17:50"
        course = "SALSA SOLO"
        level = "P2"
        notes = arrayListOf("kurs trwa", "można dołączyć", "✅ MULTISPORT")
        instructor = "Agnieszka"
        enrollment = "WOLNE MIEJSCA"
    }

}
