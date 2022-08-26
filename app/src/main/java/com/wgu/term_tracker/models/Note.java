package com.wgu.term_tracker.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * The type Note.
 */
@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "courseId",
                childColumns = "courseId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("courseId")})
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long noteId;
    private String contents;
    private long courseId;

    /**
     * Instantiates a new Note.
     *
     * @param contents the contents
     * @param courseId the course id
     */
    public Note(String contents, long courseId) {
        this.contents = contents;
        this.courseId = courseId;
    }


    /**
     * Gets note id.
     *
     * @return the note id
     */
    public long getNoteId() {
        return noteId;
    }

    /**
     * Sets note id.
     *
     * @param noteId the note id
     */
    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    /**
     * Gets contents.
     *
     * @return the contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * Sets contents.
     *
     * @param contents the contents
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * Gets course id.
     *
     * @return the course id
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * Sets course id.
     *
     * @param courseId the course id
     */
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
