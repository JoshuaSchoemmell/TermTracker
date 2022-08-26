package com.wgu.term_tracker.ui.course;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Course;

/**
 * The type Course list adapter.
 */
public class CourseListAdapter extends ListAdapter<Course, CourseViewHolder> {


    /**
     * The constant courseItemCallback.
     */
    public static final DiffUtil.ItemCallback<Course> courseItemCallback = new DiffUtil.ItemCallback<Course>() {
        @Override
        public boolean areItemsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem.getCourseId() == newItem.getCourseId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem.equals(newItem);
        }
    };
    /**
     * The Listener.
     */
    final CourseListAdapterListener listener;

    /**
     * Instantiates a new Course list adapter.
     *
     * @param listener the listener
     */
    public CourseListAdapter(CourseListAdapterListener listener) {
        super(courseItemCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        CourseViewHolder courseViewHolder = new CourseViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.list_item,
                        viewGroup,
                        false));
        courseViewHolder.itemView.setOnClickListener(v -> {
                    int position = courseViewHolder.getAdapterPosition();
                    listener.onItemClick(getItem(position));
                }
        );
        return courseViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder viewHolder, int position) {
        Course course = getItem(position);
        viewHolder.bind(course);
    }


    /**
     * The interface Course list adapter listener.
     */
    public interface CourseListAdapterListener {
        /**
         * On item click.
         *
         * @param course the course
         */
        void onItemClick(Course course);
    }
}
