package com.example.navendu.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.navendu.habittracker.data.HabitContract.HabitEntry;

/**
 * Manages a local database for Habit data
 */
public class HabitDbHelper extends SQLiteOpenHelper {

    //whenever we change the database schema, we must increment the database version
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "habit.db";

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_HABIT_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + " (" +
                HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                HabitEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                HabitEntry.COLUMN_SHORT_COM + " TEXT NOT NULL, " +

                HabitEntry.COLUMN_COUNTER + " INTEGER NOT NULL, " +
                // To assure the application have just one Habit entry per day
                // it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + HabitEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_HABIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HabitEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(HabitEntry.TABLE_NAME);
    }

}
