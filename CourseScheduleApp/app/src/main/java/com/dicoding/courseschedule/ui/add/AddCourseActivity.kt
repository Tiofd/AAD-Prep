package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION")
class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var addCourseViewModel: AddCourseViewModel
    private var start: String = ""
    private var end: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.title = getString(R.string.add_course)

        val factory = AddCourseViewModelFactory.createFactory(this)
        addCourseViewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        addCourseViewModel.saved.observe(this){
            if (it.getContentIfNotHandled() ==  true){
                Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
                onBackPressed()
            } else {
                Toast.makeText(this, "Course Name and Time must be filled", Toast.LENGTH_SHORT).show()
            }
        }

        val startTime = findViewById<ImageButton>(R.id.btn_startTime)
        val endTime = findViewById<ImageButton>(R.id.btn_endTime)

        startTime.setOnClickListener{
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, "start")
        }
        endTime.setOnClickListener{
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, "end")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_insert ->{
                val courseName = findViewById<TextInputEditText>(R.id.et_add_course).text.toString().trim()
                val day = findViewById<Spinner>(R.id.spinner_day).selectedItemPosition
                val lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString().trim()
                val note =  findViewById<TextInputEditText>(R.id.ed_note).text.toString().trim()

                addCourseViewModel.insertCourse(courseName, day, start, end, lecturer, note)
                true
            } else ->{
                super.onOptionsItemSelected(item)
            }
        }
    }


    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        when(tag){
            "start" ->{
                findViewById<TextView>(R.id.edt_startTime).text = format.format(calendar.time)
                start = format.format(calendar.time)
            }
            "end" ->{
                findViewById<TextView>(R.id.edt_endTime).text = format.format(calendar.time)
                end = format.format(calendar.time)
            }
        }
    }


}