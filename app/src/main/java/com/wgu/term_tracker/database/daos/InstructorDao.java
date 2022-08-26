package com.wgu.term_tracker.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.wgu.term_tracker.models.Instructor;

import java.util.List;

/**
 * The interface Instructor dao.
 */
@Dao
public interface InstructorDao extends BaseDao<Instructor> {

    /**
     * Delete all Instructors.
     */
    @Query("DELETE FROM instructors")
    void deleteAll();

    /**
     * Gets the existing-match instructor.
     *
     * @param name  the name
     * @param phone the phone
     * @param email the email
     * @return the existing
     */
    @Query("SELECT * FROM instructors WHERE name=:name AND phoneNumber=:phone AND email=:email")
    Instructor getExisting(String name, String phone, String email);


    /**
     * Gets instructors for course.
     *
     * @param mPrimaryKey the m primary key
     * @return the instructors for course
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM instructors AS i " +
            "JOIN course_instructor_cross_ref AS c ON i.instructorId = c.instructorId " +
            "WHERE courseId = :mPrimaryKey")
    LiveData<List<Instructor>> getInstructorsForCourse(long mPrimaryKey);

    /**
     * Delete.
     *
     * @param id the id
     */
    @Query("DELETE FROM instructors WHERE instructorId = :id")
    void delete(long id);
}
