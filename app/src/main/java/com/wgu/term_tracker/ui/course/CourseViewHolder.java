package com.wgu.term_tracker.ui.course;

import android.view.View;
import android.widget.TextView;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Course;
import com.wgu.term_tracker.ui.term.CustomViewHolder;
import com.wgu.term_tracker.utilities.DateHelper;

/**
 * The type Course view holder.
 */
public class CourseViewHolder extends CustomViewHolder<Course> {

    private final TextView title;
    private final TextView start;
    private final TextView end;

    /**
     * Instantiates a new Course view holder.
     *
     * @param view the view
     */
    public CourseViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.list_item_title);
        start = view.findViewById(R.id.list_item_start);
        end = view.findViewById(R.id.list_item_end_date);
    }

    @Override
    public void bind(Course course) {
        title.setText(course.getTitle());
        start.setText(DateHelper.formatDate(course.getStartDate()));
        end.setText(DateHelper.formatDate(course.getEndDate()));
    }
}
