package com.wgu.term_tracker.ui.assessment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu.term_tracker.database.TermTrackerRepository;
import com.wgu.term_tracker.models.Assessment;

import java.util.List;

/**
 * The type Assessment view model.
 */
public class AssessmentViewModel extends AndroidViewModel {
    private final TermTrackerRepository repository;
    private Long selectedCourseId;
    private Long selectedAssessmentId;
    private Assessment selectedAssessment;

    /**
     * Instantiates a new Assessment view model.
     *
     * @param application the application
     */
    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = TermTrackerRepository.getInstance(application);
    }

    /**
     * Gets assessments for course.
     *
     * @return the assessments for course
     */
    public LiveData<List<Assessment>> getAssessmentsForCourse() {
        return repository.getAssessmentsForCourse(selectedCourseId);
    }

    /**
     * Sets selected course.
     *
     * @param aLong the a long
     */
    public void setSelectedCourse(long aLong) {
        selectedCourseId = aLong;
    }

    /**
     * Gets selected assessment id.
     *
     * @return the selected assessment id
     */
    public long getSelectedAssessmentId() {
        return selectedAssessmentId;
    }

    /**
     * Sets selected assessment id.
     *
     * @param selectedAssessmentId the selected assessment id
     */
    public void setSelectedAssessmentId(Long selectedAssessmentId) {
        this.selectedAssessmentId = selectedAssessmentId;
    }

    /**
     * Gets selected course id.
     *
     * @return the selected course id
     */
    public Long getSelectedCourseId() {
        return selectedCourseId;
    }

    /**
     * Gets selected assessment.
     *
     * @return the selected assessment
     */
    public Assessment getSelectedAssessment() {
        return selectedAssessment;
    }

    /**
     * Sets selected assessment.
     *
     * @param assessment the assessment
     */
    public void setSelectedAssessment(Assessment assessment) {
        selectedAssessment = assessment;
        setSelectedAssessmentId(assessment != null ? assessment.getId() : null);
    }

    /**
     * Inserts assessment.
     *
     * @param assessment the assessment
     */
    public void insert(Assessment assessment) {
        repository.insert(assessment);
        setSelectedAssessment(assessment);
    }

    /**
     * Updates the assessment.
     *
     * @param assessment the assessment
     */
    public void update(Assessment assessment) {
        repository.updateAssessment(assessment);
    }

    /**
     * Deletes the assessment.
     *
     * @param assessment the assessment
     */
    public void delete(Assessment assessment) {
        repository.delete(assessment);
    }
}
