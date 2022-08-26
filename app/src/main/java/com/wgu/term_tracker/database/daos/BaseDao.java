package com.wgu.term_tracker.database.daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

/**
 * The interface Base dao.
 *
 * @param <T> the type parameter
 */
public interface BaseDao<T> {
    /**
     * Insert the object.
     *
     * @param dataObject the data object
     * @return the long
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T dataObject);

    /**
     * Delete the object.
     *
     * @param dataObject the data object
     */
    @Delete
    void delete(T dataObject);

    /**
     * Update the object.
     *
     * @param dataObject the data object
     */
    @Update
    void update(T dataObject);
}
