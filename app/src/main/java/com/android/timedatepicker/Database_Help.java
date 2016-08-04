package com.android.timedatepicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_Help extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Appointment_Fixer";
    public static final String Table_NAME = "appointment";
    public static final String row_0 = "_id";
    public static final String row_1 = "Date";
    public static final String row_2 = "Time";

    public Database_Help(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + Table_NAME+ "(" + row_0 + " INTEGER PRIMARY KEY AUTOINCREMENT," + row_1 + " Text NOT NULL,"+ row_2 + " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + Table_NAME);
        onCreate(sqLiteDatabase);
    }

        public boolean insertToAppointment(String date,String time) throws SQLiteException
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(row_1, date);
            contentValues.put(row_2, time);

            long result = db.insert(Table_NAME, null, contentValues);
            if(result == -1)
                return false;
            else
                return true;
        }

        public Cursor getAllDetails()
        {

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor mCursor = db.query(Table_NAME, new String[]{row_0,row_1,row_2},null, null, null, null,null);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;

        }

        public String dates()
        {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor=db.query(Table_NAME, new String[]{row_0,row_1}, null, null, null, null, null);
            if(cursor.getCount()<1) // UserName Not Exist
            {
                cursor.close();
                return "NOT EXIST";
            }
            cursor.moveToFirst();
            String password= cursor.getString(cursor.getColumnIndex(row_1));
            cursor.close();
            return password;
        }

}
