package com.example.navendu.habittracker.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Based on learning from few lessons of SunShine Tutorial
 */
public class TestUtilities extends AndroidTestCase {
    static final long TEST_DATE = 1467590400L;  // July 4th, 2016

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createStepsHabitValues() {
        ContentValues habitValues = new ContentValues();
        habitValues.put(HabitContract.HabitEntry.COLUMN_DATE, TEST_DATE);
        habitValues.put(HabitContract.HabitEntry.COLUMN_COUNTER, 10000);
        habitValues.put(HabitContract.HabitEntry.COLUMN_SHORT_COM, "Lot of Steps today");

        return habitValues;
    }
}
