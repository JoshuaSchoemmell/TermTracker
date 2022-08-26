package com.wgu.term_tracker.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.wgu.term_tracker.models.Note;

import java.util.List;

/**
 * The interface Note dao.
 */
@Dao
public interface NoteDao extends BaseDao<Note> {
    /**
     * Delete all.
     */
    @Query("DELETE FROM notes")
    void deleteAll();

    /**
     * Gets notes for course.
     *
     * @param mPrimaryKey the m primary key
     * @return the notes for course
     */
    @Query("SELECT * FROM notes WHERE courseId = :mPrimaryKey")
    LiveData<List<Note>> getNotesForCourse(long mPrimaryKey);

    /**
     * Delete.
     *
     * @param iD the d
     */
    @Query("DELETE FROM notes WHERE noteId = :iD")
    void delete(long iD);
}
