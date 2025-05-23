package com.example.capstone2024.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.widget.BaseAdapter;

import com.example.capstone2024.R;
import com.example.capstone2024.models.WorkoutPlan;
import com.example.capstone2024.models.WorkoutSession;
import com.example.capstone2024.ui.WorkoutCalendarActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarAdapter extends BaseAdapter {

    private final Context context;
    private final List<Date> dates; // All dates to display
    private final Map<Date, WorkoutSession> workoutSchedule; // Map of dates to workouts
    private final LayoutInflater inflater;
    private final Date today; // Today's date

    public CalendarAdapter(Context context, List<Date> dates, Map<Date, WorkoutSession> workoutSchedule, Date today) {
        this.context = context;
        this.dates = dates;
        this.workoutSchedule = workoutSchedule;
        this.inflater = LayoutInflater.from(context);
        this.today = today;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_calendar_day, parent, false);
        }

        // Get the date for this cell
        Date date = dates.get(position);

        // Set the date as the tag so it can be retrieved later
        convertView.setTag(date);

        convertView.setOnDragListener(((WorkoutCalendarActivity) context).getDragDropListener());

        TextView dateText = convertView.findViewById(R.id.dateText);
        TextView workoutInfo = convertView.findViewById(R.id.workoutInfo);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.getDefault());
        dateText.setText(dateFormat.format(date));

        // Highlight today's date
        if (isSameDay(date, today)) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }

        WorkoutSession workoutSession = workoutSchedule.get(date);
        // Set workout information
        if (workoutSession != null) {
            workoutInfo.setText("Workout Assigned");
        } else {
            workoutInfo.setText("");
        }

        return convertView;
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}