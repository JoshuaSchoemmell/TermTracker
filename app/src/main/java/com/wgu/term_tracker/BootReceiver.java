package com.wgu.term_tracker;

import static com.wgu.term_tracker.AlarmReceiver.ARG_ALERT_DATE_END;
import static com.wgu.term_tracker.AlarmReceiver.ARG_ALERT_DATE_START;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.wgu.term_tracker.database.TermTrackerRepository;
import com.wgu.term_tracker.models.Assessment;
import com.wgu.term_tracker.models.Course;
import com.wgu.term_tracker.models.Term;
import com.wgu.term_tracker.utilities.AlertsHelper;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * The type Boot receiver.
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BOOT_RECEIVER";
    /**
     * The Term observer.
     */
    Observer<List<Term>> termObserver;
    /**
     * The Course observer.
     */
    Observer<List<Course>> courseObserver;
    /**
     * The Assessment observer.
     */
    Observer<List<Assessment>> assessmentObserver;
    /**
     * The Repository.
     */
    TermTrackerRepository repository;
    /**
     * The Alarm manager.
     */
    AlarmManager alarmManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Received", "Boot received");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            termObserver = terms -> {
                for (Term term :
                        terms) {
                    if (term.alertInfo.isStartAlert()) {
                        // Set the start alert
                        setTermAlert(term, context, ARG_ALERT_DATE_START);
                        Log.d(TAG, "Term Alert Set");
                    }
                    if (term.alertInfo.isEndAlert()) {
                        // Set the end alert
                        setTermAlert(term, context, ARG_ALERT_DATE_END);
                        Log.d(TAG, "Term Alert Set");
                    }
                }
            };
            courseObserver = courses -> {
                for (Course course :
                        courses) {
                    if (course.alertInfo.isStartAlert()) {
                        setCourseAlert(course, context, ARG_ALERT_DATE_START);
                        Log.d(TAG, "Course Alert Set");
                    }
                    if (course.alertInfo.isEndAlert()) {
                        setCourseAlert(course, context, ARG_ALERT_DATE_END);
                        Log.d(TAG, "Course Alert Set");
                    }
                }
            };
            assessmentObserver = assessments -> {
                for (Assessment assessment :
                        assessments) {
                    if (assessment.alertInfo.isStartAlert()) {
                        setAssessmentAlert(assessment, context, ARG_ALERT_DATE_START);
                        Log.d(TAG, "Assessment Alert Set");
                    }
                    if (assessment.alertInfo.isEndAlert()) {
                        setAssessmentAlert(assessment, context, ARG_ALERT_DATE_END);
                        Log.d(TAG, "Assessment Alert Set");
                    }
                }
            };
            repository = TermTrackerRepository.getInstance(context);
            // Setup the alarms
            repository.getAllTerms().observeForever(termObserver);
            repository.getAllCourses().observeForever(courseObserver);
            repository.getAllAssessments().observeForever(assessmentObserver);
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void setTermAlert(Term term, Context context, String arg_date) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.KEY_ALERT_DATE, arg_date);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AlarmReceiver.KEY_ITEM_OBJECT, term);
        intent.putExtra("bundle", bundle);
        intent.setAction("com.wgu.Term_Tracker.SET_ALARM_NOTIFICATION");
        Calendar alertDate;
        PendingIntent pendingIntent;

        // Start date
        if (arg_date.equals(ARG_ALERT_DATE_START)) {
            pendingIntent = PendingIntent.getBroadcast(context, (int) (term.getId() + term.getClass().hashCode()), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alertDate = term.getStartDateCal();
            if (!term.alertInfo.isStartAlertDayOf()) {
                alertDate.add(Calendar.DAY_OF_YEAR, -1);
            }
        } else {
            // END DATE
            pendingIntent = PendingIntent.getBroadcast(context, (int) (term.getId() + term.getClass().hashCode()) + 1, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alertDate = term.getEndDateCal();
            if (!term.alertInfo.isEndAlertDayOf()) {
                alertDate.add(Calendar.DAY_OF_YEAR, -1);
            }
        }
        if (System.currentTimeMillis() < alertDate.getTimeInMillis()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertDate.getTimeInMillis(), pendingIntent);
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void setAssessmentAlert(Assessment assessment, Context context, String arg_date) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.KEY_ALERT_DATE, arg_date);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AlarmReceiver.KEY_ITEM_OBJECT, assessment);
        intent.putExtra("bundle", bundle);
        intent.setAction("com.wgu.Term_Tracker.SET_ALARM_NOTIFICATION");
        Calendar alertDate;
        PendingIntent pendingIntent;

        // Start date
        if (arg_date.equals(ARG_ALERT_DATE_START)) {
            pendingIntent = PendingIntent.getBroadcast(context, (int) (assessment.getId() + assessment.getClass().hashCode()), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alertDate = AlertsHelper.getAlertDate(assessment.getStartDate(), assessment.alertInfo.getAlertHour(), assessment.alertInfo.getAlertMinute());
            if (!assessment.alertInfo.isStartAlertDayOf()) {
                alertDate.add(Calendar.DAY_OF_YEAR, -1);
            }
        } else {
            // END DATE
            pendingIntent = PendingIntent.getBroadcast(context, (int) (assessment.getId() + assessment.getClass().hashCode()) + 1, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alertDate = AlertsHelper.getAlertDate(assessment.getEndDate(), assessment.alertInfo.getAlertHour(), assessment.alertInfo.getAlertMinute());
            if (!assessment.alertInfo.isEndAlertDayOf()) {
                alertDate.add(Calendar.DAY_OF_YEAR, -1);
            }
        }
        if (System.currentTimeMillis() < alertDate.getTimeInMillis()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertDate.getTimeInMillis(), pendingIntent);
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void setCourseAlert(Course course, Context context, String arg_date) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.KEY_ALERT_DATE, arg_date);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AlarmReceiver.KEY_ITEM_OBJECT, course);
        intent.putExtra("bundle", bundle);
        intent.setAction("com.wgu.Term_Tracker.SET_ALARM_NOTIFICATION");
        Calendar alertDate;
        PendingIntent pendingIntent;

        // Start date
        if (arg_date.equals(ARG_ALERT_DATE_START)) {
            pendingIntent = PendingIntent.getBroadcast(context, (int) (course.getId() + course.getClass().hashCode()), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alertDate = AlertsHelper.getAlertDate(course.getStartDate(), course.alertInfo.getAlertHour(), course.alertInfo.getAlertMinute());
            if (!course.alertInfo.isStartAlertDayOf()) {
                alertDate.add(Calendar.DAY_OF_YEAR, -1);
            }
        } else {
            // END DATE
            pendingIntent = PendingIntent.getBroadcast(context, (int) (course.getId() + course.getClass().hashCode()) + 1, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alertDate = AlertsHelper.getAlertDate(course.getEndDate(), course.alertInfo.getAlertHour(), course.alertInfo.getAlertMinute());
            if (!course.alertInfo.isEndAlertDayOf()) {
                alertDate.add(Calendar.DAY_OF_YEAR, -1);
            }
        }
        if (System.currentTimeMillis() < alertDate.getTimeInMillis()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertDate.getTimeInMillis(), pendingIntent);
        }
    }

}