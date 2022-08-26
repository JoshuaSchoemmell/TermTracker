package com.wgu.term_tracker.ui.term;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wgu.term_tracker.database.TermTrackerRepository;
import com.wgu.term_tracker.models.Term;

import java.util.List;

/**
 * The type Term view model.
 */
public class TermViewModel extends AndroidViewModel {
    private final LiveData<List<Term>> termList;
    /**
     * The Repository.
     */
    protected final TermTrackerRepository repository;
    private Term selectedTerm;
    private long selectedTermId = -1L;

    /**
     * Instantiates a new Term view model.
     *
     * @param application the application
     */
    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = TermTrackerRepository.getInstance(application);
        termList = repository.getAllTerms();
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    public LiveData<List<Term>> getAll() {
        return termList;
    }

    /**
     * Gets selected term.
     *
     * @return the selected term
     */
    public Term getSelectedTerm() {
        return selectedTerm;
    }

    /**
     * Sets selected term.
     *
     * @param selectedTerm the selected term
     */
    public void setSelectedTerm(Term selectedTerm) {
        this.selectedTerm = selectedTerm;
        selectedTermId = selectedTerm != null ? selectedTerm.getId() : -1L;
    }

    /**
     * Delete.
     *
     * @param mTerm the m term
     */
    public void delete(Term mTerm) {
        repository.delete(mTerm);
    }

    /**
     * Insert.
     *
     * @param mTerm the m term
     */
    public void insert(Term mTerm) {
        repository.insert(mTerm);
        setSelectedTerm(mTerm);
    }

    /**
     * Term has courses live data.
     *
     * @return the live data
     */
    public LiveData<Boolean> termHasCourses() {
        return repository.termHasCourses(selectedTermId);
    }

    /**
     * Update.
     *
     * @param mTerm the m term
     */
    public void update(Term mTerm) {
        repository.update(mTerm);
    }

    /**
     * Gets course count.
     *
     * @return the course count
     */
    public LiveData<Integer> getCourseCount() {
        return repository.getCourseCount(selectedTermId);
    }

}