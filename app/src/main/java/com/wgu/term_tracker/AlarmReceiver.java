package com.wgu.term_tracker;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.wgu.term_tracker.TermTrackerApplication.ARG_FOREIGN_KEY;
import static com.wgu.term_tracker.ui.assessment.AssessmentActivity.KEY_SELECTED_ASSESSMENT;
import static com.wgu.term_tracker.ui.course.CourseActivity.KEY_SELECTED_COURSE;
import static com.wgu.term_tracker.ui.term.TermActivity.KEY_SELECTED_TERM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.wgu.term_tracker.database.TermTrackerRepository;
import com.wgu.term_tracker.models.Assessment;
import com.wgu.term_tracker.models.Course;
import com.wgu.term_tracker.models.Term;
import com.wgu.term_tracker.ui.assessment.AssessmentActivity;
import com.wgu.term_tracker.ui.course.CourseActivity;
import com.wgu.term_tracker.ui.term.TermActivity;

import java.util.Objects;

/**
 * The type Alarm receiver.
 */
public class AlarmReceiver extends BroadcastReceiver {

    /**
     * The constant KEY_ALERT_DATE.
     */
    public static final String KEY_ALERT_DATE = "alert_date";
    /**
     * The constant KEY_ITEM_OBJECT.
     */
    public static final String KEY_ITEM_OBJECT = "key_item_object";
    private static final String CHANNEL_ID = "com.wgu.term_tracker.Alerts";
    private static final String NOTIFICATION_ID = "notification_id";
    /**
     * The constant ARG_ALERT_DATE_START.
     */
    public static final String ARG_ALERT_DATE_START = "arg_start";
    /**
     * The constant ARG_ALERT_DATE_END.
     */
    public static final String ARG_ALERT_DATE_END = "arg_end";
    /**
     * The constant ARG_ALERT_ACTION_SET.
     */
    public static final String ARG_ALERT_ACTION_SET = "arg_action_set";
    /**
     * The constant ARG_ALERT_ACTION_CANCEL.
     */
    public static final String ARG_ALERT_ACTION_CANCEL = "arg_action_cancel";
    /**
     * The Term tracker repository.
     */
    TermTrackerRepository termTrackerRepository;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "com.wgu.Term_Tracker.SET_ALARM_NOTIFICATION")) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            createNotificationChannel(context);
            String date_type = intent.getExtras().getString(KEY_ALERT_DATE);
            Bundle bundle = intent.getBundleExtra("bundle");
            Object object = bundle.getParcelable(KEY_ITEM_OBJECT);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            NotificationCompat.Builder builder = getBuilder(context);

            if (object instanceof Term) {
                Term term = (Term) object;
                Intent termIntent = new Intent(context, TermActivity.class);
                termIntent.putExtra(KEY_SELECTED_TERM, term);
                taskStackBuilder.addNextIntentWithParentStack(termIntent);
                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(term.hashCode(), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                // Build Notification

                builder = getBuilder(context);
                builder.setContentIntent(pendingIntent);
                if (date_type.equals(ARG_ALERT_DATE_START)) {
                    builder.setContentText("You have a new Term starting!");
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                            String.format("You have a new Term starting!\nTitle: %s\nStarting: %s",
                                    term.getTitle(),
                                    term.alertInfo.isStartAlertDayOf() ? "Today" : "Tomorrow")));
                } else {
                    builder.setContentText("Your Term is ending soon!");
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                            String.format("Your Term is ending soon!\nTitle: %s\nEnding: %s",
                                    term.getTitle(),
                                    term.alertInfo.isEndAlertDayOf() ? "Today" : "Tomorrow")));

                }
            } else if (object instanceof Course) {
                Course course = (Course) object;
                Intent courseIntent = new Intent(context, CourseActivity.class);
                Intent termIntent = new Intent(context, TermActivity.class);
                courseIntent.putExtra(KEY_SELECTED_COURSE, course);
                courseIntent.putExtra(ARG_FOREIGN_KEY, course.getTermId());
                termTrackerRepository = TermTrackerRepository.getInstance(context.getApplicationContext());
                Term result = termTrackerRepository.getTerm(course.getTermId());
                termIntent.putExtra(KEY_SELECTED_TERM, result);
                taskStackBuilder.addNextIntentWithParentStack(termIntent);
                taskStackBuilder.addNextIntent(courseIntent);

                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(course.hashCode(), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                builder = getBuilder(context);
                builder.setContentIntent(pendingIntent);
                if (date_type.equals(ARG_ALERT_DATE_START)) {
                    builder.setContentText("You have a new Course starting!");
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                            String.format("You have a new Course starting!\nTitle: %s\nStarting: %s",
                                    course.getTitle(),
                                    course.alertInfo.isStartAlertDayOf() ? "Today" : "Tomorrow")));
                } else {
                    builder.setContentText("Your Course is ending soon!");
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                            String.format("Your Course is ending soon!\nTitle: %s\nEnding: %s",
                                    course.getTitle(),
                                    course.alertInfo.isEndAlertDayOf() ? "Today" : "Tomorrow")));

                }
            } else if (object instanceof Assessment) {
                Assessment assessment = (Assessment) object;
                Intent assessmentIntent = new Intent(context, AssessmentActivity.class);
                Intent courseIntent = new Intent(context, CourseActivity.class);
                Intent termIntent = new Intent(context, TermActivity.class);

                termTrackerRepository = TermTrackerRepository.getInstance(context.getApplicationContext());
                Course course = termTrackerRepository.getCourse(assessment.getCourseId());
                Term term = termTrackerRepository.getTerm(course.getTermId());

                assessmentIntent.putExtra(KEY_SELECTED_ASSESSMENT, assessment);
                assessmentIntent.putExtra(ARG_FOREIGN_KEY, assessment.getCourseId());

                courseIntent.putExtra(KEY_SELECTED_COURSE, course);
                courseIntent.putExtra(ARG_FOREIGN_KEY, course.getTermId());

                termIntent.putExtra(KEY_SELECTED_TERM, term);

                taskStackBuilder.addNextIntentWithParentStack(termIntent);
                taskStackBuilder.addNextIntent(courseIntent);
                taskStackBuilder.addNextIntent(assessmentIntent);

                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(course.hashCode(), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                builder = getBuilder(context);
                builder.setContentIntent(pendingIntent);
                if (date_type.equals(ARG_ALERT_DATE_START)) {
                    builder.setContentText("You have a new Assessment starting!");
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                            String.format("You have a new Assessment starting!\nTitle: %s\nStarting: %s",
                                    assessment.getTitle(),
                                    assessment.alertInfo.isStartAlertDayOf() ? "Today" : "Tomorrow")));
                } else {
                    builder.setContentText("Your Assessment is ending soon!");
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                            String.format("Your Assessment is ending soon!\nTitle: %s\nEnding: %s",
                                    course.getTitle(),
                                    course.alertInfo.isEndAlertDayOf() ? "Today" : "Tomorrow")));

                }
            }
            // Send the Notification
            Notification notification = builder.build();
            notificationManager.notify(intent.getIntExtra(NOTIFICATION_ID, 0), notification);
        }
    }

    @NonNull
    private NotificationCompat.Builder getBuilder(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
        builder.setContentTitle(context.getString(R.string.notification_title));
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setVibrate(new long[]{0, 1000});
        builder.setAutoCancel(true);
        return builder;
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence notificationChannelName = context.getString(R.string.channel_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, notificationChannelName, importance);

        // Optional Description
        String description = context.getString(R.string.channel_description);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
