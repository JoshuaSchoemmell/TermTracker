package com.wgu.term_tracker.utilities;

import android.text.Editable;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Validation helper.
 */
public class ValidationHelper {
    /**
     * The constant INVALID_DATE_FORMAT.
     */
    public static final String INVALID_DATE_FORMAT = "Not a valid date! Must be in the format of \"MM/DD/YYYY\"";
    /**
     * The constant START_END_DATE_CHRONO_ERROR.
     */
    public static final String START_END_DATE_CHRONO_ERROR = "Start date must be before the End Date";

    /**
     * Is valid title boolean.
     *
     * @param mTitle the m title
     * @return the boolean
     */
    public static boolean isValidTitle(TextInputEditText mTitle) {
        if (Objects.requireNonNull(mTitle.getText()).length() == 0) {
            mTitle.setError("Item must have a Title");
            return false;
        }
        mTitle.setError(null);
        return true;
    }

    /**
     * Is valid date format boolean.
     *
     * @param date the date
     * @return the boolean
     */
    public static boolean isValidDateFormat(TextInputEditText date) {
        if (!isValidDateFormat(Objects.requireNonNull(Objects.requireNonNull(date.getText()).toString()))) {
            date.setError(INVALID_DATE_FORMAT);
            return false;
        }
        date.setError(null);
        return true;
    }

    /**
     * Validate form boolean.
     *
     * @param mTitle     the m title
     * @param mStartDate the m start date
     * @param mEndDate   the m end date
     * @return the boolean
     */
    public static boolean validateForm(TextInputEditText mTitle, TextInputEditText mStartDate, TextInputEditText mEndDate) {
        boolean isValid = ValidationHelper.isValidTitle(mTitle);

        if (!ValidationHelper.isValidDateFormat(mStartDate)) {
            isValid = false;
        }
        if (!ValidationHelper.isValidDateFormat(mEndDate)) {
            isValid = false;
        }
        return isValid;

    }

    /**
     * Validate start before end dates boolean.
     *
     * @param mStartDate the m start date
     * @param mEndDate   the m end date
     * @return the boolean
     */
    public static boolean validateStartBeforeEndDates(Calendar mStartDate, Calendar mEndDate) {
        return mStartDate.equals(mEndDate) || mStartDate.before(mEndDate);
    }

    /**
     * Is valid date format boolean.
     *
     * @param text the text
     * @return the boolean
     */
    protected static boolean isValidDateFormat(String text) {
        // Excerpt From
        // Regular Expressions Cookbook
        // Jan Goyvaerts and Steven Levithan
        Pattern pattern = Pattern.compile("^(?:(1[0-2]|0?[1-9])/(3[01]|[12][0-9]|0?[1-9])|(3[01]|[12][0-9]|0?[1-9])/(1[0-2]|0?[1-9]))/[0-9]{4}$");
        Matcher matcher = pattern.matcher(Objects.requireNonNull(text));
        return matcher.matches();
    }


}
