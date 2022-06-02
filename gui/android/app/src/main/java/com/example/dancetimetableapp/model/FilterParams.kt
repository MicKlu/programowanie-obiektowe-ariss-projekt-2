package com.example.dancetimetableapp.model

data class FilterParams(
    var day: String = "",
    var time: String = "",
    var course: String = ""
) {

    fun matchLesson(lesson: Lesson): Boolean {
        if(day.isNotEmpty() && !lesson.day.matches(Regex(day)))
            return false

        if(time.isNotEmpty()) {
            val t = if (time.toInt() > 0) time else "0$time"
            if(!lesson.timeStart.matches(Regex("^$t:\\d\\d\$")) && !lesson.timeEnd.matches(Regex("^$t:\\d\\d\$")))
                return false
        }

        if(course.isNotEmpty() && !lesson.course.matches(Regex(course)))
            return false

        return true
    }

    fun areEmpty(): Boolean {
        if(this == FilterParams())
            return true
        return false
    }

}
