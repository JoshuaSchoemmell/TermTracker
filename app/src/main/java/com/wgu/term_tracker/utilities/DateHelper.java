package com.wgu.term_tracker.utilities;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The type Date helper.
 */
public class DateHelper {
    /**
     * The constant simpleDateFormat.
     */
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    /**
     * Format date string.
     *
     * @param date the date
     * @return the string
     */
    public static String formatDate(Date date) {
        return simpleDateFormat.format(date);
    }

    /**
     * Extract month int.
     *
     * @param date the date
     * @return the int
     */
    public static int extractMonth(@NonNull String date) {
        return Integer.parseInt(date.substring(0, date.indexOf("/")));
    }

    /**
     * Extract year int.
     *
     * @param date the date
     * @return the int
     */
    public static int extractYear(String date) {
        return Integer.parseInt(date.substring(date.lastIndexOf("/") + 1));
    }

    /**
     * Extract day int.
     *
     * @param date the date
     * @return the int
     */
    public static int extractDay(String date) {
        return Integer.parseInt(date.substring(date.indexOf("/") + 1, date.lastIndexOf("/")));
    }

    /**
     * Sets calendar from text.
     *
     * @param calendar the calendar
     * @param text     the text
     */
    public static void setCalendarFromText(Calendar calendar, String text) {
        int extractedMonth = extractMonth(text);
        int extractedDay = extractDay(text);
        int extractedYear = DateHelper.extractYear(text);

        calendar.set(Calendar.YEAR, extractedYear);
        calendar.set(Calendar.MONTH, extractedMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, extractedDay);
    }

    /**
     * Sets date from date picker.
     *
     * @param date       the date
     * @param dateField  the date field
     * @param year       the year
     * @param month      the month
     * @param dayOfMonth the day of month
     */
    public static void setDateFromDatePicker(Calendar date, TextInputEditText dateField, int year, int month, int dayOfMonth) {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateField.setText(formatDate(date.getTime()));
        dateField.setError(null);
    }
}
