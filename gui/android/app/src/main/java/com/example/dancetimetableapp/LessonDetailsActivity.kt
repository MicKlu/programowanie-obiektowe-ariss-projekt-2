package com.example.dancetimetableapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dancetimetableapp.databinding.ActivityLessonDetailsBinding

class LessonDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}