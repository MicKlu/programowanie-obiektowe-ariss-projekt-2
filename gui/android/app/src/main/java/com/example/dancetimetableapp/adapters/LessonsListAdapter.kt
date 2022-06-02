package com.example.dancetimetableapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dancetimetableapp.model.Lesson
import com.example.dancetimetableapp.databinding.LessonListItemBinding

class LessonsListAdapter: ListAdapter<Lesson, LessonsListAdapter.ViewHolder>(DiffCallback()) {

    var clickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LessonListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = currentList[position]
        holder.binding.lessonCourse.text = lesson.course
        holder.binding.lessonDay.text = lesson.day
        holder.binding.lessonTimeStart.text = lesson.timeStart
        holder.binding.lessonTimeEnd.text = lesson.timeEnd
        holder.itemView.setOnClickListener {
            clickListener?.onClick(lesson)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    private class DiffCallback : DiffUtil.ItemCallback<Lesson>() {

        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson) =
            oldItem.hash() == newItem.hash()

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson) =
            oldItem.hash() == newItem.hash()
    }

    inner class ViewHolder(val binding: LessonListItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun interface OnClickListener {
        fun onClick(lesson: Lesson)
    }
}