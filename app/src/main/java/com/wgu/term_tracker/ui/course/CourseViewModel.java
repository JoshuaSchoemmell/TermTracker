package com.wgu.term_tracker.ui.course;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu.term_tracker.database.TermTrackerRepository;
import com.wgu.term_tracker.models.Course;
import com.wgu.term_tracker.models.Instructor;
import com.wgu.term_tracker.models.Note;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {
    private final TermTrackerRepository repository;
    private Course selectedCourse;
    private Long selectedTermId = -1L;
    private Long selectedCourseId = -1L;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = TermTrackerRepository.getInstance(application);
    }

    public void insert(Course mCourse) {
        repository.insert(mCourse);
        setSelectedCourse(mCourse);
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedTerm(Long termId) {
        selectedTermId = termId;
    }

    public Long getSelectedTermId() {
        return selectedTermId;
    }

    public Long getSelectedCourseId() {
        return selectedCourseId;
    }

    public void updateCourse(Course mCourse) {
        repository.updateCourse(mCourse);
    }

    public void deleteCourse() {
        repository.deleteCourse(selectedCourse);
    }

    public LiveData<List<Course>> getCoursesForTerm() {
        return repository.getCoursesForTerm(getSelectedTermId());
    }

    public LiveData<List<Instructor>> getInstructorsForCourse() {
        return repository.getInstructorsForCourse(getSelectedCourseId());
    }

    public void insertInstructor(Instructor instructor) {
        repository.insertNewInstructor(instructor, getSelectedCourseId());
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
        if (selectedCourse != null) {
            selectedCourseId = selectedCourse.getCourseId();
        } else {
            selectedCourseId = -1L;
        }
    }

    public void insertNewNote(Note note) {
        repository.insert(note);
    }

    public LiveData<List<Note>> getNotesForCourse() {
        return repository.getNotesForCourse(selectedCourseId);
    }

    public void deleteInstructor(long id) {
        repository.deleteInstructor(id);
    }

    public void deleteNote(long iD) {
        repository.deleteNote(iD);
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public LiveData<Integer> getAssessmentCountForCourse() {
        return repository.getAssessmentCount(getSelectedCourseId());
    }
}