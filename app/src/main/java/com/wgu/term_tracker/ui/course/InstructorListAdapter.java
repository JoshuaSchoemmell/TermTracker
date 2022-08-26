package com.wgu.term_tracker.ui.course;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Instructor;

import java.util.List;

/**
 * The type Instructor list adapter.
 */
public class InstructorListAdapter extends ListAdapter<Instructor, InstructorViewHolder> {

    /**
     * The constant INSTRUCTOR_ITEM_CALLBACK.
     */
    public static final DiffUtil.ItemCallback<Instructor> INSTRUCTOR_ITEM_CALLBACK = new DiffUtil.ItemCallback<Instructor>() {
        @Override
        public boolean areItemsTheSame(@NonNull Instructor oldItem, @NonNull Instructor newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Instructor oldItem, @NonNull Instructor newItem) {
            if (oldItem.getInstructorId() == newItem.getInstructorId()) {
                if (oldItem.getName().equals(newItem.getName())) {
                    if (oldItem.getPhoneNumber().equals(newItem.getPhoneNumber())) {
                        return oldItem.getEmail().equals(newItem.getEmail());
                    }
                }
            }
            return false;
        }
    };
    private final InstructorListener instructorListener;

    /**
     * Instantiates a new Instructor list adapter.
     *
     * @param instructorListener the instructor listener
     */
    public InstructorListAdapter(InstructorListener instructorListener) {
        super(INSTRUCTOR_ITEM_CALLBACK);
        this.instructorListener = instructorListener;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getInstructorId();
    }

    @NonNull
    @Override
    public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InstructorViewHolder viewHolder = new InstructorViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_instructor, parent, false));

        // Set on Listener
        viewHolder.itemView.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            instructorListener.onItemClick(position);
        });
        viewHolder.itemView.setOnLongClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            instructorListener.onItemLongClick(position);
            return false;
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorViewHolder holder, int position) {
        Instructor instructor = getItem(position);
        holder.bind(instructor);
    }

    @Override
    public void submitList(@Nullable List<Instructor> list) {
        super.submitList(list);
    }

    /**
     * The interface Instructor listener.
     */
    public interface InstructorListener {
        /**
         * On item click.
         *
         * @param pos the pos
         */
        void onItemClick(int pos);

        /**
         * On item long click.
         *
         * @param pos the pos
         */
        void onItemLongClick(int pos);
    }
}
