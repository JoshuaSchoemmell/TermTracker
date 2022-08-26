package com.wgu.term_tracker.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.wgu.term_tracker.models.Term;


import java.util.List;

/**
 * The interface Term dao.
 */
@Dao
public interface TermDao extends BaseDao<Term> {


    /**
     * Delete all terms.
     */
    @Query("DELETE FROM terms")
    void deleteAll();

    /**
     * Gets all terms.
     *
     * @return the all
     */
    @Query("SELECT * from terms")
    LiveData<List<Term>> getAll();

    /**
     * Gets term.
     *
     * @param selectedTerm the selected term
     * @return the term
     */
    @Query("SELECT * FROM terms WHERE id = :selectedTerm")
    Term getTerm(long selectedTerm);

}
