package com.wgu.term_tracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The type AlertInfo.
 *
 * This is a utility class to manage alert information pertaining to the object the AlertInfo belongs to.
 */
public class AlertInfo implements Parcelable {
    /**
     * The constant START_ALERT.
     */
    public static final String START_ALERT = "Start Alert";
    /**
     * The constant START_ALERT_DAY_OF.
     */
    public static final String START_ALERT_DAY_OF = "Start Alert Day Of";
    /**
     * The constant END_ALERT.
     */
    public static final String END_ALERT = "End Alert";
    /**
     * The constant END_ALERT_DAY_OF.
     */
    public static final String END_ALERT_DAY_OF = "End Alert Day Of";
    /**
     * The constant ALERT_HOUR.
     */
    public static final String ALERT_HOUR = "Alert Hour";
    /**
     * The constant ALERT_MINUTE.
     */
    public static final String ALERT_MINUTE = "Alert Minute";
    /**
     * The constant CREATOR.
     */
    public static final Creator<AlertInfo> CREATOR = new Creator<AlertInfo>() {
        @Override
        public AlertInfo createFromParcel(Parcel in) {
            return new AlertInfo(in);
        }

        @Override
        public AlertInfo[] newArray(int size) {
            return new AlertInfo[size];
        }
    };

    private boolean startAlert;
    private boolean startAlertDayOf;
    private boolean endAlert;
    private boolean endAlertDayOf;
    private Integer alertHour;
    private Integer alertMinute;

    /**
     * Instantiates a new Alert info from a Parcel.
     *
     * @param in the Parcel
     */
    public AlertInfo(Parcel in) {
        startAlert = in.readByte() != 0;
        startAlertDayOf = in.readByte() != 0;
        endAlert = in.readByte() != 0;
        endAlertDayOf = in.readByte() != 0;
        if (in.readByte() == 0) {
            alertHour = null;
        } else {
            alertHour = in.readInt();
        }
        if (in.readByte() == 0) {
            alertMinute = null;
        } else {
            alertMinute = in.readInt();
        }
    }

    /**
     * Instantiates a new Alert info.
     */
    public AlertInfo() {
        startAlert = false;
        startAlertDayOf = true;
        endAlert = false;
        endAlertDayOf = true;
    }

    /**
     * Instantiates a new Alert info from a JSON String.
     *
     * NOTE: Suppressing the warning that the int objects are null. This is intended.
     *
     * @param string the string
     */
    public AlertInfo(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            setStartAlert(jsonObject.getBoolean(START_ALERT));
            setStartAlertDayOf(jsonObject.getBoolean(START_ALERT_DAY_OF));
            setEndAlert(jsonObject.getBoolean(END_ALERT));
            setEndAlertDayOf(jsonObject.getBoolean(END_ALERT_DAY_OF));
            setAlertHour(jsonObject.getInt(ALERT_HOUR));
            setAlertMinute(jsonObject.getInt(ALERT_MINUTE));
        } catch (JSONException e) {
            // Suppressing the warning that the int objects are null. This is intended.
            if (getAlertHour() != null || getAlertMinute() != null) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Is start alert boolean.
     *
     * @return the boolean
     */
    public boolean isStartAlert() {
        return startAlert;
    }

    /**
     * Sets start alert.
     *
     * @param startAlert the start alert
     */
    public void setStartAlert(boolean startAlert) {
        this.startAlert = startAlert;
    }

    /**
     * Is start alert day of boolean.
     *
     * @return the boolean
     */
    public boolean isStartAlertDayOf() {
        return startAlertDayOf;
    }

    /**
     * Sets start alert day of.
     *
     * @param startAlertDayOf the start alert day of
     */
    public void setStartAlertDayOf(boolean startAlertDayOf) {
        this.startAlertDayOf = startAlertDayOf;
    }

    /**
     * Is end alert boolean.
     *
     * @return the boolean
     */
    public boolean isEndAlert() {
        return endAlert;
    }

    /**
     * Sets end alert.
     *
     * @param endAlert the end alert
     */
    public void setEndAlert(boolean endAlert) {
        this.endAlert = endAlert;
    }

    /**
     * Is end alert day of boolean.
     *
     * @return the boolean
     */
    public boolean isEndAlertDayOf() {
        return endAlertDayOf;
    }

    /**
     * Sets end alert day of.
     *
     * @param endAlertDayOf the end alert day of
     */
    public void setEndAlertDayOf(boolean endAlertDayOf) {
        this.endAlertDayOf = endAlertDayOf;
    }

    /**
     * Gets alert hour.
     *
     * @return the alert hour
     */
    public Integer getAlertHour() {
        return alertHour;
    }

    /**
     * Sets alert hour.
     *
     * @param alertHour the alert hour
     */
    public void setAlertHour(Integer alertHour) {
        this.alertHour = alertHour;
    }

    /**
     * Gets alert minute.
     *
     * @return the alert minute
     */
    public Integer getAlertMinute() {
        return alertMinute;
    }

    /**
     * Sets alert minute.
     *
     * @param alertMinute the alert minute
     */
    public void setAlertMinute(Integer alertMinute) {
        this.alertMinute = alertMinute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (startAlert ? 1 : 0));
        dest.writeByte((byte) (startAlertDayOf ? 1 : 0));
        dest.writeByte((byte) (endAlert ? 1 : 0));
        dest.writeByte((byte) (endAlertDayOf ? 1 : 0));
        if (alertHour == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(alertHour);
        }
        if (alertMinute == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(alertMinute);
        }
    }
}
