package com.example.android.tagsalenow.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private DatePickerListener datePickerListener;

    public interface DatePickerListener {
        void onDatePicked(String dateString);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         int thisYear = Calendar.getInstance().get(Calendar.YEAR);
         int thisMonth = Calendar.getInstance().get(Calendar.MONTH);
        int thisDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        datePickerListener = (DatePickerListener) getActivity();
        return new DatePickerDialog(getActivity(), this, thisYear, thisMonth, thisDay);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            //note: month = 0-11
            //note: dayOfMonth : 1-31
            String outDate = String.format("%4d",year) + "-"+ String.format("%02d", (month + 1)) +"-" + String.format("%02d", dayOfMonth);
            datePickerListener.onDatePicked(outDate);
    }
}
