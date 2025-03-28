package com.example.capstone2024.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.DragEvent;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.ClipData;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone2024.R;
import com.example.capstone2024.adapters.CalendarAdapter;
import com.example.capstone2024.contracts.WorkoutCalendarContract;
import com.example.capstone2024.database.UserSetupDatabaseHelper;
import com.example.capstone2024.database.WorkoutSessionWithExercises;
import com.example.capstone2024.models.WorkoutCalendar;
import com.example.capstone2024.models.WorkoutPlan;
import com.example.capstone2024.models.WorkoutSession;
import com.example.capstone2024.presenters.WorkoutCalendarPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WorkoutCalendarActivity extends AppCompatActivity implements WorkoutCalendarContract.View {

    private GridView calendarGrid;
    private WorkoutCalendar workoutCalendarModel;
    private Map<Date, WorkoutPlan> workoutSchedule;

    private WorkoutCalendarPresenter workoutCalendarPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_calendar);

        workoutCalendarPresenter = new WorkoutCalendarPresenter(this, new WorkoutCalendar());
        // References for current month section
        TextView currentMonthText = findViewById(R.id.currentMonth);
        GridView calendarGrid = findViewById(R.id.calendarGrid);

        // References for next month section
        TextView nextMonthText = findViewById(R.id.nextMonth);
        GridView calendarGridNext = findViewById(R.id.calendarGridNext);

        // Initialize the calendar model
        WorkoutCalendar workoutCalendarModel = new WorkoutCalendar();

        // Get today's date
        Calendar todayCal = Calendar.getInstance();
        Date today = todayCal.getTime();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        //Setup Current Month
        int currentYear = todayCal.get(Calendar.YEAR);
        int currentMonthInt = todayCal.get(Calendar.MONTH);
        currentMonthText.setText(monthFormat.format(todayCal.getTime()));
        List<Date> currentMonthDates = getMonthDates(currentYear, currentMonthInt);
        // Retrieve current month schedule from the model
        Map<Date, WorkoutSession> currentMonthSchedule = workoutCalendarModel.getWorkoutPlanForMonth(currentYear, currentMonthInt);
        CalendarAdapter adapterCurrent = new CalendarAdapter(this, currentMonthDates, currentMonthSchedule, today);
        calendarGrid.setAdapter(adapterCurrent);

        // Setup Next Month
        Calendar nextMonthCal = Calendar.getInstance();
        nextMonthCal.add(Calendar.MONTH, 1); // Move to next month
        int nextMonthYear = nextMonthCal.get(Calendar.YEAR);
        int nextMonthInt = nextMonthCal.get(Calendar.MONTH);
        nextMonthText.setText(monthFormat.format(nextMonthCal.getTime()));
        List<Date> nextMonthDates = getMonthDates(nextMonthYear, nextMonthInt);
        Map<Date, WorkoutSession> nextMonthSchedule = workoutCalendarModel.getWorkoutPlanForMonth(nextMonthYear, nextMonthInt);
        CalendarAdapter adapterNext = new CalendarAdapter(this, nextMonthDates, nextMonthSchedule, today);
        calendarGridNext.setAdapter(adapterNext);

        // Set up footer with workout icons
        LinearLayout footerContainer = findViewById(R.id.footerContainer);
        UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(this);

        // Retrieve the workout program
        Map<String, WorkoutSessionWithExercises> workoutProgram = helper.getStoredWorkoutProgram();
        footerContainer.removeAllViews();

        // Iterate through the workout sessions and inflate a view
        for (String dayName : workoutProgram.keySet()) {
            View workoutDayView = getLayoutInflater().inflate(R.layout.calendar_workout_icon, footerContainer, false);

            // Set the workout day name on the TextView
            TextView dayText = workoutDayView.findViewById(R.id.workout_day_text);
            dayText.setText(dayName.replace("Workout", "Day"));

            // Attach the drag start listener
            workoutDayView.setOnLongClickListener(dragStartListener);

            // Add the dynamically created view to the container
            footerContainer.addView(workoutDayView);
        }
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
            case DragEvent.ACTION_DROP:
                // Retrieve the workout name from the drag data
                String workoutName = event.getClipData().getItemAt(0).getText().toString();

                // Retrieve the date associated with the target view (set in getView of the adapter)
                Date targetDate = (Date) v.getTag();

                if (targetDate != null) {
                    UserSetupDatabaseHelper helper = new UserSetupDatabaseHelper(this);
                    int day = Integer.parseInt(workoutName.replace("Workout ", "").trim());
                    // Retrieve or create the WorkoutPlan corresponding to workoutName
                    WorkoutSession workoutSession = helper.getWorkoutSessionWithExercisesByDay(day).getWorkoutSession();

                    // Assign the workout plan to the target date via the presenter
                    workoutCalendarPresenter.assignWorkoutToDay(targetDate, workoutSession);
                }

                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                View draggedView = (View) event.getLocalState();
                if (!event.getResult()) {
                    draggedView.setVisibility(View.VISIBLE);
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

    public View.OnDragListener getDragDropListener() {
        return dragDropListener;
    }

    public void updateCalendar() {
        return;
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}