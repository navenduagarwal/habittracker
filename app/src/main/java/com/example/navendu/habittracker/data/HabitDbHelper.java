package com.example.navendu.habittracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.navendu.habittracker.data.HabitContract.HabitEntry;

/**
 * Manages a local database for Habit data
 */
public class HabitDbHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "habit.db";
    private static final String LOG_TAG = HabitDbHelper.class.getSimpleName();
    //whenever we change the database schema, we must increment the database version
    private static final int DATABASE_VERSION = 1;

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_HABIT_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + " (" +
                HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                HabitEntry.COLUMN_DATE + " LONG NOT NULL, " +
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

    //Insert data to the table
    public boolean insertData(Long date, String comment, int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long habitRowId;
        boolean result;

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitEntry.COLUMN_DATE, date);
        contentValues.put(HabitEntry.COLUMN_SHORT_COM, comment);
        contentValues.put(HabitEntry.COLUMN_COUNTER, count);
        habitRowId = db.insert(HabitEntry.TABLE_NAME, null, contentValues);
        if (habitRowId != -1) {
            Log.i(LOG_TAG, "Habit inserted successfully");
            result = true;
        } else {
            Log.i(LOG_TAG, "Habit insert failed");
            result = false;
        }
        db.close();
        return result;
    }

    // Query database for particular date
    public Cursor getData(Long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor habitCursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME, //table to query
                null, //all the columns
                HabitEntry.COLUMN_DATE + " = " + date, //columns for where clause
                null, //values for where clause
                null, //column to group by
                null, //column to filter by row groups
                null //sort order
        );
        Log.i(LOG_TAG, "cursor" + habitCursor.getColumnIndex(HabitEntry.COLUMN_SHORT_COM));
        return habitCursor;
    }

    // Delete entry in table for particular date
    public boolean deleteData(Long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        result = db.delete(HabitEntry.TABLE_NAME, HabitEntry.COLUMN_DATE + "=" + date, null);
        db.close();
        return (result > 0);
    }

    //Update counter for particular date
    public void updateData(int recordId, int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(HabitEntry._ID, recordId);
        updatedValues.put(HabitEntry.COLUMN_COUNTER, count);
        db.update(HabitEntry.TABLE_NAME, updatedValues, HabitEntry._ID + "= ?", new String[]{Long.toString(recordId)});
        db.close();

    }

}
