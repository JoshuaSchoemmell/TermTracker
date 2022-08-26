package com.wgu.term_tracker.database;

import androidx.room.TypeConverter;

import com.wgu.term_tracker.models.AlertInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * The type Converters.
 */
public class Converters {

    /**
     * From timestamp to date.
     *
     * @param value the value
     * @return the date
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Date to timestamp long.
     *
     * @param date the date
     * @return the long
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * AlertInfo to string.
     *
     * Creates a string formatted in JSON to be stored into the database.
     *
     * @param alertInfo the alert info
     * @return the string
     */
    @TypeConverter
    public static String fromAlertInfo(AlertInfo alertInfo) {
        if (alertInfo == null) return null;
        JSONObject alertInfoMap = new JSONObject();
        try {
            alertInfoMap.put(AlertInfo.START_ALERT, alertInfo.isStartAlert());
            alertInfoMap.put(AlertInfo.START_ALERT_DAY_OF, alertInfo.isStartAlertDayOf());
            alertInfoMap.put(AlertInfo.END_ALERT, alertInfo.isEndAlert());
            alertInfoMap.put(AlertInfo.END_ALERT_DAY_OF, alertInfo.isEndAlertDayOf());
            alertInfoMap.put(AlertInfo.ALERT_HOUR, alertInfo.getAlertHour());
            alertInfoMap.put(AlertInfo.ALERT_MINUTE, alertInfo.getAlertMinute());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return alertInfoMap.toString();
    }

    /**
     * String to AlertInfo.
     *
     * Creates an AlertInfo Object from a JSON-formatted string.
     *
     * @param string the string
     * @return the alert info
     */
    @TypeConverter
    public static AlertInfo stringToAlertInfo(String string) {
        return string == null ? null : new AlertInfo(string);
    }

}