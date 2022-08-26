package com.wgu.term_tracker.ui.term;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Term;

import java.util.List;

/**
 * The type Term list adapter.
 */
public class TermListAdapter extends ListAdapter<Term, TermViewHolder> {

    /**
     * The constant itemCallback.
     */
    public static final DiffUtil.ItemCallback<Term> itemCallback = new DiffUtil.ItemCallback<Term>() {
        @Override
        public boolean areItemsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
            return oldItem.equals(newItem);
        }
    };
    /**
     * The Term list adapter listener.
     */
    final TermListAdapterListener termListAdapterListener;

    /**
     * Instantiates a new Term list adapter.
     *
     * @param termListAdapterListener the term list adapter listener
     */
    public TermListAdapter(TermListAdapterListener termListAdapterListener) {
        super(itemCallback);
        this.termListAdapterListener = termListAdapterListener;
    }


    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        TermViewHolder termViewHolder = new TermViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_item, viewGroup, false));

        // Set on click listener
        termViewHolder.itemView.setOnClickListener(v -> {
            int position = termViewHolder.getAdapterPosition();
            termListAdapterListener.onItemClicked(getItem(position));
        });
        return termViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder viewHolder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Term term = getItem(position);
        viewHolder.bind(term);
    }

    @Override
    public void submitList(@Nullable List<Term> list) {
        super.submitList(list);
    }

    /**
     * The interface Term list adapter listener.
     */
    public interface TermListAdapterListener {
        /**
         * On item clicked.
         *
         * @param term the term
         */
        void onItemClicked(Term term);
    }

}
