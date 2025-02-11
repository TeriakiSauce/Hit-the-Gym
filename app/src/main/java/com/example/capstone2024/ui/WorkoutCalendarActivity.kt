package com.example.capstone2024.ui

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.View.OnDragListener
import android.view.View.OnLongClickListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone2024.R
import com.example.capstone2024.adapters.CalendarAdapter
import com.example.capstone2024.models.WorkoutCalendar
import com.example.capstone2024.models.WorkoutPlan
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WorkoutCalendarActivity : AppCompatActivity() {
    private var calendarGrid: GridView? = null
    private var workoutCalendarModel: WorkoutCalendar? = null
    private var workoutSchedule: Map<Date, WorkoutPlan>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_calendar)

        val currentMonthText = findViewById<TextView>(R.id.currentMonth)
        calendarGrid = findViewById(R.id.calendarGrid)

        // Initialize the calendar model and data
        workoutCalendarModel = WorkoutCalendar()
        workoutSchedule =
            workoutCalendarModel!!.getWorkoutPlanForMonth(2025, 0) // Example: January 2025

        // Get current date
        val calendar = Calendar.getInstance()
        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentMonth = monthFormat.format(calendar.time)

        // Set current month in the header
        currentMonthText.text = currentMonth

        // Generate a list of all dates in the current month
        val currentYear = calendar[Calendar.YEAR]
        val currentMonthInt = calendar[Calendar.MONTH]
        val monthDates = getMonthDates(currentYear, currentMonthInt)

        // Set up the adapter
        val today = calendar.time
        val adapter = CalendarAdapter(
            this, monthDates,
            workoutSchedule!!, today
        )
        calendarGrid?.setAdapter(adapter)

        // Array of workout icon IDs and names
        val workoutFrameIds = intArrayOf(
            R.id.workout_day1,
            R.id.workout_day2,
            R.id.workout_day3,
            R.id.workout_day4,
            R.id.workout_day5
        )

        val workoutNames = arrayOf(
            "Day 1 Workout",
            "Day 2 Workout",
            "Day 3 Workout",
            "Day 4 Workout",
            "Day 5 Workout"
        )

        // Set up drag listeners for all workout icons
        for (i in workoutFrameIds.indices) {
            val workoutFrame = findViewById<View>(workoutFrameIds[i])
            workoutFrame.tag = workoutNames[i] // Assign workout name as tag
            workoutFrame.setOnLongClickListener(dragStartListener) // Assign drag listener
        }

        // Set up drop listeners for calendar grid items
        calendarGrid?.setOnItemLongClickListener(OnItemLongClickListener { parent: AdapterView<*>?, view: View, position: Int, id: Long ->
            view.setOnDragListener(dragDropListener)
            true
        })
    }

    private fun getMonthDates(year: Int, month: Int): List<Date> {
        val dates: MutableList<Date> = ArrayList()
        val calendar = Calendar.getInstance()
        calendar[year, month] = 1

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 0 until daysInMonth) {
            dates.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
        }

        return dates
    }

    // Listener for starting drag events
    private val dragStartListener = OnLongClickListener { v: View ->
        val workoutName = v.tag as String // Retrieve the workout name from the tag
        val data = ClipData.newPlainText("workout", workoutName) // Create drag data
        val shadowBuilder = DragShadowBuilder(v) // Create shadow

        // Start drag-and-drop
        if (v.startDragAndDrop(data, shadowBuilder, v, View.DRAG_FLAG_GLOBAL)) {
            v.visibility = View.INVISIBLE // Hide view while dragging
        }
        true
    }

    // Listener for handling drag-and-drop events
    private val dragDropListener =
        OnDragListener { v: View, event: DragEvent ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> return@OnDragListener true
                DragEvent.ACTION_DROP -> {
                    val workout = event.clipData.getItemAt(0).text.toString() // Get workout name
                    handleWorkoutDrop(v, workout)
                    return@OnDragListener true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    val draggedView = event.localState as View
                    if (!event.result) {
                        draggedView.visibility = View.VISIBLE // Restore visibility if not dropped
                    }
                    return@OnDragListener true
                }

                else -> return@OnDragListener false
            }
        }

    // Handle workout drop events
    private fun handleWorkoutDrop(targetView: View, workout: String) {
        Toast.makeText(this, "Dropped: $workout", Toast.LENGTH_SHORT).show()
    }
}