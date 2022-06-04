package com.example.dancetimetableapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dancetimetableapp.model.Lesson
import com.example.dancetimetableapp.databinding.LessonListItemBinding

class LessonsListAdapter(var data: ArrayList<Lesson>): RecyclerView.Adapter<LessonsListAdapter.ViewHolder>() {

    var clickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LessonListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = data[position]
        holder.binding.lessonCourse.text = lesson.course
        holder.binding.lessonDay.text = lesson.day
        holder.binding.lessonTimeStart.text = lesson.timeStart
        holder.binding.lessonTimeEnd.text = lesson.timeEnd
        holder.itemView.setOnClickListener {
            clickListener?.onClick(lesson)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: LessonListItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun interface OnClickListener {
        fun onClick(lesson: Lesson)
    }
}