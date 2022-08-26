package com.wgu.term_tracker.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The type Alerts helper.
 */
public class AlertsHelper {
    /**
     * The constant ARG_IS_NEW_TERM.
     */
    public static final String ARG_IS_NEW_TERM = "ARG_IS_NEW_TERM".toLowerCase(Locale.ROOT);

    /**
     * Sets radio group enabled.
     *
     * @param radioGroup the radio group
     * @param enabled    the enabled
     */
    public static void setRadioGroupEnabled(RadioGroup radioGroup, boolean enabled) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(enabled);
        }
    }

    /**
     * Gets alert date.
     *
     * @param calendar the calendar
     * @param hour     the hour
     * @param minute   the minute
     * @return the alert date
     */
    @NonNull
    public static Calendar getAlertDate(Calendar calendar, int hour, int minute) {
        Calendar alertDate = Calendar.getInstance();
        alertDate.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                hour,
                minute,
                0);
        Log.d("Alert Date From getAlertDate method: ", alertDate.toString());
        return alertDate;
    }

    /**
     * Gets alert date.
     *
     * @param date   the date
     * @param hour   the hour
     * @param minute the minute
     * @return the alert date
     */
    public static Calendar getAlertDate(Date date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getAlertDate(calendar, hour, minute);
    }

    /**
     * Show error toast.
     *
     * @param context the context
     */
    public static void showErrorToast(Context context) {
        Toast.makeText(context, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
    }

    /**
     * Show course needs to be saved first toast.
     *
     * @param context the context
     */
    public static void showCourseNeedsToBeSavedFirstToast(Context context) {
        Toast.makeText(context, "Course must be saved first!", Toast.LENGTH_LONG).show();
    }

    /**
     * Show assessment needs to be saved first toast.
     *
     * @param context the context
     */
    public static void showAssessmentNeedsToBeSavedFirstToast(Context context) {
        Toast.makeText(context, "Assessment must be saved first!", Toast.LENGTH_LONG).show();
    }
}
