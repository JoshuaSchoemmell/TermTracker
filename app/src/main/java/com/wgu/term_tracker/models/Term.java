package com.wgu.term_tracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * The type Term.
 */
@Entity(tableName = "terms")
public class Term implements Parcelable {

    /**
     * The constant CREATOR.
     */
    public static final Creator<Term> CREATOR = new Creator<Term>() {
        @Override
        public Term createFromParcel(Parcel in) {
            return new Term(in);
        }

        @Override
        public Term[] newArray(int size) {
            return new Term[size];
        }
    };
    /**
     * The Alert info.
     */
    public AlertInfo alertInfo;
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private Date startDate;
    private Date endDate;

    /**
     * Instantiates a new Term.
     *
     * @param title     the title
     * @param startDate the start date
     * @param endDate   the end date
     */
    public Term(String title, Date startDate, Date endDate) {
        this.setTitle(title);
        this.startDate = startDate;
        this.endDate = endDate;
        alertInfo = new AlertInfo();
    }

    /**
     * Instantiates a new Term.
     *
     * @param in the in
     */
    protected Term(Parcel in) {
        id = in.readLong();
        title = in.readString();
        startDate = (Date) in.readSerializable();
        endDate = (Date) in.readSerializable();
        alertInfo = in.readParcelable(AlertInfo.class.getClassLoader());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(title, term.title) && Objects.equals(startDate, term.startDate) && Objects.equals(endDate, term.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, startDate, endDate);
    }


    /**
     * Gets start date cal.
     *
     * @return the start date cal
     */
    public Calendar getStartDateCal() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartDate());
        return calendar;
    }

    /**
     * Gets end date cal.
     *
     * @return the end date cal
     */
    public Calendar getEndDateCal() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getEndDate());
        return calendar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeSerializable(startDate);
        dest.writeSerializable(endDate);
        dest.writeParcelable(alertInfo, flags);
    }
}
