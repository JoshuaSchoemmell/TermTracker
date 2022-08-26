package com.wgu.term_tracker.models;

import androidx.room.Entity;
import androidx.room.Index;

/**
 * The type Course instructor cross ref.
 */
@Entity(tableName = "course_instructor_cross_ref", primaryKeys = {"courseId", "instructorId"}, indices = @Index("instructorId"))
public class CourseInstructorCrossRef {
    /**
     * The Course id.
     */
    public final long courseId;
    /**
     * The Instructor id.
     */
    public final long instructorId;


    /**
     * Instantiates a new Course instructor cross ref.
     *
     * @param courseId     the course id
     * @param instructorId the instructor id
     */
    public CourseInstructorCrossRef(long courseId, long instructorId) {
        this.courseId = courseId;
        this.instructorId = instructorId;
    }
}
