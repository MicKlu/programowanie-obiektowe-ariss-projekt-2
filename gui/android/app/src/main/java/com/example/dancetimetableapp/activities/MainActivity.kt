package com.example.dancetimetableapp.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dancetimetableapp.model.Lesson
import com.example.dancetimetableapp.MainViewModel
import com.example.dancetimetableapp.R
import com.example.dancetimetableapp.adapters.LessonsListAdapter
import com.example.dancetimetableapp.databinding.ActivityMainBinding
import com.example.dancetimetableapp.dialogs.FilterDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModels()

    private val filterDialog by lazy {
        FilterDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val lessonsListAdapter = LessonsListAdapter()
        lessonsListAdapter.clickListener = LessonsListAdapter.OnClickListener {
            val intent = Intent(this, LessonDetailsActivity::class.java)
            intent.putExtra("lesson", it)
            startActivity(intent)
        }
        binding.content.timetableList.adapter = lessonsListAdapter
        binding.content.timetableList.layoutManager = LinearLayoutManager(this)
        binding.content.timetableList.itemAnimator = null

        model.getLessons().observe(this) {
            lessonsListAdapter.data = it
            lessonsListAdapter.notifyDataSetChanged()

            binding.content.emptyState.visibility = if(it.isNotEmpty()) View.GONE else View.VISIBLE

            binding.content.swipeRefresh.isRefreshing = false
        }

        binding.content.swipeRefresh.setOnRefreshListener {
            model.loadLessons()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_refresh) {
            binding.content.swipeRefresh.isRefreshing = true
            model.loadLessons()
            return true
        }

        if(item.itemId == R.id.action_filter) {
            filterDialog.show(supportFragmentManager, "filterDialog", model.filterParams)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
