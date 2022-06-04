package com.example.dancetimetableapp.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dancetimetableapp.R
import com.example.dancetimetableapp.activities.LessonDetailsActivity
import com.example.dancetimetableapp.model.Lesson

class LessonReminderReceiver : BroadcastReceiver() {

    private val notificationChannelId = "remind_channel"

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)

        val lesson = intent.getSerializableExtra("com.example.dancetimetableapp.ExtraLesson") as Lesson

        val detailsIntent = Intent(context.applicationContext, LessonDetailsActivity::class.java).let {
            it.putExtra("com.example.dancetimetableapp.ExtraLesson", lesson)
            it.action = "com.example.dancetimetableapp.ShowLessonDetails${lesson.hash()}"
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val flag = PendingIntent.FLAG_UPDATE_CURRENT or
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

            PendingIntent.getActivity(context.applicationContext, 0, it, flag)
        }

        val notification = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(context.resources.getString(R.string.lesson_starts_soon))
            .setContentText(context.resources.getString(R.string.lesson_starts_soon_desc, lesson.course, lesson.timeStart, lesson.timeEnd))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(detailsIntent)
            .build()

        with(NotificationManagerCompat.from(context)) {
            this.notify("com.example.dancetimetableapp.LessonReminderNotification${lesson.hash()}", 0, notification)
        }
    }

    private fun createNotificationChannel(context: Context) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val name = context.resources.getString(R.string.notification_channel_remind)
        val desc = context.resources.getString(R.string.notification_channel_remind_desc)

        val channel = NotificationChannel(notificationChannelId, name, NotificationManager.IMPORTANCE_HIGH).apply {
            description = desc
            setShowBadge(true)
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}