package com.wgu.term_tracker.ui.assessment;

import android.view.View;
import android.widget.TextView;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Assessment;
import com.wgu.term_tracker.ui.term.CustomViewHolder;
import com.wgu.term_tracker.utilities.DateHelper;

/**
 * The type Assessment view holder.
 */
public class AssessmentViewHolder extends CustomViewHolder<Assessment> {
    private final TextView title;
    private final TextView start;
    private final TextView end;

    /**
     * Instantiates a new Assessment view holder.
     *
     * @param view the view
     */
    public AssessmentViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.list_item_title);
        start = view.findViewById(R.id.list_item_start);
        end = view.findViewById(R.id.list_item_end_date);
    }

    @Override
    public void bind(Assessment assessment) {
        title.setText(assessment.getTitle());
        start.setText(DateHelper.formatDate(assessment.getStartDate()));
        end.setText(DateHelper.formatDate(assessment.getEndDate()));
    }
}
