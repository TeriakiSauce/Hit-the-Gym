package com.example.capstone2024.ui;

import android.os.Bundle;
import android.view.View;
import android.view.DragEvent;
import android.widget.GridView;
import android.widget.TextView;
import android.content.ClipData;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.adapters.CalendarAdapter;
import com.example.capstone2024.models.WorkoutCalendar;
import com.example.capstone2024.models.WorkoutPlan;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WorkoutCalendarActivity extends AppCompatActivity {

    private GridView calendarGrid;
    private WorkoutCalendar workoutCalendarModel;
    private Map<Date, WorkoutPlan> workoutSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_calendar);

        TextView currentMonthText = findViewById(R.id.currentMonth);
        calendarGrid = findViewById(R.id.calendarGrid);

        // Initialize the calendar model and data
        workoutCalendarModel = new WorkoutCalendar();
        workoutSchedule = workoutCalendarModel.getWorkoutPlanForMonth(2025, 0); // Example: January 2025

        // Get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String currentMonth = monthFormat.format(calendar.getTime());

        // Set current month in the header
        currentMonthText.setText(currentMonth);

        // Generate a list of all dates in the current month
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonthInt = calendar.get(Calendar.MONTH);
        List<Date> monthDates = getMonthDates(currentYear, currentMonthInt);

        // Set up the adapter
        Date today = calendar.getTime();
        CalendarAdapter adapter = new CalendarAdapter(this, monthDates, workoutSchedule, today);
        calendarGrid.setAdapter(adapter);

        // Array of workout icon IDs and names
        int[] workoutFrameIds = {
                R.id.workout_day1,
                R.id.workout_day2,
                R.id.workout_day3,
                R.id.workout_day4,
                R.id.workout_day5
        };

        String[] workoutNames = {
                "Day 1 Workout",
                "Day 2 Workout",
                "Day 3 Workout",
                "Day 4 Workout",
                "Day 5 Workout"
        };

        // Set up drag listeners for all workout icons
        for (int i = 0; i < workoutFrameIds.length; i++) {
            View workoutFrame = findViewById(workoutFrameIds[i]);
            workoutFrame.setTag(workoutNames[i]); // Assign workout name as tag
            workoutFrame.setOnLongClickListener(dragStartListener); // Assign drag listener
        }

        // Set up drop listeners for calendar grid items
        calendarGrid.setOnItemLongClickListener((parent, view, position, id) -> {
            view.setOnDragListener(dragDropListener);
            return true;
        });
    }

    private List<Date> getMonthDates(int year, int month) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysInMonth; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        return dates;
    }
    // Listener for starting drag events
    private final View.OnLongClickListener dragStartListener = v -> {
        String workoutName = (String) v.getTag(); // Retrieve the workout name from the tag
        ClipData data = ClipData.newPlainText("workout", workoutName); // Create drag data
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v); // Create shadow

        // Start drag-and-drop
        if (v.startDragAndDrop(data, shadowBuilder, v, View.DRAG_FLAG_GLOBAL)) {
            v.setVisibility(View.INVISIBLE); // Hide view while dragging
        }
        return true;
    };

    // Listener for handling drag-and-drop events
    private final View.OnDragListener dragDropListener = (v, event) -> {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;
            case DragEvent.ACTION_DROP:
                String workout = event.getClipData().getItemAt(0).getText().toString(); // Get workout name
                handleWorkoutDrop(v, workout);
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                View draggedView = (View) event.getLocalState();
                if (!event.getResult()) {
                    draggedView.setVisibility(View.VISIBLE); // Restore visibility if not dropped
                }
                return true;
            default:
                return false;
        }
    };

    // Handle workout drop events
    private void handleWorkoutDrop(View targetView, String workout) {
        Toast.makeText(this, "Dropped: " + workout, Toast.LENGTH_SHORT).show();
    }
}