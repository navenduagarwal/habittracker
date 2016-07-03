package com.example.navendu.habittracker.data;

import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines tables and column names for the habit database
 */
public class HabitContract {

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the habits table- as of now we are only tracking one habit*/
    public static final class HabitEntry implements BaseColumns {

        public static final String TABLE_NAME = "habit";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";
        //Counter, stored as integer representing number of times habit is completed
        public static final String COLUMN_COUNTER = "counter";
        //Short Comments about that particular experience
        public static final String COLUMN_SHORT_COM = "short_com";

    }

}
