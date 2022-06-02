package com.example.dancetimetableapp.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import com.example.dancetimetableapp.R
import com.example.dancetimetableapp.databinding.ActivityLessonDetailsBinding
import com.example.dancetimetableapp.model.Lesson
import com.example.dancetimetableapp.receivers.LessonReminderReceiver
import java.util.*

class LessonDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLessonDetailsBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var lesson: Lesson

    private lateinit var notificationIntent: Intent

    private var reminderEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lesson = intent.getSerializableExtra("lesson") as Lesson

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_lesson_details, menu)

        notificationIntent = Intent(applicationContext, LessonReminderReceiver::class.java).also {
            it.putExtra("lesson", lesson)
            it.action = "com.example.dancetimetableapp.SetReminder${lesson.hash()}"
        }

        getPendingIntent(notificationIntent, PendingIntent.FLAG_NO_CREATE)?.let {
            menu.findItem(R.id.action_remind).icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_notifications_off_24)
            reminderEnabled = true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_remind) {
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if(!reminderEnabled) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms())
                    return true

                setReminder()
                item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_notifications_off_24)
            } else {
                unsetReminder()
                item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_notifications_24)
            }

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getPendingIntent(intent: Intent, _flags: Int = 0): PendingIntent? {
        val flags = _flags or
                PendingIntent.FLAG_ONE_SHOT or
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        return PendingIntent.getBroadcast(applicationContext, 0, intent, flags)
    }

    private fun setReminder() {
        val day = resources.getStringArray(R.array.days).asList().indexOf(lesson.day) + 1
        val hour = lesson.timeStart.split(":")[0].toInt()
        val minute = lesson.timeStart.split(":")[1].toInt()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.DAY_OF_WEEK, day)
            add(Calendar.MINUTE, -15)
        }

        val now = Calendar.getInstance()
        if(now > calendar)
            calendar.add(Calendar.DAY_OF_MONTH, 7)

        val notificationPendingIntent = getPendingIntent(notificationIntent)
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC, calendar.timeInMillis, notificationPendingIntent!!)

        reminderEnabled = true
    }

    private fun unsetReminder() {
        val notificationPendingIntent = getPendingIntent(notificationIntent)
        alarmManager.cancel(notificationPendingIntent)
        notificationPendingIntent?.cancel()
        reminderEnabled = false
    }

}
