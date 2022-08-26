package com.wgu.term_tracker.database.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.wgu.term_tracker.models.CourseInstructorCrossRef;

/**
 * The interface Course instructor cross ref dao.
 */
@Dao
public interface CourseInstructorCrossRefDao extends BaseDao<CourseInstructorCrossRef> {
    /**
     * Delete all.
     */
    @Query("DELETE FROM course_instructor_cross_ref")
    void deleteAll();

}
