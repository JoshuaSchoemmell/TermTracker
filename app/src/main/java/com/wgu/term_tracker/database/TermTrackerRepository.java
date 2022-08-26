package com.wgu.term_tracker.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.wgu.term_tracker.TermTrackerApplication;
import com.wgu.term_tracker.database.daos.AssessmentDao;
import com.wgu.term_tracker.database.daos.CourseDao;
import com.wgu.term_tracker.database.daos.CourseInstructorCrossRefDao;
import com.wgu.term_tracker.database.daos.InstructorDao;
import com.wgu.term_tracker.database.daos.NoteDao;
import com.wgu.term_tracker.database.daos.TermDao;
import com.wgu.term_tracker.models.Assessment;
import com.wgu.term_tracker.models.Course;
import com.wgu.term_tracker.models.CourseInstructorCrossRef;
import com.wgu.term_tracker.models.Instructor;
import com.wgu.term_tracker.models.Note;
import com.wgu.term_tracker.models.Term;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * The type Term tracker repository.
 */
public class TermTrackerRepository {
    private static volatile TermTrackerRepository sRepositoryInstance;
    private final TermDao termDao;
    private final CourseDao courseDao;
    private final AssessmentDao assessmentDao;
    private final InstructorDao instructorDao;
    private final NoteDao noteDao;
    private final CourseInstructorCrossRefDao courseInstructorCrossRefDao;
    /**
     * The Db.
     */
    final TermTrackerDatabase db;


    private TermTrackerRepository(Context application) {
        db = TermTrackerDatabase.getDatabaseInstance(application);
        termDao = db.termDao();
        courseDao = db.courseDao();
        assessmentDao = db.assessmentDao();
        instructorDao = db.instructorDao();
        noteDao = db.noteDao();
        courseInstructorCrossRefDao = db.courseInstructorCrossRefDao();
    }

    /**
     * Gets instance.
     *
     * @param application the application
     * @return the instance
     */
    public static TermTrackerRepository getInstance(Context application) {
        if (sRepositoryInstance == null) {
            synchronized (TermTrackerRepository.class) {
                if (sRepositoryInstance == null) {
                    sRepositoryInstance = new TermTrackerRepository(application);
                }
            }
        }
        return sRepositoryInstance;
    }

    /*TERMS*/

    /**
     * Gets all terms.
     *
     * @return the all terms
     */
    public LiveData<List<Term>> getAllTerms() {
        return termDao.getAll();
    }

    /**
     * Insert term.
     *
     * @param term the term
     */
    public void insert(Term term) {
        db.getQueryExecutor().execute(() -> term.setId(termDao.insert(term)));
    }

