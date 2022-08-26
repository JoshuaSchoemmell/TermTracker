package com.wgu.term_tracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

/**
 * The type Course.
 */
@Entity(tableName = "courses",
        foreignKeys = @ForeignKey(
                entity = Term.class,
                parentColumns = "id",
                childColumns = "termId",
                onDelete = ForeignKey.RESTRICT),
        indices = {@Index("termId"), @Index("courseId")})

public class Course implements Parcelable {

    /**
     * The constant CREATOR.
     */
    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
    /**
     * The Alert info.
     */
    public AlertInfo alertInfo;
    @PrimaryKey(autoGenerate = true)
    private long courseId;
    private String title;
    private Date startDate;
    private Date endDate;
    private CourseStatus courseStatus;
    private long termId;


    /**
     * Instantiates a new Course.
     *
     * @param title        the title
     * @param startDate    the start date
     * @param endDate      the end date
     * @param courseStatus the course status
     * @param termId       the term id
     */
    public Course(String title, Date startDate, Date endDate, CourseStatus courseStatus, long termId) {
        this.setTitle(title);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setStatus(courseStatus);
        this.setTermId(termId);
        alertInfo = new AlertInfo();
    }

    /**
     * Instantiates a new Course.
     *
     * @param in the in
     */
    protected Course(Parcel in) {
        courseId = in.readLong();
        title = in.readString();
        startDate = (Date) in.readSerializable();
        endDate = (Date) in.readSerializable();
        courseStatus = CourseStatus.values()[in.readInt()];
        termId = in.readLong();
        alertInfo = in.readParcelable(AlertInfo.class.getClassLoader());
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

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return getCourseId();
    }

    /**
     * Gets start date.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets end date.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets end date.
     *
     * @param endDate the end date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public CourseStatus getStatus() {
        return courseStatus;
    }

    /**
     * Sets status.
     *
     * @param courseStatus the course status
     */
    public void setStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    /**
     * Gets term id.
     *
     * @return the term id
     */
    public long getTermId() {
        return termId;
    }

    /**
     * Sets term id.
     *
     * @param termId the term id
     */
    public void setTermId(long termId) {
        this.termId = termId;
    }

    /**
     * Gets course status.
     *
     * @return the course status
     */
    public CourseStatus getCourseStatus() {
        return courseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(title, course.title) && Objects.equals(startDate, course.startDate) && Objects.equals(endDate, course.endDate) && courseStatus == course.courseStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, startDate, endDate, courseStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(courseId);
        dest.writeString(title);
        dest.writeSerializable(startDate);
        dest.writeSerializable(endDate);
        dest.writeInt(courseStatus.ordinal());
        dest.writeLong(termId);
        dest.writeParcelable(alertInfo, flags);
    }
}

