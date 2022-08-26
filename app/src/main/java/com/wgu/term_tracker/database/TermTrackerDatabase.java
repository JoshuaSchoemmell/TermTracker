package com.wgu.term_tracker.database;

import android.content.Context;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wgu.term_tracker.database.daos.AssessmentDao;
import com.wgu.term_tracker.database.daos.CourseDao;
import com.wgu.term_tracker.database.daos.CourseInstructorCrossRefDao;
import com.wgu.term_tracker.database.daos.InstructorDao;
import com.wgu.term_tracker.database.daos.NoteDao;
import com.wgu.term_tracker.database.daos.TermDao;
import com.wgu.term_tracker.models.Assessment;
import com.wgu.term_tracker.models.AssessmentType;
import com.wgu.term_tracker.models.Course;
import com.wgu.term_tracker.models.CourseInstructorCrossRef;
import com.wgu.term_tracker.models.CourseStatus;
import com.wgu.term_tracker.models.Instructor;
import com.wgu.term_tracker.models.Note;
import com.wgu.term_tracker.models.Term;

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Term tracker database.
 */
@Database(
        entities = {
                Term.class,
                Course.class,
                Assessment.class,
                Instructor.class,
                Note.class,
                CourseInstructorCrossRef.class},
        version = 2,
        exportSchema = false)
@TypeConverters(Converters.class)
public abstract class TermTrackerDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    /**
     * The Database write executor.
     */
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Marking the instance as volatile to ensure atomic access to the variable
    private static volatile TermTrackerDatabase INSTANCE;
    /**
     * The constant PHONE_NUMBER_LENGTH.
     */
    public static final int PHONE_NUMBER_LENGTH = 10;


    /**
     * Gets database instance.
     *
     * @param context the context
     * @return the database instance
     */
    static TermTrackerDatabase getDatabaseInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (TermTrackerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TermTrackerDatabase.class, "term_tracker_database")
                            .fallbackToDestructiveMigration()

                            // Uncomment to create dummy data and insert into database on open.
                            // Will delete existing database if uncommented!
//                            .addCallback(createDummyDatabaseDataCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback createDummyDatabaseDataCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                TermDao termDao = INSTANCE.termDao();
                CourseDao courseDao = INSTANCE.courseDao();
                AssessmentDao assessmentDao = INSTANCE.assessmentDao();
                InstructorDao instructorDao = INSTANCE.instructorDao();
                NoteDao noteDao = INSTANCE.noteDao();
                CourseInstructorCrossRefDao courseInstructorCrossRefDao = INSTANCE.courseInstructorCrossRefDao();

                courseDao.deleteAll();
                termDao.deleteAll();
                instructorDao.deleteAll();
                noteDao.deleteAll();
                courseInstructorCrossRefDao.deleteAll();

                Calendar date = Calendar.getInstance();
                date.set(2000, 1, 1);
                Date d;
                String title;

                for (int i = 0; i < 20; i++) {
                    date.add(5, i);
                    date.add(6, i);
                    date.add(7, i);
                    title = "Term ";
                    d = new Date(date.getTimeInMillis());
                    Term term = new Term(title + i, d, d);
                    long termId = termDao.insert(term);

                    title = "Course ";
                    Course course = new Course(title + i, d, d, CourseStatus.IN_PROGRESS, termId);
                    long courseId = courseDao.insert(course);

                    title = "Note";
                    Note note = new Note(title, courseId);
                    noteDao.insert(note);

                    title = "Assessment ";
                    Assessment assessment = new Assessment(title + i, d, d, AssessmentType.OBJECTIVE, courseId);
                    assessmentDao.insert(assessment);

                    title = "Instructor";
                    Instructor instructor = new Instructor(title + i, PhoneNumberUtils.formatNumber(GenerateRandomNumber(), "US"), title + i + "@wgu.com");
                    long instructorId = instructorDao.insert(instructor);
                    courseInstructorCrossRefDao.insert(new CourseInstructorCrossRef(courseId, instructorId));


                }
            });
        }
    };

    /**
     * Generate random PHONE number string.
     *
     * @return the string
     */
    static String GenerateRandomNumber() {
        return String.valueOf(new Random()
                        .nextInt(9 * (int) Math.pow(10, TermTrackerDatabase.PHONE_NUMBER_LENGTH - 1) - 1)
                        + (int) Math.pow(10, TermTrackerDatabase.PHONE_NUMBER_LENGTH - 1));
    }

    /**
     * Term dao.
     *
     * @return the term dao
     */
    public abstract TermDao termDao();

    /**
     * Course dao.
     *
     * @return the course dao
     */
    public abstract CourseDao courseDao();

    /**
     * Assessment dao.
     *
     * @return the assessment dao
     */
    public abstract AssessmentDao assessmentDao();

    /**
     * Instructor dao.
     *
     * @return the instructor dao
     */
    public abstract InstructorDao instructorDao();

    /**
     * Note dao.
     *
     * @return the note dao
     */
    public abstract NoteDao noteDao();

    /**
     * Course instructor cross ref dao.
     *
     * @return the course instructor cross ref dao
     */
    public abstract CourseInstructorCrossRefDao courseInstructorCrossRefDao();

}