    /**
     * Gets term.
     *
     * @param termId the term id
     * @return the term
     */
    public Term getTerm(Long termId) {
        Callable<Term> termCallable = () -> termDao.getTerm(termId);
        try {
            return TermTrackerApplication.singleThreadExecutor.submit(termCallable).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update term.
     *
     * @param mTerm the m term
     */
    public void update(Term mTerm) {
        db.getQueryExecutor().execute(() -> termDao.update(mTerm));
    }

    /**
     * Delete term.
     *
     * @param mTerm the m term
     */
    public void delete(Term mTerm) {
        db.getQueryExecutor().execute(() -> termDao.delete(mTerm));
    }

    /**
     * Term has courses live data.
     *
     * @param termId the term id
     * @return the live data
     */
    public LiveData<Boolean> termHasCourses(long termId) {
        return courseDao.termHasCourses(termId);
    }

    /**
     * Gets course count.
     *
     * @param termId the term id
     * @return the course count
     */
    public LiveData<Integer> getCourseCount(long termId) {
        return courseDao.getCourseCount(termId);
    }

    /*COURSES*/

    /**
     * Gets all courses.
     *
     * @return the all courses
     */
    public LiveData<List<Course>> getAllCourses() {
        return courseDao.getAll();
    }

    /**
     * Gets courses for term.
     *
     * @param id the id
     * @return the courses for term
     */
    public LiveData<List<Course>> getCoursesForTerm(Long id) {
        return courseDao.getCoursesForTerm(id);
    }

    /**
     * Gets course.
     *
     * @param itemID the item id
     * @return the course
     */
    public Course getCourse(long itemID) {
        Callable<Course> courseCallable = () -> courseDao.getCourse(itemID);
        try {
            return TermTrackerApplication.singleThreadExecutor.submit(courseCallable).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insert.
     *
     * @param course the course
     */
    public void insert(Course course) {
        Callable<Long> longCallable = () -> courseDao.insert(course);
        try {
            course.setCourseId(TermTrackerApplication.singleThreadExecutor.submit(longCallable).get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update course.
     *
     * @param course the course
     */
    public void updateCourse(Course course) {
        db.getQueryExecutor().execute(() -> courseDao.update(course));
    }

    /**
     * Delete course.
     *
     * @param course the course
     */
    public void deleteCourse(Course course) {
        db.getQueryExecutor().execute(() -> courseDao.delete(course));
    }

    /**
     * Gets assessment count.
     *
     * @param courseId the course id
     * @return the assessment count
     */
    public LiveData<Integer> getAssessmentCount(long courseId) {
        return assessmentDao.getAssessmentCountForCourse(courseId);
    }

    /*ASSESSMENTS*/

    /**
     * Gets all assessments.
     *
     * @return the all assessments
     */
    public LiveData<List<Assessment>> getAllAssessments() {
        return assessmentDao.getAll();
    }

    /**
     * Gets assessments for course.
     *
     * @param courseId the course id
     * @return the assessments for course
     */
    public LiveData<List<Assessment>> getAssessmentsForCourse(long courseId) {
        return assessmentDao.getAssessmentsForCourse(courseId);
    }

    /**
     * Insert assessment.
     *
     * @param assessment the assessment
     */
    public void insert(Assessment assessment) {
        db.getQueryExecutor().execute(() -> assessment.setId(assessmentDao.insert(assessment)));
    }

    /**
     * Update assessment.
     *
     * @param assessment the assessment
     */
    public void updateAssessment(Assessment assessment) {
        db.getQueryExecutor().execute(() -> assessmentDao.update(assessment));
    }

    /**
     * Delete assessment.
     *
     * @param mAssessment the m assessment
     */
    public void delete(Assessment mAssessment) {
        db.getQueryExecutor().execute(() -> assessmentDao.delete(mAssessment));
    }

    /*INSTRUCTORS*/

    /**
     * Gets instructors for course.
     *
     * @param mPrimaryKey the m primary key
     * @return the instructors for course
     */
    public LiveData<List<Instructor>> getInstructorsForCourse(long mPrimaryKey) {
        return instructorDao.getInstructorsForCourse(mPrimaryKey);
    }

    /**
     * Insert new instructor.
     * <p>
     * Inserts a new instructor if a match is not found, or else sets the current instructor's id
     * to the matching instructor's id.
     *
     * @param instructor the instructor
     * @param course     the course
     */
    public void insertNewInstructor(Instructor instructor, long course) {
        db.getTransactionExecutor().execute(() -> {
            Instructor existing = instructorDao.getExisting(instructor.getName(),
                    instructor.getPhoneNumber(),
                    instructor.getEmail());
            if (existing != null) {
                instructor.setInstructorId(existing.getInstructorId());
            } else {
                instructor.setInstructorId(instructorDao.insert(instructor));
            }
            courseInstructorCrossRefDao.insert(new CourseInstructorCrossRef(course, instructor.getInstructorId()));
        });

    }

    /**
     * Delete instructor.
     *
     * @param id the id
     */
    public void deleteInstructor(long id) {
        db.getQueryExecutor().execute(() -> instructorDao.delete(id));
    }

    /*NOTES*/

    /**
     * Gets notes for course.
     *
     * @param mPrimaryKey the m primary key
     * @return the notes for course
     */
    public LiveData<List<Note>> getNotesForCourse(long mPrimaryKey) {
        return noteDao.getNotesForCourse(mPrimaryKey);
    }

    /**
     * Insert note.
     *
     * @param note the note
     */
    public void insert(Note note) {
        db.getQueryExecutor().execute(() -> note.setNoteId(noteDao.insert(note)));
    }

    /**
     * Delete note.
     *
     * @param noteId the noteId
     */
    public void deleteNote(long noteId) {
        db.getQueryExecutor().execute(() -> noteDao.delete(noteId));
    }
}
