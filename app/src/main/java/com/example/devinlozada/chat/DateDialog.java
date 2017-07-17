package com.example.devinlozada.chat;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import java.util.Locale;

/**
 * Created by djl on 3/07/17.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)

public class DateDialog extends DialogFragment {

    Calendar currentCalender;
    DatePickerDialog datePicker;
    String languageToLoad, dateRange =  "normal";
    int year, month, day;
    long time;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstancestate){

        setComponentLanguage();

        setDateRange();

        return makeDialog();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDateRange() {
        currentCalender = Calendar.getInstance();

        if(getArguments() != null){
            dateRange = (String) getArguments().get("dateRange");
        }

        switch (dateRange){
            case "normal":
                year    = currentCalender.get(Calendar.YEAR);
                month   = currentCalender.get(Calendar.MONTH);
                day     = currentCalender.get(Calendar.DAY_OF_MONTH);


                break;
        }

        currentCalender.set(year,month,day,0,0,0);
        time = currentCalender.getTimeInMillis();

    }

    private void setComponentLanguage() {
        languageToLoad = "es_ES";

        Locale locale  = new Locale(languageToLoad);

        locale.setDefault(locale);

        Configuration config = new Configuration();

        config.locale = locale;

        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private DatePickerDialog makeDialog(){
         datePicker = new DatePickerDialog(getActivity(), R.style.DialogTheme,(DatePickerDialog.OnDateSetListener) getActivity(),
                 year,month,day);

        switch (dateRange){
            case "eightTeenYears":
                datePicker.getDatePicker().setMaxDate(time);
                break;
            case "validUntil":
                datePicker.getDatePicker().setMaxDate(time);

                break;
            case "firstPayDate":
                datePicker.getDatePicker().setMaxDate(time);
                month = currentCalender.get(Calendar.MONTH) + 1;
                currentCalender.set(year,month,day ,0,0,0);
                time = currentCalender.getTimeInMillis();
                datePicker.getDatePicker().setMaxDate(time);
                break;
        }

        datePicker.setTitle("");
        return datePicker;
    }
}
