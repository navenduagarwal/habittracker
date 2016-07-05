package com.example.navendu.habittracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.navendu.habittracker.data.HabitContract;
import com.example.navendu.habittracker.data.HabitDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Long date = 1467590400L;

        HabitDbHelper dbHelper = new HabitDbHelper(MainActivity.this);
        dbHelper.insertData(date, "Hello", 10);
        Cursor data = dbHelper.getData(date);
        data.moveToFirst();
        int count = data.getInt(data.getColumnIndex(HabitContract.HabitEntry.COLUMN_COUNTER));
        Log.i("Count", date + " " + count);
        int recordId = data.getInt(data.getColumnIndex(HabitContract.HabitEntry._ID));
        dbHelper.updateData(recordId, 20);
        Cursor data1 = dbHelper.getData(date);
        data1.moveToFirst();
        int count1 = data1.getInt(data.getColumnIndex(HabitContract.HabitEntry.COLUMN_COUNTER));
        Log.i("Count", date + " " + count1);

    }
}
