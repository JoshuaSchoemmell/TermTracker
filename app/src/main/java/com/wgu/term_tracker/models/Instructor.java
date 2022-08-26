package com.wgu.term_tracker.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * The type Instructor.
 */
@Entity(tableName = "instructors", indices = @Index("instructorId"))
public class Instructor {

    @PrimaryKey(autoGenerate = true)
    private long instructorId;
    private String name;
    private String phoneNumber;
    private String email;

    /**
     * Instantiates a new Instructor.
     *
     * @param name        the name
     * @param phoneNumber the phone number
     * @param email       the email
     */
    public Instructor(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets instructor id.
     *
     * @return the instructor id
     */
    public long getInstructorId() {
        return instructorId;
    }

    /**
     * Sets instructor id.
     *
     * @param instructorId the instructor id
     */
    public void setInstructorId(long instructorId) {
        this.instructorId = instructorId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Instructor: '" +
                name + '\'';
    }
}
