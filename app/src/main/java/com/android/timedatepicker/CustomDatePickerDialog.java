package com.android.timedatepicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.DatePicker;

import java.util.Calendar;

public class CustomDatePickerDialog extends DatePickerDialog {

    int minYear;
    int minMonth;
    int minDay;

    public CustomDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    public void setMinDate(long minDate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getDatePicker().setMinDate(System.currentTimeMillis());
        } else {
            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(minDate);
            minYear = c.get(Calendar.YEAR);
            minMonth = c.get(Calendar.MONTH);
            minDay = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.onDateChanged(view, year, monthOfYear, dayOfMonth);
        } else {
            if (year > minYear)
                view.updateDate(minYear, minMonth, minDay);

            if (monthOfYear > minMonth && year == minYear)
                view.updateDate(minYear, minMonth, minDay);

            if (dayOfMonth > minDay && year == minYear && monthOfYear == minMonth)
                view.updateDate(minYear, minMonth, minDay);
        }
    }
}