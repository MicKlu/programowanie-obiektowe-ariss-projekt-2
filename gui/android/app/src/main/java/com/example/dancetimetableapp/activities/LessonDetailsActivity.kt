package com.example.dancetimetableapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dancetimetableapp.model.Lesson
import com.example.dancetimetableapp.databinding.ActivityLessonDetailsBinding

class LessonDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val lesson = intent.getSerializableExtra("lesson") as Lesson

        binding.content.course.text = lesson.course
        binding.content.day.text = lesson.day
        binding.content.timeStart.text = lesson.timeStart
        binding.content.timeEnd.text = lesson.timeEnd
        binding.content.level.text = lesson.level
        binding.content.instructor.text = lesson.instructor

        val notes = ArrayList<String>()
        for(s in lesson.notes)
             notes.add("• $s")

        binding.content.notes.text = notes.joinToString("\n")
        binding.content.enrollment.text = lesson.enrollment

    }

}
