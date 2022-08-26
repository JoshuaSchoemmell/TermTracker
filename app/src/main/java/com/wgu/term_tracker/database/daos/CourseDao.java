package com.wgu.term_tracker.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.wgu.term_tracker.models.Course;

import java.util.List;

/**
 * The interface Course dao.
 */
@Dao
public interface CourseDao extends BaseDao<Course> {


    /**
     * Delete all courses.
     */
    @Query("DELETE FROM courses")
    void deleteAll();

    /**
     * Gets all courses.
     *
     * @return all courses
     */
    @Query("SELECT * from courses")
    LiveData<List<Course>> getAll();

    /**
     * Gets course count.
     *
     * @param selectedTerm the selected term
     * @return the course count
     */
    @Query("SELECT COUNT(*) > 0 FROM courses WHERE termId = :selectedTerm")
    LiveData<Integer> getCourseCount(long selectedTerm);

    /**
     * Gets courses for term.
     *
     * @param termId the term id
     * @return the courses for term
     */
    @Query("SELECT * FROM courses WHERE termId = :termId")
    LiveData<List<Course>> getCoursesForTerm(Long termId);

    /**
     * Gets course.
     *
     * @param selectedId the selected id
     * @return the course
     */
    @Query("SELECT * FROM courses WHERE courseId = :selectedId")
    Course getCourse(long selectedId);

    /**
     * Term has courses live data.
     *
     * @param id the id
     * @return the live data
     */
    @Query("SELECT COUNT(*) > 0 FROM courses WHERE termId = :id")
    LiveData<Boolean> termHasCourses(long id);
}
