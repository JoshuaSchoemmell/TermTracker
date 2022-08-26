package com.wgu.term_tracker.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.wgu.term_tracker.models.Assessment;

import java.util.List;

/**
 * The interface Assessment dao.
 */
@SuppressWarnings("ALL")
@Dao
public interface AssessmentDao extends BaseDao<Assessment> {

    /**
     * Delete all Assessments.
     */
    @Query("DELETE FROM assessments")
    void deleteAll();

    /**
     * Gets all Assessments.
     *
     * @return the Assessments
     */
    @Query("SELECT * from assessments")
    LiveData<List<Assessment>> getAll();

    /**
     * Gets assessment count for course.
     *
     * @param selectedCourseId the selected course id
     * @return the assessment count for course
     */
    @Query("SELECT COUNT(*) FROM assessments WHERE courseId = :selectedCourseId")
    LiveData<Integer> getAssessmentCountForCourse(long selectedCourseId);

    /**
     * Gets assessments for course.
     *
     * @param foreignKey the foreign key
     * @return the assessments for course
     */
    @Query("SELECT * FROM assessments WHERE courseId = :foreignKey")
    LiveData<List<Assessment>> getAssessmentsForCourse(long foreignKey);

    /**
     * Get assessment.
     *
     * @param itemID the item id
     * @return the assessment
     */
    @Query("SELECT * FROM assessments WHERE id = :itemID")
    Assessment get(long itemID);
}
