package com.example.capstone2024.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.capstone2024.R
import com.example.capstone2024.models.WorkoutPlan
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarAdapter(
    private val context: Context, // All dates to display
    private val dates: List<Date>, // Map of dates to workouts
    private val workoutSchedule: Map<Date, WorkoutPlan>, // Today's date
    private val today: Date
) :
    BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return dates.size
    }

    override fun getItem(position: Int): Any {
        return dates[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_calendar_day, parent, false)
        }

        val dateText = convertView!!.findViewById<TextView>(R.id.dateText)
        val workoutInfo = convertView.findViewById<TextView>(R.id.workoutInfo)

        val date = dates[position]
        val workoutPlan = workoutSchedule[date]

        // Format and set date
        val dateFormat = SimpleDateFormat("d", Locale.getDefault())
        dateText.text = dateFormat.format(date)

        // Highlight today's date
        if (isSameDay(date, today)) {
            convertView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.holo_green_light
                )
            )
        } else {
            convertView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.transparent
                )
            )
        }

        // Set workout information
        if (workoutPlan != null) {
            workoutInfo.text = "Workout Assigned"
        } else {
            workoutInfo.text = ""
        }

        return convertView
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2

        return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] &&
                cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
    }
}