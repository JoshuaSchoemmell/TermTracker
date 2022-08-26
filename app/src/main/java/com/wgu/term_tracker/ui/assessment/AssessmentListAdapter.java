package com.wgu.term_tracker.ui.assessment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Assessment;

/**
 * The type Assessment list adapter.
 */
public class AssessmentListAdapter extends ListAdapter<Assessment, AssessmentViewHolder> {
    /**
     * The constant assessmentItemCallback.
     */
    public static final DiffUtil.ItemCallback<Assessment> assessmentItemCallback = new DiffUtil.ItemCallback<Assessment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem.equals(newItem);
        }
    };
    /**
     * The Listener.
     */
    final AssessmentListAdapterListener listener;

    /**
     * Instantiates a new Assessment list adapter.
     *
     * @param listener the listener
     */
    public AssessmentListAdapter(AssessmentListAdapter.AssessmentListAdapterListener listener) {
        super(assessmentItemCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        AssessmentViewHolder assessmentViewHolder = new AssessmentViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.list_item,
                        viewGroup,
                        false)
        );
        assessmentViewHolder.itemView.setOnClickListener(v -> {
                    int position = assessmentViewHolder.getAdapterPosition();
                    listener.onItemClick(getItem(position));
                }
        );
        return assessmentViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder viewHolder, int position) {
        Assessment assessment = getItem(position);
        viewHolder.bind(assessment);
    }


    /**
     * The interface Assessment list adapter listener.
     */
    public interface AssessmentListAdapterListener {
        /**
         * On item click.
         *
         * @param assessment the assessment
         */
        void onItemClick(Assessment assessment);
    }
}
