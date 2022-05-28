package com.example.dancetimetableapp.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.dancetimetableapp.R
import com.example.dancetimetableapp.databinding.FragmentFilterBinding

class FilterDialog : DialogFragment() {

    private lateinit var binding: FragmentFilterBinding

    var day: String = ""
    var time: String = ""
    var course: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = FragmentFilterBinding.inflate(layoutInflater)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.filter)
            builder.setView(binding.root)

            var dayPos = resources.getStringArray(R.array.days).asList().indexOf(day)
            var timePos = resources.getStringArray(R.array.hours).asList().indexOf(time)
            var coursePos = resources.getStringArray(R.array.courses).asList().indexOf(course)

            if(dayPos == -1)
                dayPos = 0

            if(timePos == -1)
                timePos = 0

            if(coursePos == -1)
                coursePos = 0

            binding.day.setSelection(dayPos)
            binding.hour.setSelection(timePos)
            binding.course.setSelection(coursePos)

            builder.setPositiveButton(R.string.save) { dialog, _ ->
                day = binding.day.selectedItem.toString()
                if(binding.day.selectedItemPosition == 0)
                    day = ""

                time = binding.hour.selectedItem.toString()
                course = binding.course.selectedItem.toString()
                dialog.dismiss()
            }
            builder.setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
        } ?: throw IllegalStateException("Null Activity")
    }
}