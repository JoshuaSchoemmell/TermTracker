package com.wgu.term_tracker.ui.term;

import android.view.View;
import android.widget.TextView;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Term;
import com.wgu.term_tracker.utilities.DateHelper;

/**
 * The type Term view holder.
 */
public class TermViewHolder extends CustomViewHolder<Term> {
    private final TextView title;
    private final TextView start;
    private final TextView end;

    /**
     * Instantiates a new Term view holder.
     *
     * @param view the view
     */
    public TermViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.list_item_title);
        start = view.findViewById(R.id.list_item_start);
        end = view.findViewById(R.id.list_item_end_date);
    }

    @Override
    public void bind(Term term) {
        title.setText(term.getTitle());
        start.setText(DateHelper.formatDate(term.getStartDate()));
        end.setText(DateHelper.formatDate(term.getEndDate()));
    }
}
