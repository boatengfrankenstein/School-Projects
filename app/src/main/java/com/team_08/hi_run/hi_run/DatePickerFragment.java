package com.team_08.hi_run.hi_run;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private EditText dateset;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        View v = getActivity().getLayoutInflater().inflate(R.layout.datepicks, null);

        // Create a new instance of DatePickerDialog and return it
        /*DatePickerDialog m = new DatePickerDialog(getActivity(), this, year, month, day);
        m.getDatePicker().setSpinnersShonwn(true);
        m.getDatePicker().setCalendarViewShown(false);*/

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.datepicks_picker);
        datePicker.init(year,month,day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar time = Calendar.getInstance();
                String am_pm, timeformat;

                time.set(Calendar.MONTH, monthOfYear);
                time.set(Calendar.DATE, dayOfMonth);
                time.set(Calendar.YEAR, year);

                timeformat = (String) android.text.format.DateFormat.format("M-dd-yyyy", time);
                dateset.setText(timeformat);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v).setNegativeButton("Cancel", null)
                .setPositiveButton("Set", null)
                .create();
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar time = Calendar.getInstance();
        String am_pm, timeformat;

        time.set(Calendar.MONTH, month);
        time.set(Calendar.DATE, day);
        time.set(Calendar.YEAR, year);

        timeformat = (String) android.text.format.DateFormat.format("M/dd/yyyy", time);
        dateset.setText(timeformat);
    }


    @Override
    public void registerForContextMenu(View v)
    {
        dateset = (EditText) v;
    }

}
