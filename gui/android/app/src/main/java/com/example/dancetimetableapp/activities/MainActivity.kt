package com.example.dancetimetableapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dancetimetableapp.Lesson
import com.example.dancetimetableapp.R
import com.example.dancetimetableapp.adapters.LessonsListAdapter
import com.example.dancetimetableapp.databinding.ActivityMainBinding
import com.example.dancetimetableapp.dialogs.FilterDialog
import org.json.JSONObject
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val filterDialog = FilterDialog()
    private val lessons = ArrayList<Lesson>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val lessonsListAdapter = LessonsListAdapter(lessons)
        lessonsListAdapter.clickListener = LessonsListAdapter.OnClickListener {
            val intent = Intent(this, LessonDetailsActivity::class.java)
            intent.putExtra("lesson", it)
            startActivity(intent)
        }

        binding.content.timetableList.adapter = lessonsListAdapter
        binding.content.timetableList.layoutManager = LinearLayoutManager(this)
        binding.content.timetableList.itemAnimator = null

        binding.content.swipeRefresh.setOnRefreshListener {
            refreshTimetable()
        }

        refreshTimetable()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_refresh) {
            binding.content.swipeRefresh.isRefreshing = true
            refreshTimetable()
            return true
        }

        if(item.itemId == R.id.action_filter) {
            filterDialog.show(supportFragmentManager, "filterDialog")
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshTimetable() {
        val executor = Executors.newSingleThreadExecutor()
        executor.submit() {
            // TODO: Fetch JSON here

            val exampleLesson1 = Lesson(JSONObject("{}"))
            val exampleLesson2 = Lesson(JSONObject("{}"))
            val exampleLesson3 = Lesson(JSONObject("{}"))

            exampleLesson2.course = "FORMACJA DANCEHALL"
            exampleLesson3.course = "TANIEC UŻYTKOWY"

            lessons.clear()
            lessons.addAll(arrayListOf(exampleLesson1, exampleLesson2, exampleLesson3))
            binding.content.timetableList.adapter?.notifyDataSetChanged()

            binding.content.swipeRefresh.isRefreshing = false
        }
    }

}
