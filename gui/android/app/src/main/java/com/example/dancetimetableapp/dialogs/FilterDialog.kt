package com.example.dancetimetableapp.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.dancetimetableapp.*
import com.example.dancetimetableapp.databinding.FragmentFilterBinding
import com.example.dancetimetableapp.model.FilterParams

class FilterDialog : DialogFragment() {

    private lateinit var binding: FragmentFilterBinding
    private val model: MainViewModel by activityViewModels()

    private var filterParams: FilterParams? = null
    private var updatingCourses = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentFilterBinding.inflate(layoutInflater)

        filterParams?.let {
            model.dialogFilterParams = it.copy()
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.filter)
            builder.setView(binding.root)

            var dayPos = resources.getStringArray(R.array.days).asList().indexOf(model.filterParams.day)
            var timePos = resources.getStringArray(R.array.hours).asList().indexOf(model.filterParams.time)

            if(dayPos == -1)
                dayPos = 0

            if(timePos == -1)
                timePos = 0

            binding.day.setSelection(dayPos)
            binding.hour.setSelection(timePos)

            binding.course.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(updatingCourses)
                       return

                    model.dialogFilterParams.course = binding.course.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            model.getCourses().observe(this) { list ->
                updatingCourses = true
                val adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, list)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.course.adapter = adapter

                var coursePos = list.indexOf(model.dialogFilterParams.course)

                if(coursePos == -1)
                    coursePos = 0

                binding.course.setSelection(coursePos)
                updatingCourses = false
            }

            builder.setPositiveButton(R.string.save) { dialog, _ ->
                model.filterParams.day = binding.day.selectedItem.toString()
                if(binding.day.selectedItemPosition == 0)
                    model.filterParams.day = ""

                model.filterParams.time = binding.hour.selectedItem.toString()
                if(binding.hour.selectedItemPosition == 0)
                    model.filterParams.time = ""

                model.filterParams.course = binding.course.selectedItem.toString()
                if(binding.course.selectedItemPosition == 0)
                    model.filterParams.course = ""

                model.dialogFilterParams = model.filterParams.copy()
                dialog.dismiss()
            }
            builder.setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
        } ?: throw IllegalStateException("Null Activity")
    }

    fun show(manager: FragmentManager, tag: String?, filterParams: FilterParams) {
        this.filterParams = filterParams.copy()
        super.show(manager, tag)
    }
}