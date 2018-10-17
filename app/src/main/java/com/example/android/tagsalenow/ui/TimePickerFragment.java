package com.example.android.tagsalenow.ui;
/*
This class comes from here:
https://developer.android.com/guide/topics/ui/controls/pickers#java

 */
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TimePickedListener timePickedListener;
    public static interface TimePickedListener {
        void onTimePicked(String time);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        timePickedListener = (TimePickedListener) getActivity();
        // Create a new instance of TimePickerDia1log and return it
     //   return new TimePickerDialog(getActivity(), this, hour, minute,
     //           DateFormat.is24HourFormat(getActivity()));

        return new TimePickerDialog(getActivity(), this, hour, minute,
                true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        String myTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        timePickedListener.onTimePicked(myTime);

    }
}
