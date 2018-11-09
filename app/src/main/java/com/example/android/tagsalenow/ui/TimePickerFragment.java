package com.example.android.tagsalenow.ui;
/*
This class comes from here:
https://developer.android.com/guide/topics/ui/controls/pickers#java

 */
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.example.android.tagsalenow.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TimePickedListener timePickedListener;
    public interface TimePickedListener {
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
      //  return new TimePickerDialog(getActivity(), R.style.MyTimePickerDialogStyle, this, hour, minute, false); //false = NOT 24hr view
        return new TimePickerDialog(getActivity(), this, hour, minute, false); //false = NOT 24hr view
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        String myTime = String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            Date d = sdf.parse(myTime);
            myTime = new SimpleDateFormat("K:mm a").format(d);
        } catch (ParseException ex){
            Log.e("TimePickerFragment", "onTimeSet: "+ex.getMessage() );
        }
        timePickedListener.onTimePicked(myTime);

    }
}
