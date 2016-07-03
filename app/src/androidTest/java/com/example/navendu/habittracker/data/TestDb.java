package com.example.navendu.habittracker.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Based on learning from few lessons of SunShine Tutorial
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(HabitDbHelper.DATABASE_NAME);
    }

    /*
    This function gets called before each test is executed to delete the database.  This makes
    sure that we always have a clean test.
    */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
    This function to test insert and read in the habit Table
     */
    public void testHabitInsertReadTable() {

        // Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // error will be thrown here when we try to get a writable database
        HabitDbHelper dbHelper = new HabitDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Create Content Values of what we want to insert
        ContentValues habitValues = TestUtilities.createStepsHabitValues();

        Long habitRowId;
        habitRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, habitValues);

        //Verify we got row back
        assertTrue("Error: Failure to insert Steps Habit Values", habitRowId != -1);

        //Query the database and receive a Cursor Back
        Cursor habitCursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME, //table to query
                null, //all the columns
                null, //columns for where clause
                null, //values for where clause
                null, //column to group by
                null, //column to filter by row groups
                null //sort order
        );
        // Move the cursor to a valid database row
        assertTrue("Error: No Records returned from the habit query", habitCursor.moveToFirst());
        // Moving the cursor to test that there is only one record in the database
        assertFalse("Error: More than one record returned from habit query", habitCursor.moveToNext());
        // Finally, close the cursor and database
        habitCursor.close();
        dbHelper.close();
    }

    /*
       This helper function deletes all records from database table using the database
       functions only.
    */
    public void deleteAllRecordsFromDb() {
        HabitDbHelper dbHelper = new HabitDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(HabitContract.HabitEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void testUpdateHabit() {

        //Create Content Values of what we want to insert
        ContentValues values = TestUtilities.createStepsHabitValues();

        // Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // error will be thrown here when we try to get a writable database
        HabitDbHelper dbHelper = new HabitDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Long habitRowId;
        habitRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);

        //Verify we got row back
        assertTrue("Error: Failure to insert Steps Habit Values", habitRowId != -1);
        Log.d(LOG_TAG, "New row id: " + habitRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(HabitContract.HabitEntry._ID, habitRowId);
        updatedValues.put(HabitContract.HabitEntry.COLUMN_SHORT_COM, "Updated Text");

        int count = db.update(HabitContract.HabitEntry.TABLE_NAME, updatedValues, HabitContract.HabitEntry._ID + "= ?"
                , new String[]{Long.toString(habitRowId)});
        assertEquals(count, 1);


        //Query the database and receive a Cursor Back
        Cursor habitCursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME, //table to query
                null, //all the columns
                HabitContract.HabitEntry._ID + " = " + habitRowId, //columns for where clause
                null, //values for where clause
                null, //column to group by
                null, //column to filter by row groups
                null //sort order
        );

        TestUtilities.validateCursor("testUpdateHabit.  Error validating habit entry update.",
                habitCursor, updatedValues);

        habitCursor.close();

    }
}
