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
 * The type Assessment.
 */
@Entity(tableName = "assessments",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "courseId",
                childColumns = "courseId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("courseId")})
public class Assessment implements Parcelable {
    /**
     * The constant CREATOR.
     */
    public static final Creator<Assessment> CREATOR = new Creator<Assessment>() {
        @Override
        public Assessment createFromParcel(Parcel in) {
            return new Assessment(in);
        }

        @Override
        public Assessment[] newArray(int size) {
            return new Assessment[size];
        }
    };

    /**
     * The Alert info.
     */
    public AlertInfo alertInfo;
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private AssessmentType assessmentType;
    private Date startDate;
    private Date endDate;
    private long courseId;

    /**
     * Instantiates a new Assessment.
     *
     * @param title          the title
     * @param startDate      the start date
     * @param endDate        the end date
     * @param assessmentType the assessment type
     * @param courseId       the course id
     */
    public Assessment(String title, Date startDate, Date endDate, AssessmentType assessmentType, long courseId) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assessmentType = assessmentType;
        this.courseId = courseId;
        alertInfo = new AlertInfo();
    }

    /**
     * Instantiates a new Assessment from a Parcel.
     *
     * @param in the in
     */
    protected Assessment(Parcel in) {
        id = in.readLong();
        title = in.readString();
        assessmentType = AssessmentType.values()[in.readInt()];
        startDate = (Date) in.readSerializable();
        endDate = (Date) in.readSerializable();
        courseId = in.readLong();
        alertInfo = in.readParcelable(AlertInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeInt(assessmentType.ordinal());
        dest.writeSerializable(startDate);
        dest.writeSerializable(endDate);
        dest.writeLong(courseId);
        dest.writeParcelable(alertInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assessment that = (Assessment) o;
        return id == that.id && courseId == that.courseId && Objects.equals(title, that.title)
                && assessmentType == that.assessmentType && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, assessmentType, startDate, endDate, courseId);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
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
    @SuppressWarnings("unused")
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the title.
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title String.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets assessment type.
     *
     * @return the assessment type
     */
    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    /**
     * Sets assessment type.
     *
     * @param assessmentType the assessment type
     */
    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
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
     * Gets the end date.
     *
     * @return the end Date.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDate the end Date.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

