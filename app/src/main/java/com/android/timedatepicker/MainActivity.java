package com.android.timedatepicker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText from_date,from_time,to_time;
    LinearLayout layout;
    Button submit;

    SimpleDateFormat dateFormat;
    SimpleDateFormat dateFormat1;
    Database_Help database_help;

    CustomTimePickerDialog timePickerDialog;
    CustomTimePickerDialog customTimePickerDialog;
    CustomDatePickerDialog customDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        from_date = (EditText) findViewById(R.id.from_date_picker);
        from_time = (EditText) findViewById(R.id.from_time_picker);
        to_time = (EditText) findViewById(R.id.to_time_picker);
        layout = (LinearLayout) findViewById(R.id.date);
        submit = (Button) findViewById(R.id.submit);

        dateFormat = new SimpleDateFormat("MMM dd,yyyy");

        dateFormat1 = new SimpleDateFormat("hh:mm a");

        database_help = new Database_Help(this);

        setFromDateField();
        setFromTimeField();
        setToTimeField();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmptyField(from_date)) return;
                else if (isEmptyField(from_time)) return;
                else if (isEmptyField(to_time)) return;
                else if (!checktimings(from_time.getText().toString(),to_time.getText().toString())) {

                    Toast.makeText(MainActivity.this,"Enter Valid Timing",Toast.LENGTH_LONG).show();
                }
                else
                {
                    database_help.insertToAppointment(from_date.getText().toString(),from_time.getText().toString() +"-"+to_time.getText().toString());

                    Toast.makeText(getApplicationContext(),"Appointment Fixed",Toast.LENGTH_LONG).show();
                    MainActivity.this.finish();
                    Intent intent = new Intent(MainActivity.this,Appointments.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setFromDateField() {

        from_date.setOnClickListener(this);

        final Calendar calendar1 = Calendar.getInstance();

        customDatePickerDialog = new CustomDatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,monthOfYear);
                calendar1.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                String date = dateFormat.format(calendar1.getTime());

                from_date.setText(date);

            }
        },calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH));

        customDatePickerDialog.setMinDate(new Date().getTime());

    }

    public void setFromTimeField()
    {
        from_time.setOnClickListener(this);

        final Calendar calendar = Calendar.getInstance();

        timePickerDialog = new CustomTimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                calendar.set(0,0,0,selectedHour,selectedMinute);

                String time[] = dateFormat1.format(calendar.getTime()).split(":");

                int hrs = Integer.parseInt(time[0]);

                String[] mi = time[1].split(" ");

                int min = Integer.parseInt(mi[0]);

                String am_pm = mi[1];

                int am;

                if (am_pm.matches("AM"))
                {
                    am = 0;
                }
                else
                {
                    am = 1;
                }

                min = min % 15;

                if (min != 0) {

                    int minuteToAdd = 15 - min;

                    calendar.add(Calendar.MINUTE, minuteToAdd);

                    String s =dateFormat1.format(calendar.getTime());

                    from_time.setText(s);

                }
                else
                {
                    from_time.setText(dateFormat1.format(calendar.getTime()));
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),true);

    }

    private void setToTimeField() {

        to_time.setOnClickListener(this);

        final Calendar calendar2 = Calendar.getInstance();

        customTimePickerDialog = new CustomTimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                calendar2.set(0,0,0,i,i1);

                String[] time = dateFormat1.format(calendar2.getTime()).split(":");

                int hrs = Integer.parseInt(time[0]);

                String[] mi = time[1].split(" ");

                int min = Integer.parseInt(mi[0]);


                String am_pm = mi[1];

                int am;

                if (am_pm.matches("AM"))
                {
                    am = 0;
                }
                else
                {
                    am = 1;
                }

                min = min % 15;

                if (min != 0)
                {
                    int add = 15 - min;

                    calendar2.add(Calendar.MINUTE,add);

                    String totime = dateFormat1.format(calendar2.getTime());

                    to_time.setText(totime);

                }
                else
                {
                    to_time.setText(dateFormat1.format(calendar2.getTime()));
                }

            }
        },calendar2.get(Calendar.HOUR_OF_DAY),
                calendar2.get(Calendar.MINUTE),true);

    }

    private boolean isEmptyField (EditText editText){

        boolean result = editText.getText().toString().length() <= 0;

        if (result)

            Snackbar.make(layout,"Fill All the Fields",Snackbar.LENGTH_LONG).show();

        return result;

    }

    @Override
    public void onClick(View view) {

        if(view == from_date)
        {
            customDatePickerDialog.show();
        }
        else if (view == from_time)
        {
            timePickerDialog.show();
        }
        else if (view == to_time)
        {
            customTimePickerDialog.show();
        }

    }

    private boolean checktimings(String time, String endtime) {

        String pattern = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if(date1.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e){
            Toast.makeText(MainActivity.this,"Enter Valid Timing",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (java.text.ParseException e) {
//            Toast.makeText(MainActivity.this,"Enter Valid Timing",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return false;
    }

}
