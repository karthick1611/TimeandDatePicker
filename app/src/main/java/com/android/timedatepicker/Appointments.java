package com.android.timedatepicker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Appointments extends AppCompatActivity {

    ListView listView;
    TextView time;
    TextView view;

    Database_Help help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        listView = (ListView) findViewById(R.id.list_view);
        time = (TextView) findViewById(R.id.texts);
        view = (TextView) findViewById(R.id.images);

        help = new Database_Help(this);

        int[] from = {R.id.images,R.id.texts};

        String[] to = {help.row_1,help.row_2};

        Cursor cursor=help.getAllDetails();

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.layout_row,cursor,to,from);

        listView.setAdapter(cursorAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Appointments.this,MainActivity.class);
        startActivity(intent);
    }
}
