package neoncore.com.servi.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import neoncore.com.servi.Utils.SharedViewmodel;

/**
 * Created by Musa on 3/21/2018.
 */

public class DueDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private SharedViewmodel viewmodel;

    public static final String DUE_PICKER = "DUE_DATE_PICKER";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewmodel = ViewModelProviders.of(getActivity()).get(SharedViewmodel.class);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        //set the date to the viewmodel
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);
        //i can get difference between the date gotten and the current date here and pass a string
        //so the addTaskfragment can jus observe a string;
        Date currDate = Calendar.getInstance().getTime();
        Date nuDate = c.getTime();

        long diff = nuDate.getTime() - currDate.getTime();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);
        int x = Float.floatToIntBits(dayCount);

        Log.d("Days Gotten",x + " ");



        viewmodel.setDate(String.valueOf(dayCount));
        viewmodel.selectNuDate(currDate);
    }
}
